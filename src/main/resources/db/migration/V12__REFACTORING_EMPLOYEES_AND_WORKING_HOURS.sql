set
    search_path = project, pg_catalog;

ALTER TABLE beauty_service
    DROP COLUMN price,
    DROP COLUMN duration;

ALTER TABLE employee_services
    ADD COLUMN price NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
    ADD COLUMN duration INT NOT NULL CHECK (duration > 0);

ALTER TABLE working_hours
    DROP COLUMN date,
    ADD COLUMN day INT NOT NULL CHECK (day BETWEEN 1 AND 7);