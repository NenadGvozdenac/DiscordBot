package com.discord;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpSrv {
	
	//httpserver-1.nenadgvozdenac.repl.co/test
  
	static class MyHandler implements HttpHandler {									// Static funkcija za Handler
        @Override
        public void handle(HttpExchange t) throws IOException {						
            String response = "Ovo je server za odrzavanje bota.";					// Tekst na stranicu koja se koristi
            t.sendResponseHeaders(200, response.length());							// Slanje response headera, duzine 200
            OutputStream os = t.getResponseBody();									// Outputstream za primanje podataka
            os.write(response.getBytes());											// Pisanje odgovora
            os.close();																// Zatvaranje odgovora
        }
    }
	
	public static Thread getServerThread() {
		Thread threadServera = new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpServer server;
				try {
					server = HttpServer.create(new InetSocketAddress(8000), 0);
					
					server.createContext("/", new MyHandler());	
			    
			    	server.setExecutor(null); 
			    
			    	server.start();	
			    	
			    	System.out.println("Poceo thread servera!");
					
				} catch (IOException e) {
					System.out.println("Adresa se vec koristi! Thread servera nije poceo!");
				} 
			}
		});
		
		return threadServera;
		
	}
}