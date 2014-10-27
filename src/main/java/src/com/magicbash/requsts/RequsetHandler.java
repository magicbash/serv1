package src.com.magicbash.requsts;

	
import src.com.magicbash.server.Server;
import src.com.magicbash.stats.Redirects;

public class RequsetHandler {				//this class return data to write it in response
	
	public static byte[] Hello(){
		
		return "Hello World".getBytes();
	}
	
	public static byte[] Stat(){
		return StatHtmlBuilder.createStat().getBytes();
	}
	
	public static String Redir(String uri){
		String redirectURL = uri.substring("/redirect?url=".length());
		Server.STAT.addRedirect(new Redirects(redirectURL));
		return redirectURL;
	}

	public static byte[] NotFound() {
		
		return "404 Page not found".getBytes();
	}
}
