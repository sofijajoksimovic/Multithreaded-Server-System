package Requests;

import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Connection;

import UPDATE.UpdateRType;
import UPDATE.UpdateRTypeMessages;
import UPDATE.UpdateRTypeUsers;
import main.ConnectionPool;
import main.Main;
/**
 * Handles UPDATE requests and routes them to the correct UpdateRType handler.
 * Ensures proper authorization and request format before executing the operation.
 */
public class UpdateRequest extends Request{

	public UpdateRequest(Socket sock, String userPriority, String operationPriority, String[] params) {
		super(sock, userPriority, operationPriority, params);
	}

	@Override
	public void handle() {
		Connection conn;
		try {
			conn = ConnectionPool.getConnectionPool().getConnection();
			UpdateRType req=null;
			
			// Determine type of POST operation from parameters
			if(params.length==5 && params[2].equals("users") && Main.isInteger(params[3])) {
				req=new UpdateRTypeUsers(Integer.parseInt(params[3]),params[4], conn);
			}
			else if(params.length==5 && params[2].equals("messages")&& Main.isInteger(params[3])) {
				req=new UpdateRTypeMessages(Integer.parseInt(params[3]), params[4], conn);
			}
			else {
				System.out.println("REQUEST DENIED: REQUEST NOT FOUND");
				DataOutputStream ds = new DataOutputStream(sock.getOutputStream());
				ds.writeUTF("REQUEST NOT FOUND");
				sock.close();
				ConnectionPool.getConnectionPool().returnConnection(conn);
				return;
			}
			
			// Role-based access check
			if(params[0].equals("user")) {
				System.out.println("REQUEST DENIED: UNAUTHORIZED");
				DataOutputStream ds = new DataOutputStream(sock.getOutputStream());
				ds.writeUTF("REQUEST DENIED: You are not admin or priviledged user");
				sock.close();
				ConnectionPool.getConnectionPool().returnConnection(conn);
				return;
			}
			// Execute update operation and send result
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
