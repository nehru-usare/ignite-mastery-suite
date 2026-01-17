# Apache Ignite Mastery - Run Instructions 🚀

This project uses **Apache Ignite** in a Standalone Architecture. Because we are using Java 17+, you **MUST** run the app with specific JVM flags to allow memory access.

---

## 🛠️ Option 1: Run with Maven (Recommended)

This is the easiest way as all flags are pre-configured in the `pom.xml`.

1.  **Start the Ignite Server**:
    In a terminal, run:
    ```cmd
    mvn exec:exec@start-ignite-server
    ```
2.  **Start the Spring Boot App**:
    In a **NEW** terminal, run:
    ```cmd
    mvn spring-boot:run
    ```

3.  **Explore the API (Swagger UI)**:
    Open your browser and navigate to:
    👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

4.  **Real-Time Cache Dashboard**:
    Watch your hits and misses in real-time:
    👉 [http://localhost:8080/index.html](http://localhost:8080/index.html)

---

## 🔧 Why am I seeing "InaccessibleObjectException"?

If you try to run the Java code using the IDE's "Play" button without configuration, it will fail. This is because **Java 17/21** restricts access to internal memory (which Ignite needs for high performance). 

**The Fix:** Always use the Maven commands or the scripts provided, which include the `--add-opens` flags.
