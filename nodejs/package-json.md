# Package.json Configuration

Common package.json setups and scripts.

## Basic Node.js Project

```json
{
  "name": "my-project",
  "version": "1.0.0",
  "description": "My awesome project",
  "main": "index.js",
  "scripts": {
    "start": "node index.js",
    "dev": "nodemon index.js",
    "test": "jest"
  },
  "keywords": [],
  "author": "Your Name",
  "license": "MIT",
  "dependencies": {
    "express": "^4.18.2"
  },
  "devDependencies": {
    "nodemon": "^3.0.1",
    "jest": "^29.7.0"
  }
}
```

## TypeScript Node.js Project

```json
{
  "name": "my-ts-project",
  "version": "1.0.0",
  "description": "TypeScript Node.js project",
  "main": "dist/index.js",
  "scripts": {
    "build": "tsc",
    "start": "node dist/index.js",
    "dev": "ts-node-dev --respawn src/index.ts",
    "watch": "tsc --watch",
    "test": "jest",
    "lint": "eslint . --ext .ts",
    "format": "prettier --write \"src/**/*.ts\""
  },
  "keywords": [],
  "author": "Your Name",
  "license": "MIT",
  "dependencies": {
    "express": "^4.18.2"
  },
  "devDependencies": {
    "@types/express": "^4.17.18",
    "@types/node": "^20.8.0",
    "@typescript-eslint/eslint-plugin": "^6.7.5",
    "@typescript-eslint/parser": "^6.7.5",
    "eslint": "^8.51.0",
    "jest": "^29.7.0",
    "nodemon": "^3.0.1",
    "prettier": "^3.0.3",
    "ts-node": "^10.9.1",
    "ts-node-dev": "^2.0.0",
    "typescript": "^5.2.2"
  }
}
```

## React Project (Create React App alternative)

```json
{
  "name": "my-react-app",
  "version": "1.0.0",
  "description": "React application",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview",
    "lint": "eslint . --ext js,jsx",
    "test": "vitest"
  },
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-router-dom": "^6.16.0"
  },
  "devDependencies": {
    "@vitejs/plugin-react": "^4.1.0",
    "eslint": "^8.51.0",
    "eslint-plugin-react": "^7.33.2",
    "vite": "^4.5.0",
    "vitest": "^0.34.6"
  }
}
```

## Express API with MongoDB

```json
{
  "name": "express-mongodb-api",
  "version": "1.0.0",
  "description": "Express API with MongoDB",
  "main": "server.js",
  "scripts": {
    "start": "node server.js",
    "dev": "nodemon server.js",
    "test": "jest --watchAll",
    "test:ci": "jest"
  },
  "dependencies": {
    "express": "^4.18.2",
    "mongoose": "^7.6.0",
    "dotenv": "^16.3.1",
    "cors": "^2.8.5",
    "helmet": "^7.0.0",
    "morgan": "^1.10.0",
    "bcrypt": "^5.1.1",
    "jsonwebtoken": "^9.0.2"
  },
  "devDependencies": {
    "nodemon": "^3.0.1",
    "jest": "^29.7.0",
    "supertest": "^6.3.3"
  }
}
```

## Full-Stack Monorepo

```json
{
  "name": "fullstack-monorepo",
  "version": "1.0.0",
  "private": true,
  "workspaces": [
    "packages/*",
    "apps/*"
  ],
  "scripts": {
    "dev": "npm-run-all --parallel dev:*",
    "dev:api": "npm run dev --workspace=apps/api",
    "dev:web": "npm run dev --workspace=apps/web",
    "build": "npm-run-all build:*",
    "build:api": "npm run build --workspace=apps/api",
    "build:web": "npm run build --workspace=apps/web",
    "test": "npm run test --workspaces",
    "clean": "rm -rf node_modules apps/*/node_modules packages/*/node_modules"
  },
  "devDependencies": {
    "npm-run-all": "^4.1.5"
  }
}
```

## Common Script Patterns

```json
{
  "scripts": {
    "start": "node server.js",
    "dev": "nodemon server.js",
    "dev:watch": "nodemon --watch src server.js",
    "build": "npm run clean && npm run build:ts",
    "build:ts": "tsc",
    "clean": "rm -rf dist",
    "test": "jest",
    "test:watch": "jest --watch",
    "test:coverage": "jest --coverage",
    "lint": "eslint .",
    "lint:fix": "eslint . --fix",
    "format": "prettier --write .",
    "format:check": "prettier --check .",
    "prepare": "husky install",
    "precommit": "lint-staged",
    "prepush": "npm run test",
    "deploy": "npm run build && npm run deploy:prod",
    "docker:build": "docker build -t myapp .",
    "docker:run": "docker run -p 3000:3000 myapp"
  }
}
```

## Engine Constraints

```json
{
  "engines": {
    "node": ">=18.0.0",
    "npm": ">=9.0.0"
  },
  "engineStrict": true
}
```

## ESM Package

```json
{
  "type": "module",
  "exports": {
    ".": {
      "import": "./dist/index.js",
      "require": "./dist/index.cjs"
    }
  }
}
```
