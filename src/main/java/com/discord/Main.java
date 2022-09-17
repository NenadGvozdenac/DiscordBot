package com.discord;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.login.LoginException;

import com.sun.net.httpserver.HttpServer;

public class Main {			
	
	public static void main(String[] args) throws LoginException, InterruptedException {		

		DiscordBot discordBot = new DiscordBot();

		RssReaderClass readerMatematickeAnalize = new RssReaderClass();
		readerMatematickeAnalize.ReadMatematickaAnaliza();

		ArrayList<HttpServer> httpServers = HttpSrv.getServers(25921, 25652);

		restartujServer(48);
	}

	private static void restartujServer(Integer sati) {

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				System.exit(0);
			}
				
		}, 60 * 1000 * 60 * sati);
	}
}