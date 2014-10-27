package src.com.magicbash.server;

import src.com.magicbash.stats.Log;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;


public class ServerInitializer extends io.netty.channel.ChannelInitializer<SocketChannel>{
	
	@Override
	protected void initChannel(SocketChannel arg0) throws Exception {
		// TODO Auto-generated method stub
		
		Log lg = new Log(10_000L); //creating new log 
		String ip = arg0.remoteAddress().getHostString(); // getting ip from socket
		
		lg.setIp(ip);//setting IP to log
		lg.setUri(arg0.remoteAddress().getAddress().getCanonicalHostName());//setting uri to log
		
		Server.STAT.incNumOfConnections();//increment number of open connections
		ChannelPipeline p = arg0.pipeline();// creating pipeline
		p.addLast(lg);										//adding hendlers to pipeline
		p.addLast("decoder", new HttpRequestDecoder());		//
		p.addLast("encoder", new HttpResponseEncoder());	//
		p.addLast("handler", new ServerHandler());			//
		
		
	}
	
	

}
