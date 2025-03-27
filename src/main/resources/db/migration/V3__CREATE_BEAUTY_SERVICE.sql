SET
    search_path = project, pg_catalog;

CREATE TABLE beauty_service
(
    id          uuid,
    name        text             not null,
    description text,
    price       double precision not null check (price >= 0),
    salon_id    uuid,
    PRIMARY KEY (id),
    FOREIGN KEY (salon_id) REFERENCES project.beauty_salon (id) ON DELETE CASCADE
);

CREATE TABLE employee_services
(
    employee_id UUID NOT NULL,
    service_id  UUID NOT NULL,
    PRIMARY KEY (employee_id, service_id),
    FOREIGN KEY (employee_id) REFERENCES project.employees (id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES beauty_service (id) ON DELETE CASCADE
);