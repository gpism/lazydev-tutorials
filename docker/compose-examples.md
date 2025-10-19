# Docker Compose Examples

Ready-to-use docker-compose configurations.

## Basic Web App + Database

```yaml
# docker-compose.yml
version: '3.8'

services:
  web:
    build: .
    ports:
      - "3000:3000"
    environment:
      - DATABASE_URL=postgresql://user:pass@db:5432/mydb
    depends_on:
      - db
    volumes:
      - .:/app
    command: npm start

  db:
    image: postgres:15
    environment:
      - POSTGRES_USER=user
      - POSTGRES_PASSWORD=pass
      - POSTGRES_DB=mydb
    volumes:
      - pgdata:/var/lib/postgresql/data
    ports:
      - "5432:5432"

volumes:
  pgdata:
```

```bash
# Start services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

## MERN Stack

```yaml
# docker-compose.yml
version: '3.8'

services:
  mongodb:
    image: mongo:6
    environment:
      - MONGO_INITDB_ROOT_USERNAME=admin
      - MONGO_INITDB_ROOT_PASSWORD=password
    volumes:
      - mongodb_data:/data/db
    ports:
      - "27017:27017"

  backend:
    build: ./backend
    ports:
      - "5000:5000"
    environment:
      - MONGO_URI=mongodb://admin:password@mongodb:27017/
      - NODE_ENV=development
    depends_on:
      - mongodb
    volumes:
      - ./backend:/app

  frontend:
    build: ./frontend
    ports:
      - "3000:3000"
    environment:
      - REACT_APP_API_URL=http://localhost:5000
    volumes:
      - ./frontend:/app
      - /app/node_modules
    depends_on:
      - backend

volumes:
  mongodb_data:
```

## WordPress + MySQL

```yaml
# docker-compose.yml
version: '3.8'

services:
  wordpress:
    image: wordpress:latest
    ports:
      - "8080:80"
    environment:
      - WORDPRESS_DB_HOST=mysql
      - WORDPRESS_DB_USER=wpuser
      - WORDPRESS_DB_PASSWORD=wppass
      - WORDPRESS_DB_NAME=wordpress
    volumes:
      - ./wp-content:/var/www/html/wp-content
    depends_on:
      - mysql

  mysql:
    image: mysql:8
    environment:
      - MYSQL_DATABASE=wordpress
      - MYSQL_USER=wpuser
      - MYSQL_PASSWORD=wppass
      - MYSQL_ROOT_PASSWORD=rootpass
    volumes:
      - mysql_data:/var/lib/mysql

volumes:
  mysql_data:
```

## Development Environment with Multiple Services

```yaml
# docker-compose.yml
version: '3.8'

services:
  api:
    build: ./api
    ports:
      - "8000:8000"
    environment:
      - REDIS_URL=redis://redis:6379
      - DATABASE_URL=postgresql://user:pass@postgres:5432/mydb
    depends_on:
      - postgres
      - redis
    volumes:
      - ./api:/app

  postgres:
    image: postgres:15
    environment:
      - POSTGRES_USER=user
      - POSTGRES_PASSWORD=pass
      - POSTGRES_DB=mydb
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data

  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
    depends_on:
      - api

volumes:
  postgres_data:
  redis_data:
```

## Useful Commands

```bash
# Start in background
docker-compose up -d

# Rebuild and start
docker-compose up -d --build

# View logs
docker-compose logs -f [service_name]

# Execute command in service
docker-compose exec api bash

# Scale service
docker-compose up -d --scale worker=3

# Stop services
docker-compose stop

# Stop and remove containers
docker-compose down

# Stop and remove everything including volumes
docker-compose down -v

# View running services
docker-compose ps
```
