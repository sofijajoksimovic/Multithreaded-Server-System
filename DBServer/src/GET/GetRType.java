package GET;

import java.sql.Connection;
/**
 * Base class for read-only database operations in your GET commands.
 *
 * Each subclass represents a specific GET request type (e.g. fetching all users,
 * fetching a message by ID, fetching users by recipient, etc.).
 */
public abstract class GetRType {
	protected String query=null;
	protected Connection conn;
	
	/**
     * Initialize with a database {@link Connection} from the pool.
     *
     * @param conn already-open JDBC connection; must not be null.
     */
	public GetRType(Connection conn) {
		this.conn=conn;
	}
	
	/**
     * Executes the SQL update or related transaction logic.
     *
     * @return client-readable result text.
     */
	public abstract String executeQuery();
}
