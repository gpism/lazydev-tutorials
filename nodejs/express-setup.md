# Express Server Setup

Quick Express.js server templates.

## Minimal Express Server

```javascript
// server.js
const express = require('express');
const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());

app.get('/', (req, res) => {
  res.json({ message: 'Hello World!' });
});

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
```

```bash
npm install express
node server.js
```

## Express with Routes

```javascript
// server.js
const express = require('express');
const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());

// Routes
app.use('/api/users', require('./routes/users'));
app.use('/api/posts', require('./routes/posts'));

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
```

```javascript
// routes/users.js
const express = require('express');
const router = express.Router();

// GET all users
router.get('/', async (req, res) => {
  res.json({ users: [] });
});

// GET user by id
router.get('/:id', async (req, res) => {
  const { id } = req.params;
  res.json({ user: { id } });
});

// POST create user
router.post('/', async (req, res) => {
  const userData = req.body;
  res.status(201).json({ user: userData });
});

// PUT update user
router.put('/:id', async (req, res) => {
  const { id } = req.params;
  const updates = req.body;
  res.json({ user: { id, ...updates } });
});

// DELETE user
router.delete('/:id', async (req, res) => {
  const { id } = req.params;
  res.json({ message: 'User deleted', id });
});

module.exports = router;
```

## Express with Middleware

```javascript
// server.js
const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const morgan = require('morgan');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(helmet());
app.use(cors());
app.use(morgan('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Custom middleware
const authenticate = (req, res, next) => {
  const token = req.headers.authorization;
  if (!token) {
    return res.status(401).json({ error: 'Unauthorized' });
  }
  // Verify token logic here
  next();
};

// Protected routes
app.get('/api/protected', authenticate, (req, res) => {
  res.json({ message: 'Protected data' });
});

// Error handling middleware
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({ error: 'Something went wrong!' });
});

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
```

```bash
npm install express cors helmet morgan
```

## Express with Database (MongoDB)

```javascript
// server.js
const express = require('express');
const mongoose = require('mongoose');

const app = express();
const PORT = process.env.PORT || 3000;
const MONGO_URI = process.env.MONGO_URI || 'mongodb://localhost:27017/mydb';

app.use(express.json());

// Connect to MongoDB
mongoose.connect(MONGO_URI)
  .then(() => console.log('MongoDB connected'))
  .catch(err => console.error('MongoDB connection error:', err));

// User schema
const userSchema = new mongoose.Schema({
  name: String,
  email: String,
  createdAt: { type: Date, default: Date.now }
});

const User = mongoose.model('User', userSchema);

// Routes
app.get('/api/users', async (req, res) => {
  const users = await User.find();
  res.json(users);
});

app.post('/api/users', async (req, res) => {
  const user = new User(req.body);
  await user.save();
  res.status(201).json(user);
});

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
```

```bash
npm install express mongoose
```

## Express with PostgreSQL

```javascript
// server.js
const express = require('express');
const { Pool } = require('pg');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());

// PostgreSQL connection
const pool = new Pool({
  host: process.env.DB_HOST || 'localhost',
  port: process.env.DB_PORT || 5432,
  database: process.env.DB_NAME || 'mydb',
  user: process.env.DB_USER || 'user',
  password: process.env.DB_PASSWORD || 'password'
});

// Test connection
pool.query('SELECT NOW()', (err, res) => {
  if (err) {
    console.error('Database connection error:', err);
  } else {
    console.log('Database connected:', res.rows[0].now);
  }
});

// Routes
app.get('/api/users', async (req, res) => {
  try {
    const result = await pool.query('SELECT * FROM users');
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/users', async (req, res) => {
  const { name, email } = req.body;
  try {
    const result = await pool.query(
      'INSERT INTO users (name, email) VALUES ($1, $2) RETURNING *',
      [name, email]
    );
    res.status(201).json(result.rows[0]);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
```

```bash
npm install express pg
```

## Express with Environment Variables

```javascript
// server.js
require('dotenv').config();
const express = require('express');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());

app.get('/', (req, res) => {
  res.json({
    environment: process.env.NODE_ENV,
    apiKey: process.env.API_KEY ? '***' : 'not set'
  });
});

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
```

```bash
# .env file
NODE_ENV=development
PORT=3000
API_KEY=your-secret-key
DATABASE_URL=postgresql://user:pass@localhost:5432/mydb
```

```bash
npm install express dotenv
```
