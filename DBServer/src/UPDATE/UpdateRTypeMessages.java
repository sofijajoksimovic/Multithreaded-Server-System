package UPDATE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * Updates the message content for a given message ID.
 */
public class UpdateRTypeMessages extends UpdateRType{
	private int id;
	private String content;

	public UpdateRTypeMessages(int id, String content, Connection conn) {
		super(conn);
		this.id=id;
		this.content=content;
		query="UPDATE messages SET content = ? WHERE id = ?";
	}

	@Override
	public String executeQuery() {
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, content);
            pstmt.setInt(2, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                return "Content edited successfully for message ID " + id;
            } else {
                return "No message found with ID " + id;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "SQL Error: " + e.getMessage();
        }
	}

}
