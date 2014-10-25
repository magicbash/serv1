package src.com.magicbash.stats;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;




public class Statistic {
	private AtomicInteger numOfRequests = new AtomicInteger(0);
	private AtomicInteger numOfUniqRequests = new AtomicInteger(0);
	private AtomicInteger numOfConnections = new AtomicInteger(0);
	private HashMap<String, Requests> req = new HashMap<String, Requests>();
	private HashMap<String, Redirects> red = new HashMap<String, Redirects>();
	private Queue<Log> log = new ConcurrentLinkedQueue<Log>();
	
	public void incReq(){
		this.numOfRequests.incrementAndGet();
		
		//use next method if ip is uniq 
	}
	
	private void incUniqReq() {
		this.numOfUniqRequests.incrementAndGet();
	}
	
	public int getNumOfRequests(){
		return this.numOfRequests.intValue();
	}
	
	public int getNumOfUniqRequests(){
		return this.numOfUniqRequests.intValue();
	}
	
	public void incNumOfConnections (){
		this.numOfConnections.incrementAndGet();
	}
	
	public void decNumOfConnections (){
		this.numOfConnections.decrementAndGet();
	}
	
	public int getNumOfConnections (){
		return numOfConnections.intValue();
	}
	
	public synchronized void addRequest(Requests req){
		if (!this.req.containsKey(req.getIp())){
			this.req.put(req.getIp(), req);
			this.incUniqReq();
		}
		else{
			this.req.get(req.getIp()).incNumOfrequests();
			this.req.get(req.getIp()).setTime(Calendar.getInstance());
		}
		this.incReq();
	}
	
	public HashMap<String, Requests> getRequests() {
		return this.req;
	}
	
	public synchronized void  addRedirect (Redirects red){
		if (!this.red.containsKey(red.getUri())){
			this.red.put(red.getUri(), red);
		}
		else {
			this.red.get(red.getUri()).incNumOfRedir();
		}
		
	}
	
	public HashMap<String, Redirects> getRedirects() {
		return this.red;
	}
	
	public synchronized void addLog(Log log) {
		if (log.getReciveBytes() == 0 || log.getSendBytes() == 0) return;
		if (this.log.size()>=16){
			
			this.log.remove();
		}
		this.log.add(log);
	}
	
	public Queue<Log> getLog(){
		return log;
	}
	public Statistic() {
		// TODO Auto-generated constructor stub
	}
	
}
	