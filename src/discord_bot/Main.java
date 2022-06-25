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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

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
		
	String token = System.getenv("token");		
    	
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
	
	jda.addEventListener(new SlashCommands());

  jda.addEventListener(new Notifications());
		
	jda.awaitReady();	
	
	CommandListUpdateAction commands = jda.updateCommands();
	
	commands.addCommands(
			net.dv8tion.jda.api.interactions.commands.build.Commands.slash("shutdown", "Iskljucuje bota na odredjeno vreme"),
			net.dv8tion.jda.api.interactions.commands.build.Commands.slash("say", "Iskljucuje bota na odredjeno vreme")
				.addOption(OptionType.STRING, "izjava", "Sta zelite da bot kaze"),
				
			net.dv8tion.jda.api.interactions.commands.build.Commands.slash("info", "Informacije o komandama bota"),
			net.dv8tion.jda.api.interactions.commands.build.Commands.slash("ping", "Pingovanje bota 🏓"),
			net.dv8tion.jda.api.interactions.commands.build.Commands.slash("izadji", "Izbaci bota sa servera"),
			net.dv8tion.jda.api.interactions.commands.build.Commands.slash("preimenuj_kanal", "Preimenuj kanal u drugi naziv")
				.addOption(OptionType.CHANNEL, "origkanal", "Kanal za preimenovanje")
				.addOption(OptionType.STRING, "novikanal", "Novi naziv kanala")
			);
	
	commands.addCommands((
			net.dv8tion.jda.api.interactions.commands.build.Commands.slash("testiranje_dugmeta", "Testiranje dugmica nekih")
			));
	
	commands.queue();
		
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