SET
    search_path = project, pg_catalog;

CREATE TABLE category
(
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT UNIQUE NOT NULL
);

CREATE TABLE beauty_salon
(
    id            uuid,
    name          text,
    city          text,
    address       text,
    email         text,
    phone         text,
    num_employees integer,
    category_id   UUID,
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE
);

CREATE TABLE employees
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    UUID UNIQUE NOT NULL,
    salon_id   UUID        NOT NULL,
    experience INT CHECK (experience >= 0),
    FOREIGN KEY (user_id) REFERENCES project.users (id) ON DELETE CASCADE,
    FOREIGN KEY (salon_id) REFERENCES beauty_salon (id) ON DELETE CASCADE
);