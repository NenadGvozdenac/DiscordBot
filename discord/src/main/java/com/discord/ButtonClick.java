package com.discord;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class ButtonClick extends ListenerAdapter {
	
	static List<Member> glasali = new ArrayList<>();
	Message message;
	static List<Integer> listaBrojevaStatic = new ArrayList<>();
	
	public ButtonClick(Message message) {
		this.message = message;
	}
	
	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent e) {
		
		if(!glasali.contains(e.getMember())) {
			glasali.add(e.getMember());
			
			e.deferReply(true).queue();
			InteractionHook hook = e.getHook();
			hook.setEphemeral(true);
			
			hook.sendMessage((e.getMember().getAsMention() + " je glasao za **" + e.getButton().getLabel() + "**!")).queue();
			
			MessageEmbed aktivniEmbed = e.getMessage().getEmbeds().get(0);
			
			List<Field> listaField = aktivniEmbed.getFields();
			
			List<String> listaStringovaBrojeva = new ArrayList<>();
			
			for(Field f : listaField) {
				listaStringovaBrojeva.add(f.getValue());
			}
			
			List<Integer> listaBrojeva = new ArrayList<>();
			
			for(String s : listaStringovaBrojeva) {
				listaBrojeva.add(Integer.parseInt(s));
			}
			
			listaBrojeva.set(Integer.parseInt(e.getButton().getId()), listaBrojeva.get(Integer.parseInt(e.getButton().getId())) + 1);
				
			listaBrojevaStatic = listaBrojeva;
			
			String novo = String.valueOf(listaBrojeva.get(Integer.parseInt(e.getButton().getId())));
			
			Field f = new Field(listaField.get(Integer.parseInt(e.getButton().getId())).getName(), novo, true);
			
			EmbedBuilder embedZaPostavljanje = new EmbedBuilder();
			embedZaPostavljanje.setTitle(aktivniEmbed.getTitle());
			embedZaPostavljanje.setDescription(aktivniEmbed.getDescription());
			embedZaPostavljanje.setColor(Color.blue);
			embedZaPostavljanje.setFooter(aktivniEmbed.getFooter().getText());
			embedZaPostavljanje.setThumbnail(aktivniEmbed.getThumbnail().getUrl());
			
			int brojac = 0;
			
			for(Field f2 : listaField) {
				
				if(Integer.parseInt(e.getButton().getId()) == brojac) {
					embedZaPostavljanje.addField(f);
					brojac++;
					continue;
				}
				
				embedZaPostavljanje.addField(f2);
				
				brojac++;
			}
			
			message.editMessageEmbeds(embedZaPostavljanje.build()).queue();
			
		} else {
			e.deferReply(true).queue();
			InteractionHook hook = e.getHook();
			hook.setEphemeral(true);
			
			hook.sendMessage("Niste u mogucnosti ponovo glasati. Vec ste glasali jednom!").queue();
		}
	}
}
