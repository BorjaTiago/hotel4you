-- v1: create the hotel4you database.
-- Run this script first. You can run it again: it skips what already exists.
-- The collation CI_AS means usernames are not case sensitive (Rita and rita are the same).

IF DB_ID('hotel4you') IS NULL
    CREATE DATABASE hotel4you COLLATE SQL_Latin1_General_CP1_CI_AS;
