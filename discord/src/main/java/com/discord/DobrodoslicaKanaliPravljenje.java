package com.discord;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;

public class DobrodoslicaKanaliPravljenje extends ListenerAdapter {

	final static String admin_kategorija = "Admin Kategorija";
	final static String dobrodoslica_kategorija = "Dobrodosli";
	
	Boolean napraviDobrodoslicu, napraviAdmin, zavrsi;
	
	public DobrodoslicaKanaliPravljenje() {
		napraviDobrodoslicu = false;
		napraviAdmin = false;
		zavrsi = false;
	}
	
	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent e) {
		if(e.getButton().getId().equals("dobrodoslica")) {
			e.getChannel().sendMessage("Kanal za dobrodoslicu ce biti napravljen.").queue(k -> k.delete().queueAfter(10, TimeUnit.SECONDS));
			e.deferEdit().setActionRows(ActionRow.of(
				e.getButton().asDisabled(),
				e.getMessage().getButtonById("adminkanal"),
				e.getMessage().getButtonById("zavrsi")
			)).queue();

			napraviDobrodoslicu = true;
		}
		
		if(e.getButton().getId().equals("adminkanal")) {
			e.getChannel().sendMessage("Kanal za admine ce biti napravljen.").queue(k -> k.delete().queueAfter(10, TimeUnit.SECONDS));
			e.deferEdit().setActionRows(ActionRow.of(
				e.getMessage().getButtonById("dobrodoslica"),
				e.getButton().asDisabled(),
				e.getMessage().getButtonById("zavrsi")
			)).queue();
			napraviAdmin = true;
		}
	
		if(e.getButton().getId().equals("zavrsi") || (e.getMessage().getButtonById("dobrodoslica").isDisabled() && e.getMessage().getButtonById("adminkanal").isDisabled())) {
			e.deferEdit().setActionRows(ActionRow.of(
				e.getMessage().getButtonById("dobrodoslica").asDisabled(),
				e.getMessage().getButtonById("adminkanal").asDisabled(),
				e.getMessage().getButtonById("zavrsi").asDisabled()
			)).queue();
			NAPRAVI_KANALE(e);
		}
	}

	private void NAPRAVI_KANALE(@NotNull ButtonInteractionEvent e) {
		
		if(napraviDobrodoslicu) {
			NAPRAVI_KANAL_ZA_DOBRODOSLICU(e);
		}
		
		if(napraviAdmin) {
			NAPRAVI_KANAL_ZA_ADMINE(e);
		}
		
		e.getMessage().editMessage(e.getMessage().getContentRaw()).setActionRows().queue();
		e.getJDA().removeEventListener(this);
		e.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
	}

	private void NAPRAVI_KANAL_ZA_ADMINE(@NotNull ButtonInteractionEvent e) {
		EnumSet<Permission> permissions = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);
		
		e.getGuild().createCategory(admin_kategorija).queue(
				m -> {
					m.createTextChannel(OtherEvents.admin_kanal)
				.setTopic("Admin kanal za slanje logova")
				.setPosition(0)
				.addPermissionOverride(e.getGuild().getPublicRole(), null, permissions)
				.queue(
						n -> n.sendMessage("❗ Kanal napravljen za slanje admin logova.").queueAfter(2, TimeUnit.SECONDS)
					  );
				}
			);
	}

	private void NAPRAVI_KANAL_ZA_DOBRODOSLICU(@NotNull ButtonInteractionEvent e) {
		EnumSet<Permission> dobrodoslica_permissions = EnumSet.of(Permission.MESSAGE_SEND);
		
		e.getGuild().createCategory(dobrodoslica_kategorija).setPosition(0).queue(
				m -> m.createTextChannel(OtherEvents.dobrodoslica_kanal)
				.setTopic("Kanal za slanje toplih poruka dobrodoslice za nove korisnike")
				.setPosition(0)
				.addPermissionOverride(e.getGuild().getPublicRole(), null, dobrodoslica_permissions)
				.queue (
						n -> n.sendMessage("❗ Kanal napravljen za dobrodoslicu novih ljudi.").queueAfter(2, TimeUnit.SECONDS)
					   )
			);
	}
	
}
