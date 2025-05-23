PRAGMA foreign_keys = OFF;
BEGIN TRANSACTION;

-- 0) Eliminar cualquier rastro previo
DROP VIEW   IF EXISTS partida_resumen;
DROP TABLE  IF EXISTS partida_eliminacion;
DROP TABLE  IF EXISTS partida_personaje;
DROP TABLE  IF EXISTS partida;
DROP TABLE  IF EXISTS escenario_personaje;
DROP TABLE  IF EXISTS escenario_modificador;
DROP TABLE  IF EXISTS escenario;
DROP TABLE  IF EXISTS pasiva;
DROP TABLE  IF EXISTS ataque;
DROP TABLE  IF EXISTS tipo_ataque;
DROP TABLE  IF EXISTS personaje;
DROP TABLE  IF EXISTS usuario;

-- 1) Usuarios
CREATE TABLE usuario (
  id             INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre         TEXT    NOT NULL,
  email          TEXT    NOT NULL UNIQUE,
  password_hash  TEXT    NOT NULL,
  creado_en      DATETIME NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);
INSERT INTO usuario (nombre, email, password_hash) VALUES
  ('admin', 'admin@local', 'admin');

-- 2) Personajes (incluye formas)
CREATE TABLE personaje (
  id                INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre            TEXT    NOT NULL,
  nombre_codigo     TEXT    NOT NULL UNIQUE,
  descripcion       TEXT,
  imagen_miniatura  TEXT,
  imagen_combate    TEXT,
  personaje_base_id INTEGER REFERENCES personaje(id) ON DELETE SET NULL,
  duracion_turnos   INTEGER NOT NULL DEFAULT 0,
  vida              INTEGER NOT NULL,
  fuerza            INTEGER NOT NULL,
  velocidad         INTEGER NOT NULL,
  poder             INTEGER NOT NULL
);
CREATE INDEX idx_personaje_base ON personaje(personaje_base_id);

-- Base
INSERT INTO personaje (nombre, nombre_codigo, descripcion, imagen_miniatura, imagen_combate, personaje_base_id, duracion_turnos, vida, fuerza, velocidad, poder) VALUES
  ('Magik (Forma Base)',      'magik',          'Illyana Rasputin, la hechicera',          'images/Personajes/magik.png','images/Ingame/magik-ingame.png',          NULL,0,   800,  400,  800,  1500),
  ('Thanos (Sin Guantelete)', 'thanos',         'Thanos, el Titán Loco',                   'images/Personajes/thanos.png','images/Ingame/thanos-ingame.png',         NULL,0,  8000, 5000, 1500,  5500),
  ('Scarlet Witch',           'scarlet_witch',  'Wanda Maximoff, Bruja Escarlata',         'images/Personajes/scarlet.png','images/Ingame/scarlet-witch-ingame.png',        NULL,0,   500,   80,   70, 25000),
  ('Legion',                  'legion',         'David Haller, múltiples personalidades',  'images/Personajes/legion.png','images/Ingame/legion-ingame.png',         NULL,0,   400,   70,   50, 20000),
  ('Doctor Strange',          'doctor_strange', 'Stephen Strange, Hechicero Supremo',      'images/Personajes/doctor-strange.png','images/Ingame/doctorstrange-ingame.png',        NULL,0,   350,   20,   30, 20000),
  ('Silver Surfer',           'silver_surfer',  'Herald of Galactus',                      'images/Personajes/surfer.png','images/Ingame/silver-surfer-ingame.png',          NULL,0,  5000, 4500, 9000,  8000),
  ('Arishem (Celestial)',     'arishem',        'Uno de los Celestiales',                  'images/Personajes/arishem.png','images/Ingame/arishem-ingame.png',        NULL,0, 25000,20000,1500, 20000),
  ('Knull',                   'knull',          'El Dios del Simbionte',                   'images/Personajes/knull.png','images/Ingame/knull-ingame.png',          NULL,0, 30000,28000,2500, 28000),
  ('Hulk (Máximo)',           'hulk',           'Bruce Banner en furia máxima',            'images/Personajes/hulk.png','images/Ingame/hulk-ingame.png',            NULL,0,  6500, 7500,1200,  2500),
  ('Doctor Doom',             'doctor_doom',    'Victor von Doom, monarca de Latveria',    'images/Personajes/doom.png','images/Ingame/doom-ingame.png',            NULL,0,  4500, 1500,  500,  8000),
  ('Iron Man (Mark 85)',      'iron_man',       'Tony Stark en su Mark 85',                'images/Personajes/ironman.png','images/Ingame/ironman-ingame.png',            NULL,0,  1000,  500,  400,   400),
  ('Wolverine',               'wolverine',      'Logan, el mutante inmortal',              'images/Personajes/logan.png','images/Ingame/logan-ingame.png',          NULL,0,  3500, 1200,  500,   300),
  ('Sebastian Shaw',          'sebastian_shaw','Mutante absorbente de energía',            'images/Personajes/shaw.png','images/Ingame/sebastian-shaw-ingame.png',            NULL,0,  2000, 1500,  400,   250),
  ('Spider-Man',              'spider_man',     'Peter Parker, el trepamuros',             'images/Personajes/spiderman.png','images/Ingame/spiderman-ingame.png',        NULL,0,   900, 1000, 1500,   250),
  ('Black Panther',           'black_panther',  'TChalla, Rey de Wakanda',                 'images/Personajes/panther.png','images/Ingame/balck-panther-ingame.png',      NULL,0,   850,  800,  800,   200),
  ('Captain America',         'captain_america','Steve Rogers, Capitán América',            'images/Personajes/captain-america.png','images/Ingame/captainamerica-ingame.png',              NULL,0,   750,  500,  400,   100),
  ('Deadpool',                'deadpool',       'El Mercenario Bocazas',                   'images/Personajes/dead.png','images/Ingame/deadpool-ingame.png',            NULL,0,  5000,  300,  500,    80);

