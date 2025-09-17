-- V2__Update_user_table_structure.sql

-- Remove username column and add first_name, last_name
ALTER TABLE users DROP COLUMN username;
ALTER TABLE users DROP COLUMN full_name;
ALTER TABLE users ADD COLUMN first_name VARCHAR(50) NOT NULL DEFAULT '';
ALTER TABLE users ADD COLUMN last_name VARCHAR(50) NOT NULL DEFAULT '';

-- Rename phone_number to phone
ALTER TABLE users RENAME COLUMN phone_number TO phone;

-- Rename active to is_active  
ALTER TABLE users RENAME COLUMN active TO is_active;

-- Remove the default values after adding the columns
ALTER TABLE users ALTER COLUMN first_name DROP DEFAULT;
ALTER TABLE users ALTER COLUMN last_name DROP DEFAULT;

-- Remove old indexes that referenced username
DROP INDEX IF EXISTS idx_users_username;