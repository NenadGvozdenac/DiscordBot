package discord_bot;

import java.util.EnumSet;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Notifications extends ListenerAdapter implements EventListener {
	
	public Guild server_originalni, server_za_slanje;

	public void onMessageReceived(MessageReceivedEvent event) {
		
		server_originalni = Main.jda.getGuildById(945790514605727755L);
		server_za_slanje = Main.jda.getGuildById(931636632992497684L);
		
		if(event.getMessage().getContentRaw().equals("!napravi_notifikacije_kanal")) {
			EnumSet<Permission> permissions = EnumSet.of(Permission.MESSAGE_SEND);
			
			server_za_slanje.createCategory("Besplatne igrice").setPosition(0).queue(n -> n.createTextChannel("besplatne igrice").addPermissionOverride(server_za_slanje.getPublicRole(), null, permissions).queue());
			return;
		}
		
		if(!event.getAuthor().isBot()) return;
		
		if(!event.getChannel().getName().equals("freegames")) return;
		
		Message msg = event.getMessage();
		TextChannel slanje = server_za_slanje.getTextChannelById(990153941369110568L);
		
		slanje.sendMessage(msg).queue();
	}
}
