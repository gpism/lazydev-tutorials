# ArcadeDB Embedded Mode - Developer Guide

## 🎯 Overview

This guide shows you how to use ArcadeDB in **embedded mode** within your Java application. ArcadeDB is a multi-model database that supports Document, Graph, Key-Value, and Time Series data models in a single database.

## 🚀 Quick Start

### 1. Add Dependencies to `pom.xml`

```xml
<dependency>
    <groupId>com.arcadedb</groupId>
    <artifactId>arcadedb-server</artifactId>
    <version>25.9.1</version>
</dependency>

<dependency>
    <groupId>com.arcadedb</groupId>
    <artifactId>arcadedb-studio</artifactId>
    <version>25.9.1</version>
</dependency>
```

### 2. Basic Setup (Idiot-Proof)

```java
import com.arcadedb.server.ArcadeDBServer;
import com.arcadedb.database.Database;

public class ArcadeDBExample {
    private ArcadeDBServer server;
    private Database database;
    
    public void startDatabase() {
        // Step 1: Configure for non-interactive mode
        System.setProperty("arcadedb.server.rootPassword", "your_password_here");
        System.setProperty("arcadedb.server.autoOpenDatabases", "my_database");
        System.setProperty("arcadedb.server.plugins", "");
        System.setProperty("arcadedb.server.autoCreateDatabases", "true");
        
        // Step 2: Start the server
        server = new ArcadeDBServer();
        server.start();
        
        // Step 3: Get your database
        database = server.getDatabase("my_database");
        
        System.out.println("✅ ArcadeDB is running!");
    }
    
    public void stopDatabase() {
        if (database != null) {
            database.close();
        }
        if (server != null) {
            server.stop();
        }
        System.out.println("🛑 ArcadeDB stopped!");
    }
}
```

## 📚 Step-by-Step Tutorial

### Step 1: Document Operations

Documents are like JSON objects that you can store and query.

```java
@Test
void documentExample() {
    // Create a document type (like a table schema)
    database.command("sql", "CREATE DOCUMENT TYPE User IF NOT EXISTS");
    
    // Create a new document
    MutableDocument user = database.newDocument("User");
    user.set("name", "John Doe");
    user.set("email", "john@example.com");
    user.set("age", 30);
    user.save();
    
    // Query the document
    ResultSet result = database.query("sql", 
        "SELECT FROM User WHERE name = 'John Doe'");
    
    while (result.hasNext()) {
        var record = result.next();
        System.out.println("Found: " + record.getProperty("name"));
    }
}
```

### Step 2: Graph Operations (Vertices and Edges)

Graphs are perfect for relationships between entities.

```java
@Test
void graphExample() {
    // Start a transaction for safety
    database.begin();
    
    try {
        // Create vertex types (nodes in the graph)
        database.command("sql", "CREATE VERTEX TYPE Person IF NOT EXISTS");
        database.command("sql", "CREATE VERTEX TYPE Company IF NOT EXISTS");
        database.command("sql", "CREATE EDGE TYPE WorksAt IF NOT EXISTS");
        
        // Create vertices (nodes)
        MutableVertex person = database.newVertex("Person");
        person.set("name", "Alice");
        person.set("age", 28);
        person.save();
        
        MutableVertex company = database.newVertex("Company");
        company.set("name", "TechCorp");
        company.set("industry", "Technology");
        company.save();
        
        // Create edge (relationship)
        person.newEdge("WorksAt", company, "since", 2020, "role", "Developer");
        
        // Commit the transaction
        database.commit();
        
        // Query the graph
        ResultSet result = database.query("sql", 
            "SELECT expand(out('WorksAt')) FROM Person WHERE name = 'Alice'");
        
        while (result.hasNext()) {
            var companyRecord = result.next();
            System.out.println("Alice works at: " + companyRecord.getProperty("name"));
        }
        
    } catch (Exception e) {
        database.rollback();
        throw e;
    }
}
```

### Step 3: Session Management Example

Perfect for storing user session data.

