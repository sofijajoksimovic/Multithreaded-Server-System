package DELETE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Deletes user from the 'users' table where the ID matches the specified ID.
 */
public class DeleteRTypeUsersId extends DeleteRType{
	private int id;
	public DeleteRTypeUsersId(int id, Connection conn) {
		super(conn);
		this.id=id;
		query="DELETE FROM users WHERE id = ?";
	}

	@Override
	public String executeQuery() {
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			String query1 = "SELECT * FROM Messages WHERE sender_id=" + id + " OR recipient_id=" + id;
			Statement st = conn.createStatement();
			if (st.execute(query1)) {
				System.out.println("REQUEST DENIED: ON DELETE RESTRICT");
				return "REQUEST DENIED: ON DELETE RESTRICT";
			}
			
	        stmt.setInt(1, id);
	        int rowsAffected = stmt.executeUpdate();
	    
	        return rowsAffected > 0 ? "User deleted successfully." : "No user found with the given ID.";
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error deleting user: " + e.getMessage();
	    }
	}

}
