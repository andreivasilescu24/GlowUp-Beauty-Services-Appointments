SET
    search_path = project, pg_catalog;

Alter table beauty_service
    add column duration integer not null check (duration > 0) default 0;