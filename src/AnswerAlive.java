import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class AnswerAlive{

	private int alive = 1;
	private int message = 0;
	private Semaphore mutex = new Semaphore(1);
	private String answer = "whale";
	
	
	public int answerStatus(String guess) throws IOException {
		try {
			mutex.acquire();
			if(guess.equals(answer) && alive >0) {
				alive = alive-1;
				message = 1;
			}
			else if(!guess.equals(answer) && alive>0) {
				message = 2;
			}
			else if(!guess.equals(answer) && alive == 0) {
				message = 3;
			}
			else {
				message = 4;
		}
	} catch(InterruptedException e) {
		
	} finally {
		mutex.release();
		return message;
	}
	
}
}
