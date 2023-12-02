INSERT INTO item (id, status, description, created_at, due_at, done_at)
VALUES (1, 'NOT_DONE', 'test 1 description', now(), TIMESTAMPADD('MINUTE', 1, now()), null);
INSERT INTO item (id, status, description, created_at, due_at, done_at)
VALUES (2, 'NOT_DONE', 'test 2 description', now(), TIMESTAMPADD('MINUTE', 2, now()), null);
INSERT INTO item (id, status, description, created_at, due_at, done_at)
VALUES (3, 'NOT_DONE', 'test 3 description', now(), TIMESTAMPADD('MINUTE', 3, now()), null);
INSERT INTO item (id, status, description, created_at, due_at, done_at)
VALUES (4, 'DONE', 'test 4 description', TIMESTAMPADD('MINUTE', -5, now()), TIMESTAMPADD('MINUTE', 1, now()), now());
INSERT INTO item (id, status, description, created_at, due_at, done_at)
VALUES (5, 'PAST_DUE', 'test 5 description', TIMESTAMPADD('MINUTE', -5, now()), TIMESTAMPADD('MINUTE', -4, now()), null);
