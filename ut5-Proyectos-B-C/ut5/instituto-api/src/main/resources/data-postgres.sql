INSERT INTO cursos (id, codigo, nombre, plazas, matriculados) VALUES
  (1, '1DAW', 'Primero de DAW', 30, 0),
  (2, '2DAW', 'Segundo de DAW', 25, 0),
  (3, '1ASIR','Primero de ASIR', 30, 0)
ON CONFLICT (id) DO NOTHING;

INSERT INTO alumnos (id, expediente, nombre, apellidos, curso_id,
                     activo, created_at, updated_at) VALUES
  (1, 'EXP-0001', 'Lucía', 'García Soto',   2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, 'EXP-0002', 'Marc',  'Puig Ferrer',   2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, 'EXP-0003', 'Aitor', 'Gaztelu Mendi', 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, 'EXP-0004', 'Sara',  'Nieto Ramos',   3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

INSERT INTO notas (id, asignatura, valor, fecha, alumno_id) VALUES
  (1, 'DWES', 8.5, '2026-01-20', 1),
  (2, 'DWEC', 7.0, '2026-01-21', 1),
  (3, 'DWES', 5.5, '2026-01-20', 2),
  (4, 'DWES', 9.0, '2026-01-20', 3)
ON CONFLICT (id) DO NOTHING;

UPDATE cursos c SET matriculados =
  (SELECT count(*) FROM alumnos a WHERE a.curso_id = c.id AND a.activo = true);

SELECT setval('cursos_id_seq',  (SELECT COALESCE(MAX(id),0)+1 FROM cursos),  false);
SELECT setval('alumnos_id_seq', (SELECT COALESCE(MAX(id),0)+1 FROM alumnos), false);
SELECT setval('notas_id_seq',   (SELECT COALESCE(MAX(id),0)+1 FROM notas),   false);
