package POST;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Inserts a new message record into the `messages` table.
 */
public class PostRTypeMessages extends PostRType{
	private int sender_id;
	private int recipient_id;
	private String content;
	public PostRTypeMessages(int sender_id, int recipient_id, String content, Connection conn) {
		super(conn);
		this.sender_id = sender_id;
		this.recipient_id = recipient_id;
		this.content = content;
		query = "INSERT INTO messages (sender_id, recipient_id, content, sent_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
	}

	@Override
	public String executeQuery() {
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {

			String query1 = "SELECT * FROM Users WHERE id = " + sender_id;
			String query2 = "SELECT * FROM Users WHERE id = " + recipient_id;

			Statement stmt1 = conn.createStatement();
			Statement stmt2 = conn.createStatement();

			ResultSet rs1 = stmt1.executeQuery(query1);
			ResultSet rs2 = stmt2.executeQuery(query2);

			if (!rs1.next() || !rs2.next()) {
			    System.out.println("REQUEST DENIED: No user ID");
			    return "REQUEST DENIED: No user ID";
			}

			
            pstmt.setInt(1, sender_id);
            pstmt.setInt(2, recipient_id);
            pstmt.setString(3, content);
            
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                return "Message inserted successfully.";
            } else {
                return "Message insertion failed.";
            }
		}
		catch (SQLException e) {
            e.printStackTrace();
            return "SQL Error: " + e.getMessage();
        }

	}
}
