CREATE DATABASE "petadopt"

CREATE TABLE users(
    email VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE user_groups(
    email VARCHAR(255) NOT NULL,
    groupnameVARCHAR(32) NOT NULL,
    PRIMARY KEY(email)
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
    health_status VARCHAR(250),
    adoption_cost DECIMAL NOT NULL,
    shelter_name VARCHAR(50)
);

CREATE TABLE adoptionRequests(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    clientEmail VARCHAR(50) NOT NULL,
    petId INTEGER NOT NULL,
    requestDate DATE NOT NULL,
    petStatus VARCHAR(20) NOT NULL
);