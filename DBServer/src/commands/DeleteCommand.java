package commands;

import java.net.Socket;

import Requests.DeleteRequest;
import main.ThreadPool;
/**
 * Encapsulates a DELETE command by queuing a prioritized Request.
 *
 * params[0] – credential 
 * params[1] – command type ("DELETE")
 * params[2..] – resource identifiers (Users, messages, IDs, etc.)
 */
public class DeleteCommand extends Command{
	public DeleteCommand(Socket sock, String[] params) {
		super(sock, params);
	}
	/**
     * Delegates execution by creating a new DeleteRequest
     * and assigning it to the ThreadPool.
     */
	@Override
	public void execute() {
		ThreadPool.getThreadPool().assignTask(new DeleteRequest(sock, params[0], params[1], params));
	}
}
