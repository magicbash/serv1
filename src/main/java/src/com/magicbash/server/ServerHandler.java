package src.com.magicbash.server;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import src.com.magicbash.requsts.RequsetHandler;
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

	

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Server.STAT.decNumOfConnections();// decrementing number of open
										  // connections when channel inactive
		super.channelInactive(ctx);
	}

	@Override
	protected void channelRead0(final ChannelHandlerContext ctx,
			HttpRequest request) throws Exception {
		// TODO Auto-generated method stub
		FullHttpResponse response = new DefaultFullHttpResponse( //creating new response
				HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		boolean ready = true; 									 //does response ready to flush
		String ip = ctx.channel().remoteAddress().toString();	 //getting ip from client
		String uri = request.getUri();							 //getting uri
		
		Server.STAT.addRequest(new Requests(ip.substring(0, ip.length() - 6),//adding new request to statistic
				Calendar.getInstance()));
		
		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, 	 //setting to response information about type
				"text/html; charset=UTF-8");
		
		switch (uri) {	

		case "/hello":
			response.content().writeBytes(RequsetHandler.Hello());//write hello world
			ready = false;										  //now our respons not ready to flush
			break;

		case "/status":
			response.content().writeBytes(RequsetHandler.Stat());//write statistic to response
			break;

		default:
			// redir
			if (uri.matches("\\/redirect\\?url=.+")) {			 	//check uri on redirecting
				response.setStatus(HttpResponseStatus.FOUND);		//change response status 
				response.headers().set(HttpHeaders.Names.LOCATION,	//redirecting
						RequsetHandler.Redir(uri));

			} else {			
				response.setStatus(HttpResponseStatus.NOT_FOUND);	//change response status 
				response.content().writeBytes(RequsetHandler.NotFound());//write 404 error
			}
			break;
		}

		ctx.write(response);//writing response
		if (ready) { 															//flushing
			ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT).addListener(
					ChannelFutureListener.CLOSE);
		}
		else {																	//if response not ready 
			ctx.executor().schedule(new Runnable() {							//to flushing
																				//wait 10 seconds

				@Override
				public void run() {
					// TODO Auto-generated method stub
					ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT)
							.addListener(ChannelFutureListener.CLOSE);
				}
			}, 10, TimeUnit.SECONDS);
		}
	}

	public ServerHandler() {

	}

}
