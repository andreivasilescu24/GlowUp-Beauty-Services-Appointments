SET
    search_path = project, pg_catalog;

CREATE TABLE beauty_saloon
(
    id            uuid,
    name          text,
    city          text,
    address       text,
    email         text,
    phone         text,
    num_employees integer,
    PRIMARY KEY (id)
);