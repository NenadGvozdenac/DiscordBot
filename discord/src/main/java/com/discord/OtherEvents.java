package com.discord;

import java.awt.Color;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostCountEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostTierEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class OtherEvents extends ListenerAdapter {
	
	final static String prefix = "!";
	
	final static String admin_kanal = "admin-logs";
	final static String dobrodoslica_kanal = "dobrodosli", dobrodoslica_kanal2 = "welcome";

	@Override
	public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
			
		TextChannel dobrodoslica_text_kanal = null;
		
		List<TextChannel> kanali = event.getGuild().getTextChannels();
        
        for(TextChannel c : kanali) {
        	if(c.getName().equalsIgnoreCase(dobrodoslica_kanal) || c.getName().equalsIgnoreCase(dobrodoslica_kanal2)) {
        		dobrodoslica_text_kanal = c;
        	}
        }
        
        EmbedBuilder eb = new EmbedBuilder();
        
        eb.setTitle("Dobrodosli u " + event.getGuild().getName());
        eb.setColor(Color.CYAN);
        
        eb.setDescription("Zelim vam lep provod, " + event.getMember().getAsMention() +"! 🎉");
        
        dobrodoslica_text_kanal.sendMessageEmbeds(eb.build()).queue();
	}
	
	@Override
	public void onReady(@NotNull ReadyEvent event) {
		Logger logger = LoggerFactory.getLogger(Main.class);
		logger.info("--- onReady event override --- " + " Uspesno paljenje bota!");
	}
	
	@Override
	public void onShutdown(@NotNull ShutdownEvent event) {
		Logger logger = LoggerFactory.getLogger(Main.class);
		logger.warn("--- onShutdown event override --- " + event.getJDA().getStatus() + " statusa." + " ---");
	}

	@Override
	public void onReconnected(@NotNull ReconnectedEvent event) {
		Logger logger = LoggerFactory.getLogger(Main.class);
		logger.warn("--- onReconnected event override --- " + event.getJDA().getStatus() + " statusa." + " ---");
	}

	@Override
	public void onException(@NotNull ExceptionEvent event){
		event.getJDA().getGuildById("945790514605727755").getTextChannelById("1002918401750085643").sendMessage(event.getCause().getMessage() + "\n" + event.getCause().toString());
		event.getCause().printStackTrace();
	}

	@Override
	public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
		
		TextChannel adminTextKanal = null;

		for(TextChannel c : event.getGuild().getTextChannels()) {
			if(c.getName().equals(admin_kanal)) {
				adminTextKanal = c;
				break;
			}
		}
		
		LogChannelEvent.POSALJI_KANAL_BRISANJE_LOG(adminTextKanal, event);
	}

	@Override
	public void onChannelCreate(@NotNull ChannelCreateEvent event) {

		TextChannel adminTextKanal = null;

		for(TextChannel c : event.getGuild().getTextChannels()) {
			if(c.getName().equals(admin_kanal)) {
				adminTextKanal = c;
				break;
			}
		}
		
		LogChannelEvent.POSALJI_KANAL_PRAVLJENJE_LOG(adminTextKanal, event);

	}
	
	@Override
	public void onGuildUpdateBoostCount(@NotNull GuildUpdateBoostCountEvent event) {
		TextChannel channel = event.getGuild().getTextChannelsByName(dobrodoslica_kanal, true).get(0);

		if(channel == null) {
			channel = event.getGuild().getTextChannelsByName(dobrodoslica_kanal2, true).get(0);
		}

		if(channel == null) {
			channel = event.getGuild().getDefaultChannel().asTextChannel();
		}

		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Novi boost \uE10D");
		eb.setThumbnail("https://pngimg.com/uploads/confetti/confetti_PNG86957.png");
		eb.setFooter("Hvala na boost-ovanju! \u2764\uFE0F \u2764\uFE0F");
		eb.setDescription("Broj boost-ova servera se upravo povecao na **" + event.getNewBoostCount() + "**! \u2764\uFE0F");

		channel.sendMessageEmbeds(eb.build()).queue();
	}

	@Override
	public void onGuildUpdateBoostTier(@NotNull GuildUpdateBoostTierEvent event) {

		TextChannel channel = event.getGuild().getTextChannelsByName(dobrodoslica_kanal, true).get(0);

		if(channel == null) {
			channel = event.getGuild().getTextChannelsByName(dobrodoslica_kanal2, true).get(0);
		}

		if(channel == null) {
			channel = event.getGuild().getDefaultChannel().asTextChannel();
		}

		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Novi boost tier \uE10D \uE10D \uE10D");
		eb.setThumbnail("https://pngimg.com/uploads/confetti/confetti_PNG86957.png");
		eb.setFooter("Hvala na boost-ovanju! \u2764\uFE0F \u2764\uFE0F");
		eb.setDescription("Tier servera servera se upravo povecao na **" + event.getNewBoostTier() + "**! \u2764\uFE0F");

		channel.sendMessageEmbeds(eb.build()).queue();
	}

	@Override
	public void onGuildJoin(@NotNull GuildJoinEvent event) {
		
		Thread thread = new Thread(new Runnable() {
			
			long vreme = 120_000;
			
			@Override
			public void run() {
				
				JDA jda = event.getJDA();
				
				DefaultGuildChannelUnion channel1 = event.getGuild().getDefaultChannel();
				
				TextChannel channel = channel1.asTextChannel();
				
				channel.sendMessage("👋 Pozdrav! Hvala što ste dodali ovog bota u **" + event.getGuild().getName() + "**. Sva pitanja, mogu ići direktno na **NenadG#0001**.").queue();
				channel.sendMessage("Da li zelite kanal za dobrodoslicu ili kanal za slanje admin komandi? \nUkoliko da, pritisnite na dugme ispod i \"Zavrsi\" kada ste gotovi.").setActionRow(Button.primary("dobrodoslica", "DOBRODOSLICA"), Button.danger("adminkanal", "ADMIN KANAL"), Button.secondary("zavrsi", "ZAVRSI")).queue();
				
				jda.addEventListener(new DobrodoslicaKanaliPravljenje());
				
				new Timer().schedule(new TimerTask() {
					
					public void run() {	
					    
						List<Object> listaListenera = jda.getRegisteredListeners();
						
						for(Object o : listaListenera) {
							if(o instanceof DobrodoslicaKanaliPravljenje) {
								channel.sendMessage("Niste pritisnuli nista prethodnih " + vreme / 1000 + " sekundi.\nNece ni jedan kanal biti napravljen.").queue();
								jda.removeEventListener(o);
							}
						}
						
						this.cancel();
						
					}
				}, vreme, 1);	
			}
		});
		
		thread.run();
	}

	@Override
	public void onGuildBan(@NotNull GuildBanEvent event) {
		
		TextChannel adminTextKanal = null;

		for(TextChannel c : event.getGuild().getTextChannels()) {
			if(c.getName().equals(admin_kanal)) {
				adminTextKanal = c;
				break;
			}
		}

		LogChannelEvent.POSALJI_BAN_LOG(adminTextKanal, event);
	}

	@Override
	public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {

		TextChannel adminTextKanal = null;

		for(TextChannel c : event.getGuild().getTextChannels()) {
			if(c.getName().equals(admin_kanal)) {
				adminTextKanal = c;
				break;
			}
		}

		LogChannelEvent.POSALJI_KICK_LOG(adminTextKanal, event);

	}
}
