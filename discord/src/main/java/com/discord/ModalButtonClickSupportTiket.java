package com.discord;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class ModalButtonClickSupportTiket extends ListenerAdapter {
	
	public Integer id;
	public Member member;
	public TextChannel kanal;
	
	public ModalButtonClickSupportTiket(Integer id, Member member, TextChannel kanal) {
		this.id = id;
		this.member = member;
		this.kanal = kanal;
	}

	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		if(event.getButton().getId().equals(String.valueOf(id))) {
			event.editButton(event.getButton().asDisabled()).queue();
			
			event.getMessage().getEmbeds().get(0);
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("PROCITANO");
			eb.setDescription("Vas tiket je procitan. \nTiket ID: " + id + "\nNaslov: **" + event.getMessage().getEmbeds().get(0).getTitle() + "**.");
			eb.setFooter("Nenad Gvozdenac");
			eb.setThumbnail("https://pngimg.com/uploads/confetti/confetti_PNG86957.png");
			
			kanal.sendMessage(member.getAsMention()).setEmbeds(eb.build()).queue();
			
			InteractionHook hook = event.getHook();
			hook.setEphemeral(true);
			
			hook.sendMessage("Procitana je poruka sa id '" + id + "'.").queue();
			event.getJDA().removeEventListener(this);
		}
	}
	
}
