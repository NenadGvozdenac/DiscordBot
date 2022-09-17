package com.discord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.ArrayList;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HttpSrv {
  
	static class HtmlHandler implements HttpHandler {	
		
		String path;
		
		HtmlHandler(String path) {
			this.path = path;
		}

        @Override
        public void handle(HttpExchange t) throws IOException {						
			loadHtml(path, t);
        }
    }

	static class CssHandler implements HttpHandler {

		String path;
		
		CssHandler(String path) {
			this.path = path;
		}

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			loadCss(path, exchange);
		}
	}

	static class JsHandler implements HttpHandler {

		String path;
		
		JsHandler(String path) {
			this.path = path;
		}

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			loadJs(path, exchange);
		}
	}

	static class ImageHandler implements HttpHandler {
		String path;
		
		ImageHandler(String path) {
			this.path = path;
		}

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			loadImage(path, exchange);
		}
	}

	public static ArrayList<HttpServer> getServers(Integer port1, Integer port2) {
		
		HttpServer server1, server2;
		try {
			server1 = HttpServer.create(new InetSocketAddress(port1), 0);
			
			server1.createContext("/", 						new HtmlHandler("./prvaHtmlStranica/mainPage.html"));	
			server1.createContext("/style.css", 				new CssHandler("./prvaHtmlStranica/style.css"));
			server1.createContext("/buttonToTop.js",		 	new JsHandler("./prvaHtmlStranica/buttonToTop.js"));

			server1.createContext("/slike/analiza2.jpg", 		new ImageHandler("prvaHtmlStranica/slike/analiza2.jpg"));
			server1.createContext("/slike/automatika.jpg", 	new ImageHandler("prvaHtmlStranica/slike/automatika.jpg"));
			server1.createContext("/slike/background.jpg", 	new ImageHandler("prvaHtmlStranica/slike/background.jpg"));
			server1.createContext("/slike/logo.jpg", 			new ImageHandler("prvaHtmlStranica/slike/logo.jpg"));
			server1.createContext("/slike/logo.png", 			new ImageHandler("prvaHtmlStranica/slike/logo.png"));
			server1.createContext("/slike/programiranje.jpg", 	new ImageHandler("prvaHtmlStranica/slike/programiranje.jpg"));
			server1.createContext("/slike/sistemi.jpg", 		new ImageHandler("prvaHtmlStranica/slike/sistemi.jpg"));
			server1.createContext("/slike/teorija.jpg", 		new ImageHandler("prvaHtmlStranica/slike/teorija.jpg"));
		
			server1.setExecutor(null); 
		
			server1.start();	

			server2 = HttpServer.create(new InetSocketAddress(port2), 0);

			server2.createContext("/", new HtmlHandler("./drugaHtmlStranica/mainPage.html"));
			server2.createContext("/mainPageStyle.css", new CssHandler("./drugaHtmlStranica/mainPageStyle.css"));
			server2.createContext("/navbarScrollToDiv.js", new JsHandler("./drugaHtmlStranica/navbarScrollToDiv.js"));
			server2.createContext("/scrollbarHideNav.js", new JsHandler("./drugaHtmlStranica/scrollbarHideNav.js"));

			server2.createContext("/slike/firstSlide.png", new ImageHandler("drugaHtmlStranica/slike/firstSlide.png"));
			server2.createContext("/slike/croppedBootstrapLogo.png", new ImageHandler("drugaHtmlStranica/slike/croppedBootstrapLogo.png"));
			server2.createContext("/slike/bootstrapLogo.png", new ImageHandler("drugaHtmlStranica/slike/bootstrapLogo.png"));
			server2.createContext("/slike/fifthSlide.jpg", new ImageHandler("drugaHtmlStranica/slike/fifthSlide.jpg"));
			server2.createContext("/slike/firstImageInstallation.jpg", new ImageHandler("drugaHtmlStranica/slike/firstImageInstallation.jpg"));
			server2.createContext("/slike/fourthImageInstallation.jpg", new ImageHandler("drugaHtmlStranica/slike/fourthImageInstallation.jpg"));
			server2.createContext("/slike/fourthImageInstallation.png", new ImageHandler("drugaHtmlStranica/slike/fourthImageInstallation.png"));
			server2.createContext("/slike/secondImageInstallation.png", new ImageHandler("drugaHtmlStranica/slike/secondImageInstallation.png"));
			server2.createContext("/slike/secondSlide.png", new ImageHandler("drugaHtmlStranica/slike/secondSlide.png"));
			server2.createContext("/slike/thirdSlide.png", new ImageHandler("drugaHtmlStranica/slike/thirdSlide.png"));

			server2.setExecutor(null); 
		
			server2.start();
			
			System.out.println("Poceo thread servera!");
			
			return new ArrayList<>() {
				{
					add(server1);
					add(server2);
				}
			};

		} catch (IOException e) {
			System.out.println("Adresa se vec koristi! Thread servera nije poceo!");

			return null;
		}
	}

	public static void loadImage(String path, HttpExchange exchange) throws IOException {

		Headers h = exchange.getResponseHeaders();

		File newFile = new File(path);

		exchange.sendResponseHeaders(200, newFile.length());

		h.add("Content-Type", (newFile.getName().contains(".jpg") || newFile.getName().contains(".jpeg")) ? "image/jpeg" : "image/png");

		OutputStream os = exchange.getResponseBody();
		Files.copy(newFile.toPath(), os);
		os.close();
	}

	public static void loadJs(String path, HttpExchange exchange) throws IOException {

		Headers h = exchange.getResponseHeaders();

		String line;
		String resp = "";

		File newFile = new File(path);
				
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(newFile)));

		while ((line = bufferedReader.readLine()) != null) {
			resp += line;
		}
		bufferedReader.close();
		
		h.add("Content-Type", "text/javascript");

		exchange.sendResponseHeaders(200, 0);

		OutputStream os = exchange.getResponseBody();
		os.write(resp.getBytes());
		os.close();
	}

	public static void loadCss(String path, HttpExchange exchange) throws IOException {
		
		Headers h = exchange.getResponseHeaders();

		String line;
		String resp = "";

		File newFile = new File(path);
				
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(newFile)));

		while ((line = bufferedReader.readLine()) != null) {
			resp += line;
		}

		bufferedReader.close();

		h.add("Content-Type", "text/css");

		exchange.sendResponseHeaders(200, 0);
		OutputStream os = exchange.getResponseBody();
		os.write(resp.getBytes());
		os.close();
	}

	public static void loadHtml(String pathToHtmlPage, HttpExchange t) throws IOException {

		Headers h = t.getResponseHeaders();

		String line;
        String resp = "";

        File newFile = new File(pathToHtmlPage);
				
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(newFile)));

        while ((line = bufferedReader.readLine()) != null) {
            resp += line;
        }

        bufferedReader.close();

        h.add("Content-Type", "text/html");

        t.sendResponseHeaders(200, 0);
        OutputStream os = t.getResponseBody();

        os.write(resp.getBytes());
        os.close();
	}
}