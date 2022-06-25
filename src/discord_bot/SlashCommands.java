package discord_bot;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class SlashCommands extends ListenerAdapter {

	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        
		if(event.getGuild() == null) {
			return;
		}
		
		switch(event.getName()) {
			case "shutdown":
				SHUTDOWN_KOMANDA(event);
			break;
			
			case "say":
				RECI_KOMANDA(event);
			break;
			
			case "info":
				INFO_KOMANDA(event);
			break;
			
			case "ping": 
				PING_KOMANDA(event);
			break;
			
			case "izadji":
				IZADJI_KOMANDA(event);
			break;
			
			case "preimenuj_kanal":
				PREIMENUJ_KANAL_KOMANDA(event);
			break;
			
			case "testiranje_dugmeta":
				TESTIRANJE_DUGMETA_KOMANDA(event);
			break;
			
			default:
				event.deferReply(true).queue();
				InteractionHook hook = event.getHook();
				hook.setEphemeral(true);
				
				hook.sendMessage("Ne mogu vam to sada uraditi...").queue();
		}
    }
	
	private void TESTIRANJE_DUGMETA_KOMANDA(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		hook.setEphemeral(true);
		
		hook.sendMessage("Pritisnite dugme").addActionRow(
				Button.of(ButtonStyle.SUCCESS, "potvrda", "😋")
				)
		.queue();
	}

	public static void SHUTDOWN_KOMANDA(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		hook.setEphemeral(true);
		
		if(event.getUser().getAsTag().equals(Commands.kreator)) {
			hook.sendMessage("Iskljucivanje za 5 sekundi...").queue();
			Cooldowns.ISKLJUCI_BOTA_COOLDOWN(Cooldowns.cooldown_iskljucivanje);
			
		} else {
			hook.sendMessage("Nemate pravo uraditi ovu komandu...").queue();
		}
	}
	
	public static void RECI_KOMANDA(SlashCommandInteractionEvent event) {
		try {
			String content = event.getOption("izjava").getAsString();
			
			event.reply(content).queue();
		} catch(NullPointerException e) {
			event.deferReply(true).queue();
			InteractionHook hook = event.getHook();
			hook.setEphemeral(true);
			
			hook.sendMessage("Morate dostaviti drugi argument.").queue();
		}
	}
	
	public static void INFO_KOMANDA(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		hook.setEphemeral(true);
		
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle("**INFORMACIJE O BOTU**");
		eb.setColor(Color.CYAN);
		String vreme = "<t:" + (System.currentTimeMillis() / 1000L) + ":f>";
		eb.setDescription("Iskoriscenjo od strane: " + event.getUser().getAsMention() + "\nVreme koriscenja: " + vreme);
		
		eb.addField("Namena", "- Discord Bot se koristi samo za zajebanciju...", false);
		eb.addField("Prikupljanje informacije", "- Discord Bot ne prikuplja informacije o korisnicima Discord servera", false);
		eb.addField("Kreator", "- Discord Bot je napravljen od strane Nenada Gvozdenca", false);
		eb.addField("Github", "- Ceo projekat se nalazi na Github, (https://github.com/NenadGvozdenac/DiscordBot)", false);
		
		eb.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Info_icon_002.svg/1200px-Info_icon_002.svg.png");
		
		hook.sendMessageEmbeds(eb.build()).queue();
		
	}
	
	public static void PING_KOMANDA(SlashCommandInteractionEvent event) {
		long start = System.currentTimeMillis();
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		hook.setEphemeral(true);
		long end = System.currentTimeMillis();
		
		hook.sendMessage("🏓🏓🏓\nVreme: " + (end - start) + " milisekundi.").queue();
	}
	
	public static void IZADJI_KOMANDA(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		hook.setEphemeral(true);
		
		if(!event.getMember().equals(event.getGuild().getOwner())) {
			hook.sendMessage("Ne mozes me izbaciti iz servera!").queue();
			return;
		}
		
		hook.sendMessage("Mozete invite-ovati bota putem ovog linka: https://discord.com/api/oauth2/authorize?client_id=706482474200334366&permissions=8&scope=bot%20applications.commands\nBot izlazi iz servera za 3 sekunde.")
		.queue();
		
		Cooldowns.IZADJI_IZ_SERVERA(event.getGuild(), 3);
	}
	
	public static void PREIMENUJ_KANAL_KOMANDA(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		hook.setEphemeral(true);
		
		try {
			GuildChannel kanal = event.getOption("origkanal").getAsGuildChannel();
			
			String naziv = event.getOption("novikanal").getAsString();
		
			kanal.getManager().setName(naziv).queue();
			
			hook.sendMessage("Preimenovao sam kanal...").queue();
			
		} catch(NullPointerException e) {
			
			hook.sendMessage("Morate dostaviti drugi argument.").queue();
		}
	}
}
