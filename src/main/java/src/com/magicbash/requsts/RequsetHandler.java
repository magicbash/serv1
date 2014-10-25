package src.com.magicbash.requsts;

public class RequsetHandler {
	public static byte[] Hello(){
		
		return "Hello World".getBytes();
	}
	public static byte[] Stat(){
		return StatHtmlBuilder.createStat().getBytes();
	}
	
}
