package src.com.magicbash.stats;


import java.util.Calendar;


public class Requests {
	private String ip;
	private int numOfRequests = 0;
	private Calendar time;
	
	public Requests(String ip, Calendar time) {
		// TODO Auto-generated constructor stub
		this.ip = ip;
		this.time = time;
		this.numOfRequests++;
	}
	
	public void incNumOfrequests(){
		this.numOfRequests++;
	}
	
	public int getNumOfReq(){
		return this.numOfRequests;
	}
	
	public String getIp(){
		return this.ip;
	}
	
	public Calendar getTime(){
		return this.time;
	}
	
	public void setTime(Calendar cal){
		this.time = cal;
	}
	/*public boolean equals(Requests rq) {
		// TODO Auto-generated method stub
		if (this.getIp() == rq.getIp()){
			Test.LOG.log(Level.INFO, this.getIp());
			Test.LOG.log(Level.INFO, rq.getIp());
			return true;
		}
		else return false;
	}*/
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Requests rq = (Requests) obj;
		if (this.getIp() == rq.getIp()){
			return true;
		}
		else return false;
	}
}
