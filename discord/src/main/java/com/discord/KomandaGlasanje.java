package com.discord;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

class KomandaGlasanje extends ListenerAdapter {
	
	public Member member;
	private static String naslov;
	private static String deskripcija;
	SlashCommandInteractionEvent e;
	EmbedBuilder eb;
	long vremeTajmera = 25;
	Boolean aktivnoGlasanje = false;
	int brojac = 0;
	
	static List<String> opcijeZaGlasanje = new ArrayList<>();
	
	public KomandaGlasanje(Member member, String naslov1, String deskripcija1, SlashCommandInteractionEvent event, Integer trajanje) {
		this.member = member;
		naslov = naslov1;
		deskripcija = deskripcija1;
		this.e = event;
		this.vremeTajmera = trajanje;
		opcijeZaGlasanje.clear();
		
		if(!ButtonClick.glasali.isEmpty())
			ButtonClick.glasali.clear();
		
		if(!ButtonClick.listaBrojevaStatic.isEmpty())
			ButtonClick.listaBrojevaStatic.clear();
		
		aktivnoGlasanje = true;
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event1) {
		
		if(event1.getMember().equals(member) && event1.getGuild().equals(member.getGuild()) && event1.getChannel().equals(e.getChannel())) {
			brojac++;
			
			if(event1.getMessage().getContentRaw().equals("stop")) {
				event1.getMessage().delete().queue();
				this.ZAVRSI_DODAVANJE(event1);
				return;
			}

			if(brojac == 5) {
				opcijeZaGlasanje.add(event1.getMessage().getContentRaw());
				event1.getMessage().delete().queue();
				this.ZAVRSI_DODAVANJE(event1);
				return;
			}
			
			opcijeZaGlasanje.add(event1.getMessage().getContentRaw());
			
			event1.getMessage().delete().queue();
		}
		
	}
	
	private void ZAVRSI_DODAVANJE(MessageReceivedEvent event1) {
		
		eb = new EmbedBuilder();
		eb.setTitle(naslov);
		eb.setDescription(deskripcija + "\nVREME GLASANJA: " + vremeTajmera + " sekundi.");
		eb.setColor(Color.blue);
		eb.setFooter("AUTOR GLASANJA: " + this.e.getMember().getEffectiveName());
		eb.setThumbnail("http://clipart-library.com/new_gallery/72-723054_big-question-mark-big-question-mark-transparent.png");
		
		List<Button> buttons = new ArrayList<>();
		
		Integer brojac = 0;
		
		for(String s : opcijeZaGlasanje) {
			buttons.add(Button.primary(brojac.toString(), s));
			eb.addField(s, "0", true);
			brojac++;
			
			if(brojac == 5) {
				break;
			}
		}
		
		this.e.getChannel().sendMessageEmbeds(eb.build()).setActionRows(ActionRow.of(buttons)).queue(
				k -> ISPISI_POBEDNIKA(k, buttons, eb, event1)
		);
	}

	private void ISPISI_POBEDNIKA(Message message, List<Button> b, EmbedBuilder eb2, MessageReceivedEvent event) {

		JDA jda = event.getJDA();
		jda.removeEventListener(this);
		ButtonClick clickEvent = new ButtonClick(message);
		
		jda.addEventListener(clickEvent);
		
		try {
			Thread.sleep(vremeTajmera * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int brojPobednika = Collections.max(ButtonClick.listaBrojevaStatic);
		int[] indexPobednika = new int[5];
		
		int brojac = 0;
		
		for(int i = 0; i < b.size(); i++) {
			if(ButtonClick.listaBrojevaStatic.get(i) == brojPobednika) {
				indexPobednika[brojac] = i;
				brojac++;
			}
		}
		
		EmbedBuilder eb1 = new EmbedBuilder();
		
		eb1.setTitle(eb2.build().getTitle());
		
		String deskripcija = "OPIS: " + eb2.build().getDescription().toUpperCase() + "\nPOBEDNIK: **";
		
		for(int pob : indexPobednika) {
			deskripcija += b.get(pob).getLabel().toUpperCase() + " ";
			brojac--;
			
			if(brojac == 0) {
				break;
			}
		}
		
		eb1.setDescription(deskripcija + "**. \n\nSVAKA CAST! \n\nBROJ GLASOVA: " + brojPobednika);
		eb1.setColor(Color.GREEN);
		eb1.setThumbnail("https://pngimg.com/uploads/confetti/confetti_PNG86957.png");
		eb1.setFooter("AUTOR GLASANJA: " + this.e.getMember().getEffectiveName());
		
		String footer = "";
		
		for(Member m : ButtonClick.glasali) {
			footer += m.getAsMention() + "; ";
		}
		
		eb1.addField("HVALA NA GLASANJU!", footer, false);
		message.editMessageEmbeds(eb1.build()).setActionRows().queue();
		
		jda.removeEventListener(clickEvent);
		
		aktivnoGlasanje = false;
	}
}