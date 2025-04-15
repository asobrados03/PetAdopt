CREATE DATABASE "petadopt"

CREATE TABLE users(
    email VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE clients(
    email VARCHAR(50) PRIMARY KEY,
    surname VARCHAR(50) NOT NULL,
    nif CHAR(9) NOT NULL,
    addres VARCHAR(100) NOT NULL,
    phone VARCHAR(13) NOT NULL,
    birth_date DATE NOT NULL
);

CREATE TABLE shelters(
    email VARCHAR(50) PRIMARY KEY,
    cif CHAR(9) NOT NULL,
    addres VARCHAR(100) NOT NULL,
    phone VARCHAR(13) NOT NULL,
    shelter_name VARCHAR(50) NOT NULL,
    authorized BOOLEAN DEFAULT FALSE
);

CREATE TABLE pets(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    species VARCHAR(50) NOT NULL,
    breed VARCHAR(50) NOT NULL,
    age INT NOT NULL,
    healt_status VARCHAR(250),
    adoption_cost DECIMAL NOT NULL,
    shelter_name VARCHAR(50)
);

CREATE TYPE status_type AS ENUM ('aceptada', 'pendiente', 'rechazada');

CREATE TABLE adoption_requests(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client_email VARCHAR(50) NOT NULL,
    pet_id INTEGER NOT NULL,
    request_date DATE NOT NULL,
    status status_type DEFAULT 'pendiente'
);