INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_MODERATOR');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO users (email, username, password) VALUES ('gai_znak@mail.ru', 'admin', '$2a$10$U9f8v4Pvvso4ClAXwxDSoOzy2fnYuKjcuz.5ZmwPAdPLcIjWfh8fG');
INSERT INTO users (email, username, password) VALUES ('user@mail.ru', 'user', '$2a$10$WwMAuXVXefWuZry.3LjHoOpN6rw4Yi7bnncLqzAWhELIoGRyNwSpa');
INSERT INTO users (email, username, password) VALUES ('mod@mail.ru', 'mod', '$2a$10$77VJirOQ68IwuSgtigZMlOfZIGl6BHPnBhAl8/9wqyifkrt6lZ0N6');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 3);

INSERT INTO user_roles (user_id, role_id) VALUES (2, 1);

INSERT INTO user_roles (user_id, role_id) VALUES (3, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (3, 2);