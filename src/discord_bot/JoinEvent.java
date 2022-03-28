package discord_bot;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.entities.BaseGuildMessageChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinEvent extends ListenerAdapter {
	
	final static String prefix = "!";
	
	final static String admin_kanal = "admin-logs";
	final static String dobrodoslica_kanal = "dobrodosli";
	
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
			
			event.getGuild().getTextChannelById(dobrodoslica_kanal).sendMessage("👋 Dobro dosli u " + event.getGuild().getName() + "!").queue();
	}
	

	public void onReady(ReadyEvent event) {
		System.out.println("Bot is alive and well to function!");
	}
	
	public void onShutdown(ShutdownEvent event) {
		System.out.println("Bot is turning off.");
	}
	
	public void onGuildJoin(GuildJoinEvent event) {
		BaseGuildMessageChannel channel = event.getGuild().getDefaultChannel();
		Guild guild = event.getGuild();
		
		channel.sendMessage("👋 Pozdrav! Hvala što ste dodali ovog bota u **" + event.getGuild().getName() + "**. Sva pitanja, mogu ići direktno na **NenadG#0000**.").queue();
		
		guild.createTextChannel(admin_kanal).queue(m -> m.sendMessage("❗ Kanal napravljen zbog slanja logova za admine.").queueAfter(2, TimeUnit.SECONDS));
		guild.createTextChannel(dobrodoslica_kanal).queue(m -> m.sendMessage("❗ Kanal napravljen za dobrodoslicu novih ljudi.").queueAfter(2, TimeUnit.SECONDS));
	}
}
