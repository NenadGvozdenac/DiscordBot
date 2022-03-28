package discord_bot;

import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class Main {

	public static JDA jda;
	public static String token = "NzA2NDgyNDc0MjAwMzM0MzY2.Xq65Ew.0Ty0qSeTbDNo_A435x0jdFPIVqQ";
	
	public static String[] messages = {"being the best", "made by Nenad", "beating others", "having some bitches..."};
	public static int currentIndex = 0;
	
	public static void main(String[] args) throws LoginException, InterruptedException {
		
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

