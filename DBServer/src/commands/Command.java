package commands;

import java.net.Socket;
/**
 * Abstract base for server commands.
 * Each subclass implements a specific action (e.g., GET, POST, UPDATE).
 * Implements the *Command* pattern (GoF), encapsulating a request (with
 * parameters and client socket) into a self-contained object.
 * The thread-safe invoker (e.g. worker thread) is responsible for
 * instantiating and calling the command.
 */
public abstract class Command {
	/** TCP socket connected to the client for I/O */
	protected Socket sock;
	/** Tokenized request parameters, including credential and command info */
	protected String []params;
	
	public Command(Socket sock, String[]params) {
		this.sock=sock;
		this.params=params;
	}
	/**
     * Execute the command. Implementing classes must:
     *   • parse and validate params
     *   • call business logic (GET, POST, UPDATE, DELETE, etc.)
     *   • write response back to client via sock.getOutputStream()
     *   • close or manage the socket appropriately
     *   • handle exceptions and ensure respond even on failure
     */
	public abstract void execute();
}
