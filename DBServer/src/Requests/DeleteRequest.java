package Requests;

import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Connection;

import DELETE.DeleteRType;
import DELETE.DeleteRTypeMessagesContent;
import DELETE.DeleteRTypeMessagesId;
import DELETE.DeleteRTypeUsersId;
import DELETE.DeleteRTypeUsersUsername;
import main.ConnectionPool;
import main.Main;
/**
 * Handles DELETE requests based on resource type and identifier.
 * Accepts deletion of users/messages by ID or content/username.
 */
public class DeleteRequest extends Request{

	public DeleteRequest(Socket sock, String userPriority, String operationPriority, String[]params) {
		super(sock, userPriority, operationPriority, params);
	}

	@Override
	public void handle() {
		Connection conn;
		try {
			conn = ConnectionPool.getConnectionPool().getConnection();
			DeleteRType req=null;
			
			// Parse command format and create appropriate delete request
			if(params.length==4 && params[2].equals("users") && Main.isInteger(params[3])) {
				req=new DeleteRTypeUsersId(Integer.parseInt(params[3]), conn);
			}
			else if(params.length==4 && params[2].equals("messages") && Main.isInteger(params[3])) {
				req=new DeleteRTypeMessagesId(Integer.parseInt(params[3]), conn);
			}
			else if(params.length==4 && params[2].equals("messages") && !Main.isInteger(params[3])) {
				req=new DeleteRTypeMessagesContent(params[3], conn);
			}
			else if(params.length==4 && params[2].equals("users") && !Main.isInteger(params[3])) {
				req=new DeleteRTypeUsersUsername(params[3], conn);
			}
			else {
				System.out.println("REQUEST DENIED: REQUEST NOT FOUND");
				DataOutputStream ds = new DataOutputStream(sock.getOutputStream());
				ds.writeUTF("REQUEST DENIED: Incorrect format");
				sock.close();
				ConnectionPool.getConnectionPool().returnConnection(conn);
				return;
			}
			
			// Role-based access check
			if(params[0].equals("user") || params[0].equals("priviledged_user")) {
				System.out.println("REQUEST DENIED: UNAUTHORIZED");
				DataOutputStream ds = new DataOutputStream(sock.getOutputStream());
				ds.writeUTF("REQUEST DENIED: You are not admin or priviledged user");
				sock.close();
				ConnectionPool.getConnectionPool().returnConnection(conn);
				return;
			}
			
			// Execute query and return result
			String result=req.executeQuery();
	        System.out.println("REQUEST SUCCESSFULLY EXECUTED");
	        DataOutputStream ds = new DataOutputStream(sock.getOutputStream());
			ds.writeUTF(result);
			ConnectionPool.getConnectionPool().returnConnection(conn);
			sock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
