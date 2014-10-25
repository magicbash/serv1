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
		
		Log lg = new Log(10_000L);
		String ip = arg0.remoteAddress().getHostString();
		lg.setIp(ip);
		ChannelPipeline p = arg0.pipeline();
		ServerHandler sh = new ServerHandler();
		lg.setUri(arg0.remoteAddress().getAddress().getCanonicalHostName());
		Server.STAT.incNumOfConnections();
		p.addLast(lg);
		p.addLast("decoder", new HttpRequestDecoder());
		p.addLast("encoder", new HttpResponseEncoder());
		p.addLast("handler", sh);
		
		
	}
	
	

}
