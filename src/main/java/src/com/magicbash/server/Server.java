package src.com.magicbash.server;

import src.com.magicbash.stats.Statistic;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	private final int port;
	public static Statistic STAT;
	@SuppressWarnings("static-access")
	public Server(int port){
		this.port = port;
		this.STAT = new Statistic();
	}
	
	public void start() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.localAddress(port)
			.childHandler(new ServerInitializer());
			ChannelFuture f = b.bind().sync();
			
			f.channel().closeFuture().sync();
		}
		finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
			
		}
	}
}
