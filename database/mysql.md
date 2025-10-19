# MySQL Commands

Essential MySQL commands and queries.

## Connection

```bash
# Connect to MySQL
mysql -u username -p

# Connect to specific database
mysql -u username -p database_name

# Connect to remote server
mysql -h hostname -u username -p database_name

# Connect with password (not recommended)
mysql -u username -ppassword database_name
```

## Database Operations

```sql
-- List databases
SHOW DATABASES;

-- Create database
CREATE DATABASE mydb;

-- Create with charset
CREATE DATABASE mydb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Drop database
DROP DATABASE mydb;

-- Use database
USE mydb;

-- Show current database
SELECT DATABASE();
```

## Table Operations

```sql
-- List tables
SHOW TABLES;

-- Describe table
DESCRIBE users;
DESC users;
SHOW COLUMNS FROM users;

-- Create table
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Show create table statement
SHOW CREATE TABLE users;

-- Drop table
DROP TABLE users;

-- Rename table
RENAME TABLE old_name TO new_name;

-- Add column
ALTER TABLE users ADD COLUMN age INT;

-- Drop column
ALTER TABLE users DROP COLUMN age;

-- Modify column
ALTER TABLE users MODIFY COLUMN email VARCHAR(150);

-- Change column name and type
ALTER TABLE users CHANGE old_name new_name VARCHAR(100);
```

## CRUD Operations

```sql
-- INSERT
INSERT INTO users (username, email, password_hash)
VALUES ('john', 'john@example.com', 'hash123');

-- Multiple inserts
INSERT INTO users (username, email, password_hash)
VALUES 
  ('alice', 'alice@example.com', 'hash456'),
  ('bob', 'bob@example.com', 'hash789');

-- INSERT IGNORE (skip duplicates)
INSERT IGNORE INTO users (username, email, password_hash)
VALUES ('john', 'john@example.com', 'hash123');

-- INSERT ... ON DUPLICATE KEY UPDATE
INSERT INTO users (id, username, email, password_hash)
VALUES (1, 'john', 'john@example.com', 'newhash')
ON DUPLICATE KEY UPDATE email = VALUES(email);

-- SELECT
SELECT * FROM users;

SELECT username, email FROM users;

SELECT * FROM users WHERE id = 1;

SELECT * FROM users WHERE username LIKE 'j%';

SELECT * FROM users ORDER BY created_at DESC;

SELECT * FROM users LIMIT 10 OFFSET 20;

-- UPDATE
UPDATE users SET email = 'newemail@example.com' WHERE id = 1;

UPDATE users SET updated_at = NOW() WHERE id = 1;

-- DELETE
DELETE FROM users WHERE id = 1;

DELETE FROM users WHERE created_at < '2023-01-01';

-- TRUNCATE (fast delete all)
TRUNCATE TABLE users;
```

## Indexes

```sql
-- Create index
CREATE INDEX idx_users_email ON users(email);

-- Create unique index
CREATE UNIQUE INDEX idx_users_username ON users(username);

-- Create composite index
CREATE INDEX idx_users_name_email ON users(username, email);

-- Show indexes
SHOW INDEX FROM users;

-- Drop index
DROP INDEX idx_users_email ON users;
-- Or
ALTER TABLE users DROP INDEX idx_users_email;
```

## Constraints

```sql
-- Primary key
ALTER TABLE users ADD PRIMARY KEY (id);

-- Foreign key
ALTER TABLE posts ADD CONSTRAINT fk_user
  FOREIGN KEY (user_id) REFERENCES users(id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

-- Unique constraint
ALTER TABLE users ADD CONSTRAINT unique_email UNIQUE (email);

-- Check constraint (MySQL 8.0+)
ALTER TABLE users ADD CONSTRAINT check_age CHECK (age >= 18);

-- Not null
ALTER TABLE users MODIFY email VARCHAR(100) NOT NULL;
```

## Joins

```sql
-- Inner join
SELECT u.username, p.title
FROM users u
INNER JOIN posts p ON u.id = p.user_id;

-- Left join
SELECT u.username, p.title
FROM users u
LEFT JOIN posts p ON u.id = p.user_id;

-- Right join
SELECT u.username, p.title
FROM users u
RIGHT JOIN posts p ON u.id = p.user_id;

-- Multiple joins
SELECT u.username, p.title, c.content
FROM users u
INNER JOIN posts p ON u.id = p.user_id
INNER JOIN comments c ON p.id = c.post_id;
```