-- Transformaciones
INSERT INTO personaje (nombre, nombre_codigo, descripcion, imagen_miniatura, imagen_combate, personaje_base_id, duracion_turnos, vida, fuerza, velocidad, poder) VALUES
  ('Magik (Darkchild)',        'magik_darkchild','Darkchild de Magik',                     'images/Personajes/dark.png','images/Ingame/darkchild-ingame.png',
   (SELECT id FROM personaje WHERE nombre_codigo='magik'),        2, 4500, 3500, 3000, 18000),
  ('Thanos (Guantelete)',      'thanos_gauntlet','Thanos con Guantelete',                  'images/Personajes/gaunt.png','images/Ingame/gauntlet-ingame.png',
   (SELECT id FROM personaje WHERE nombre_codigo='thanos'),      0,40000,35000,30000, 85000);

-- 3) Tipos de ataque
CREATE TABLE tipo_ataque (
  id    INTEGER PRIMARY KEY AUTOINCREMENT,
  clave TEXT    NOT NULL UNIQUE
);
INSERT INTO tipo_ataque (clave) VALUES
  ('ACC'),('AAD'),('habilidad_mas_poderosa'),('habilidad_caracteristica');

-- 4) Ataques
CREATE TABLE ataque (
  id               INTEGER PRIMARY KEY AUTOINCREMENT,
  personaje_id     INTEGER NOT NULL REFERENCES personaje(id) ON DELETE CASCADE,
  tipo_ataque_id   INTEGER NOT NULL REFERENCES tipo_ataque(id),
  codigo           TEXT    NOT NULL,  -- Nuevo campo: código único para identificar la habilidad
  nombre           TEXT    NOT NULL,
  dano_base        INTEGER NOT NULL,
  usos_maximos     INTEGER NOT NULL DEFAULT 0,
  cooldown_turnos  INTEGER NOT NULL DEFAULT 0
);
CREATE INDEX idx_ataque_personaje ON ataque(personaje_id);
CREATE INDEX idx_ataque_codigo ON ataque(codigo);  -- Nuevo índice

