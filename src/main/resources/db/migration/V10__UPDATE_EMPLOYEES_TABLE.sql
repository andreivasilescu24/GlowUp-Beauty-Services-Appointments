set
    search_path = project, pg_catalog;

ALTER TABLE employees
DROP CONSTRAINT employees_user_id_fkey;

ALTER TABLE employees
DROP COLUMN user_id;