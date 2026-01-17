# 05 - SQL & Indexing 🔍

## Ignite as a SQL Database
Ignite is one of the few memory systems that provides **ANSI-99 SQL** support. You can use standard JOINs, Groups, and Aggregations.

### How it Works:
Ignite maps "Caches" to "SQL Tables".
*   `CacheConfiguration.name` = Table Name.
*   Fields annotated with `@QuerySqlField` = Table Columns.

---

## ⚡ Indexing for Speed
Without indexes, SQL queries perform a "Full Scan" (slow).

### Types of Indexes:
1.  **Primary Index**: Automatically created for the cache key.
2.  **Sorted Index**: Created using `@QuerySqlField(index = true)`. Good for ranges (`price > 100`).
3.  **Group Index**: Combining multiple fields into one index.

### ⚠️ The Cost of Indexing:
Every index speeds up **Reads** but slightly slows down **Writes** (because the index must be updated).

---

## 🤝 Distributed Joins
In a distributed system, joining data is hard because data is on different nodes.

### 1. Collocated Joins (Fastest)
You design your data so that related rows are on the same node.
*   *Example*: All "Orders" for "User 1" are stored on the same node as "User 1".
*   This makes the JOIN local and extremely fast.

### 2. Non-Collocated Joins (Slower)
Ignite sends data across the network to perform the JOIN.
*   Enable this with `query.setDistributedJoins(true)`.
*   *Warning*: Use sparingly, as it causes massive network traffic.

---

## 🛠️ Tooling
You can connect to Ignite using standard SQL tools:
*   **DBeaver / DataGrip**: Connect via JDBC.
*   **Ignite Web Console**: Interactive SQL editor.
*   **SQLLine**: Command-line tool provided in the Ignite binary.

---

## 💣 Common SQL Mistakes
> [!CAUTION]
> **Missing @QuerySqlField**: If you forget this annotation, the field exists in Java but simply **does not exist** in the SQL engine.
>
> **SELECT ***: Avoid this! In a distributed system, transferring every column across the network for millions of rows will cause high latency. Always select specific columns.
