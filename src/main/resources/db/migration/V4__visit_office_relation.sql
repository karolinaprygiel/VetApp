ALTER TABLE visits
ADD COLUMN office_id INTEGER REFERENCES offices(id);