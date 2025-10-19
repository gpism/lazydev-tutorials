# ArcadeDB Embedded - Quick Reference Card

## 🚀 Setup (Copy-Paste Ready)

```java
// 1. Add to pom.xml
<dependency>
    <groupId>com.arcadedb</groupId>
    <artifactId>arcadedb-server</artifactId>
    <version>25.9.1</version>
</dependency>

// 2. Basic setup
System.setProperty("arcadedb.server.rootPassword", "your_password_here");
System.setProperty("arcadedb.server.autoCreateDatabases", "true");
ArcadeDBServer server = new ArcadeDBServer();
server.start();
Database database = server.getDatabase("my_db");
```

## 📄 Document Operations

```java
// Create document type
database.command("sql", "CREATE DOCUMENT TYPE User IF NOT EXISTS");

// Create document
MutableDocument user = database.newDocument("User");
user.set("name", "John");
user.set("email", "john@example.com");
user.save();

// Query document
ResultSet result = database.query("sql", "SELECT FROM User WHERE name = 'John'");
```

## 🔗 Graph Operations

```java
// Create vertex types
database.command("sql", "CREATE VERTEX TYPE Person IF NOT EXISTS");
database.command("sql", "CREATE EDGE TYPE Knows IF NOT EXISTS");

// Create vertices
MutableVertex person1 = database.newVertex("Person");
person1.set("name", "Alice");
person1.save();

MutableVertex person2 = database.newVertex("Person");
person2.set("name", "Bob");
person2.save();

// Create edge
person1.newEdge("Knows", person2, "since", 2020);

// Graph traversal
ResultSet result = database.query("sql", 
    "SELECT expand(out('Knows')) FROM Person WHERE name = 'Alice'");
```

## 🔐 Session Management

```java
// Create session
MutableDocument session = database.newDocument("Session");
session.set("sessionId", "user_123");
session.set("userId", "user_123");
session.set("data", new HashMap<>());
session.save();

// Update session data
@SuppressWarnings("unchecked")
Map<String, Object> data = (Map<String, Object>) session.getProperty("data");
data.put("lastMessage", "Hello!");
session.set("data", data);
session.save();
```

## 🔄 Transactions (IMPORTANT!)

```java
// Always use transactions for safety
database.begin();
try {
    // Your operations here
    database.commit();
} catch (Exception e) {
    database.rollback();
    throw e;
}
```

## 🔍 Common Queries

```java
// Simple select
ResultSet result = database.query("sql", "SELECT FROM User");

// With parameters
ResultSet result = database.query("sql", 
    "SELECT FROM User WHERE age > ?", 18);

// Graph traversal
ResultSet result = database.query("sql", 
    "SELECT expand(out('WorksAt')) FROM Person WHERE name = 'Alice'");

// Aggregation
ResultSet result = database.query("sql", 
    "SELECT COUNT(*) as total FROM User");
```

## 🚨 Common Mistakes

❌ **Don't forget the password:**
```java
// WRONG
server = new ArcadeDBServer();
server.start(); // Will prompt for password!

// RIGHT
System.setProperty("arcadedb.server.rootPassword", "your_password");
server = new ArcadeDBServer();
server.start();
```

❌ **Don't forget transactions:**
```java
// WRONG
MutableDocument doc = database.newDocument("User");
doc.save(); // Not safe!

// RIGHT
database.begin();
try {
    MutableDocument doc = database.newDocument("User");
    doc.save();
    database.commit();
} catch (Exception e) {
    database.rollback();
}
```

❌ **Don't forget to create schema:**
```java
// WRONG
MutableDocument doc = database.newDocument("User"); // Will fail!

// RIGHT
database.command("sql", "CREATE DOCUMENT TYPE User IF NOT EXISTS");
MutableDocument doc = database.newDocument("User");
```

## 🎯 Use Cases

- **Documents**: User profiles, settings, configurations
- **Graphs**: Social networks, recommendations, workflows
- **Sessions**: User sessions, bot conversations, caching
- **Key-Value**: Simple lookups, feature flags

## 📊 Performance Tips

1. **Use transactions for batch operations**
2. **Use parameterized queries** (prevents SQL injection)
3. **Create indexes** for frequently queried fields
4. **Clean up test data** between tests

## 🧪 Testing

```java
@BeforeEach
void setUp() {
    // Clear data before each test
    database.command("sql", "DELETE FROM User");
    database.command("sql", "DELETE FROM Session");
}
```

## 🛑 Cleanup

```java
@AfterEach
void tearDown() {
    if (database != null) database.close();
    if (server != null) server.stop();
}
```

---

**Need more help?** Check the full documentation in `docs/ArcadeDB_Embedded_Developer_Guide.md`

