package discord_bot;

import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.login.LoginException;

import com.sun.net.httpserver.HttpServer;

import discord_bot.HttpSrv.MyHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Main {																		
	// Main klasa

	public static JDA jda;																
	// Bot

	public static String[] messages = 
		{
			"!help",
      "bit.ly/ShruggoBot"
		};																				
	// Statusne poruke
  
	public static int currentIndex = 0;													
	// Index poruke
	
	public static void main(String[] args) throws LoginException, 
									InterruptedException, IOException {					
	// Main funkcija
	
	// -- Vadjenja tokena --
		
	File token_fajl = new File("token.txt");										
    	// Fajl za token, gde se cuva
    
   	Scanner tR = new Scanner(token_fajl);											
    	// Scanner za token

    	String token = tR.nextLine();													
    	// Uzimamo token iz fajla
    	
    	tR.close();	
    	// Zavrsavamo, izlaz iz fajla
    	
    	// -- Instanciranje objekta -- 
	
	jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MEMBERS).build();
    	// Instanciranje JDA objekta, sa tokenom
		
	// -- Aktivnost, status, onlineStatus --
		
	new Timer().schedule(new TimerTask() {	
      	// Timer za menjanje statusne poruke
		public void run() {	
        	// Funkcija za rad, async
		    jda.getPresence().setActivity(Activity.playing(messages[currentIndex]));	
			// Menjanje statusa na osnovu indexa
		    	currentIndex=(currentIndex+1)%messages.length;  
        		// Povecavanje indexa
		}},0,30_000);	
    		// Svakih 30 sekundi
		
	jda.getPresence().setStatus(OnlineStatus.IDLE);	
    	// OnlineStatus stavljamo na IDLE
		
	// -- Listeneri --
		
	jda.addEventListener(new Commands());	
    	// Dodavanje event listenera
    
	jda.addEventListener(new OtherEvents());	
    	// Klase Commands i JoinEvent
		
	jda.awaitReady();	
    	// Cekamo pocetak
		
	// jda.setAutoReconnect(true);	
    	// U slucaju diskonekta, ponovo se ukljuci
		
	// -- Odrzavanje bota aktivnim --
		
	HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);			
    	// Aktiviramo server na portu 8
    
    	server.createContext("/", new MyHandler());	
    	// Pravi handle za nastavak URL-a, /test
    
    	server.setExecutor(null); 
    	// Executor stavljamo na nista
    
    	server.start();	
    	// Pocinjemo server

	}
}