## Aggregation

```sql
-- Count
SELECT COUNT(*) FROM users;

SELECT COUNT(DISTINCT email) FROM users;

-- Sum, Avg, Min, Max
SELECT AVG(age) FROM users;

SELECT MIN(created_at), MAX(created_at) FROM users;

-- Group by
SELECT country, COUNT(*) as user_count
FROM users
GROUP BY country;

SELECT country, AVG(age) as avg_age
FROM users
GROUP BY country
HAVING COUNT(*) > 10;
```

## Subqueries

```sql
-- Subquery in WHERE
SELECT * FROM users
WHERE id IN (SELECT user_id FROM posts WHERE status = 'published');

-- Subquery in FROM
SELECT avg_age FROM (
  SELECT AVG(age) as avg_age FROM users
) AS subquery;

-- Exists
SELECT * FROM users u
WHERE EXISTS (
  SELECT 1 FROM posts p WHERE p.user_id = u.id
);
```

## Transactions

```sql
-- Start transaction
START TRANSACTION;
-- Or
BEGIN;

INSERT INTO users (username, email, password_hash)
VALUES ('test', 'test@example.com', 'hash');

UPDATE accounts SET balance = balance - 100 WHERE id = 1;
UPDATE accounts SET balance = balance + 100 WHERE id = 2;

-- Commit transaction
COMMIT;

-- Rollback transaction
ROLLBACK;
```

## User Management

```sql
-- Create user
CREATE USER 'myuser'@'localhost' IDENTIFIED BY 'mypassword';

-- Create user with host wildcard
CREATE USER 'myuser'@'%' IDENTIFIED BY 'mypassword';

-- Grant privileges
GRANT ALL PRIVILEGES ON mydb.* TO 'myuser'@'localhost';

GRANT SELECT, INSERT, UPDATE ON mydb.users TO 'myuser'@'localhost';

-- Apply changes
FLUSH PRIVILEGES;

-- Show grants
SHOW GRANTS FOR 'myuser'@'localhost';

-- Revoke privileges
REVOKE ALL PRIVILEGES ON mydb.* FROM 'myuser'@'localhost';

-- Drop user
DROP USER 'myuser'@'localhost';

-- Change password
ALTER USER 'myuser'@'localhost' IDENTIFIED BY 'newpassword';
```

## Backup & Restore

```bash
# Backup database
mysqldump -u username -p database_name > backup.sql

# Backup with compression
mysqldump -u username -p database_name | gzip > backup.sql.gz

# Backup all databases
mysqldump -u username -p --all-databases > all_databases.sql

# Backup specific tables
mysqldump -u username -p database_name table1 table2 > tables.sql

# Restore database
mysql -u username -p database_name < backup.sql

# Restore compressed backup
gunzip -c backup.sql.gz | mysql -u username -p database_name
```

## Useful Queries

```sql
-- Table size
SELECT 
  table_name,
  ROUND(((data_length + index_length) / 1024 / 1024), 2) AS size_mb
FROM information_schema.TABLES
WHERE table_schema = 'mydb'
ORDER BY size_mb DESC;

-- Database size
SELECT 
  table_schema AS database_name,
  ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS size_mb
FROM information_schema.TABLES
GROUP BY table_schema;

-- Running queries
SHOW PROCESSLIST;

-- Full process list
SHOW FULL PROCESSLIST;

-- Kill query
KILL QUERY process_id;
KILL CONNECTION process_id;

-- Show table status
SHOW TABLE STATUS FROM mydb;
```

## MySQL Configuration

```sql
-- Show variables
SHOW VARIABLES;

SHOW VARIABLES LIKE 'max_connections';

-- Set variable (session)
SET SESSION sql_mode = 'STRICT_TRANS_TABLES';

-- Set variable (global)
SET GLOBAL max_connections = 200;

-- Show status
SHOW STATUS;

SHOW STATUS LIKE 'Threads_connected';
```

## Useful Commands

```bash
# MySQL command line
mysql> USE mydb;           # Select database
mysql> SOURCE file.sql;    # Execute SQL file
mysql> \G                  # Vertical output
mysql> exit                # Exit MySQL
mysql> quit                # Exit MySQL
```
