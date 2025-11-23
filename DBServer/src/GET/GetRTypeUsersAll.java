package GET;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Retrieves *all* rows from the `users` table and formats them as a readable string.
 */
public class GetRTypeUsersAll extends GetRType{
	public GetRTypeUsersAll(Connection conn) {
		super(conn);
		query = "SELECT * FROM users";
	}

	@Override
	public String executeQuery() {
		StringBuilder sb=new StringBuilder();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs= stmt.executeQuery(query);
			
			sb.append("Users:\n");
			while(rs.next()) {
				int id = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String pass = rs.getString("password_hash");
                String timestamp = rs.getString("created_at");

                sb.append("ID: ").append(id)
                  .append(", Username: ").append(username)
                  .append(", Email: ").append(email)
                  .append(", Password: ").append(pass)
                  .append(", Timestamp: ").append(timestamp)
                  .append("\n");
			}
			rs.close();
            stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
		return sb.toString();
	}

}
