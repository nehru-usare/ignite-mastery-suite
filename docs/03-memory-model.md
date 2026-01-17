# 03 - Memory Model & Data Regions 🧠

## Off-Heap Memory (The Secret Sauce)
Most Java applications store objects on the "Heap". This causes **GC (Garbage Collection) pauses**, which kill performance when you have TBs of data.

Ignite uses **Off-Heap Memory** (Direct Memory).
*   Data is stored in "Raw Bytes" outside the standard Java Heap.
*   Result: Zero GC impact, regardless of how much data you store.

---

## 🏙️ Data Regions
Think of a Data Region as a **dedicated RAM bucket**. You can define multiple regions with different characteristics.

### Example Configuration:
*   **Default Region**: 4GB, Persistent (to disk).
*   **Session Region**: 1GB, Volatile (In-memory only, no disk).

### Key Settings for a Region:
1.  **InitialSize**: How much RAM to grab at startup.
2.  **MaxSize**: High-water mark for growth.
3.  **PersistenceEnabled**: Whether to save this region to disk.
4.  **EvictionPolicy**: What to do when the region is full (Random, LRU, or Stop).

---

## 📦 Page Architecture
Ignite organizes memory into **Pages** (conceptually like a Linux OS).
*   **Standard Page Size**: 4KB.
*   **Header**: Stores metadata.
*   **Payload**: Stores your actual serialized objects.

When memory fragments, Ignite performs **Defragmentation** to keep access times fast.

---

## 📈 Performance & Monitoring
Because data is off-heap, you cannot see Ignite's memory usage via `jconsole` or standard JVM tools easily.

### Monitoring Tools:
*   **JMX**: Native MBeans for memory monitoring.
*   **Ignite Control Center**: GUI for real-time visualization.
*   **OS Level**: Use `top` or Task Manager to see the "Resident Set Size" (RSS) of the process.

---

## 🚀 How to choose Region Size?
> [!IMPORTANT]
> **The 80/20 Rule**: 
> Always leave at least **20% of your machine's physical RAM** for the Operating System. If Ignite tries to take 100%, the OS will start "swapping" to disk, and your performance will drop by 90%+.
