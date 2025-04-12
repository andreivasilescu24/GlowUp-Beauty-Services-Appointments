set
    search_path = project, pg_catalog;

CREATE TABLE appointment_status
(
    id   integer,
    name text,
    PRIMARY KEY (id)
);

ALTER TABLE appointment
    DROP COLUMN status;

ALTER TABLE appointment
    ADD COLUMN status_id integer NOT NULL;


ALTER TABLE appointment
    ADD CONSTRAINT fk_status
        FOREIGN KEY (status_id)
            REFERENCES appointment_status(id);

INSERT INTO appointment_status (id, name)
VALUES
(1, 'SCHEDULED'),
(2, 'CANCELED'),
(3, 'COMPLETED');

