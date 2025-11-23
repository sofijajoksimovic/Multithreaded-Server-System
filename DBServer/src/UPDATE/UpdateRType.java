package UPDATE;

import java.sql.Connection;

/**
* Base class for UPDATE-style database operations.
*/
public abstract class UpdateRType {
	protected String query=null;
	protected Connection conn;
	
	/**
     * Initialize with a database {@link Connection} from the pool.
     *
     * @param conn already-open JDBC connection; must not be null.
     */
	public UpdateRType(Connection conn) {
		this.conn=conn;
	}
	
	/**
     * Executes the SQL update or related transaction logic.
     *
     * @return client-readable result text.
     */
	public abstract String executeQuery();
}


