# 10 - Ignite Interview Q&A (Beginner to Advanced) 🎓

## 🔰 Beginner Level

**Q1: What is the primary advantage of Apache Ignite over a traditional RDBMS?**
**A**: Speed and Scalability. Ignite stores data in-memory (RAM), making it orders of magnitude faster. It is also horizontally scalable, meaning you can add nodes to increase capacity.

**Q2: Does Apache Ignite support SQL?**
**A**: Yes, it supports ANSI-99 SQL, including JOINs, aggregations, and DML/DDL.

**Q3: What is the difference between a Server Node and a Client Node?**
**A**: Server nodes store data and perform computations. Client nodes do not store data; they act as an entry point for applications to connect to the cluster.

---

## 💻 Intermediate Level (Developer)

**Q4: What is "Collocated Processing"?**
**A**: It is the practice of sending the computation to the data, rather than bringing the data to the computation. In Ignite, you use the `Compute Grid` to run logic on the specific node where the data lives, eliminating network overhead.

**Q5: Explain the difference between REPLICATED and PARTITIONED cache modes.**
**A**: 
*   **REPLICATED**: Every node has a full copy. Great for small tables read often.
*   **PARTITIONED**: Data is split across nodes. Great for large tables that need scalability.

**Q6: What is a "Data Region"?**
**A**: A Data Region is a logical bucket of off-heap memory. You can configure multiple regions with different sizes, eviction policies, and persistence settings.

---

## 🏗️ Advanced Level (Architect)

**Q7: How does Ignite handle Java Garbage Collection (GC) issues for TBs of data?**
**A**: Ignite uses an **Off-Heap Memory** architecture. Data is stored in pages outside the standard Java Heap, so the JVM Garbage Collector doesn't see or manage it. This allows for massive data sets without GC pauses.

**Q8: What is the "Write-Ahead Log" (WAL) in Native Persistence?**
**A**: The WAL is a sequential disk file where every update is appended before it's updated in RAM. It ensures that if a node crashes, the data can be recovered 100% by replaying the log.

**Q9: Explain the "Baseline Topology" and its importance.**
**A**: Baseline Topology (BLT) is the set of server nodes that store data. It's crucial for persistence because it defines the "quorum" of nodes needed for a healthy cluster. If nodes join or leave, the BLT ensures data is rebalanced correctly.

**Q10: What is a "Split-Brain" scenario and how does Ignite fix it?**
**A**: Split-brain happens when a network partition divides the cluster into two groups that can't see each other. Ignite uses a **Segmentation Policy** (usually shutting down the smaller group) to prevent both sides from accepting writes, which would lead to data corruption.

**Q11: How do you monitor the health and performance of an Ignite Cluster in production?**
**A**: You can use JMX, Ignite Control Center, or exposed metrics endpoints. Key metrics to watch include **Cache Hit Percentage**, **Heap vs. Off-heap usage**, **Transaction Latency**, and **WAL speed**. In our project, we implemented a real-time dashboard using the `ignite.cluster().enableStatistics()` API.

**Q12: Why prefer Standalone Mode over Embedded Mode for production?**
**A**: Decoupling. Standalone mode allows the data layer to persist independently of application deployments. It also enables multiple microservices to share the same cached data, providing a unified "Data Fabric" for the entire enterprise.

---

## 🏆 Top Tip for Candidates
> [!TIP]
> When asked about Ignite performance, always mention **"Data Collocation"** and **"Off-Heap Memory"**. These are the two key features that make Ignite an elite enterprise platform.
