package src.com.magicbash.requsts;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import src.com.magicbash.server.Server;
import src.com.magicbash.stats.Log;
import src.com.magicbash.stats.Redirects;
import src.com.magicbash.stats.Requests;

public class StatHtmlBuilder { // this class build html document of page /status
	private final static String HTML_HEADER = "<!doctype html><html><head><meta charset=\"utf-8\"> <title>Status page</title> </head> <body> <h1>Status Page</h1>";
	private final static String HTML_FOOTER = "</body> </html>";
	private final static String REQ_NUMBERS = "<p>Number of request: ";
	private final static String REQ_UNIQ_NUMBERS = "<p>Number of uniqum request(IP): ";
	private final static String TABLE_COUNTER_HEADER = "<table width=\"699\" border=\"1\"> <caption> Request counter </caption> <tr> <th width=\"402\" scope=\"col\">IP</th> <th width=\"335\" scope=\"col\">Number of requset</th> <th width=\"262\" scope=\"col\">Time of last request</th> </tr>";
	private final static String TABLE_FOOTER = "</Table>";
	private final static String TABLE_REDIR_HEADER = "<table width=\"699\" border=\"1\"> <caption> Number of redirects </caption> <tr> <th width=\"587\" scope=\"col\">URL</th> <th width=\"96\" scope=\"col\">Number</th> </tr>";
	private final static String NUM_OPEN_CONNECTIONS = "<p>Number of open connections: ";
	private final static String TABLE_LOG_HEADER = "<table width=\"699\" border=\"1\"><caption>Log</caption><tr><th width=\"109\" scope=\"col\">Src_ip</th><th width=\"203\" scope=\"col\">URI</th><th width=\"94\" scope=\"col\">timestamp</th><th width=\"80\" scope=\"col\">send</th><th width=\"80\" scope=\"col\">recive</th><th width=\"128\" scope=\"col\">speed up/down(byte/sec)</th></tr>";

	private static String reqestNum(int a) {
		return REQ_NUMBERS + a + "</p>";
	}

	private static String reqestUniqNum(int a) {
		return REQ_UNIQ_NUMBERS + a + "</p>";
	}

	private static String counterTable() {
		StringBuilder sb = new StringBuilder();
		sb.append(TABLE_COUNTER_HEADER);
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
		for (Requests value : Server.STAT.getRequests().values()) {

			sb.append("<tr>");
			sb.append("<td>");
			sb.append(value.getIp());
			sb.append("</td>");

			sb.append("<td>");
			sb.append(value.getNumOfReq());
			sb.append("</td>");

			sb.append("<td>");
			sb.append(form.format(value.getTime().getTime()));
			sb.append("</td>");
			sb.append("</tr>");
		}
		// content
		sb.append(TABLE_FOOTER);
		return sb.toString();
	}

	private static String redirTable() {
		StringBuilder sb = new StringBuilder();
		sb.append(TABLE_REDIR_HEADER);
		for (Redirects value : Server.STAT.getRedirects().values()) {
			sb.append("<tr>");
			sb.append("<td>");
			sb.append(value.getUri());
			sb.append("</td>");

			sb.append("<td>");
			sb.append(value.getNumOfRedir());
			sb.append("</td>");

			sb.append("</tr>");
		}
		// content
		sb.append(TABLE_FOOTER);
		return sb.toString();
	}

	private static String openConn(int a) {
		return NUM_OPEN_CONNECTIONS + a + "</p>";
	}

	private static String logTable() {
		StringBuilder sb = new StringBuilder();
		sb.append(TABLE_LOG_HEADER);

		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
		Log lg;
		Iterator<Log> it = Server.STAT.getLog().iterator();

		while (it.hasNext()) {
			lg = it.next();
			sb.append("<tr>");
			sb.append("<td>");
			sb.append(lg.getIp());
			sb.append("</td>");

			sb.append("<td>");
			sb.append(lg.getUri());
			sb.append("</td>");

			sb.append("<td>");
			sb.append(form.format(lg.getTime().getTime()));
			sb.append("</td>");

			sb.append("<td>");
			sb.append(lg.getSendBytes());
			sb.append("</td>");

			sb.append("<td>");
			sb.append(lg.getReciveBytes());
			sb.append("</td>");

			sb.append("<td>");
			sb.append(lg.getUpSpeed());
			sb.append('/');
			sb.append(lg.getDownSpeed());
			sb.append("</td>");
			sb.append("</tr>");
		}
		// content
		sb.append(TABLE_FOOTER);
		sb.append(TABLE_FOOTER);
		return sb.toString();
	}

	public static String createStat() {
		StringBuilder sb = new StringBuilder();
		sb.append(HTML_HEADER);
		sb.append(reqestNum(Server.STAT.getNumOfRequests()));
		sb.append(reqestUniqNum(Server.STAT.getNumOfUniqRequests()));
		sb.append(counterTable());
		sb.append(redirTable());
		sb.append(openConn(Server.STAT.getNumOfConnections()));
		sb.append(logTable());
		sb.append(HTML_FOOTER);
		return sb.toString();
	}

}
