SET
    search_path = project, pg_catalog;

CREATE TABLE appointment_status
(
    id   integer,
    name text,
    PRIMARY KEY (id)
);

INSERT INTO appointment_status (id, name)
VALUES
    (1, 'CONFIRMED'),
    (2, 'CANCELED'),
    (3, 'COMPLETED');

CREATE TABLE appointment
(
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id        UUID      NOT NULL,
    salon_id         UUID      NOT NULL,
    employee_id      UUID      NOT NULL,
    service_id       UUID      NOT NULL,
    appointment_time TIMESTAMP NOT NULL,
    status_id        integer   NOT NULL,
    FOREIGN KEY (salon_id) REFERENCES beauty_salon (id) ON DELETE CASCADE,
    FOREIGN KEY (employee_id, service_id) REFERENCES employee_services (employee_id, service_id) ON DELETE CASCADE,
    FOREIGN KEY (client_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES appointment_status (id) ON DELETE CASCADE
);

