package GET;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Retrieves one message row by its ID.
 * Performs a safe, parameterized query and formats the output.
 */
public class GetRTypeMessagesId extends GetRType{
	public GetRTypeMessagesId(int param, Connection conn ) {
		super(conn);
		query = "SELECT * FROM messages WHERE id = "+ param;
	}

	@Override
	public String executeQuery() {
		StringBuilder sb=new StringBuilder();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs= stmt.executeQuery(query);
			
			sb.append("Messages:\n");
			if(rs.next()) {
				int id = rs.getInt("id");
	            String sender = rs.getString("sender_id");
	            String receiver = rs.getString("recipient_id");
	            String content = rs.getString("content");
	            String timestamp = rs.getString("sent_at");
	
	            sb.append("ID: ").append(id)
	              .append(", Sender: ").append(sender)
	              .append(", Receiver: ").append(receiver)
	              .append(", Content: ").append(content)
	              .append(", Timestamp: ").append(timestamp)
	              .append("\n");
			}
			else {
				sb.append("Message not found");
			}
			rs.close();
            stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
		return sb.toString();
	}

}