-- Magik Base
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='magik'),           1,'magik_melee','Corte del Alma',         1320,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='magik'),           2,'magik_range','Proyectil Demoníaco',    1100,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='magik'),           3,'magik_hab1','Darkchild Rising',         0,0,3),
  ((SELECT id FROM personaje WHERE nombre_codigo='magik'),           4,'magik_hab2','Exilio al Limbo',          0,1,0);

-- Magik Darkchild
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='magik_darkchild'),1,'magik_darkchild_melee','Garra de la Reina Infernal',1800,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='magik_darkchild'),2,'magik_darkchild_range','Estallido del Limbo',      1600,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='magik_darkchild'),3,'magik_darkchild_hab1','Trono del Caos',           4000,1,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='magik_darkchild'),4,'magik_darkchild_hab2','Auge Infernal',               0,1,0);

-- Thanos Sin Guantelete
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='thanos'),          1,'thanos_melee','Puño del Titán',        1560,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='thanos'),          2,'thanos_range','Explosión Cósmica',     1300,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='thanos'),          3,'thanos_hab1','Aniquilación Cósmica',    0,0,4),
  ((SELECT id FROM personaje WHERE nombre_codigo='thanos'),          4,'thanos_hab2','Intimidación Titán',       0,0,3);

-- Thanos Guantelete
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='thanos_gauntlet'),1,'thanos_gauntlet_melee','Golpe del Infinito',    2200,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='thanos_gauntlet'),2,'thanos_gauntlet_range','Rayo Universal',        2000,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='thanos_gauntlet'),3,'thanos_gauntlet_hab1','Chasquido del Infinito',  0,1,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='thanos_gauntlet'),4,'thanos_gauntlet_hab2','Voluntad de Thanos',       0,0,5);

-- Scarlet Witch
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='scarlet_witch'),   1,'scarlet_witch_melee','Impacto de Caos',       770,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='scarlet_witch'),   2,'scarlet_witch_range','Hechizo Hex',           935,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='scarlet_witch'),   3,'scarlet_witch_hab1','Caos Absoluto',        5000,0,3),
  ((SELECT id FROM personaje WHERE nombre_codigo='scarlet_witch'),   4,'scarlet_witch_hab2','Realidad Distorsionada', 0,0,3);

-- Legion
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='legion'),          1,'legion_melee','Golpe Psíquico',       880,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='legion'),          2,'legion_range','Rayo de Personalidad',1045,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='legion'),          3,'legion_hab1','Personalidades Desatadas',0,0,6),
  ((SELECT id FROM personaje WHERE nombre_codigo='legion'),          4,'legion_hab2','Plano Mental',           0,0,0);

-- Doctor Strange
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='doctor_strange'),  1,'doctor_strange_melee','Golpe Arcano',         715,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='doctor_strange'),  2,'doctor_strange_range','Rayo de los Vishanti',990,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='doctor_strange'),  3,'doctor_strange_hab1','Dimensión Espejo',        0,1,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='doctor_strange'),  4,'doctor_strange_hab2','Ojo de Agamotto',         0,0,5);

-- Silver Surfer
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='silver_surfer'),   1,'silver_surfer_melee','Corte de Tabla Estelar',1500,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='silver_surfer'),   2,'silver_surfer_range','Rayo Cósmico',         1800,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='silver_surfer'),   3,'silver_surfer_hab1','Nova Cósmica',         4500,0,4),
  ((SELECT id FROM personaje WHERE nombre_codigo='silver_surfer'),   4,'silver_surfer_hab2','Velocidad Estelar',      0,0,5);

-- Arishem
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='arishem'),         1,'arishem_melee','Juicio de la Mano Cósmica',3000,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='arishem'),         2,'arishem_range','Explosión Estelar',     3750,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='arishem'),         3,'arishem_hab1','Juicio Celestial',        0,0,6),
  ((SELECT id FROM personaje WHERE nombre_codigo='arishem'),         4,'arishem_hab2','Presencia Imponente',      0,0,4);

