package src.com.magicbash.server;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import src.com.magicbash.requsts.RequsetHandler;
import src.com.magicbash.stats.Redirects;
import src.com.magicbash.stats.Requests;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;

public class ServerHandler extends SimpleChannelInboundHandler<HttpRequest> {

	private String uri;
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Server.STAT.decNumOfConnections();
		super.channelInactive(ctx);
	}
	
	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, HttpRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		this.channelActive(ctx);
		Test.LOG.log(Level.INFO, "123");
		FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		response.headers().set(HttpHeaders.Names.CONTENT_TYPE,
				"text/html; charset=UTF-8");
		boolean ready = true;
		Test.LOG.log(Level.INFO, request.getUri());
		uri = request.getUri();
		String ip = ctx.channel().remoteAddress().toString();
		Server.STAT.addRequest(new Requests(ip.substring(0, ip.length() - 6),
				Calendar.getInstance()));
		switch (uri) {
		case "/hello":
			response.content().writeBytes(RequsetHandler.Hello());
			ready = false;
			Test.LOG.log(Level.INFO, "hello");
			ctx.executor().schedule(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT)
							.addListener(ChannelFutureListener.CLOSE);
				}
			}, 10, TimeUnit.SECONDS);
			break;

		case "/status":

			Test.LOG.log(Level.INFO, "status");
			response.content().writeBytes(RequsetHandler.Stat());// statistic
			break;

		default:
			// redir
			if (uri.matches("\\/redirect\\?url=.+")) {
				Test.LOG.log(Level.INFO, "redir");// redirecting
				response.setStatus(HttpResponseStatus.FOUND);
				String redirectURL = uri.substring("/redirect?url=".length());
				response.headers().set(HttpHeaders.Names.LOCATION, redirectURL);
				Server.STAT.addRedirect(new Redirects(redirectURL));

			} else {
				response.setStatus(HttpResponseStatus.NOT_FOUND);
				response.content().writeBytes("404 Page not found".getBytes());
				Test.LOG.log(Level.INFO, "404");
			}
			break;
		}
		ctx.write(response);
		if (ready) {
			ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT).addListener(
					ChannelFutureListener.CLOSE);
		}
		Test.LOG.log(Level.INFO, response.toString());
		

	}
	
	public ServerHandler() {

	}

}
