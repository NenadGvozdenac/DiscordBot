package com.discord;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class ModalCommands extends ListenerAdapter {
	
	Guild supportMainGuild;
	long id = 945790514605727755L;
	String supportGuildKanal = "support-tiket";
	ModalInteractionEvent event;
	public static Member m;

	@Override
	public void onModalInteraction(@Nonnull ModalInteractionEvent event) {
		
		this.event = event;
		
		switch(event.getModalId()) {
			case "tiket":
				POSALJI_TIKET(event);
			break;

			case "banTiket":
				POSALJI_BAN_TIKET(event);
			break;

			case "kickTiket":
				POSALJI_KICK_TIKET(event);
			break;

			case "timeoutTiket":
				POSALJI_TIMEOUT_TIKET(event);
			break;

			case "muteTiket":
				POSALJI_MUTE_TIKET(event);
			break;
		}
	}

	private void POSALJI_MUTE_TIKET(ModalInteractionEvent event2) {
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();

		String razlog = event.getValue("razlog").getAsString();

		try {
			String naziv = m.getEffectiveName();
			
			m.deafen(true).queue();
				
			m.getUser().openPrivateChannel().queue(k -> {
				k.sendMessage("Nazalost, dobili ste mute u serveru **" + event.getGuild().getName() + "**.\nRazlog: " + razlog).queue();
			});

			hook.sendMessage("Uspesno mutovan korisnik " + naziv + ".").queue();
		} catch(InsufficientPermissionException e1) {
			hook.sendMessage("Bot nema pravo ovo da uradi. Bot nema permisiju za ovu akciju.").queue();
			return;
		} catch(HierarchyException e2) {
			hook.sendMessage("Bot nema pravo ovo da uradi. Korisnik se nalazi iznad bota na skali permisija.").queue();
			return;
		} 
	}

	private void POSALJI_TIMEOUT_TIKET(ModalInteractionEvent event2) {
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();

		String razlog = event.getValue("razlog").getAsString();

		try {
			Integer vreme = Integer.parseInt(event.getValue("vreme").getAsString()) * 60;		// U sekundama

			String naziv = m.getEffectiveName();

			m.getUser().openPrivateChannel().queue(k -> {
				k.sendMessage("Nazalost, dobili ste timeout u serveru **" + event.getGuild().getName() + "**.\nRazlog: " + razlog).queue(
					l -> {
						m.timeoutFor(vreme, TimeUnit.SECONDS).queueAfter(5, TimeUnit.SECONDS);
					}
				);
			});

			hook.sendMessage("Uspesno timeout-ovan korisnik " + naziv + ".").queue();
		} catch(InsufficientPermissionException e1) {
			hook.sendMessage("Bot nema pravo ovo da uradi. Bot nema permisiju za ovu akciju.").queue();
			return;
		} catch(HierarchyException e2) {
			hook.sendMessage("Bot nema pravo ovo da uradi. Korisnik se nalazi iznad bota na skali permisija.").queue();
			return;
		} catch(IllegalArgumentException e3) {
			hook.sendMessage("Bot nije u mogucnosti ovo da uradi. Mozda ste ukucali pogresnu vrednost nekog polja?").queue();
			return;
		} 	
	}

	private void POSALJI_KICK_TIKET(ModalInteractionEvent event2) {
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		hook.setEphemeral(true);

		String razlog = event.getValue("razlog").getAsString();

		try {
			String naziv = m.getEffectiveName();
				
			m.getUser().openPrivateChannel().queue(k -> {
					k.sendMessage("Nazalost, dobili ste kick u serveru **" + event.getGuild().getName() + "**.\nRazlog: " + razlog).queue(
						l -> {
							m.kick(razlog).queueAfter(5, TimeUnit.SECONDS);
						}
					);
			});

			hook.sendMessage("Uspesno kickovan korisnik " + naziv + ".").queue();
		} catch(InsufficientPermissionException e1) {
			hook.sendMessage("Bot nema pravo ovo da uradi. Bot nema permisiju za ovu akciju.").queue();
			return;
		} catch(HierarchyException e2) {
			hook.sendMessage("Bot nema pravo ovo da uradi. Korisnik se nalazi iznad bota na skali permisija.").queue();
			return;
		} catch(IllegalArgumentException e3) {
			hook.sendMessage("Bot nije u mogucnosti ovo da uradi. Mozda ste ukucali pogresnu vrednost nekog polja?").queue();
			return;
		} 
	}

	private void POSALJI_BAN_TIKET(ModalInteractionEvent event2) {
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		hook.setEphemeral(true);

		String razlog = event.getValue("razlog").getAsString();

		try {
			Integer vremeBrisanjaPoruka = Integer.parseInt(event.getValue("vreme").getAsString());
					
			String naziv = m.getEffectiveName();
				
			m.getUser().openPrivateChannel().queue(k -> {
					k.sendMessage("Nazalost, dobili ste ban u serveru **" + event.getGuild().getName() + "**.").queue(
						l -> {
							m.ban(vremeBrisanjaPoruka, razlog).queueAfter(5, TimeUnit.SECONDS);
						}
					);
						
			});

			hook.sendMessage("Uspesno banovan korisnik " + naziv + ".").queue();
		} catch(InsufficientPermissionException e1) {
			hook.sendMessage("Bot nema pravo ovo da uradi. Bot nema permisiju za ovu akciju.").queue();
			return;
		} catch(HierarchyException e2) {
			hook.sendMessage("Bot nema pravo ovo da uradi. Korisnik se nalazi iznad bota na skali permisija.").queue();
			return;
		} catch(IllegalArgumentException e3) {
			hook.sendMessage("Bot nije u mogucnosti ovo da uradi. Mozda ste ukucali pogresnu vrednost nekog polja?").queue();
			return;
		} 
	}

	private void POSALJI_TIKET(ModalInteractionEvent event2) {
		String naslov = event.getValue("naslov").getAsString();
		String paragraf = event.getValue("paragraf").getAsString();
			
		posaljiSupportTiket(naslov, paragraf);
	}

	private void posaljiSupportTiket(String naslov, String paragraf) {
		
		JDA jda = event.getJDA();
		
		supportMainGuild = jda.getGuildById(id);
		
		TextChannel supportKanal = supportMainGuild.getTextChannelsByName(supportGuildKanal, true).get(0);
		
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle(naslov);
		eb.setDescription(paragraf);
		eb.setAuthor(event.getMember().getEffectiveName());
		eb.setColor(Color.MAGENTA);
		eb.setThumbnail("https://flyclipart.com/thumb2/warning-sign-png-icon-free-download-460285.png");
		
		Integer id = (int)((Math.random() * 100000) % 100000);
		
		eb.setFooter(event.getGuild().getName() + " | ID: " + id);
		
		supportKanal.sendMessageEmbeds(eb.build()).setActionRow(Button.danger(String.valueOf(id), "PROCITAN")).queue();
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		hook.setEphemeral(true);
		
		hook.sendMessage("Support tiket je poslat. Hvala na koriscenju Modal Mail-a.").queue();
		
		ModalButtonClickSupportTiket modalButtonClick = new ModalButtonClickSupportTiket(id, event.getMember(), event.getChannel().asTextChannel());
		
		jda.addEventListener(modalButtonClick);
	}
}
