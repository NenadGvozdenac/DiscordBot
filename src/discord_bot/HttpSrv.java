package discord_bot;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

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
}