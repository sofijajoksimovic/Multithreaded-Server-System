package GET;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Retrieves *all* rows from the `messages` table and formats them into
 * a client-readable String.
 */
public class GetRTypeMessagesAll extends GetRType{
	public GetRTypeMessagesAll(Connection conn) {
		super(conn);
		query="SELECT * FROM messages";
	}

	@Override
	public String executeQuery() {
		StringBuilder sb=new StringBuilder();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs= stmt.executeQuery(query);
			
			sb.append("Messages:\n");
			while(rs.next()) {
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
			rs.close();
            stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
		return sb.toString();
	}

}
