CREATE TABLE users (
    id bigserial PRIMARY KEY NOT NULL,
    email VARCHAR (50) UNIQUE NOT NULL,
    username VARCHAR (20) UNIQUE NOT NULL,
    password VARCHAR (120) NOT NULL
);

CREATE TABLE roles (
    id serial PRIMARY KEY NOT NULL,
    name VARCHAR (20) UNIQUE NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users (id),
    role_id INTEGER NOT NULL REFERENCES roles (id)
);

CREATE TABLE refresh_token (
    id bigserial PRIMARY KEY NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users (id),
    token VARCHAR (255) UNIQUE NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL
);