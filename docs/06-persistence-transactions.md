# 06 - Persistence & Transactions 🛡️

## Apache Ignite Native Persistence
Ignite is more than a cache; it is a **Persistent Database**. By enabling Native Persistence, you store a full copy of data on disk.

### 1. How it Works:
*   **RAM**: Holds the most frequently used data (Hot Data).
*   **Disk**: Holds the complete data set (Cold Data).
*   **Buffer**: When data is written, it goes to RAM first and then is periodically "Checkpointed" to disk.

### 2. The WAL (Write Ahead Log)
This is the most critical part of persistence. Every update is **immediately** written to a sequential log file on disk before it is even updated in RAM.
*   **Purpose**: If the server crashes, Ignite reads the WAL to replay the missing updates and recover perfectly.

---

## 💎 ACID Transactions
Ignite supports fully ACID-compliant transactions across the entire cluster.

### 1. 2PC (Two-Phase Commit)
Ignite uses a 2PC protocol to ensure that a transaction either succeeds on all nodes or fails on all nodes. No partial updates!

### 2. Transaction Modes
*   **PESSIMISTIC**: Locks data during the operation. Safe but slower. Best for high-contention data.
*   **OPTIMISTIC**: Checks for conflicts at the end (Commit time). Faster but fails if someone else changed the data.

### 3. Isolation Levels
*   **READ_COMMITTED**: Prevents reading uncommitted data.
*   **REPEATABLE_READ**: Ensures you read the same value twice in one transaction.
*   **SERIALIZABLE**: Most strict level; prevents "Phantom Reads".

---

## 🏗️ Persistence vs. 3rd Party Persistence
Ignite can persist data in two ways:
1.  **Native Persistence**: (Recommended) Fast, supports instant restarts, and zero-data-loss.
2.  **External DB**: Sync Ignite with MySQL/Oracle using `CacheStore`. (Slower, harder to maintain).

---

## 🚀 Instant Restart Property
> [!IMPORTANT]
> Because data is on disk, you don't have to wait for Ignite to "load" data into RAM before starting your app. Ignite starts **instantly** and pulls data from disk on-demand as your queries arrive.
