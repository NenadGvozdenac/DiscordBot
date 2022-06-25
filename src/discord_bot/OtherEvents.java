package discord_bot;

import java.awt.Color;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.BaseGuildMessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class OtherEvents extends ListenerAdapter {
	
	final static String prefix = "!";
	
	final static String admin_kanal = "admin-logs";
	final static String dobrodoslica_kanal = "welcome";
	
	final static String admin_kategorija = "Admin Kategorija";
	final static String dobrodoslica_kategorija = "Dobrodosli";
	
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
			
		TextChannel dobrodoslica_text_kanal = null;
		
		List<TextChannel> kanali = event.getGuild().getTextChannels();
        
        for(TextChannel c : kanali) {
        	if(c.getName().equalsIgnoreCase(OtherEvents.dobrodoslica_kanal)) {
        		dobrodoslica_text_kanal = c;
        	}
        }
        
        EmbedBuilder eb = new EmbedBuilder();
        
        System.out.print("Test");
        
        eb.setTitle("Dobrodosli u " + event.getGuild().getName());
        eb.setColor(Color.CYAN);
        
        eb.setDescription("Zelim vam lep provod, " + event.getMember().getAsMention() +"! 🎉");
        
        dobrodoslica_text_kanal.sendMessageEmbeds(eb.build()).queue();
	}
	

	public void onReady(ReadyEvent event) {
		System.out.println("Bot is alive and well to function!");
	}
	
	public void onShutdown(ShutdownEvent event) {
		System.out.println("Bot is turning off.");
	}
	
	public void onGuildJoin(GuildJoinEvent event) {
		BaseGuildMessageChannel channel = event.getGuild().getDefaultChannel();
		
		channel.sendMessage("👋 Pozdrav! Hvala što ste dodali ovog bota u **" + event.getGuild().getName() + "**. Sva pitanja, mogu ići direktno na **NenadG#0000**.").queue();
		
		EnumSet<Permission> permissions = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);
		
		event.getGuild().createCategory(admin_kategorija).queue(
				m -> m.createTextChannel(OtherEvents.admin_kanal)
				.setTopic("Admin kanal za slanje logova")
				.setPosition(0)
				.addPermissionOverride(event.getGuild().getPublicRole(), null, permissions)
				.queue(
						n -> n.sendMessage("❗ Kanal napravljen za slanje admin logova.").queueAfter(2, TimeUnit.SECONDS)
					  )
			);
		
		EnumSet<Permission> dobrodoslica_permissions = EnumSet.of(Permission.MESSAGE_SEND);
		
		event.getGuild().createCategory(dobrodoslica_kategorija).setPosition(0).queue(
				m -> m.createTextChannel(OtherEvents.dobrodoslica_kanal)
				.setTopic("Kanal za slanje toplih poruka dobrodoslice za nove korisnike")
				.setPosition(0)
				.addPermissionOverride(event.getGuild().getPublicRole(), null, dobrodoslica_permissions)
				.queue (
						n -> n.sendMessage("❗ Kanal napravljen za dobrodoslicu novih ljudi.").queueAfter(2, TimeUnit.SECONDS)
					   )
			);
	}
}
