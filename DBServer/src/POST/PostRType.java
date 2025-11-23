package POST;

import java.sql.Connection;
/**
 * Base class for "POST" operations (Create) in the database.
 */
public abstract class PostRType {
	protected String query=null;
	protected Connection conn;
	
	/**
     * Initialize with a database {@link Connection} from the pool.
     *
     * @param conn already-open JDBC connection; must not be null.
     */
	public PostRType(Connection conn) {
		this.conn=conn;
	}
	
	/**
     * Executes the SQL update or related transaction logic.
     *
     * @return client-readable result text.
     */
	public abstract String executeQuery();
}
