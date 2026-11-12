MERGE INTO cursos (id, codigo, nombre, plazas, matriculados) KEY(id) VALUES
  (1, '1DAW', 'Primero de DAW', 30, 0),
  (2, '2DAW', 'Segundo de DAW', 25, 0),
  (3, '1ASIR','Primero de ASIR', 30, 0);

MERGE INTO alumnos (id, expediente, nombre, apellidos, curso_id,
                    activo, created_at, updated_at) KEY(id) VALUES
  (1, 'EXP-0001', 'Lucía', 'García Soto',   2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, 'EXP-0002', 'Marc',  'Puig Ferrer',   2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, 'EXP-0003', 'Aitor', 'Gaztelu Mendi', 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, 'EXP-0004', 'Sara',  'Nieto Ramos',   3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

MERGE INTO notas (id, asignatura, valor, fecha, alumno_id) KEY(id) VALUES
  (1, 'DWES', 8.5, '2026-01-20', 1),
  (2, 'DWEC', 7.0, '2026-01-21', 1),
  (3, 'DWES', 5.5, '2026-01-20', 2),
  (4, 'DWES', 9.0, '2026-01-20', 3);

UPDATE cursos SET matriculados =
  (SELECT count(*) FROM alumnos a WHERE a.curso_id = cursos.id AND a.activo = true);

ALTER TABLE cursos  ALTER COLUMN id RESTART WITH 4;
ALTER TABLE alumnos ALTER COLUMN id RESTART WITH 5;
ALTER TABLE notas   ALTER COLUMN id RESTART WITH 5;
