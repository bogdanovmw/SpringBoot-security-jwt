INSERT INTO users (email, username, password) VALUES ('user@mail.ru', 'user', '$2a$10$WwMAuXVXefWuZry.3LjHoOpN6rw4Yi7bnncLqzAWhELIoGRyNwSpa');
INSERT INTO users (email, username, password) VALUES ('mod@mail.ru', 'mod', '$2a$10$77VJirOQ68IwuSgtigZMlOfZIGl6BHPnBhAl8/9wqyifkrt6lZ0N6');

INSERT INTO user_roles (user_id, role_id) VALUES (2, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (3, 2);