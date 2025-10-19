# PostgreSQL Commands

Essential PostgreSQL commands and queries.

## Connection

```bash
# Connect to database
psql -U username -d database_name

# Connect to remote database
psql -h hostname -U username -d database_name

# Connect with password prompt
psql -U username -W -d database_name

# Connect via URL
psql postgresql://username:password@hostname:5432/database_name
```

## Database Operations

```sql
-- List databases
\l

-- Create database
CREATE DATABASE mydb;

-- Drop database
DROP DATABASE mydb;

-- Connect to database
\c mydb

-- Show current database
SELECT current_database();
```

## Table Operations

```sql
-- List tables
\dt

-- Describe table
\d table_name

-- Create table
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Drop table
DROP TABLE users;

-- Rename table
ALTER TABLE old_name RENAME TO new_name;

-- Add column
ALTER TABLE users ADD COLUMN age INTEGER;

-- Drop column
ALTER TABLE users DROP COLUMN age;

-- Modify column
ALTER TABLE users ALTER COLUMN email TYPE TEXT;
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

-- INSERT and return
INSERT INTO users (username, email, password_hash)
VALUES ('jane', 'jane@example.com', 'hash000')
RETURNING *;

-- SELECT
SELECT * FROM users;

SELECT username, email FROM users;

SELECT * FROM users WHERE id = 1;

SELECT * FROM users WHERE username LIKE 'j%';

SELECT * FROM users ORDER BY created_at DESC;

SELECT * FROM users LIMIT 10 OFFSET 20;

-- UPDATE
UPDATE users SET email = 'newemail@example.com' WHERE id = 1;

UPDATE users SET updated_at = CURRENT_TIMESTAMP WHERE id = 1;

-- DELETE
DELETE FROM users WHERE id = 1;

DELETE FROM users WHERE created_at < '2023-01-01';
```

## Indexes

```sql
-- Create index
CREATE INDEX idx_users_email ON users(email);

-- Create unique index
CREATE UNIQUE INDEX idx_users_username ON users(username);

-- Create composite index
CREATE INDEX idx_users_name_email ON users(username, email);

-- List indexes
\di

-- Drop index
DROP INDEX idx_users_email;
```

## Constraints

```sql
-- Primary key
ALTER TABLE users ADD PRIMARY KEY (id);

-- Foreign key
ALTER TABLE posts ADD CONSTRAINT fk_user
  FOREIGN KEY (user_id) REFERENCES users(id)
  ON DELETE CASCADE;

-- Unique constraint
ALTER TABLE users ADD CONSTRAINT unique_email UNIQUE (email);

-- Check constraint
ALTER TABLE users ADD CONSTRAINT check_age
  CHECK (age >= 18);

-- Not null constraint
ALTER TABLE users ALTER COLUMN email SET NOT NULL;
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
-- Begin transaction
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
CREATE USER myuser WITH PASSWORD 'mypassword';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE mydb TO myuser;

GRANT SELECT, INSERT, UPDATE ON users TO myuser;

-- Revoke privileges
REVOKE ALL PRIVILEGES ON DATABASE mydb FROM myuser;

-- List users
\du

-- Drop user
DROP USER myuser;
```

## Backup & Restore

```bash
# Backup database
pg_dump -U username database_name > backup.sql

# Backup with compression
pg_dump -U username database_name | gzip > backup.sql.gz

# Restore database
psql -U username database_name < backup.sql

# Restore compressed backup
gunzip -c backup.sql.gz | psql -U username database_name
```

## Useful Queries

```sql
-- Table size
SELECT
  pg_size_pretty(pg_total_relation_size('users')) as size;

-- Database size
SELECT
  pg_size_pretty(pg_database_size('mydb')) as size;

-- Running queries
SELECT pid, query, state
FROM pg_stat_activity
WHERE state = 'active';

-- Kill query
SELECT pg_cancel_backend(pid);
SELECT pg_terminate_backend(pid);
```

## psql Meta-Commands

```bash
\l              # List databases
\c dbname       # Connect to database
\dt             # List tables
\d table_name   # Describe table
\du             # List users
\di             # List indexes
\df             # List functions
\x              # Toggle expanded output
\q              # Quit
\?              # Help
\h SQL_COMMAND  # SQL command help
```
