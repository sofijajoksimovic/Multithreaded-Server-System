package DELETE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * Deletes users from the 'users' table where the username matches the specified string.
 */
public class DeleteRTypeUsersUsername extends DeleteRType{
	private String username;
	public DeleteRTypeUsersUsername(String username, Connection conn) {
		super(conn);
		this.username=username;
		query="DELETE FROM users WHERE username = ?";
	}

	@Override
	public String executeQuery() {
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			String query1 = "SELECT * FROM Messages m JOIN Users sender ON sender.id=m.sender_id JOIN Users recipient ON recipient.id=m.recipient_id "
					+ " WHERE recipient.username = ? OR sender.username = ?";
			PreparedStatement st = conn.prepareStatement(query1);
			st.setString(1, username);
			st.setString(2, username);
			if (st.executeQuery() != null) {
				System.out.println("REQUEST DENIED: ON DELETE RESTRICT");
				return "REQUEST DENIED: ON DELETE RESTRICT";
			}
			
	        stmt.setString(1, username);
	        int rowsAffected = stmt.executeUpdate();
	    
	        return rowsAffected > 0 ? "User deleted successfully." : "No user found with the given username.";
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error deleting user: " + e.getMessage();
	    }
	}
}
