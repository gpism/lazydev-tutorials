# ArcadeDB Embedded Mode - Complete Developer Package

## 📦 What's Included

This package provides everything you need to get started with ArcadeDB in embedded mode:

### 📚 Documentation
- **`docs/ArcadeDB_Embedded_Developer_Guide.md`** - Complete step-by-step guide
- **`docs/ArcadeDB_Quick_Reference.md`** - Quick reference card for daily use
- **`docs/README.md`** - This overview (you are here!)

### 💻 Working Examples
- **`src/main/java/dnky/zero/examples/ArcadeDBEmbeddedExample.java`** - Complete working example
- **`src/test/java/dnky/zero/examples/ArcadeDBExampleTest.java`** - Test runner
- **`src/test/java/dnky/zero/models/prop/session/SimpleArcadeDBEmbeddedTest.java`** - Advanced test examples

## 🚀 Quick Start (30 seconds)

### 1. Add Dependencies
```xml
<dependency>
    <groupId>com.arcadedb</groupId>
    <artifactId>arcadedb-server</artifactId>
    <version>25.9.1</version>
</dependency>
```

### 2. Copy-Paste Code
```java
// Set password (IMPORTANT!)
System.setProperty("arcadedb.server.rootPassword", "your_password_here");
System.setProperty("arcadedb.server.autoCreateDatabases", "true");

// Start server
ArcadeDBServer server = new ArcadeDBServer();
server.start();
Database database = server.getDatabase("my_db");

// Create document
database.command("sql", "CREATE DOCUMENT TYPE User IF NOT EXISTS");
MutableDocument user = database.newDocument("User");
user.set("name", "John");
user.save();

// Query
ResultSet result = database.query("sql", "SELECT FROM User");
```

### 3. Run Tests
```bash
mvn test -Dtest=ArcadeDBExampleTest
```

## 🎯 What You Can Do

### 📄 Documents (JSON-like data)
- User profiles, settings, configurations
- Session data, bot conversations
- Product catalogs, inventory

### 🔗 Graphs (Relationships)
- Social networks, friend connections
- Product recommendations
- Workflow dependencies
- Knowledge graphs

### 🗃️ Key-Value (Simple storage)
- Caching, feature flags
- Simple lookups
- Session storage

## 📖 Learning Path

### Beginner (Start Here)
1. Read `docs/ArcadeDB_Embedded_Developer_Guide.md` (sections 1-3)
2. Run `ArcadeDBExampleTest` to see it working
3. Copy code from `ArcadeDBEmbeddedExample.java`

### Intermediate
1. Read the complete developer guide
2. Try the advanced examples in `SimpleArcadeDBEmbeddedTest.java`
3. Build your own session management system

### Advanced
1. Study the session management patterns
2. Implement your own storage backends
3. Optimize for production use

## 🛠️ Common Use Cases

### Session Management
```java
// Perfect for bot conversations, user sessions
MutableDocument session = database.newDocument("Session");
session.set("sessionId", "user_123");
session.set("data", Map.of("lastMessage", "Hello!"));
session.save();
```

### Social Graph
```java
// Perfect for relationships, recommendations
MutableVertex person = database.newVertex("Person");
person.set("name", "Alice");
person.save();

person.newEdge("KNOWS", otherPerson, "since", 2020);
```

### Document Storage
```java
// Perfect for user profiles, settings
MutableDocument user = database.newDocument("User");
user.set("name", "John");
user.set("preferences", Map.of("theme", "dark"));
user.save();
```

## 🚨 Common Mistakes (Avoid These!)

### ❌ Forgetting Password
```java
// WRONG - Will prompt for password
server = new ArcadeDBServer();
server.start();

// RIGHT - Set password first
System.setProperty("arcadedb.server.rootPassword", "your_password");
server = new ArcadeDBServer();
server.start();
```

### ❌ Not Using Transactions
```java
// WRONG - Not safe
MutableDocument doc = database.newDocument("User");
doc.save();

// RIGHT - Use transactions
database.begin();
try {
    MutableDocument doc = database.newDocument("User");
    doc.save();
    database.commit();
} catch (Exception e) {
    database.rollback();
}
```

### ❌ Forgetting Schema
```java
// WRONG - Type doesn't exist
MutableDocument doc = database.newDocument("User"); // Will fail!

// RIGHT - Create type first
database.command("sql", "CREATE DOCUMENT TYPE User IF NOT EXISTS");
MutableDocument doc = database.newDocument("User");
```

## 🎉 Success Stories

This package has been used for:
- ✅ **Bot session management** - Store conversation state
- ✅ **User profile systems** - JSON-like user data
- ✅ **Recommendation engines** - Graph-based suggestions
- ✅ **Workflow management** - Task dependencies
- ✅ **Caching layers** - Fast key-value lookups

## 📞 Need Help?

1. **Read the docs** - Start with the developer guide
2. **Run the examples** - See working code in action
3. **Check the tests** - Learn from test patterns
4. **Use the quick reference** - Daily coding helper

## 🎯 Next Steps

1. **Start with documents** - Easiest to understand
2. **Add graphs** - For relationships
3. **Implement sessions** - For state management
4. **Optimize** - For production use

---

**Happy coding!** 🚀

*This package follows the KISS principle - Keep It Simple, Stupid. Everything is designed to be easy to understand and use.*
