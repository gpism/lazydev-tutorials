package dnky.zero.examples;

import com.arcadedb.database.Database;
import com.arcadedb.database.MutableDocument;
import com.arcadedb.graph.MutableVertex;
import com.arcadedb.query.sql.executor.Result;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.server.ArcadeDBServer;

import java.util.HashMap;
import java.util.Map;

/**
 * Complete ArcadeDB Embedded Example
 * 
 * This class demonstrates all the common patterns for using ArcadeDB in embedded mode.
 * Copy and modify this code for your own projects.
 */
public class ArcadeDBEmbeddedExample {
    
    private ArcadeDBServer server;
    private Database database;
    
    /**
     * Step 1: Initialize ArcadeDB Server
     * Call this method first before doing anything else.
     */
    public void initializeDatabase() {
        System.out.println("🚀 Initializing ArcadeDB...");
        
        // Configure for non-interactive mode (IMPORTANT!)
        System.setProperty("arcadedb.server.rootPassword", "my_secure_password_123");
        System.setProperty("arcadedb.server.autoOpenDatabases", "example_db");
        System.setProperty("arcadedb.server.plugins", "");
        System.setProperty("arcadedb.server.autoCreateDatabases", "true");
        
        // Start the server
        server = new ArcadeDBServer();
        server.start();
        
        // Get the database (create if it doesn't exist)
        try {
            database = server.getDatabase("example_db");
        } catch (Exception e) {
            // Database doesn't exist, create it
            server.createDatabase("example_db", com.arcadedb.engine.ComponentFile.MODE.READ_WRITE);
            database = server.getDatabase("example_db");
        }
        
        System.out.println("✅ ArcadeDB initialized successfully!");
    }
    
    /**
     * Step 2: Document Operations
     * Documents are like JSON objects - perfect for user profiles, settings, etc.
     */
    public void documentOperations() {
        System.out.println("\n📄 Document Operations Example:");
        
        // Create document type (like a table schema)
        database.command("sql", "CREATE DOCUMENT TYPE User IF NOT EXISTS");
        
        // Create a user document
        MutableDocument user = database.newDocument("User");
        user.set("name", "Alice Johnson");
        user.set("email", "alice@example.com");
        user.set("age", 28);
        user.set("city", "New York");
        user.set("preferences", Map.of(
            "theme", "dark",
            "notifications", true,
            "language", "en"
        ));
        user.save();
        
        System.out.println("✅ Created user: " + user.getString("name"));
        
        // Query the user
        ResultSet result = database.query("sql", 
            "SELECT FROM User WHERE email = ?", "alice@example.com");
        
        while (result.hasNext()) {
            Result userRecord = result.next();
            System.out.println("   Found user: " + userRecord.getProperty("name"));
            System.out.println("   Age: " + userRecord.getProperty("age"));
            System.out.println("   City: " + userRecord.getProperty("city"));
        }
    }
    
