SET
    search_path = project, pg_catalog;

Alter table working_hours
    add column date DATE not null;

Alter table working_hours
alter column start_time DROP NOT NULL,
alter column end_time DROP NOT NULL;