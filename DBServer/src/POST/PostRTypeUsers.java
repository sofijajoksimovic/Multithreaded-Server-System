package POST;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * Inserts a new user record into the `users` table.
 */
public class PostRTypeUsers extends PostRType{
	private String username;
	private String email;
	private String password;
	public PostRTypeUsers(String username, String email, String password, Connection conn) {
		super(conn);
		this.username=username;
		this.email=email;
		this.password=password;
		query = "INSERT INTO users (username, email, password_hash, created_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
	}

	@Override
	public String executeQuery() {
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			String query1 = "SELECT * FROM Users WHERE username = ?";
			PreparedStatement st = conn.prepareStatement(query1);
			st.setString(1, username);
			if (st.executeQuery()!=null) {
				System.out.println("REQUEST DENIED: DUPLICATE USERNAME");
				return "REQUEST DENIED: DUPLICATE USERNAME";
			}
			
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                return "User inserted successfully.";
            } else {
                return "User insertion failed.";
            }
		}
		catch (SQLException e) {
            e.printStackTrace();
            return "SQL Error: " + e.getMessage();
        }
	}

}
