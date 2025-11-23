# Internship Project – Schneider Electric  

Author: Sofija Joksimović

## Project Topic

A multithreaded server system with **thread-safe** request processing and a **custom connection pool** for database operations.

## Project Description

The goal of this project is to develop a server application capable of processing multiple client requests in parallel using:

a custom, thread-safe thread pool

a database connection pool for efficient connection reuse

The server communicates with clients via **TCP/IP sockets**. Clients send requests as formatted strings containing user credentials and an operation (GET, POST, UPDATE, DELETE).

Initial operation priorities:
GET = 3, POST = 1, UPDATE = 2, DELETE = 1
These can be modified using the SET_PRIORITY request.
The server determines processing order using a combined priority (credential priority + operation priority).

## Features
1. Server
Opens a TCP socket and accepts requests in separate threads
Forwards requests to the thread pool with their assigned priority
Uses a priority queue for scheduling
During processing, obtains a database connection from the connection pool
Sends the response back to the client

2. Thread Pool (Singleton)
Fixed number of worker threads
Uses a priority queue with combined priority
Credential-based priority:
admin = 3
privileged = 2
user = 1
Operation priority is dynamic (GET, POST, UPDATE, DELETE).
Total priority = credential priority + operation priority.
Only admin can modify operation priorities via SET_PRIORITY.

3. Connection Pool (Singleton)
Limited number of available database connections (e.g., 10)
If all connections are busy, threads wait
After processing, the connection is returned to the pool
Fully thread-safe

4. Client
Clients can update operation priorities using:
CREDENTIAL#SET_PRIORITY#OP1:a#OP2:b#...#OPX:x
Regular requests follow the format:
CREDENTIAL#OPERATION#PARAMS
Example:
admin#GET#users
The client receives and displays the server's response.

6. Operation Priority Management
The server starts with initial priorities
Priorities can be updated at any time using SET_PRIORITY
Updates apply to both new and already queued requests
Updated requests are reinserted into the priority queue

6. Database (MySQL Workspace)

Database structure includes tables:
  users
  messages
Supported operations: GET, POST, UPDATE, DELETE.
Database Request Formats:
GET

GET#users
GET#users#id
GET#messages
GET#messages#id
GET#users#recipient#id

POST

POST#users#username#email#password
POST#messages#sender_id#reader_id#content

UPDATE

UPDATE#users#password#id
UPDATE#messages#content#id

DELETE

DELETE#users#id
DELETE#users#username
DELETE#messages#id
DELETE#messages#content

## Testing
Requests can be sent:
Synchronously: sending one request at a time and waiting for the response
Asynchronously: simulating many concurrent requests (e.g., 100) to test:
  thread pool efficiency
  connection pool behavior
  overall server performance
