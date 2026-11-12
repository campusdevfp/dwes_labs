INSERT INTO artistas (id, nombre, pais) VALUES
  (1, 'Rosalía', 'España'), (2, 'Daft Punk', 'Francia'),
  (3, 'Radiohead', 'Reino Unido'), (4, 'Natalia Lafourcade', 'México')
ON CONFLICT (id) DO NOTHING;

INSERT INTO sellos (id, nombre, activo) VALUES
  (1, 'Sony Music', true), (2, 'Warner', true),
  (3, 'XL Recordings', true), (4, 'Sello Extinto', false)
ON CONFLICT (id) DO NOTHING;

INSERT INTO discos (id, titulo, anio, precio, artista_id, sello_id,
                    is_deleted, created_at, updated_at) VALUES
  (1, 'El Mal Querer', 2018, 18.90, 1, 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, 'Motomami',      2022, 21.50, 1, 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, 'Discovery',     2001, 24.00, 2, 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, 'Random Access Memories', 2013, 27.90, 2, 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (5, 'OK Computer',   1997, 22.00, 3, 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (6, 'In Rainbows',   2007, 19.95, 3, 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (7, 'Musas',         2017, 16.50, 4, 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

SELECT setval('artistas_id_seq', (SELECT COALESCE(MAX(id),0)+1 FROM artistas), false);
SELECT setval('sellos_id_seq',   (SELECT COALESCE(MAX(id),0)+1 FROM sellos),   false);
SELECT setval('discos_id_seq',   (SELECT COALESCE(MAX(id),0)+1 FROM discos),   false);