```java
@Test
void sessionExample() {
    // Create session document
    MutableDocument session = database.newDocument("Session");
    session.set("sessionId", "user-123-session");
    session.set("userId", "user-123");
    session.set("agentId", "bot-agent-1");
    session.set("status", "ACTIVE");
    session.set("createdAt", System.currentTimeMillis());
    
    // Store complex data as a Map
    Map<String, Object> sessionData = Map.of(
        "lastMessage", "Hello from the bot!",
        "messageCount", 42,
        "preferences", Map.of("theme", "dark", "language", "en")
    );
    session.set("data", sessionData);
    session.save();
    
    // Query session
    ResultSet result = database.query("sql", 
        "SELECT FROM Session WHERE sessionId = 'user-123-session'");
    
    if (result.hasNext()) {
        var sessionRecord = result.next();
        System.out.println("Session Status: " + sessionRecord.getProperty("status"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) sessionRecord.getProperty("data");
        System.out.println("Message Count: " + data.get("messageCount"));
    }
}
```

## 🔧 Configuration Options

### System Properties (Set before starting server)

```java
// Required: Set root password (minimum 8 characters)
System.setProperty("arcadedb.server.rootPassword", "your_secure_password");

// Optional: Auto-open databases
System.setProperty("arcadedb.server.autoOpenDatabases", "db1,db2,db3");

// Optional: Disable plugins for faster startup
System.setProperty("arcadedb.server.plugins", "");

// Optional: Auto-create databases when accessed
System.setProperty("arcadedb.server.autoCreateDatabases", "true");

// Optional: Set server port (default: 2480)
System.setProperty("arcadedb.server.httpPort", "2480");
```

## 📝 Common Patterns

### 1. Safe Database Operations

```java
public void safeOperation() {
    database.begin();
    try {
        // Your database operations here
        database.commit();
    } catch (Exception e) {
        database.rollback();
        throw e;
    }
}
```

### 2. Batch Operations

```java
public void batchInsert() {
    database.begin();
    try {
        for (int i = 0; i < 1000; i++) {
            MutableDocument doc = database.newDocument("MyType");
            doc.set("id", i);
            doc.set("data", "value_" + i);
            doc.save();
        }
        database.commit();
    } catch (Exception e) {
        database.rollback();
        throw e;
    }
}
```

### 3. Query with Parameters

```java
public void parameterizedQuery() {
    ResultSet result = database.query("sql", 
        "SELECT FROM User WHERE age > ? AND name LIKE ?", 
        25, "%John%");
    
    while (result.hasNext()) {
        var record = result.next();
        System.out.println("User: " + record.getProperty("name"));
    }
}
```

## 🎨 Data Models Explained

### 1. Document Model
- **Use for**: JSON-like data, user profiles, settings
- **Example**: User accounts, product catalogs, configuration

```java
// Create document
MutableDocument user = database.newDocument("User");
user.set("name", "John");
user.set("email", "john@example.com");
user.save();
```

### 2. Graph Model
- **Use for**: Relationships, social networks, recommendations
- **Example**: User connections, product recommendations, workflow

```java
// Create vertex
MutableVertex person = database.newVertex("Person");
person.set("name", "Alice");
person.save();

// Create edge
person.newEdge("KNOWS", otherPerson, "since", 2020);
```

### 3. Key-Value Model
- **Use for**: Caching, simple lookups
- **Example**: Session storage, feature flags

```java
// Store key-value
database.command("sql", "INSERT INTO KV SET key = 'feature_flag', value = 'enabled'");

// Retrieve
ResultSet result = database.query("sql", "SELECT FROM KV WHERE key = 'feature_flag'");
```

## 🚨 Common Mistakes & Solutions

### ❌ Mistake 1: Forgetting to set password
```java
// WRONG - Will prompt for password
server = new ArcadeDBServer();
server.start();
```

### ✅ Solution:
```java
// RIGHT - Set password first
System.setProperty("arcadedb.server.rootPassword", "your_password");
server = new ArcadeDBServer();
server.start();
```

### ❌ Mistake 2: Not using transactions
```java
// WRONG - No transaction safety
MutableDocument doc = database.newDocument("User");
doc.set("name", "John");
doc.save();
```

### ✅ Solution:
```java
// RIGHT - Use transactions
database.begin();
try {
    MutableDocument doc = database.newDocument("User");
    doc.set("name", "John");
    doc.save();
    database.commit();
} catch (Exception e) {
    database.rollback();
    throw e;
}
```

