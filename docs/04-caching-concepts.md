# 04 - Caching Concepts & Strategies 📊

## Cache Modes
Ignite offers different ways to distribute data. Choosing the right mode is critical for performance.

### 1. PARTITIONED (Default)
Data is split into pieces (partitions) and spread across all server nodes.
*   **Scalability**: High. Adding nodes increases total capacity.
*   **Performance**: Good for large data sets.
*   **Best For**: Big data, transaction history, user profiles.

### 2. REPLICATED
Every node has a **complete copy** of the data.
*   **Scalability**: Low for writes (every node must update).
*   **Performance**: Lightning fast for reads (always local).
*   **Best For**: Dictionary data, configuration settings, country codes.

---

## 🏗️ Common Caching Strategies

### Cache-Aside (Lazy Load)
The application code handles the logic:
1.  Check Ignite.
2.  If Miss -> Read from DB.
3.  Write result to Ignite.
*   **Pro**: Simple, works with any DB.
*   **Con**: Stale data risk if DB is updated directly.

### Read-Through / Write-Through
Ignite handles the logic automatically via a `CacheStore`.
1.  App asks Ignite for data.
2.  If Miss -> Ignite calls DB and populates itself.
3.  App gets the data transparently.
*   **Pro**: Centralized logic, cleaner app code.

### Write-Behind
Ignite stores the write in memory immediately and **batches** the updates to the DB asynchronously.
*   **Pro**: Extreme write speed.
*   **Con**: Risk of data loss if Ignite crashes before the DB is updated.

---

## 🕐 Data Expiry & Eviction

### Expiry Policies
Remove data based on time.
*   **CreatedExpiryPolicy**: Delete after X minutes since creation.
*   **AccessedExpiryPolicy**: Delete after X minutes since the last time it was read.

### Eviction Policies
Remove data based on RAM limit.
*   **LRU (Least Recently Used)**: Toss the oldest, unused data.
*   **FIFO (First In First Out)**: Toss the oldest created data.

---

## 🌉 Atomic vs. Transactional
*   **ATOMIC**: Fast, high performance. No deadlocks. Good for simple KV updates.
*   **TRANSACTIONAL**: Supports ACID (Atomicity, Consistency, Isolation, Durability). Good for complex updates involving multiple keys.

---
> [!TIP]
> **Performance Winner**: If you have a table that is small (< 10,000 rows) and read very often, use **REPLICATED** mode. It will eliminate all network overhead for reads!
