package Requests;

import java.net.*;

import main.Main;
/**
* Base class for prioritized execution of requests.
*/
public abstract class Request implements Comparable<Request>{
	private String userPriority;
	private String operationPriority;
	protected String params[];
	
	public enum Status { OK, ERROR };
	protected Socket sock;
	
	public Request (Socket sock, String userPriority, String operationPriority, String []params) {
		this.sock = sock;
		this.userPriority=userPriority;
		this.operationPriority=operationPriority;
		this.params=params;
	}
	
	/** Contains the request-specific processing logic. */
	public abstract void handle();
	
	@Override
    public int compareTo(Request other) {
		// Step 1: order by credential level (lower number == higher privilege)
        int result = Integer.compare(Main.credentials.get(this.userPriority),
        		Main.credentials.get(other.userPriority));
        // Step 2: order by configured operation priority
        // e.g. if DELETE(1), UPDATE(2), GET(3), then DELETE runs before GET
        if (result != 0) return result;
        return Integer.compare(Main.priorities.get(operationPriority),
        		Main.priorities.get(other.operationPriority));
    }
}
