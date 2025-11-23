package Requests;

import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Connection;

import POST.PostRType;
import POST.PostRTypeMessages;
import POST.PostRTypeUsers;
import main.ConnectionPool;
import main.Main;
/**
 * Handles POST requests by identifying the specific operation (e.g., adding users or messages)
 * and delegating to the appropriate PostRType implementation.
 */

public class PostRequest extends Request{

	public PostRequest(Socket sock, String userPriority, String operationPriority, String[]params) {
		super(sock, userPriority, operationPriority, params);
	}

	@Override
	public void handle() {
		Connection conn;
		try {
			conn = ConnectionPool.getConnectionPool().getConnection();
			PostRType req=null;
			
			// Determine type of POST operation from parameters
			if(params.length==6 && params[2].equals("users")) {
				req=new PostRTypeUsers(params[3], params[4], params[5], conn);
			}
			else if(params.length==6 && params[2].equals("messages") && Main.isInteger(params[3]) && Main.isInteger(params[4])) {
				req=new PostRTypeMessages(Integer.parseInt(params[3]), Integer.parseInt(params[4]),params[5], conn);
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
			
			// Execute the request and respond to the client
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