    /**
     * Step 3: Graph Operations
     * Graphs are perfect for relationships - social networks, recommendations, etc.
     */
    public void graphOperations() {
        System.out.println("\n🔗 Graph Operations Example:");
        
        database.begin();
        try {
            // Create vertex types (nodes)
            database.command("sql", "CREATE VERTEX TYPE Person IF NOT EXISTS");
            database.command("sql", "CREATE VERTEX TYPE Company IF NOT EXISTS");
            database.command("sql", "CREATE EDGE TYPE WorksAt IF NOT EXISTS");
            database.command("sql", "CREATE EDGE TYPE Knows IF NOT EXISTS");
            
            // Create people
            MutableVertex alice = database.newVertex("Person");
            alice.set("name", "Alice");
            alice.set("age", 28);
            alice.save();
            
            MutableVertex bob = database.newVertex("Person");
            bob.set("name", "Bob");
            bob.set("age", 32);
            bob.save();
            
            // Create company
            MutableVertex company = database.newVertex("Company");
            company.set("name", "TechCorp");
            company.set("industry", "Technology");
            company.save();
            
            // Create relationships
            alice.newEdge("WorksAt", company, "since", 2020, "role", "Developer");
            bob.newEdge("WorksAt", company, "since", 2019, "role", "Manager");
            alice.newEdge("Knows", bob, "since", 2021, "relationship", "colleague");
            
            database.commit();
            
            System.out.println("✅ Created graph with people and company");
            
            // Query: Who works at TechCorp?
            ResultSet worksAt = database.query("sql", 
                "SELECT expand(in('WorksAt')) FROM Company WHERE name = 'TechCorp'");
            
            System.out.println("   People who work at TechCorp:");
            while (worksAt.hasNext()) {
                Result person = worksAt.next();
                System.out.println("   - " + person.getProperty("name") + 
                    " (role: " + person.getProperty("role") + ")");
            }
            
            // Query: Who does Alice know?
            ResultSet aliceKnows = database.query("sql", 
                "SELECT expand(out('Knows')) FROM Person WHERE name = 'Alice'");
            
            System.out.println("   People Alice knows:");
            while (aliceKnows.hasNext()) {
                Result person = aliceKnows.next();
                System.out.println("   - " + person.getProperty("name"));
            }
            
        } catch (Exception e) {
            database.rollback();
            System.err.println("❌ Graph operation failed: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Step 4: Session Management
     * Perfect for storing user session data, bot conversations, etc.
     */
    public void sessionManagement() {
        System.out.println("\n🔐 Session Management Example:");
        
        // Create session
        String sessionId = "session_" + System.currentTimeMillis();
        createSession(sessionId, "user_123", "bot_agent_1");
        
        // Add session data
        updateSessionData(sessionId, "lastMessage", "Hello from the bot!");
        updateSessionData(sessionId, "messageCount", 1);
        updateSessionData(sessionId, "userPreferences", Map.of(
            "theme", "dark",
            "language", "en"
        ));
        
        // Get session data
        Map<String, Object> sessionData = getSessionData(sessionId);
        System.out.println("✅ Session created with data:");
        sessionData.forEach((key, value) -> 
            System.out.println("   " + key + ": " + value));
        
        // Update session
        updateSessionData(sessionId, "messageCount", 2);
        updateSessionData(sessionId, "lastMessage", "How can I help you?");
        
        System.out.println("✅ Session updated");
    }
    
    /**
     * Step 5: Batch Operations
     * Efficient way to insert many records at once.
     */
    public void batchOperations() {
        System.out.println("\n📊 Batch Operations Example:");
        
        database.begin();
        try {
            // Create document type
            database.command("sql", "CREATE DOCUMENT TYPE Product IF NOT EXISTS");
            
            // Insert many products at once
            for (int i = 1; i <= 100; i++) {
                MutableDocument product = database.newDocument("Product");
                product.set("id", i);
                product.set("name", "Product " + i);
                product.set("price", 10.0 + i);
                product.set("category", "Category " + (i % 5));
                product.save();
            }
            
            database.commit();
            System.out.println("✅ Inserted 100 products in batch");
            
            // Query some products
            ResultSet result = database.query("sql", 
                "SELECT FROM Product WHERE price > ? LIMIT 5", 50.0);
            
            System.out.println("   Products with price > 50:");
            while (result.hasNext()) {
                Result product = result.next();
                System.out.println("   - " + product.getProperty("name") + 
                    " ($" + product.getProperty("price") + ")");
            }
            
        } catch (Exception e) {
            database.rollback();
            System.err.println("❌ Batch operation failed: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Step 6: Advanced Queries
     * Complex queries with joins, aggregations, etc.
     */
    public void advancedQueries() {
        System.out.println("\n🔍 Advanced Queries Example:");
        
        // Create some test data
        database.command("sql", "CREATE DOCUMENT TYPE Order IF NOT EXISTS");
        database.command("sql", "CREATE DOCUMENT TYPE Customer IF NOT EXISTS");
        
        // Insert test data
        MutableDocument customer = database.newDocument("Customer");
        customer.set("id", "CUST_001");
        customer.set("name", "John Doe");
        customer.set("email", "john@example.com");
        customer.save();
        
        for (int i = 1; i <= 5; i++) {
            MutableDocument order = database.newDocument("Order");
            order.set("id", "ORDER_" + i);
            order.set("customerId", "CUST_001");
            order.set("amount", 100.0 * i);
            order.set("date", System.currentTimeMillis() - (i * 86400000L)); // i days ago
            order.save();
        }
        
        // Complex query: Customer with their orders
        ResultSet result = database.query("sql", 
            "SELECT c.name, c.email, o.id as orderId, o.amount " +
            "FROM Customer c " +
            "JOIN Order o ON c.id = o.customerId " +
            "WHERE c.id = 'CUST_001'");
        
        System.out.println("   Customer orders:");
        while (result.hasNext()) {
            Result record = result.next();
            System.out.println("   - " + record.getProperty("name") + 
                " | Order: " + record.getProperty("orderId") + 
                " | Amount: $" + record.getProperty("amount"));
        }
        
        // Aggregation query
        ResultSet totalResult = database.query("sql", 
            "SELECT SUM(amount) as total FROM Order WHERE customerId = 'CUST_001'");
        
        if (totalResult.hasNext()) {
            Result total = totalResult.next();
            System.out.println("   Total amount: $" + total.getProperty("total"));
        }
    }
    
    /**
     * Cleanup method - call this when you're done
     */
    public void cleanup() {
        System.out.println("\n🛑 Cleaning up...");
        
        try {
            if (database != null) {
                database.close();
            }
            if (server != null) {
                server.stop();
            }
            System.out.println("✅ Cleanup completed");
        } catch (Exception e) {
            System.err.println("⚠️ Cleanup error: " + e.getMessage());
        }
    }
    
    // Helper methods for session management
    
    private void createSession(String sessionId, String userId, String agentId) {
        database.begin();
        try {
            MutableDocument session = database.newDocument("Session");
            session.set("sessionId", sessionId);
            session.set("userId", userId);
            session.set("agentId", agentId);
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
    
    private void updateSessionData(String sessionId, String key, Object value) {
        // Simple approach: use SQL UPDATE
        database.command("sql", 
            "UPDATE Session SET data = data || {'" + key + "': $value} WHERE sessionId = ?", 
            value, sessionId);
    }
    
    private Map<String, Object> getSessionData(String sessionId) {
        ResultSet result = database.query("sql", 
            "SELECT data FROM Session WHERE sessionId = ?", sessionId);
        
        if (result.hasNext()) {
            Result session = result.next();
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) session.getProperty("data");
            return data != null ? data : new HashMap<>();
        }
        return new HashMap<>();
    }
    
    /**
     * Main method to run all examples
     */
    public static void main(String[] args) {
        ArcadeDBEmbeddedExample example = new ArcadeDBEmbeddedExample();
        
        try {
            // Run all examples
            example.initializeDatabase();
            example.documentOperations();
            example.graphOperations();
            example.sessionManagement();
            example.batchOperations();
            example.advancedQueries();
            
            System.out.println("\n🎉 All examples completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Example failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            example.cleanup();
        }
    }
}