-- Knull
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='knull'),           1,'knull_melee','Mandoble de All-Black', 2210,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='knull'),           2,'knull_range','Lluvia de Simbiontes', 1560,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='knull'),           3,'knull_hab1','Oscuridad Primordial',    0,0,5),
  ((SELECT id FROM personaje WHERE nombre_codigo='knull'),           4,'knull_hab2','Rey de Simbiontes',        0,0,2);

-- Hulk
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='hulk'),            1,'hulk_melee','Puño Colosal',         1600,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='hulk'),            2,'hulk_range','Lanzamiento de Roca',   900,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='hulk'),            3,'hulk_hab1','Furia Gamma',            0,0,5),
  ((SELECT id FROM personaje WHERE nombre_codigo='hulk'),            4,'hulk_hab2','Cuanto más enfadado…',   0,0,0);

-- Doctor Doom
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='doctor_doom'),     1,'doctor_doom_melee','Descarga Latveriana',   1210,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='doctor_doom'),     2,'doctor_doom_range','Bomba Arcana',         1320,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='doctor_doom'),     3,'doctor_doom_hab1','Conjuro de Muerte',      0,0,2),
  ((SELECT id FROM personaje WHERE nombre_codigo='doctor_doom'),     4,'doctor_doom_hab2','Escudo de Latveria',     0,0,4);

-- Iron Man
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='iron_man'),        1,'iron_man_melee','Puñetazo Reforzado',    800,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='iron_man'),        2,'iron_man_range','Micro‑Misiles Dirigidos',1100,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='iron_man'),        3,'iron_man_hab1','Unibeam Máximo',         0,0,3),
  ((SELECT id FROM personaje WHERE nombre_codigo='iron_man'),        4,'iron_man_hab2','Nanotecnología Adaptativa',0,0,4);

-- Wolverine
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='wolverine'),       1,'wolverine_melee','Doble Corte',          1400,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='wolverine'),       2,'wolverine_range','Lanzamiento de Navaja', 600,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='wolverine'),       3,'wolverine_hab1','Furia Berserker',        0,0,4),
  ((SELECT id FROM personaje WHERE nombre_codigo='wolverine'),       4,'wolverine_hab2','Regeneración Extrema',    0,0,5);

-- Sebastian Shaw
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='sebastian_shaw'),  1,'sebastian_shaw_melee','Puño Absorbente',      1000,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='sebastian_shaw'),  2,'sebastian_shaw_range','Impacto Reforzado',      800,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='sebastian_shaw'),  3,'sebastian_shaw_hab1','Absorción Cinética Total',0,0,5),
  ((SELECT id FROM personaje WHERE nombre_codigo='sebastian_shaw'),  4,'sebastian_shaw_hab2','Contraataque Cinético',   0,0,3);

-- Spider‑Man
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='spider_man'),      1,'spider_man_melee','Combo Acrobático',     1080,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='spider_man'),      2,'spider_man_range','Disparo de Telaraña',   765,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='spider_man'),      3,'spider_man_hab1','Ataque de Máxima Agilidad',0,0,4),
  ((SELECT id FROM personaje WHERE nombre_codigo='spider_man'),      4,'spider_man_hab2','Telaraña Inmovilizante',  0,0,3);

-- Black Panther
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='black_panther'),   1,'black_panther_melee','Combo Felino',         1080,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='black_panther'),   2,'black_panther_range','Dagas de Vibranium',    900,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='black_panther'),   3,'black_panther_hab1','Liberación Energía Cinética',0,0,4),
  ((SELECT id FROM personaje WHERE nombre_codigo='black_panther'),   4,'black_panther_hab2','Garras de Vibranium',    500,0,3);

-- Captain America
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='captain_america'), 1,'captain_america_melee','Golpe de Escudo',       720,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='captain_america'), 2,'captain_america_range','Lanzamiento de Escudo', 810,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='captain_america'), 3,'captain_america_hab1','Justicia Imparable',     400,0,4),
  ((SELECT id FROM personaje WHERE nombre_codigo='captain_america'), 4,'captain_america_hab2','Escudo del Capitán',      0,0,4);

