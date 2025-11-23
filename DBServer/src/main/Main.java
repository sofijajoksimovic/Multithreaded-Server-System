package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import commands.Command;
import commands.DeleteCommand;
import commands.GetCommand;
import commands.PostCommand;
import commands.SetPriorityCommand;
import commands.UpdateCommand;


public class Main {
	/**
     * Global request-type priorities.
     * ConcurrentHashMap is thread-safe for concurrent updates via commands.
     */
	public static final Map<String, Integer> priorities = new ConcurrentHashMap<>();
	/**
     * Maps credentials/roles to numeric privilege level.
     * Lower number = higher privilege.
     * HashMap as only read (static) after initialization.
     */
	public static final Map<String, Integer> credentials= new HashMap<String, Integer>();
	
	 /** Utility: check if a string is a valid integer (positive or negative).*/
	public static boolean isInteger(String str) {
	    if (str == null) return false;
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	/**Static initializer to set default priority values.*/
	static {
		priorities.put("GET", 3);
		priorities.put("POST", 1);
		priorities.put("UPDATE", 2);
		priorities.put("DELETE", 1);
	}
	/**Static initializer to define credential levels.*/
	static {
		credentials.put("admin", 1);
		credentials.put("priviledged_user", 2);
		credentials.put("user", 3);
	}
	
	public static void main(String[] args) {
		
		try {
			// Listen on TCP port 5555 for incoming client connections.
			ServerSocket ss = new ServerSocket(5555, 1000);
			while(true) {
				// Accept a new client connection (blocking).
				Socket sock = ss.accept();
				DataInputStream dis = new DataInputStream(sock.getInputStream());
				// Read client request string: format "CREDENTIAL#REQUESTTYPE#..."
				String request = dis.readUTF();
				
				// Split request string by "#" to parse tokens.
				String[] params = request.split("#");
				
				Command c=null;
				// Special handling: if top-level command is "PRIORITY"
				if(params[0].equals("PRIORITY")) {
					StringBuilder sb=new StringBuilder();
					for (Map.Entry<String, Integer> entry : Main.priorities.entrySet()) {
					    sb.append(entry.getKey() + " : " + entry.getValue()+" ");
					}
					DataOutputStream ds= new DataOutputStream(sock.getOutputStream());
					ds.writeUTF(sb.toString());
					sock.close();
				}
				if(params.length>=2) {
					String reqType = params[1];
					// Dispatch to appropriate command class based on REQUESTTYPE.
					if(reqType.equals("SET_PRIORITY")) {
						c=new SetPriorityCommand(sock, params);
					}
					else if(reqType.equals("GET")) {
						c=new GetCommand(sock, params);
					}
					else if(reqType.equals("POST")) {
						c=new PostCommand(sock, params);
					}
					else if(reqType.equals("UPDATE")) {
						c=new UpdateCommand(sock, params);
					}
					else if(reqType.equals("DELETE")) {
						c=new DeleteCommand(sock, params);
					}
					// Execute the logic defined by the command.
					if(c!=null)c.execute();
					else {
						// If command was unrecognized:
						DataOutputStream ds= new DataOutputStream(sock.getOutputStream());
						ds.writeUTF("REQUEST DENIED: Request type not found");
						System.out.println("REQUEST DENIED: BAD REQUEST");
						sock.close();
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
