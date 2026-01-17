# 08 - Deployment & Standalone Setup 🚀

## Standalone Architecture (Enterprise Standard)
In a professional environment, Apache Ignite is not embedded within your application. It runs as a **Standalone Cluster**.

### Why Standalone?
1.  **Isolation**: Your app crashing doesn't kill the data layer.
2.  **Shared Memory**: Multiple microservices can connect to the same data.
3.  **Independent Scaling**: Add more Ignite nodes without restarting your apps.

---

## 🛠️ Running as a Service

### 1. Using XML Configuration
You define the cluster behavior in a `.xml` file (like we did in our `ignite-server.xml`).

### 2. Starting the Node
You can run the binary directly:
```cmd
ignite.bat path/to/ignite-server.xml
```
**Or use our pre-configured Maven command (Recommended):**
```bash
mvn exec:exec@start-ignite-server
```

### 3. Native Service Install (Windows/Linux)
*   **Windows**: Use `sc create` or tools like NSSM to run the command above as a Windows Service.
*   **Linux**: Create a `systemd` service file to ensure Ignite starts automatically on reboot.

---

## 🌐 Connecting Other Services

### 1. Thick Client (Java/.NET/C++)
The application joins the cluster as a "Client Node". It is topology-aware. Best for high-performance Java apps.

### 2. Thin Client (Odbc, Jdbc, Python, NodeJS)
Lightweight connection via a single port. Great for polyglot microservices (e.g., a Python AI service reading data from an Ignite cluster).

### 3. REST API
Enable HTTP connectivity in the configuration:
```xml
<property name="connectorConfiguration">
    <bean class="org.apache.ignite.configuration.ConnectorConfiguration"/>
</property>
```
Now you can `curl` your data!

---

## 🏗️ Deployment Strategies

### 1. Bare Metal / VM
Direct installation on physical or virtual servers. Provides the best raw performance.

### 2. Docker & Kubernetes
*   **Docker**: Use the official `apacheignite/ignite` image.
*   **Kubernetes**: Use the **Ignite Kubernetes Operator**. It handles discovery and self-healing in cloud environments automatically.

### 3. Dedicated Cloud
Use managed versions of Ignite (like GridGain Nebula) if you don't want to manage the infrastructure yourself.

---
> [!IMPORTANT]
> **Topology Tip**: In production, place your Ignite Cluster and your Application in the **same VPC or Data Center**. High network latency between the app and the cluster will ruin your "In-Memory" speed!
