package DELETE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * Deletes messages from the 'messages' table where the content matches the specified string.
 */
public class DeleteRTypeMessagesContent extends DeleteRType{
	private String content;
	public DeleteRTypeMessagesContent(String content, Connection conn) {
		super(conn);
		this.content=content;
		query="DELETE FROM messages WHERE content = ?";
	}

	@Override
	public String executeQuery() {
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			
	        stmt.setString(1, content);
	        int rowsAffected = stmt.executeUpdate();
	    
	        return rowsAffected > 0 ? "Message deleted successfully." : "No message found with the given content.";
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error deleting user: " + e.getMessage();
	    }
	}

}
