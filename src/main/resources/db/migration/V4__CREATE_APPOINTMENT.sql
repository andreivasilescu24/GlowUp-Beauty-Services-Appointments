SET
    search_path = project, pg_catalog;

CREATE TABLE appointment
(
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id        UUID                                                                     NOT NULL,
    salon_id         UUID                                                                     NOT NULL,
    employee_id      UUID                                                                     NOT NULL,
    service_id       UUID                                                                     NOT NULL,
    appointment_time TIMESTAMP                                                                NOT NULL,
    status           TEXT CHECK (status IN ('Pending', 'Confirmed', 'Completed', 'Canceled')) NOT NULL,
    FOREIGN KEY (salon_id) REFERENCES beauty_salon (id) ON DELETE CASCADE,
    FOREIGN KEY (employee_id, service_id) REFERENCES employee_services (employee_id, service_id) ON DELETE CASCADE
);