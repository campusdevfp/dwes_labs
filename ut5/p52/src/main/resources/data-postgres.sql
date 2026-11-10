INSERT INTO categorias (id, nombre) VALUES
  (1, 'DISNEY'), (2, 'MARVEL'), (3, 'ANIME'), (4, 'OTROS')
ON CONFLICT (id) DO NOTHING;

INSERT INTO funkos (id, nombre, precio, cantidad, categoria_id,
                    is_deleted, created_at, updated_at) VALUES
  (1, 'Mickey Mouse', 15.99, 10, 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, 'Stitch',       17.00,  8, 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, 'Spider-Man',   19.99,  5, 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, 'Iron Man',     21.50,  2, 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (5, 'Goku',         22.50,  3, 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (6, 'Naruto',       18.75,  0, 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Dejar la secuencia donde toca, o el primer POST choca
SELECT setval('categorias_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM categorias), false);
SELECT setval('funkos_id_seq',     (SELECT COALESCE(MAX(id), 0) + 1 FROM funkos),     false);