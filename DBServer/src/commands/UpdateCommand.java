package commands;

import java.net.Socket;

import Requests.UpdateRequest;
import main.ThreadPool;
/**
 * Encapsulates a UPDATE command by queuing a prioritized Request.
 *
 * params[0] – credential
 * params[1] – command type ("UPDATE")
 * params[2..] – resource identifiers (Users, messages, IDs, etc.)
 */
public class UpdateCommand extends Command{
	public UpdateCommand(Socket sock, String[] params) {
		super(sock, params);
	}
	/**
     * Delegates execution by creating a new GetRequest
     * and assigning it to the ThreadPool.
     */
	@Override
	public void execute() {
		ThreadPool.getThreadPool().assignTask(new UpdateRequest(sock, params[0], params[1], params));
	}
}
