set
    search_path = project, pg_catalog;

ALTER TABLE working_hours
    ALTER COLUMN start_time TYPE time
        USING start_time::time;


ALTER TABLE working_hours
    ALTER COLUMN end_time TYPE time
        USING end_time::time;