package com.discord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Cooldowns {

	public static Boolean cooldown_spamovanje = false;
	public static Boolean cooldown_respond = false;
	public static Boolean cooldown_brisanje_rp = false;
	
	public static Integer cooldown_brojno_respond_poruke = 3;
	public static Integer cooldown_iskljucivanje = 5;
	
	public static void UKLJUCI_COOLDOWN(Integer cooldown_duzina) {
    cooldown_spamovanje = true;
    
		new Timer().schedule(new TimerTask() {	
			public void run() {	
				cooldown_spamovanje = false;
			}
		}, cooldown_duzina*1000);	
	}

  public static void UKLJUCI_COOLDOWN_RESPONSE(Integer cooldown_duzina) {
    cooldown_respond = true;
    
  		new Timer().schedule(new TimerTask() {	
  			public void run() {	
  				cooldown_respond = false;
  			}
  		}, cooldown_duzina*1000);	
  	}
  
  public static void UKLJUCI_COOLDOWN_ZA_BRISANJE_RP(MessageReceivedEvent event) {
	
	  cooldown_brisanje_rp = true;
	  
	  	new Timer().schedule(new TimerTask() {	
  			public void run() {	
  				List<Message> messages = event.getChannel().getHistory().retrievePast(50).complete();
  		        
  				List<Message> poruke_od_autora = new ArrayList<Message>();
  				
  		        for(Message m : messages) {
  		        	if(m.getAuthor().equals(event.getAuthor())) {
  		        		poruke_od_autora.add(m);
  		        	}
  		        	
  		        	if(event.getMessage().getId().equals(m.getId())) {
  		        		break;
  		        	}
  		        }
  		        
  		        poruke_od_autora.remove(poruke_od_autora.size() - 1);
  		        
  		        Collections.reverse(poruke_od_autora);
  		        
  		        if(poruke_od_autora.isEmpty()) {
  		        	event.getChannel().sendTyping().queue();
  		    	  	event.getChannel().sendMessage("Brisanje svih RESPONDPORUKA spreceno.\nMorate odgovoriti na poruku...").queue(m -> m.delete().queueAfter(15, TimeUnit.SECONDS));
  		        	
  		        	cooldown_brisanje_rp = false;
        			return;
  		        }
  		        
  		        if(poruke_od_autora.get(0).getContentRaw().toLowerCase().matches("da")) {
  		        	File triggeri = new File("triggeri.txt");
	  		  		File poruke = new File("poruke.txt");
	  		  		
	  		  		triggeri.delete();
	  		  		poruke.delete();
	  		  		
	  		  		try {
						triggeri.createNewFile();
						poruke.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
  		        	
		  		  	event.getChannel().sendTyping().queue();
	    	  	    event.getChannel().sendMessage("Brisanje svih RESPONDPORUKA uspesno.").queue(m -> m.delete().queueAfter(15, TimeUnit.SECONDS));
	  		  		
  		        	cooldown_brisanje_rp = false;
        			return;
  		        }
  		        
	  		    event.getChannel().sendTyping().queue();
	    	  	event.getChannel().sendMessage("Brisanje svih RESPONDPORUKA spreceno.").queue(m -> m.delete().queueAfter(15, TimeUnit.SECONDS));
  		        
  		        cooldown_brisanje_rp = false;
  			}
  		}, 15*1000);	
  	  }
  
	  public static void ISKLJUCI_BOTA_COOLDOWN(Integer vreme) {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				Main.jda.shutdown();
				System.exit(0);
			}
			
		}, vreme * 1000);
	  }
	  
	  public static void IZADJI_IZ_SERVERA(Guild guild, Integer vreme) {
			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					guild.leave().queue();
				}
				
			}, vreme * 1000);
		  }
}
