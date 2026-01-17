# 11 - Scenario-Based Interview Challenges 🧠💼

These questions test your ability to apply Apache Ignite to complex, real-world architectural problems. This is exactly what senior-level interviewers look for.

---

## 🏗️ Scenario 1: The "Hot Partition" Problem
**Scenario:** You have a large `Order` cache partitioned across 10 nodes. One specific customer (Amazon) has millions of orders, while others have few. You notice that the node holding Amazon's orders is at 100% CPU, while others are idle. How do you solve this "data skew"?

**Answer:** 
1.  **Affinity Collocation Analysis**: The problem is that the default partitioning is likely using `customerId` as the affinity key.
2.  **Solution**: Change the **Affinity Key**. Instead of just `customerId`, use `orderId` or a combination of `customerId + month`. This will spread Amazon's orders across all nodes.
3.  **Trade-off**: If you spread orders across nodes, you can no longer do a local JOIN between a Customer and their Orders on a single node. You must balance "processing speed" vs. "distribution".

---

## 💾 Scenario 2: Legacy Database Bottleneck
**Scenario:** You have a legacy Oracle DB that can only handle 500 transactions per second (TPS). Your new mobile app expects 20,000 TPS. How do you use Ignite as a "Digital Integration Hub" here?

**Answer:**
1.  **Architecture**: Implement a **Read-Through / Write-Behind** strategy.
2.  **Read-Through**: Cache the data in Ignite. The app reads from Ignite at memory speed.
3.  **Write-Behind**: When the app writes, Ignite saves the change in RAM instantly and batches the updates to Oracle every 30 seconds.
4.  **Result**: The app "sees" 20,000 TPS speed, while the legacy DB is protected by the batching layer.

---

## ⚡ Scenario 3: Real-Time Fraud Processing
**Scenario:** A bank needs to run 50 fraud rules on every credit card transaction in less than 20ms. The transaction data and the user's historical data are on different nodes. How do you meet the 20ms SLA?

**Answer:**
1.  **Compute Grid**: Do not pull data to your server. Use `ignite.compute().broadcast()` or `affinityRun()`.
2.  **Data Collocation**: Ensure the User Profile and their Transactions are stored on the same node using an `Affinity Key`.
3.  **Local Execution**: The fraud rules will run locally on the node where the data resides. No network latency = meeting the 20ms deadline.

---

## 🔄 Scenario 4: The 100% RAM Crash
**Scenario:** During a heavy marketing campaign, your Ignite cluster reaches 95% RAM usage. If it hits 100%, the cluster will crash. You cannot add new servers right now. What is your emergency strategy?

**Answer:**
1.  **Enable Eviction Policy**: Immediately configure an LRU (Least Recently Used) or FIFO eviction policy on non-critical caches.
2.  **Swap to Disk**: If Native Persistence is enabled, Ignite can automatically "evict" data from RAM to Disk. The data is still there, just slightly slower to read.
3.  **TTL (Time To Live)**: Set a short expiration time on session/temporary data to free up space automatically.

---

## 🛡️ Scenario 5: Disaster Recovery (DR)
**Scenario:** Your main data center in NYC goes offline. You have a second data center in London. How do you ensure your Ignite cluster continues to work with zero data loss?

**Answer:**
1.  **Data Center Replication (DR)**: Use Ignite's Data Center Replication feature (GridGain plugin or custom implementation).
2.  **Asynchronous vs Synchronous**:
    *   **Synchronous**: London must confirm the write before NYC finishes. Zero data loss but slower performance due to transatlantic latency.
    *   **Asynchronous**: NYC writes instantly, and London catches up. High performance but risks losing the last few seconds of data during the crash.

---

## 🎓 Tips for the Interviewer
*   **Show, don't just tell**: Mention that you've implemented standalone server nodes and handled Java 17 memory flags, as this shows practical experience.
*   **Mention "ACID"**: Always emphasize that Ignite isn't just a cache—it's a transactional database.
