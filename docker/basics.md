# Docker Basics

Essential Docker commands for containerization.

## Install & Verify

```bash
# Verify installation
docker --version
docker run hello-world
```

## Image Management

```bash
# List images
docker images

# Pull image
docker pull ubuntu:22.04

# Remove image
docker rmi ubuntu:22.04

# Remove unused images
docker image prune -a
```

## Container Management

```bash
# Run container
docker run -d --name myapp nginx

# Run with port mapping
docker run -d -p 8080:80 --name webapp nginx

# Run with volume
docker run -d -v /host/path:/container/path nginx

# Run interactive shell
docker run -it ubuntu bash

# List running containers
docker ps

# List all containers
docker ps -a

# Stop container
docker stop myapp

# Start container
docker start myapp

# Remove container
docker rm myapp

# Remove all stopped containers
docker container prune
```

## Build Image

```dockerfile
# Create Dockerfile
FROM node:18-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 3000
CMD ["node", "index.js"]
```

```bash
# Build image
docker build -t myapp:1.0 .

# Build with no cache
docker build --no-cache -t myapp:1.0 .
```

## Logs & Debugging

```bash
# View logs
docker logs myapp

# Follow logs
docker logs -f myapp

# Execute command in running container
docker exec -it myapp bash

# Inspect container
docker inspect myapp

# View container stats
docker stats
```

## Network

```bash
# List networks
docker network ls

# Create network
docker network create mynetwork

# Run container in network
docker run -d --network mynetwork --name db postgres

# Connect container to network
docker network connect mynetwork myapp
```

## Docker System

```bash
# View disk usage
docker system df

# Clean up everything
docker system prune -a --volumes

# Remove specific things
docker system prune --volumes  # Keep images
```

## Quick Examples

### Python App
```bash
docker run -d \
  -p 8000:8000 \
  -v $(pwd):/app \
  -w /app \
  python:3.11 \
  python -m http.server 8000
```

### Node.js App
```bash
docker run -d \
  -p 3000:3000 \
  -v $(pwd):/app \
  -w /app \
  node:18 \
  npm start
```

### Database
```bash
docker run -d \
  --name postgres \
  -e POSTGRES_PASSWORD=secret \
  -p 5432:5432 \
  -v pgdata:/var/lib/postgresql/data \
  postgres:15
```
