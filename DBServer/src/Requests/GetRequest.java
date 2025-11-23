package Requests;

import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Connection;

import GET.GetRType;
import GET.GetRTypeMessagesAll;
import GET.GetRTypeMessagesId;
import GET.GetRTypeUsersAll;
import GET.GetRTypeUsersId;
import GET.GetRTypeUsersRecipient;
import main.ConnectionPool;
import main.Main;
/**
 * Handles GET requests by parsing parameters and delegating to the appropriate query object.
 */
public class GetRequest extends Request{

	public GetRequest(Socket sock, String userPriority, String operationPriority, String[]params) {
		super(sock, userPriority, operationPriority, params);
	}

	@Override
	public void handle() {
		Connection conn;
		try {
			conn = ConnectionPool.getConnectionPool().getConnection();
			GetRType req=null;
			
			// Determine which type of GET operation to perform based on params
			if(params.length==3 && params[2].equals("users")) {
				req=new GetRTypeUsersAll(conn);
			}
			else if(params.length==4 && params[2].equals("users") && Main.isInteger(params[3])) {
				req=new GetRTypeUsersId(Integer.parseInt(params[3]), conn);
			}
			else if(params.length==3 && params[2].equals("messages")) {
				req=new GetRTypeMessagesAll(conn);
			}
			else if(params.length==4 && params[2].equals("messages") && Main.isInteger(params[3])) {
				req=new GetRTypeMessagesId(Integer.parseInt(params[3]), conn);
			}
			else if(params.length==5 && params[2].equals("users") && params[3].equals("recipient") && Main.isInteger(params[4])) {
				req=new GetRTypeUsersRecipient(Integer.parseInt(params[4]), conn);
			}
			else {
				System.out.println("REQUEST DENIED: REQUEST NOT FOUND");
				DataOutputStream ds = new DataOutputStream(sock.getOutputStream());
				ds.writeUTF("REQUEST DENIED: Incorrect format");
				sock.close();
				ConnectionPool.getConnectionPool().returnConnection(conn);
				return;
			}
			
			 // Execute query and return the result to client
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
