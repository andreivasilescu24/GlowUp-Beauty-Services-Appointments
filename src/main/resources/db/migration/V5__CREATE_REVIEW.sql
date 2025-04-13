SET
    search_path = project, pg_catalog;


CREATE TABLE review
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id   UUID                               NOT NULL,
    salon_id    UUID,
    rating      INT CHECK (rating BETWEEN 1 AND 5) NOT NULL,
    comment     TEXT,
    created_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (client_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (salon_id) REFERENCES beauty_salon (id) ON DELETE CASCADE
);