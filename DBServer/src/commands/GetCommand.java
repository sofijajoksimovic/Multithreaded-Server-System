package commands;


import java.net.Socket;

import Requests.GetRequest;
import main.ThreadPool;
/**
 * Encapsulates a GET command by queuing a prioritized Request.
 *
 * params[0] – credential
 * params[1] – command type ("GET")
 * params[2..] – resource identifiers (Users, messages, IDs, etc.)
 */
public class GetCommand extends Command{

	public GetCommand(Socket sock, String[] params) {
		super(sock, params);
	}
	/**
     * Delegates execution by creating a new GetRequest
     * and assigning it to the ThreadPool.
     */
	@Override
	public void execute() {
		ThreadPool.getThreadPool().assignTask(new GetRequest(sock, params[0], params[1], params));
	}

}
