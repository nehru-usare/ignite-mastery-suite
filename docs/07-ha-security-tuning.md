# 07 - HA, Security & Performance Tuning ⚙️

## 🛡️ High Availability (HA) & Failover

### Baseline Topology
When persistence is enabled, Ignite uses a "Baseline Topology" (BLT). This is a set of nodes allowed to store data.
*   If a node leaves the BLT, Ignite triggers **Rebalancing** to ensure the backup count remains correct.

### Split-Brain (Network Partitioning)
If your network breaks and the cluster splits into two halves:
*   Ignite uses a **Segmentation Policy**.
*   Standard Policy: The smaller half shuts down to prevent data corruption ("Split-Brain").

---

## 🔒 Security in Ignite

### 1. Authentication
*   **Native Authentication**: Basic Username/Password stored in Ignite.
*   **JAAS/LDAP**: Integrate with enterprise directory services.

### 2. Authorization
Define who can Read, Write, or Administer specific caches.

### 3. Encryption
*   **TDE (Transparent Data Encryption)**: Encrypts data sitting on the disk.
*   **SSL/TLS**: Encrypts data moving across the network between nodes.

---

## 🏎️ Performance Tuning Best Practices

### 1. JVM Flags (The "Secret" Tuning)
Always use G1 or ZGC and provide the `--add-opens` flags as we did in this project. This allows Ignite to optimize data serialization.

### 2. Collocated Computations
**Don't bring data to your app; Move your logic to the data.**
*   Instead of `cache.get(100MB_Object)`, use `IgniteCompute` to run the calculation *on the node* where the data lives. Result: 0 network overhead!

### 3. Thread Pool Tuning
Adjust the `systemThreadPoolSize` and `queryThreadPoolSize` based on your machine's CPU cores.

---

## 📊 Essential Monitoring Metrics
Keep an eye on these via JMX:
*   **Cache Hits vs Misses**: Is your cache actually helping?
*   **Checkpointing Time**: If this takes too long, your disk is too slow.
*   **Off-Heap Memory Usage**: Ensure you are not reaching 90%+.

---
> [!CAUTION]
> **Disk Speed Matters**: If you enable Native Persistence, use **SSDs (NVMe preferred)**. Using old HDDs will bottleneck Ignite's entire performance because the WAL (Write Ahead Log) will be too slow.
