# Internship Project – Schneider Electric  
**Author:** Sofija Joksimović  

## Project Topic  
A multithreaded server system with **thread-safe request processing** and a **custom connection pool** for database operations.

---

## Project Description  
The goal of this project is to develop a server application capable of handling multiple client requests in parallel using:

- a custom **thread-safe thread pool**  
- a **database connection pool** for efficient connection management  

The server communicates with clients over a **TCP/IP socket**.  
Clients send requests as strings containing user credentials and an operation (`GET`, `POST`, `UPDATE`, `DELETE`).

Default operation priorities are:

| Operation | Priority |
|----------|----------|
| GET      | 3        |
| POST     | 1        |
| UPDATE   | 2        |
| DELETE   | 1        |

These can be changed using the `SET_PRIORITY` request.  
The server uses a **combined priority** (credential + operation) to schedule requests.

---

## Features

### 1. **Server**
- Opens a socket and accepts requests in separate threads  
- Forwards requests to the thread pool with their priority  
- Uses a **priority queue** for scheduling  
- Uses a connection from the connection pool during request processing  
- Sends results back to the client via socket  

---

### 2. **Thread Pool (Singleton)**
- Fixed number of worker threads  
- Uses a priority queue with combined priority  

**Fixed credential-based priority**:  
- `admin = 3`  
- `privileged = 2`  
- `user = 1`  

**Variable operation priority** (e.g., GET=2, POST=1, ...)  

**Total priority = credential priority + operation priority**

- Operation priorities can be updated dynamically using `SET_PRIORITY`, but **only by admin**

---

### 3. **Connection Pool (Singleton)**
- Limited number of database connections (e.g., 10)  
- Threads wait if all connections are busy  
- Connections are returned to the pool after use  
- Fully thread-safe access control  

---

### 4. **Client**
- Can update operation priorities:  
