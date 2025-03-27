SET
    search_path = project, pg_catalog;


CREATE TABLE working_hours
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_id UUID      NOT NULL,
    start_time  TIMESTAMP NOT NULL,
    end_time    TIMESTAMP NOT NULL,

    FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE
);
