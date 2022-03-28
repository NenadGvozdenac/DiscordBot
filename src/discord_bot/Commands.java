package discord_bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
	
	final static String prefix = "!";

	public void onMessageReceived(MessageReceivedEvent event) {
		
		String poruka = event.getMessage().getContentRaw();
		
		if(event.getAuthor().isBot() || !event.isFromType(ChannelType.TEXT)) {
			return;
		} else {
			System.out.println(event.getGuild().getName() + " : " 
					+ event.getChannel().getName() + " : " 
					+ event.getMessage().getAuthor().getAsTag() + " -> "
					+ event.getMessage().getContentRaw());
		}
		
		komande(event);

		List<String> triggeri = new ArrayList<String>();
		List<String> poruke = new ArrayList<String>();
		
		File triggers = new File("C:\\Projects\\TinaBot\\DiscordBot\\src\\discord_bot\\triggeri.txt");
		File messages = new File("C:\\Projects\\TinaBot\\DiscordBot\\src\\discord_bot\\poruke.txt");
		
		try {
			Scanner tR = new Scanner(triggers);
			Scanner mR = new Scanner(messages);
			
			 while (tR.hasNextLine()) {
			    String data = tR.nextLine();
			    triggeri.add(data);
			 }
			 
			 while(mR.hasNextLine()) {
				 String data = mR.nextLine();
				 poruke.add(data);
			 }
			 
			 tR.close();
			 mR.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0; i < triggeri.size(); i++) {
			if(poruka.toLowerCase().contains(triggeri.get(i))) {
				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage(poruke.get(i)).
					queue();
				
				break;
			}
		}
	}
	
	public void komande(MessageReceivedEvent event) {
		
		String[] args = event.getMessage().getContentRaw().split(" ");
		Member autor = event.getMember();
		
		switch(args[0].toLowerCase()) {
		case prefix + "info":
			event.getMessage().addReaction("✅").queue();
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage("Informacije o botu: \n"
					+ "- Discord Bot se koristi samo za zajebanciju...\n"
					+ "- Discord Bot ne prikuplja informacije o korisnicima Discord servera\n"
					+ "- Discord Bot je napravljen od strane Nenada Gvozdenca").queue();
		break;
		
		case prefix + "help":
			event.getMessage().addReaction("✅").queue();
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage("Informacije o komandama bota: \n"
					+ "**" + prefix + "info**\t--> Pokazuje sve informacije o botu\n"
					+ "**" + prefix + "help**\t--> Pokazuje sve komande bota\n"
					+ "**" + prefix + "nick @user**\t--> Menja nickname korisniku\n"
					+ "**" + prefix + "kick @user (ADMIN)**\t--> Izbacivanje korisnika\n"
					+ "**" + prefix + "shutdown (ADMIN)**\t--> Iskljucuje bota\n"
					+ "**" + prefix + "purge <broj> (ADMIN)**\t--> Masivno brisanje poruka\n"
					+ "**" + prefix + "bot_invite**\t--> Invite-ovanje bota u vas server\n"
					+ "Bot je u mogucnosti raditi jos stvari, kao sto su: slanje poruka za dobrodoslicu novih ljudi, slanje logova nakon akcija moderatora, itd.\n"
					).queue();
			
			if(event.getGuild().getName().toLowerCase().equals("genggengv2")) {
				String poruka = "**" + "customWords** \t--> Specijalno za GangGang, reci: ";
				List<String> triggeri = new ArrayList<String>();
				
				File fajl = new File("C:\\Projects\\TinaBot\\DiscordBot\\src\\discord_bot\\triggeri.txt");
				try {
					Scanner myReader = new Scanner(fajl);
					
					 while (myReader.hasNextLine()) {
					        String data = myReader.nextLine();
					        triggeri.add(data);
					      }
					 
					 myReader.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				for(String s : triggeri) {
					String temp = s + "; ";
					poruka += temp;
				}
				
				event.getChannel().sendMessage(poruka).queue();
				
			}
		break;
		
		case prefix + "s":
		case prefix + "shutdown":
			if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
				event.getMessage().addReaction("✅").queue();
				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage("Iskljucivanje bota...").queue();
				Main.jda.shutdownNow();
				System.exit(0);
			} else {
				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage("Korisnik nema prava da ovo uradi.").queue();
			}
		break;
		
		case prefix + "nick":
			if(autor.hasPermission(Permission.NICKNAME_CHANGE)) {
				event.getMessage().addReaction("✅").queue();
				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage("Menjanje nickname-a...").queue(m -> m.delete().queueAfter(2, TimeUnit.SECONDS));
				
				Boolean reset = false;
				
				if(args.length == 2) {
					event.getChannel().sendTyping().queue();
					event.getChannel().sendMessage("Resetovanje nickname-a gotovo.").queue(m -> m.delete().queueAfter(4, TimeUnit.SECONDS));
					
					reset = true;
				}
				
				if(args.length < 2) {
					event.getChannel().sendTyping().queue();
					event.getChannel().sendMessage("Losa sintaksa. Koriscenje: " + prefix + "nick @korisnik <nickname>").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
					break;
				}
				
				List<Member> mentioned = event.getMessage().getMentionedMembers();
				
				if(mentioned.size() > 1) {
					
					event.getChannel().sendTyping().queue();
					event.getChannel().sendMessage("Losa sintaksa, vise od 1 korisnik napisan. Koriscenje: " + prefix + "nick @korisnik <nickname>").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
					
					break;
				}
				
				String nick = "";
				if(reset != true) {
					if(args.length > 3) {
						for(int i = 2; i < args.length; i++) {
							nick = nick + args[i] + " ";
						}
					} else if(args.length == 3) nick = args[2];
				} 
				
				for(Member m : mentioned) {
					m.modifyNickname(nick).queue();
				}
				
			} else {
				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage("Korisnik nema prava da ovo uradi.").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
			}
		break;
		
		case prefix + "kick":
			
			if(event.getAuthor().isBot())	break;
		
			if(event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
				
				String s = "";
				
				if(args.length == 3)
					s = args[2];
				else if(args.length > 3)
					for(int i = 2; i < args.length; i++)
						s = s + args[i] + " ";
				else if(args.length < 3) {
					event.getChannel().sendMessage("Losa sintaksa. Koriscenje: " + prefix + "kick @korisnik <razlog>").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
					break;
				}
				
				List<Member> member = event.getMessage().getMentionedMembers();
				
				if(member.size() > 1) {
					event.getChannel().sendMessage("Pogresna sintaksa, vise od 1 korisnik selektovan.").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
					break;
				}
				
				TextChannel k = (TextChannel) event.getGuild().getGuildChannelById(ChannelType.TEXT, 893170494642225162l);
				k.sendMessage("Kickovan korisnik **" + args[1] + "**. Razlog: **" + s + "**.").queue();
				event.getChannel().sendMessage("Radnja izvrsena ✅").queue();

				event.getMessage().addReaction("✅").queue();
				event.getGuild().kick(member.get(0)).queue();
			}
		break;
		
		case prefix + "ping":
			event.getChannel().sendMessage("Ping!").queue();
		break;
		
		case prefix + "purge":
			if(autor.hasPermission(Permission.MESSAGE_MANAGE)) {
				if(args.length != 2) {
					event.getChannel().sendMessage("Pogresna sintaksa. Koriscenje: " + prefix + "purge <br poruka>").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
					break;
				}
			
			 	try{
		            int brojPoruka = Integer.parseInt(args[1]);
		            
		            List<Message> messages = event.getChannel().getHistory().retrievePast(brojPoruka + 1).complete();
		            event.getTextChannel().deleteMessages(messages).queue();;
		        }
		        catch (NumberFormatException ex){
		        	event.getChannel().sendMessage("Pogresna sintaksa. Koriscenje: " + prefix + "purge <br poruka>").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
		        	break;
		        }
			}
		break;
		
		case prefix + "bot_invite":
			event.getChannel().sendMessage("Mozete invite-ovati bota putem ovog linka: https://discord.com/api/oauth2/authorize?client_id=706482474200334366&permissions=8&scope=bot")
				.queue(m -> m.addReaction("😊").queueAfter(2, TimeUnit.SECONDS));
		break;
		
		}
	}
}
