package main;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.sql.Connection;
/**
 * A very simple JDBC Connection Pool using the Singleton pattern and an
 * ArrayBlockingQueue to reuse a pool of open Connections to MySQL.
 */
public class ConnectionPool {
	/**
     * PRIVATE static inner class ensures:
     * Lazy initialization of ConnectionPool.INSTANCE only when getConnectionPool() is first called.
     * Thread safety guaranteed by JVM class‐initialization rules.
     */
	private static class Holder {
        static final ConnectionPool INST = new ConnectionPool();
    }
	/**Maximum number of pooled connections*/
	private static int POOL_SIZE=10;
	 /**
     * Underlying storage for available connections, bounded to POOL_SIZE.
     * ArrayBlockingQueue is thread-safe and blocks on take()/offer()
     * if the queue is empty/full, ensuring FIFO usage.
     */
	private ArrayBlockingQueue<Connection> pool=new ArrayBlockingQueue<Connection>(POOL_SIZE);
	 /**
     * Protected constructor: loads JDBC driver and creates initial connections.
     */
    protected ConnectionPool(){
    	try {
            for (int i = 0; i < POOL_SIZE; i++) {
            	//Use Class.forName to ensure the MySQL driver is registered.
            	Class.forName("com.mysql.cj.jdbc.Driver"); 
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "root");
                pool.offer(conn);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize connection pool", e);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
    /**
     * Public accessor for the singleton instance.
     * Thread-safe, efficient.
     */
    public static ConnectionPool getConnectionPool() {
        return Holder.INST;
    }
    /**
     * Retrieves a Connection from the pool, blocking if none are available.
     * Caller should return the connection via returnConnection() when done,
     * or the pool will eventually exhaust.
     *
     * InterruptedException is thrown if the waiting thread is interrupted.
     */
    public Connection getConnection()throws InterruptedException{
    	return pool.take();
    }
    /**
     * Returns a Connection back to the pool.
     * If the queue is full, excess connections are silently discarded.
     */
    public void returnConnection(Connection c) {
    	if(c!=null)pool.offer(c);
    }
}
