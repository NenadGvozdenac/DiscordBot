package discord_bot;

import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

	public static JDA jda;
	
	public static String[] messages = {"being the best", "made by Nenad", "beating others", "having some bitches..."};
	public static int currentIndex = 0;
	
	public static void main(String[] args) throws LoginException, InterruptedException, FileNotFoundException {

    File token_fajl = new File("token.txt");
    Scanner tR = new Scanner(token_fajl);

    String token = tR.nextLine();

    tR.close();
		
		jda = JDABuilder.createDefault(token).build();
		
		//Run this once
		new Timer().schedule(new TimerTask(){
		  public void run(){
		    jda.getPresence().setActivity(Activity.playing(messages[currentIndex]));
		    currentIndex=(currentIndex+1)%messages.length;
		  }},0,30_000);
		
		jda.getPresence().setStatus(OnlineStatus.IDLE);
		
		jda.addEventListener(new Commands());
		jda.addEventListener(new JoinEvent());
		
		jda.awaitReady();
		
		jda.setAutoReconnect(true);

	}
}

