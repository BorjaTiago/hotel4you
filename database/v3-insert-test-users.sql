-- v3: add one test user for each role that has an account (manager, receptionist, restaurant).
-- For local tests only. Do not run this script on a real hotel database.
-- Run after v2. You can run it again: it skips what already exists.
-- CLIENT and SUPPLIER get no user: they have no account (Q-13, Q-17).
--
-- Test logins (username / password):
--   gestor       / 1234
--   rececionista / 1234
--   restaurante  / 1234
-- The hashes are bcrypt hashes made with AuthService.hashPassword.

USE hotel4you;

IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'gestor')
    INSERT INTO users (name, username, password_hash, role_id)
    VALUES (N'Gestor de Teste', 'gestor',
            '$2a$12$2hygRmkAP.wl0t9XOpS8OOKqWb4eGSMVSBuox8QT4IzDyuz25rF6a',
            (SELECT id FROM roles WHERE name = 'MANAGER'));

IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'rececionista')
    INSERT INTO users (name, username, password_hash, role_id)
    VALUES (N'Rececionista de Teste', 'rececionista',
            '$2a$12$XMOIWPqGThGcGBGt0bZebeIMnz12JVQgpduyqpsoC5/SWB8e7wTcu',
            (SELECT id FROM roles WHERE name = 'RECEPTIONIST'));

IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'restaurante')
    INSERT INTO users (name, username, password_hash, role_id)
    VALUES (N'Restaurante de Teste', 'restaurante',
            '$2a$12$NpLYcyKjlBI9b.hWxe2HbumHLcJ67GyZNn5s2LsH70R4hOZSa0ElC',
            (SELECT id FROM roles WHERE name = 'RESTAURANT'));
