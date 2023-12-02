INSERT INTO item (id, status, description, created_at, done_at, due_at)
VALUES (1, 'DONE', 'test description', now(), TIMESTAMPADD('MINUTE', 2, now()), TIMESTAMPADD('MINUTE', 5, now()));