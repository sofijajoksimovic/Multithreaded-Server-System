package commands;

import java.net.Socket;

import Requests.PostRequest;
import main.ThreadPool;
/**
 * Encapsulates a POST command by queuing a prioritized Request.
 *
 * params[0] – credential 
 * params[1] – command type ("POST")
 * params[2..] – resource identifiers (Users, messages, IDs, etc.)
 */
public class PostCommand extends Command{
	public PostCommand(Socket sock, String[] params) {
		super(sock, params);
	}
	/**
     * Delegates execution by creating a new PostRequest
     * and assigning it to the ThreadPool.
     */
	@Override
	public void execute() {
		ThreadPool.getThreadPool().assignTask(new PostRequest(sock, params[0], params[1], params));
	}
}