### ❌ Mistake 3: Not creating schema first
```java
// WRONG - Type doesn't exist
MutableDocument doc = database.newDocument("User"); // Will fail!
```

### ✅ Solution:
```java
// RIGHT - Create type first
database.command("sql", "CREATE DOCUMENT TYPE User IF NOT EXISTS");
MutableDocument doc = database.newDocument("User");
```

## 🧪 Testing Best Practices

### 1. Test Isolation
```java
@BeforeEach
void setUp() {
    // Clear data before each test
    database.command("sql", "DELETE FROM User");
    database.command("sql", "DELETE FROM Session");
}
```

### 2. Use Different Databases
```java
@BeforeEach
void setUp() {
    // Use unique database name for each test
    String testDbName = "test_" + System.currentTimeMillis();
    database = server.getDatabase(testDbName);
}
```

## 🔍 Debugging Tips

### 1. Enable Logging
```java
// Add to your test setup
System.setProperty("arcadedb.server.plugins", "");
System.setProperty("arcadedb.server.autoOpenDatabases", "debug_db");
```

### 2. Check Database Status
```java
// Check if database is open
if (database.isOpen()) {
    System.out.println("✅ Database is ready");
} else {
    System.out.println("❌ Database is not open");
}
```

### 3. Query Database Info
```java
// List all types
ResultSet types = database.query("sql", "SELECT FROM schema:types");
while (types.hasNext()) {
    var type = types.next();
    System.out.println("Type: " + type.getProperty("name"));
}
```

## 📊 Performance Tips

### 1. Use Transactions for Batch Operations
```java
// Good: Batch in transaction
database.begin();
for (int i = 0; i < 1000; i++) {
    // Create documents
}
database.commit();
```

### 2. Use Parameterized Queries
```java
// Good: Parameterized (prevents SQL injection)
database.query("sql", "SELECT FROM User WHERE age > ?", 18);

// Bad: String concatenation
database.query("sql", "SELECT FROM User WHERE age > " + age);
```

### 3. Index Frequently Queried Fields
```java
// Create index for better performance
database.command("sql", "CREATE INDEX User.email IF NOT EXISTS");
```

## 🎯 Real-World Example: Session Management

```java
public class SessionManager {
    private Database database;
    
    public SessionManager(Database database) {
        this.database = database;
    }
    
    public void createSession(String sessionId, String userId) {
        database.begin();
        try {
            MutableDocument session = database.newDocument("Session");
            session.set("sessionId", sessionId);
            session.set("userId", userId);
            session.set("status", "ACTIVE");
            session.set("createdAt", System.currentTimeMillis());
            session.set("data", new HashMap<>());
            session.save();
            database.commit();
        } catch (Exception e) {
            database.rollback();
            throw e;
        }
    }
    
    public void updateSessionData(String sessionId, String key, Object value) {
        database.begin();
        try {
            ResultSet result = database.query("sql", 
                "SELECT FROM Session WHERE sessionId = ?", sessionId);
            
            if (result.hasNext()) {
                MutableDocument session = result.next().modify();
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) session.getProperty("data");
                data.put(key, value);
                session.set("data", data);
                session.save();
            }
            database.commit();
        } catch (Exception e) {
            database.rollback();
            throw e;
        }
    }
    
    public Map<String, Object> getSessionData(String sessionId) {
        ResultSet result = database.query("sql", 
            "SELECT FROM Session WHERE sessionId = ?", sessionId);
        
        if (result.hasNext()) {
            var session = result.next();
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) session.getProperty("data");
            return data;
        }
        return new HashMap<>();
    }
}
```

## 🎉 Conclusion

ArcadeDB embedded mode is perfect for:
- ✅ **Development and testing**
- ✅ **Single-application deployments**
- ✅ **Session management**
- ✅ **Caching layers**
- ✅ **Prototyping**

Remember:
1. **Always set the password** before starting the server
2. **Use transactions** for data safety
3. **Create schema** before inserting data
4. **Clean up** in your tests
5. **Use parameterized queries** for security

Happy coding! 🚀
