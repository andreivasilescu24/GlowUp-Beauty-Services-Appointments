SET
    search_path = project, pg_catalog;

ALTER TABLE project.users
    RENAME column username to name;