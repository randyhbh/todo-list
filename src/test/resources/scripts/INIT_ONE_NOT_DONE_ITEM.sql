INSERT INTO item (id, status, description, created_at, due_at, done_at)
VALUES (1, 'NOT_DONE', 'test description', now(), TIMESTAMPADD('MINUTE', 5, now()), null);
