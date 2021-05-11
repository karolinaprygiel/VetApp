create table clients
(
    id SERIAL PRIMARY KEY,
    name varchar(255),
    surname varchar(255)
);



create table animals(
    id SERIAL PRIMARY KEY,
    name VARCHAR(127),
    type INTEGER ,
    year_of_birth INTEGER NOT NULL,
    month_o
    client_id INTEGER REFERENCES clients(id)
    );

create table  vets
(
    id SERIAL PRIMARY KEY,
    name varchar(255),
    shift_end timestamp,
    shift_start timestamp,
    surname varchar(255)
    );

create table visits
(
    id SERIAL PRIMARY KEY,
    description varchar(255),
    duration interval,
    price numeric(19,2),
    start_time timestamp,
    visit_status integer,
    animal_id INTEGER references animals(id),
    client_id INTEGER references clients(id),
    vet_id INTEGER references vets(id)
    );





