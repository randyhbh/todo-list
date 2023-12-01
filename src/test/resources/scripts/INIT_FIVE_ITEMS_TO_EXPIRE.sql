INSERT INTO item (id, status, description, created_at, due_at, done_at)
VALUES (1, 'NOT_DONE', 'test 1 description', now() - 5, now() - 1, null);
INSERT INTO item (id, status, description, created_at, due_at, done_at)
VALUES (2, 'NOT_DONE', 'test 2 description', now() - 5, now() - 2, null);
INSERT INTO item (id, status, description, created_at, due_at, done_at)
VALUES (3, 'NOT_DONE', 'test 3 description', now() - 5, now() - 3, null);
INSERT INTO item (id, status, description, created_at, due_at, done_at)
VALUES (4, 'DONE', 'test 4 description', now(), now() + 4, now() + 2);
INSERT INTO item (id, status, description, created_at, due_at, done_at)
VALUES (5, 'PAST_DUE', 'test 5 description', now(), now() + 5, null);