-- Deadpool
INSERT INTO ataque (personaje_id,tipo_ataque_id,codigo,nombre,dano_base,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='deadpool'),        1,'deadpool_melee','Espadazo Múltiple',     900,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='deadpool'),        2,'deadpool_range','Disparo Caótico',       630,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='deadpool'),        3,'deadpool_hab1','Factor Curativo Extremo',0,0,3),
  ((SELECT id FROM personaje WHERE nombre_codigo='deadpool'),        4,'deadpool_hab2','Ataque Impredecible',     0,0,2);

-- 5) Pasivas
CREATE TABLE pasiva (
  id               INTEGER PRIMARY KEY AUTOINCREMENT,
  personaje_id     INTEGER NOT NULL REFERENCES personaje(id) ON DELETE CASCADE,
  nombre           TEXT    NOT NULL,
  descripcion      TEXT    NOT NULL,
  trigger_tipo     TEXT    NOT NULL,
  efecto_tipo      TEXT    NOT NULL,
  efecto_valor     INTEGER NOT NULL,
  usos_maximos     INTEGER NOT NULL DEFAULT 0,
  cooldown_turnos  INTEGER NOT NULL DEFAULT 0
);

INSERT INTO pasiva (personaje_id,nombre,descripcion,trigger_tipo,efecto_tipo,efecto_valor,usos_maximos,cooldown_turnos) VALUES
  ((SELECT id FROM personaje WHERE nombre_codigo='magik'),
   'Voluntad Inquebrantable','Inmune a teletransporte o encierro dimensional.','on_attempt_teleport','prevent_effect',100,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='magik_darkchild'),
   'Sello del Limbo','Marca al atacante con Sello del Limbo 1 turno.','on_damage_taken','apply_mark_limbo',1,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='thanos'),
   'Voluntad Inquebrantable','Recibe 10% menos de daño >50% HP.','on_damage_taken','reduce_damage_pct',10,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='thanos_gauntlet'),
   'Dominio Absoluto','Activa gema aleatoria cada 3 turnos.','on_enter_combat','activate_random_gem',0,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='scarlet_witch'),
   'Inestabilidad de la Realidad','25% de prob. cada turno de anular efecto negativo.','on_apply_negative_effect','negate_effect_pct',25,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='legion'),
   'Conciencia Fragmentada','20% de prob. de contraataque melee al recibir daño.','on_damage_taken','counterattack_melee_pct',20,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='doctor_strange'),
   'Protección de Vishanti','Escudo 100% al caer <30% HP (1 turno).','below_hp_pct','grant_shield_pct',100,1,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='silver_surfer'),
   'Corredor Cósmico','Siempre ataca primero.','on_turn_start','always_first',1,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='arishem'),
   'Juicio Universal','Reduce 20% stats enemigo al entrar o cada 3 turnos.','on_enter_combat','reduce_enemy_stats_pct',20,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='knull'),
   'Oscuridad Viviente','Recupera 5% de vida al infligir daño.','on_deal_damage','heal_pct',5,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='hulk'),
   'Ira Incontenible','+10% Fuerza al recibir daño (máx.3).','on_damage_taken','increase_strength_pct',10,3,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='doctor_doom'),
   'Intelecto Supremo','Efectos negativos duran 1 turno menos.','on_negative_effect_apply','reduce_duration_turns',1,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='iron_man'),
   'Análisis de Combate RT','Tras 2 daños recibidos, +15% daño 2 turnos.','on_damage_taken','increase_damage_pct',15,2,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='wolverine'),
   'Instinto Animal','20% prob. de esquivar melee.','on_attack_received_melee','evade_pct',20,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='sebastian_shaw'),
   'Conversión Cinética','+10% Fuerza c/1500 daño recibido (máx.3).','on_damage_taken','increase_strength_pct',10,3,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='spider_man'),
   'Sentido Arácnido','Obliga IA a usar ataque aleatorio y revela elección.','before_enemy_action','force_random_attack',0,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='black_panther'),
   'Instinto del Rey','Primer hit: -50% daño.','on_first_hit','reduce_damage_pct',50,1,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='captain_america'),
   'Aguantaría Todo el Día','<50% HP→hasta 40% reducción.','below_hp_pct','reduce_damage_pct',40,0,0),
  ((SELECT id FROM personaje WHERE nombre_codigo='deadpool'),
   'No Me Puedes Matar','Revive 1 vez con 50% HP.','on_fatal_damage','revive_pct',50,1,0);

