-- This script will be executed after the databases are created by the create-multiple-postgresql-databases.sh script

-- Connect to auth_service_db and create initial schema
\connect auth_service_db;

-- Add your auth service schema initialization here
-- Example:
-- CREATE TABLE IF NOT EXISTS users (
--     id SERIAL PRIMARY KEY,
--     username VARCHAR(255) NOT NULL,
--     password VARCHAR(255) NOT NULL,
--     email VARCHAR(255) NOT NULL,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );

-- Connect to main_service_db and create initial schema
\connect main_service_db;

-- Add your main service schema initialization here

-- Connect to scrim_service_db and create initial schema
\connect scrim_service_db;

-- Add your scrim service schema initialization here

-- Connect to team_service_db and create initial schema
\connect team_service_db;

-- Add your team service schema initialization here

-- Connect to user_service_db and create initial schema
\connect user_service_db;

-- Add your user service schema initialization here
