package src.com.magicbash.server;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {
	public static Logger LOG = Logger.getLogger("com.magicbash.server");
	public static int port =8080;
	public static void main(String[] args) throws InterruptedException {
		
		// TODO Auto-generated method stub
		new Server(port).start();
		LOG.log(Level.INFO, "server start at port:" + port);
	}

}
