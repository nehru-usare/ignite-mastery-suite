# 02 - Architecture & Cluster Concepts 🏗️

## The Distributed Topology
Apache Ignite is designed to run on a cluster of machines. Unlike a master-slave database, Ignite is **Peer-to-Peer**. All nodes are equal in the eyes of the discovery protocol.

### 1. Server Nodes
*   **Role**: The "Workers".
*   **Storage**: They store the actual data partitions.
*   **Compute**: Executing incoming SQL queries or compute tasks.
*   **Persistence**: If enabled, they write data to their local disk.

### 2. Client Nodes (Thick Clients)
*   **Role**: Part of the cluster but **do not store data**.
*   **Topology Aware**: They know which server node holds which piece of data, making them extremely fast for "Collocated Processing".
*   **Usage**: Usually used for Spring Boot / Java application integrations.

### 3. Thin Clients
*   **Role**: Lightweight clients (JDBC, Python, Node.js).
*   **Connection**: Communicate via a binary protocol to a specific gateway node.
*   **Pro**: Lower RAM footprint on the application side.

---

## 🧩 Core Cluster Concepts

### Discovery SPI
How nodes find each other.
*   **Static IP Finder**: You provide a list of IPs.
*   **Multicast**: Nodes find each other on the local network automatically.
*   **Cloud Finders**: Specialized finders for AWS (S3/Zookeeper) and Kubernetes.

### Communication SPI
Once nodes find each other, this handles the actual data transfer (TCP/IP).

### Partitioning
Data is split into **1024 virtual partitions** (by default). These partitions are balanced across all available server nodes.
*   If you have 2 nodes, each gets ~512 partitions.
*   If you add a 3rd node, Ignite automatically "rebalances" and moves partitions to the new node.

---

## 🔄 High Availability & Failover
Ignite handles node failures gracefully:
1.  **Backups**: You can configure 1, 2, or more backups.
2.  **Detection**: If Node A stops heartbeat, Node B (holding the backup) immediately becomes the "Primary" for those partitions.
3.  **Self-Healing**: Once Node A returns, it automatically catches up with missed data.

---

## 🛠️ Practical Tip: Server vs Client
> [!TIP]
> **Production Rule**: Never run business logic on a Server Node. 
> Keep Server Nodes dedicated to storage and calculation. Run your API logic on **Client Nodes** to prevent business bugs from crashing your database storage nodes.
