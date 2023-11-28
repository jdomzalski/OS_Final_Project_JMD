import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String username;
	
	public Client(Socket socket, String username) {
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.username = username;
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// Send messages to the Client Handler
	public void send() {
		try {
			bufferedWriter.write(username);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			
			Scanner scanner = new Scanner(System.in);
			while(socket.isConnected()) {
				String guess = scanner.nextLine();
				bufferedWriter.write(guess);
				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// Listening to messages from the server
	public void read() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String messageFromServer;
				
				while(socket.isConnected()) {
					try {
						messageFromServer = bufferedReader.readLine();
						System.out.println(messageFromServer);
					} catch(IOException e) {
					}
					
				}
			}
			
		}).start();
	}
	
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter username: ");
		String username = scanner.nextLine();
		System.out.println("Welcome to the game "+username +". Guess the word before other players to win!");
		System.out.println("(hint: the word is an animal)");
		Socket socket = new Socket("localhost", 8080);
		Client client = new Client(socket, username);
		client.read();
		client.send();
	}
}