-- 6) Escenarios
CREATE TABLE escenario (
  id          INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre      TEXT    NOT NULL UNIQUE,
  descripcion TEXT
);
INSERT INTO escenario (nombre, descripcion) VALUES
  ('Limbo',          'Magik +700% Poder y x3 resto; no curaciones'),
  ('Plano Mental',   'Legion +1000% Poder y x5 resto'),
  ('Dimensión Espejo','Doctor Strange +400% Poder');

-- 7) Modificadores de escenario
CREATE TABLE escenario_modificador (
  id               INTEGER PRIMARY KEY AUTOINCREMENT,
  escenario_id     INTEGER NOT NULL REFERENCES escenario(id) ON DELETE CASCADE,
  atributo         TEXT    NOT NULL,
  modificador_tipo TEXT    NOT NULL,
  valor            INTEGER NOT NULL,
  duracion_turnos  INTEGER NOT NULL DEFAULT 0
);
INSERT INTO escenario_modificador (escenario_id, atributo, modificador_tipo, valor, duracion_turnos) VALUES
  -- Limbo
  ((SELECT id FROM escenario WHERE nombre='Limbo'),        'poder','add_pct',700,0),
  ((SELECT id FROM escenario WHERE nombre='Limbo'),        'vida','multiplier',3,0),
  ((SELECT id FROM escenario WHERE nombre='Limbo'),        'fuerza','multiplier',3,0),
  ((SELECT id FROM escenario WHERE nombre='Limbo'),        'velocidad','multiplier',3,0),
  ((SELECT id FROM escenario WHERE nombre='Limbo'),        'disable_heal','flag',1,0),
  -- Plano Mental
  ((SELECT id FROM escenario WHERE nombre='Plano Mental'),'poder','add_pct',1000,0),
  ((SELECT id FROM escenario WHERE nombre='Plano Mental'),'vida','multiplier',5,0),
  ((SELECT id FROM escenario WHERE nombre='Plano Mental'),'fuerza','multiplier',5,0),
  ((SELECT id FROM escenario WHERE nombre='Plano Mental'),'velocidad','multiplier',5,0),
  -- Dimensión Espejo
  ((SELECT id FROM escenario WHERE nombre='Dimensión Espejo'),'poder','add_pct',400,0);

-- 8) Escenario–Personaje
CREATE TABLE escenario_personaje (
  escenario_id INTEGER NOT NULL REFERENCES escenario(id) ON DELETE CASCADE,
  personaje_id INTEGER NOT NULL REFERENCES personaje(id) ON DELETE CASCADE,
  PRIMARY KEY (escenario_id, personaje_id)
);
INSERT INTO escenario_personaje (escenario_id, personaje_id) VALUES
  ((SELECT id FROM escenario WHERE nombre='Limbo'),           (SELECT id FROM personaje WHERE nombre_codigo='magik')),
  ((SELECT id FROM escenario WHERE nombre='Limbo'),           (SELECT id FROM personaje WHERE nombre_codigo='magik_darkchild')),
  ((SELECT id FROM escenario WHERE nombre='Plano Mental'),    (SELECT id FROM personaje WHERE nombre_codigo='legion')),
  ((SELECT id FROM escenario WHERE nombre='Dimensión Espejo'),(SELECT id FROM personaje WHERE nombre_codigo='doctor_strange'));

COMMIT;
PRAGMA foreign_keys = ON;
