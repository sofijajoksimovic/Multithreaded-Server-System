package DELETE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * Deletes message from the 'messages' table where the ID matches the specified ID.
 */
public class DeleteRTypeMessagesId extends DeleteRType{
	private int id;
	public DeleteRTypeMessagesId(int id, Connection conn) {
		super(conn);
		this.id=id;
		query="DELETE FROM messages WHERE id = ?";
	}

	@Override
	public String executeQuery() {
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setInt(1, id);
	        int rowsAffected = stmt.executeUpdate();
	    
	        return rowsAffected > 0 ? "Message deleted successfully." : "No message found with the given ID.";
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error deleting user: " + e.getMessage();
	    }
	}
}
