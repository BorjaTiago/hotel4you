-- v2: create the roles and users tables, and add the 5 roles.
-- Run after v1. You can run it again: it skips what already exists.

USE hotel4you;

-- Table: roles
IF OBJECT_ID('roles') IS NULL
    CREATE TABLE roles (
        id   TINYINT     IDENTITY NOT NULL,
        name VARCHAR(20) NOT NULL,
        CONSTRAINT pk_roles      PRIMARY KEY (id),
        CONSTRAINT uq_roles_name UNIQUE (name)
    );

-- Table: users
IF OBJECT_ID('users') IS NULL
    CREATE TABLE users (
        id            SMALLINT      IDENTITY NOT NULL,
        name          NVARCHAR(100) NOT NULL,
        username      VARCHAR(50)   NOT NULL,
        password_hash VARCHAR(255)  NOT NULL,
        role_id       TINYINT       NOT NULL,
        CONSTRAINT pk_users          PRIMARY KEY (id),
        CONSTRAINT uq_users_username UNIQUE (username),
        CONSTRAINT fk_users_role     FOREIGN KEY (role_id) REFERENCES roles (id)
    );

-- The 5 roles
IF NOT EXISTS (SELECT 1 FROM roles WHERE name = 'CLIENT')       INSERT INTO roles (name) VALUES ('CLIENT');
IF NOT EXISTS (SELECT 1 FROM roles WHERE name = 'RECEPTIONIST') INSERT INTO roles (name) VALUES ('RECEPTIONIST');
IF NOT EXISTS (SELECT 1 FROM roles WHERE name = 'MANAGER')      INSERT INTO roles (name) VALUES ('MANAGER');
IF NOT EXISTS (SELECT 1 FROM roles WHERE name = 'RESTAURANT')   INSERT INTO roles (name) VALUES ('RESTAURANT');
IF NOT EXISTS (SELECT 1 FROM roles WHERE name = 'SUPPLIER')     INSERT INTO roles (name) VALUES ('SUPPLIER');
