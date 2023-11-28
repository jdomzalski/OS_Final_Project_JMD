import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.io.IOException;
import java.net.*;

public class ClientHandler implements Runnable{

	// Create an array list of every ClientHandler object
	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	
	// Socket passed from our Server class, to establish a connection
	private Socket socket;
	// Reads messages sent from the client
	private BufferedReader bufferedReader;
	// Sends messages to the client
	private BufferedWriter bufferedWriter;
	
	private String username;
	
	private AnswerAlive answerAlive;
	
	
	// Instances of this class will be passed a Socket object from our Server class
	public ClientHandler(Socket socket, AnswerAlive answerAlive) {
		try {
			this.socket = socket;

			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			this.username = bufferedReader.readLine();
			this.answerAlive = answerAlive;
			
			clientHandlers.add(this);
			
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// Everything in run method is what is run on a separate thread
	@Override
	public void run() {
		
		String guess;
		
		while(socket.isConnected()) {
			try {
				int check;
				int connections = clientHandlers.size();
				guess = bufferedReader.readLine();
				check = answerAlive.answerStatus(guess);
					
					if(clientHandlers.contains(this)) {
						if(check == 1) {
							bufferedWriter.write("Winner!!");
							bufferedWriter.newLine();
							bufferedWriter.flush();
							bufferedWriter.write("Number of connections: "+connections);
							bufferedWriter.newLine();
							bufferedWriter.flush();
							clientHandlers.remove(this);
						}
						else if(check == 2) {
							bufferedWriter.write("Wrong! But the right answer is still out there...");
							bufferedWriter.newLine();
							bufferedWriter.flush();
						}
						else if(check == 3) {
							bufferedWriter.write("Wrong! Someone has found the right answer, the game is now over");
							bufferedWriter.newLine();
							bufferedWriter.flush();
							clientHandlers.remove(this);
						}
						else {
							bufferedWriter.write("This is the right answer, but someone beat you to it! Better luck next time");
							bufferedWriter.newLine();
							bufferedWriter.flush();
							clientHandlers.remove(this);
						}
						
					 }
			} catch(IOException e) {
				e.printStackTrace();
				break;
			}
		}
		
	}

}
