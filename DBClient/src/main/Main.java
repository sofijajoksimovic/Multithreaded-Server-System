package main;

import java.io.*;
import java.net.*;
import java.util.*;

public class Main extends Thread {
	
	// Random instance used for generating random test data
	private static Random rand = new Random();
	
	@Override
	public void run() {
		try {
			// Sample test data for simulating user and message requests
			String credentials[]= {"admin", "user", "priviledged_user"};
			String operations[]= {"GET","POST", "UPDATE", "DELETE"};
			String tables[]= {"users", "messages"};
			String usernames_old[]= {"alice", "bob", "iva", "sofix", "charlie"};
			String usernames_new[]= {"amara","nick", "peter"};
			String emails_new[]= {"mary@bg.rs", "pera@etf.rs"};
			String passwords_new[]= {"LuxPass!2025","Nick@1234","PeterNew#1"};
			String content_old[]= {"Sure Alice, I will be there.","I am so exited to meet you.","Hello"};
			String content_new[]= {"Hi, it’s me — how are you?", "Are you at school or at home?", "What project are you working on?"};
			
			// Establish a connection to the server
			Socket s = new Socket("localhost", 5555);
			DataOutputStream wr = new DataOutputStream(s.getOutputStream());
			DataInputStream dis = new DataInputStream(s.getInputStream());
			
			// Build a random request
			String credential = credentials[rand.nextInt(credentials.length)];
            String operation= operations[rand.nextInt(operations.length)];
            String table = tables[rand.nextInt(tables.length)];
            
            // Start building the request string
            StringBuilder req = new StringBuilder(credential)
                    .append("#").append(operation)
                    .append("#").append(table);
            
            // Append additional parameters depending on the operation and table
            switch (operation) {
            case "GET":
                if ("users".equals(table)) {
                    if (rand.nextBoolean()) {
                        int id = rand.nextInt(10) + 1;
                        req.append("#").append(id);
                    } else {
                        // Select by recipient
                        int recipientId = rand.nextInt(10) + 1;
                        req.append("#recipient#").append(recipientId);
                    }
                } else {
                    if (rand.nextBoolean()) {
                        int id = rand.nextInt(10) + 1;
                        req.append("#").append(id);
                    }
                }
                break;
            case "POST":
                if ("users".equals(table)) {
                    String user = usernames_new[rand.nextInt(usernames_new.length)];
                    String email = emails_new[rand.nextInt(emails_new.length)];
                    String pass = passwords_new[rand.nextInt(passwords_new.length)];
                    req.append("#").append(user)
                       .append("#").append(email)
                       .append("#").append(pass);
                } else {
                    int sender = rand.nextInt(10) + 1;
                    int recipient = rand.nextInt(10) + 1;
                    String content = content_new[rand.nextInt(content_new.length)];
                    req.append("#").append(sender)
                       .append("#").append(recipient)
                       .append("#").append(content);
                }
                break;
            case "UPDATE":
                if ("users".equals(table)) {
                    int userId = rand.nextInt(10) + 1;
                    String newPass = passwords_new[rand.nextInt(passwords_new.length)];
                    req.append("#").append(userId).append("#").append(newPass);
                } else {
                    int msgId = rand.nextInt(100) + 1;
                    String newContent = content_new[rand.nextInt(content_new.length)];
                    req.append("#").append(msgId).append("#").append(newContent);
                }
                break;
            case "DELETE":
                if ("users".equals(table)) {
                    if (rand.nextBoolean()) {
                        // e.g. delete by id only admin
                        int userId = rand.nextInt(10) + 1;
                        req.append("#").append(userId);
                    } else {
                        // delete by username
                        String user = usernames_old[rand.nextInt(usernames_old.length)];
                        req.append("#").append(user);
                    }
                } else {
                    if (rand.nextBoolean()) {
                        int msgId = rand.nextInt(100) + 1;
                        req.append("#").append(msgId);
                    } else {
                        String content = content_old[rand.nextInt(content_old.length)];
                        req.append("#").append(content);
                    }
                }
                break;
            }
			
			wr.writeUTF(req.toString());
			String answer = dis.readUTF();
			System.out.println(answer);
			s.close();
						
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// Displays help instructions for the user
	private static void help() {
		System.out.println("Priorities requests:\n"
				+ "\t 1.CREDENTIAL#SET_PRIORITY#OPERATION:a - for one or more operations\n"
				+ "\t 2.PRIORITY - outputs all current priorities");
		System.out.print("General requests:\n"
				+ "1.GET Requests - all users can execute\n"
				+ "\t 1.1. CREDENTIAL#GET#users - outputs all users\n"
				+ "\t 1.2. CREDENTIAL#GET#messages - outputs all messages\n"
				+ "\t 1.3. CREDENTIAL#GET#users#id - outputs user with id\n"
				+ "\t 1.4. CREDENTIAL#GET#messages#id - outputs message with id\n"
				+ "\t 1.5. CREDENTIAL#GET#users#recipient#id - outputs sender users to recipient user\n"
				+ "2.POST Requests - only admin and priviledged users can execute\n"
				+ "\t 2.1. CREDENTIAL#POST#users#username#email#password - posts new user\n"
				+ "\t 2.2. CREDENTIAL#POST#messages#sender_id#recipient_id#contect - posts new message\n"
				+ "3.UPDATE Requests - only admin and priviledged users can execute\n"
				+ "\t 3.1. CREDENTIAL#UPDATE#users#id#password - changes password for user\n"
				+ "\t 3.2. CREDENTIAL#UPDATE#messages#id#content - edits content for message\n"
				+ "4.DELETE Requests - only admin can execute\n"
				+ "\t 4.1. CREDENTIAL#DELETE#users#id - deletes user with id\n"
				+ "\t 4.2. CREDENTIAL#DELETE#messages#id - deletes message with id\n"
				+ "\t 4.3. CREDENTIAL#DELETE#users#username - deletes user with username\n"
				+ "\t 4.4. CREDENTIAL#DELETE#messages#content - deletes message with content\n");
		System.out.println("For exit enter: exit");
	}
	// Simulates multiple client threads sending random requests
	private static void simulation() {
		try {
			int cycles = 100;
			Main[] threads = new Main[cycles];
			for (int i = 0; i < cycles; ++i) {
				threads[i] = new Main();
				// Start all threads
				threads[i].start();
			}
			// Wait for all threads to complete
			for (int i = 0; i < cycles; ++i) {
				threads[i].join();
			}
		} catch (InterruptedException ie) {}
	}

	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(System.in);
			String msg="";
			
			System.out.println("Do you want to start the simulation? yes/no");
			msg=sc.nextLine();
			if(msg.equals("yes")){
				simulation();
			}
			
			else {
			
				while (!msg.equals("exit")) {
					System.out.println("For help enter: help");
					msg = sc.nextLine();
					if(msg.equals("help")) {
						help();
					}
					else {
						// Establish a connection to the server at localhost on port 5555
						Socket s = new Socket("localhost", 5555);
						// Create output stream to send data to the server
						DataOutputStream wr = new DataOutputStream(s.getOutputStream());
						// Create input stream to receive data from the server
						DataInputStream dis = new DataInputStream(s.getInputStream());
						
						// Send manually entered request to the server
						wr.writeUTF(msg);
						String answer = dis.readUTF();
						System.out.println(answer);
					}
				}
				sc.close();
			}
			
		}
		catch (UnknownHostException e) {
			System.out.println("Host unreachable");
		} 
		catch (IOException e) {}
	}
}
