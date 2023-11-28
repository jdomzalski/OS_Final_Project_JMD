
import java.io.IOException;
import java.net.*;


public class Server {

	private ServerSocket serverSocket;
	private AnswerAlive answerAlive = new AnswerAlive();
	
	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public void start() {
		
		try {
			
			while(!serverSocket.isClosed()) {
				// When a client connects, a Socket object is returned which can be used to communicate with the Client
				Socket socket = serverSocket.accept();
				System.out.println("A new client has connected!");
				// Each object of the ClientHandler class will be responsible for communicating with a Client
				ClientHandler clientHandler = new ClientHandler(socket, answerAlive);
				
				
				Thread thread = new Thread(clientHandler);
				thread.start();
				System.out.println("Number of active threads: "+(java.lang.Thread.activeCount() - 1));
			}
		} catch(IOException e) {
			
		}
		
	}

	public void start200() {
		try {
			while(!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println("Creating 200 threads...");
				
				ClientHandler clientHandler = new ClientHandler(socket, answerAlive);
				
				for(int i = 1; i<=200; i++) {
					Thread thread = new Thread(clientHandler);
					thread.start();
				}
				
				System.out.println("Number of active threads: "+(java.lang.Thread.activeCount() - 1));
			}
		} catch(IOException e) {
			
		}
	}
	
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(8080);
		Server server = new Server(serverSocket);
		server.start();
		// server.start200();
	}
}
