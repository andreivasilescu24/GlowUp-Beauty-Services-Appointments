SET
    search_path = project, pg_catalog;


CREATE TABLE working_hours
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_id UUID      NOT NULL,
    start_time  time,
    end_time    time,
    day INT NOT NULL CHECK (day BETWEEN 1 AND 7),

    FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE
);
