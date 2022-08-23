package com.discord;

import java.awt.Color;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.Guild.BoostTier;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
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

		if(dobrodoslica_text_kanal == null) {
			return;
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
		event.getJDA().getGuildById("945790514605727755").getTextChannelById("1002918401750085643").sendMessage(event.getCause().getMessage() + "\n" + event.getCause().toString()).queue();
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

		TextChannel kanalZaSlanjeObavestenja = null;
		
		List<TextChannel> kanali = event.getGuild().getTextChannels();
        
        for(TextChannel c : kanali) {
        	if(c.getName().equalsIgnoreCase(dobrodoslica_kanal) || c.getName().equalsIgnoreCase(dobrodoslica_kanal2)) {
        		kanalZaSlanjeObavestenja = c;
        	}
        }

		if(kanalZaSlanjeObavestenja == null) {
			return;
		}

		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Novi boost broj \u2757");
		eb.setThumbnail(event.getGuild().getIconUrl());
		eb.setColor(Color.pink);

		if(event.getNewBoostCount() > event.getOldBoostCount()) {
			eb.setFooter("Hvala na boost-ovanju! \u2764\uFE0F \u2764\uFE0F");
			eb.setDescription("Broj boostova servera se povecao na **" + event.getNewBoostCount() + "**! Pre je bio **" + event.getOldBoostCount() + "**. \n\n\u2764\uFE0F \u2764\uFE0F");
		} else {
			eb.setFooter("by ShruggoBot");
			eb.setDescription("Broj boostova servera se smanjio na **" + event.getNewBoostCount() + "**! Pre je bio **" + event.getOldBoostCount() + "**. \n\n\uD83D\uDE1E \uD83D\uDE1E");
		}

		kanalZaSlanjeObavestenja.sendMessageEmbeds(eb.build()).queue();
	}

	@Override
	public void onGuildUpdateBoostTier(@NotNull GuildUpdateBoostTierEvent event) {

		TextChannel kanalZaSlanjeObavestenja = null;
		
		List<TextChannel> kanali = event.getGuild().getTextChannels();
        
        for(TextChannel c : kanali) {
        	if(c.getName().equalsIgnoreCase(dobrodoslica_kanal) || c.getName().equalsIgnoreCase(dobrodoslica_kanal2)) {
        		kanalZaSlanjeObavestenja = c;
        	}
        }

		if(kanalZaSlanjeObavestenja == null) {
			return;
		}

		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Novi boost tier \u2757");
		eb.setThumbnail(event.getGuild().getIconUrl());
		eb.setColor(Color.ORANGE);

		if(event.getNewBoostTier().getMaxEmojis() > event.getOldBoostTier().getMaxEmojis()) {
			eb.setFooter("Hvala na boost-ovanju! \u2764\uFE0F \u2764\uFE0F");
			eb.setDescription("Tier servera servera se upravo povecao na **" + 
				(event.getNewBoostTier() == BoostTier.NONE ? "0" : event.getNewBoostTier()) + 
				"**! Pre je bio **" + (event.getOldBoostTier() == BoostTier.NONE ? "0" : event.getOldBoostTier()) + 
				"**. \n\n\u2764\uFE0F \u2764\uFE0F"
			);
		} else {
			eb.setDescription("Tier servera servera se upravo smanjio na **" + 
				(event.getNewBoostTier() == BoostTier.NONE ? "0" : event.getNewBoostTier()) + 
				"**! Pre je bio **" + (event.getOldBoostTier() == BoostTier.NONE ? "0" : event.getOldBoostTier()) + 
				"**. \n\n\uD83D\uDE1E \uD83D\uDE1E"
			);
		}

		kanalZaSlanjeObavestenja.sendMessageEmbeds(eb.build()).queue();
	}

	@Override
	public void onGuildJoin(@NotNull GuildJoinEvent event) {
			long vreme = 7_000;	// PROMENITI U KURAC

			TextChannel channel = event.getGuild().getDefaultChannel().asTextChannel();
				
			channel.sendMessage("👋 Pozdrav! Hvala što ste dodali ovog bota u **" + event.getGuild().getName() + "**. Sva pitanja, mogu ići direktno na **NenadG#0001**.").queue();
				
				channel.sendMessage(
					"Da li zelite kanal za dobrodoslicu ili kanal za slanje admin komandi? \nUkoliko da, pritisnite na dugme ispod i \"Zavrsi\" kada ste gotovi.")
					.setActionRow(
						Button.primary("dobrodoslica", "DOBRODOSLICA"), 
						Button.danger("adminkanal", "ADMIN KANAL"), 
						Button.secondary("zavrsi", "ZAVRSI")
					)
				.queue(k -> {

					DobrodoslicaKanaliPravljenje dob = new DobrodoslicaKanaliPravljenje();

					event.getJDA().addEventListener(dob);

					new Timer().schedule(new TimerTask() {

						@Override
						public void run() {
							
							if(dob.napraviAdmin == false && dob.napraviDobrodoslicu == false && dob.zavrsi == false) {
								k.editMessage("Niste pritisnuli na vreme ni jedno dugme. Prestanak radnje").queue(m -> m.delete().queueAfter(15, TimeUnit.SECONDS));
								Main.jda.removeEventListener(dob);
							} else {
								dob.NAPRAVI_KANALE();
								Main.jda.removeEventListener(dob);
							}
							
							this.cancel();
						}
						
					}, vreme, 1);
				});

			
				
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Novo ulazenje u server!");
			eb.setDescription("Server: " + event.getGuild().getName());
			eb.setThumbnail(event.getGuild().getIconUrl());
			eb.setFooter("ShruggoBot");

			Message message = new MessageBuilder().setEmbeds(eb.build()).build();
			event.getJDA().getGuildById("945790514605727755").getTextChannelById("1002918401750085643").sendMessage(message).queue();
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

	@Override
	public void onGuildLeave(@NotNull GuildLeaveEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Izlaz is servera!");
		eb.setDescription("Server: " + event.getGuild().getName());
		eb.setThumbnail(event.getGuild().getIconUrl());
		eb.setFooter("ShruggoBot");

		try {
			Message message = new MessageBuilder().setEmbeds(eb.build()).build();
			event.getJDA().getGuildById("945790514605727755").getTextChannelById("1002918401750085643").sendMessage(message).queue();
		} catch(NullPointerException e) {
			
		}
	}
}