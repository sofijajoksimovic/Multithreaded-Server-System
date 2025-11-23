package commands;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import main.Main;
import main.ThreadPool;
/**
 * Handles the SET_PRIORITY command.
 * Only "admin" is authorized to change operation priorities.
 */
public class SetPriorityCommand extends Command{

	public SetPriorityCommand(Socket sock, String[] params) {
		super(sock, params);
	}

	@Override
	public void execute() {
		DataOutputStream ds;
		try {
			ds = new DataOutputStream(sock.getOutputStream());
			if(!params[0].equals("admin")) {
				System.out.println("REQUEST DENIED: UNAUTHORIZED");
				ds.writeUTF("UNAUTHORIZED: Only admin can change operation priorities");
			}
			else {
				StringBuilder sb=new StringBuilder();
				int len=params.length;
				sb.append("REQUEST ACCEPTED: Operation priotities changed\nChanged priorites: ");
				
				// Parse and update priorities
				for(int i=2;i<len;i++) {
					String[]operations=params[i].split(":");
					if(operations.length<2) {
						System.out.println("REQUEST DENIED: BAD REQUEST");
						ds.writeUTF("BAD REQUEST: Format is incorrect");
						return;
					}
					Main.priorities.put(operations[0], Integer.parseInt(operations[1]));
					sb.append(params[i]+" ");
				}
				sb.append("\n");
				System.out.println("REQUEST ACCEPTED: SET_PRIORITY REQUEST");
				// Re-sort the priority queue
				ThreadPool.getThreadPool().changeOfPriorities();
				ds.writeUTF(sb.toString());
			}
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
