package UPDATE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * Updates the users password for a given user ID.
 */
public class UpdateRTypeUsers extends UpdateRType{
	private int id;
	private String password;
	public UpdateRTypeUsers(int id, String password, Connection conn) {
		super(conn);
		this.id=id;
		this.password=password;
		query = "UPDATE users SET password_hash = ? WHERE id = ?";
	}

	@Override
	public String executeQuery() {
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, password);
            pstmt.setInt(2, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                return "Password updated successfully for user ID " + id;
            } else {
                return "No user found with ID " + id;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "SQL Error: " + e.getMessage();
        }
    }

}
