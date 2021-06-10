ALTER TABLE aufgabe ADD CONSTRAINT unique_name UNIQUE (name);
ALTER TABLE file ADD CONSTRAINT unique_name_aufgabe_id UNIQUE (name,aufgabe_id);