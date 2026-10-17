-- v1: create the hotel4you database.
-- Run this script first. You can run it again: it skips what already exists.

IF DB_ID('hotel4you') IS NULL
    CREATE DATABASE hotel4you;
