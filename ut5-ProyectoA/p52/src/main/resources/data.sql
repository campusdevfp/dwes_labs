MERGE INTO categorias (id, nombre) KEY(id) VALUES
  (1, 'DISNEY'), (2, 'MARVEL'), (3, 'ANIME'), (4, 'OTROS');

MERGE INTO funkos (id, nombre, precio, cantidad, categoria_id,
                   is_deleted, created_at, updated_at) KEY(id) VALUES
  (1, 'Mickey Mouse', 15.99, 10, 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, 'Stitch',       17.00,  8, 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, 'Spider-Man',   19.99,  5, 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, 'Iron Man',     21.50,  2, 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (5, 'Goku',         22.50,  3, 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (6, 'Naruto',       18.75,  0, 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

ALTER TABLE categorias ALTER COLUMN id RESTART WITH 5;
ALTER TABLE funkos     ALTER COLUMN id RESTART WITH 7;