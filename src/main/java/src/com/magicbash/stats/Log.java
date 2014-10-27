package src.com.magicbash.stats;


import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;


import java.util.Calendar;

import src.com.magicbash.server.Server;

public class Log extends ChannelTrafficShapingHandler{			//Log class with typical getters and setters
																//Extend ChannelTrafficShapinghandler to getting 
	public Log(long checkInterval) {							//traffic statistic 
		
		super(checkInterval);
		// TODO Auto-generated constructor stub
	}
	private boolean isAdd = false;
	private String ip;
	private String uri;
	private Calendar time;
	private long sendBytes;
	private long reciveBytes;
	private long speedDown = 0;
	private long speedUp = 0;
	
	
	@Override
	protected void doAccounting(TrafficCounter counter) {		//override method with setting statistic data
		
		this.sendBytes = counter.cumulativeWrittenBytes();
		this.reciveBytes = counter.cumulativeReadBytes();
		this.setTime(Calendar.getInstance());
		this.speedUp = counter.lastWriteThroughput();
		this.speedDown = counter.lastWriteThroughput();
		// TODO Auto-generated method stub
		super.doAccounting(counter);
		if (!this.isAdd){
			Server.STAT.addLog(this);
			this.isAdd = true;
		}
	}
	public String getIp(){
		return ip;
	}
	
	public String getUri(){
		return uri;
	}
	
	public Calendar getTime(){
		return time;
	}
	
	public long getReciveBytes(){
		return reciveBytes;
	}
	
	public long getSendBytes(){
		return sendBytes;
	}
	
	public long getUpSpeed(){
		return speedUp;
	}
	
	public long getDownSpeed(){
		return speedDown;
	}
	
	public void setIp(String ip){
		this.ip = ip;
	}
	
	public void setTime(Calendar time){
		this.time = time;
	}
	
	public void setUri(String uri){
		this.uri = uri;
	}
}
