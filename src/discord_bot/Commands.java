package discord_bot;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
	
	final static String prefix = "!";
	
	public static List<TextChannel> kanali;
	public static String kreator = "NenadG#0001";
	
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
		
		if(poruka.length() < 1)	return;
		else if(poruka.charAt(0) == prefix.charAt(0)) {
			try {
				POSALJI_KOMANDE(event);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
      
			List<String> triggeri = new ArrayList<String>();
			List<String> poruke = new ArrayList<String>();
			
			File triggers = new File("triggeri.txt");
			File messages = new File("poruke.txt");
			
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
			        if(Cooldowns.cooldown_respond == true) {
			          return;
			        }
					event.getChannel().sendTyping().queue();
					event.getChannel().sendMessage(poruke.get(i)).queue();

					Cooldowns.UKLJUCI_COOLDOWN_RESPONSE(Cooldowns.cooldown_brojno_respond_poruke);

					return;
				}
			}
		}
		
	}
	
	public void POSALJI_KOMANDE(MessageReceivedEvent event) throws IOException {
		
		String[] niz_reci = event.getMessage().getContentRaw().split(" ");
		
		TextChannel admin_logovi = null;
		
		kanali = event.getGuild().getTextChannels();
        
        for(TextChannel c : kanali) {
        	if(c.getName().equalsIgnoreCase(OtherEvents.admin_kanal)) {
        		admin_logovi = c;
        	}
        }
		
        // SWITCH CASE- ZA KOMANDE
        
		switch(niz_reci[0].toLowerCase()) {
		case prefix + "info":
			INFO_KOMANDA(event, niz_reci);
		break;
		
		case prefix + "help":
			HELP_KOMANDA(event, niz_reci);
			// POSALJI_LOG(admin_logovi, event, event.getMember());
		break;
		
		case prefix + "korisni_linkovi":
			KORISNI_LINKOVI_KOMANDA(event, niz_reci);
		break;
		
		case prefix + "s":
		case prefix + "shutdown":
			SHUTDOWN_KOMANDA(event, niz_reci);
		break;
		
		case prefix + "nick":
			NICK_KOMANDA(event, niz_reci, event.getMember());
			POSALJI_LOG(admin_logovi, event, event.getMember());
		break;
		
		case prefix + "kick":
			// KICK_KOMANDA(event, args);								// NAPRAVITI!!!!
			// POSALJI_LOG(admin_logovi, event, event.getMember());
		break;
		
		case prefix + "ping": 
			PING_KOMANDA(event, niz_reci);
		break;
		
		case prefix + "purge":
			if(PURGE_KOMANDA(event, niz_reci)) {
				POSALJI_LOG(admin_logovi, event, event.getMember());
			}
		break;
		
		case prefix + "bot_invite":
			BOT_INVITE_KOMANDA(event, niz_reci);
		break;
		
		case prefix + "create_admin_logs":
			CREATE_ADMIN_LOGS_KOMANDA(event, niz_reci);
		break;
		
		case prefix + "create_welcome":
			CREATE_WELCOME_KOMANDA(event, niz_reci);
		break;
		
		case prefix + "roast":
			ROAST_KOMANDA(event, niz_reci);
		break;
		
		case prefix + "kalendar_ispita":
			KALENDAR_ISPITA_KOMANDA(event, niz_reci);
		break;
		
		case prefix + "dodaj_respondporuku":
			DODAJ_RESPONDPORUKU_KOMANDA(event, niz_reci);
		break;
		
		case prefix + "listaj_respondporuke":
			LISTAJ_RESPONDPORUKE_KOMANDA(event, niz_reci);
		break;

	    case prefix + "spamuj":
	    	SPAMUJ_KOMANDA(event, niz_reci);
	    	POSALJI_LOG(admin_logovi, event, event.getMember());
	    break;
	    
	    case prefix + "spamuj_bot_rp":
	    	SPAMUJ_BOT_RESPONDPORUKE(event, niz_reci);
	    break;
	    
	    case prefix + "obrisi_bot_rp":
	    	OBRISI_BOT_RESPONDPORUKE(event, niz_reci);
	    break;
	    
	    case prefix + "postavi_cooldown":
	    	POSTAVI_COOLDOWN(event, niz_reci);
	    break;
		}
	}
	
	public static void POSALJI_LOG(TextChannel admin_logovi, MessageReceivedEvent event, Member autor) {
		if(admin_logovi == null) {
			return;
		}
		EmbedBuilder eb = new EmbedBuilder();
		String vreme = "<t:" + (System.currentTimeMillis() / 1000L) + ":f>";
		eb.setTitle("KORISCENJE KOMANDE!");
		eb.setDescription("Korisnik: " + autor.getAsMention());
		eb.addField("KOMANDA", event.getMessage().getContentRaw(), false);
		eb.addField("KANAL", event.getChannel().getAsMention(), false);
		eb.addField("VREME", vreme, false);
		
		eb.setColor(Color.MAGENTA);
		
		eb.setThumbnail("https://emojipedia-us.s3.amazonaws.com/source/skype/289/double-exclamation-mark_203c-fe0f.png");
		
		admin_logovi.sendMessageEmbeds(eb.build()).queue();
	}
	
	public static void SPAMUJ_BOT_RESPONDPORUKE(MessageReceivedEvent event, String[] poruka) throws FileNotFoundException {
		if(!event.getGuild().getName().toLowerCase().equals("genggengv2")) {
			return;
		}
		
		if(!event.getAuthor().getAsTag().equals(kreator)){
			event.getChannel().sendTyping().queue();
  	  		event.getChannel().sendMessage("Korisnik nema prava da ovo uradi.").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
			event.getMessage().addReaction("❌").queue();
			return;
		}
		
		if(poruka.length != 2) {
			return;
		}
		
		String tC = poruka[1];
		
		tC = tC.substring(2, 20);
		
		TextChannel kanal = event.getGuild().getTextChannelById(tC);
		
		if(kanal == null) {
			return;
		}
		
		List<String> triggeri = new ArrayList<String>();
		List<String> poruke = new ArrayList<String>();

		File triggers = new File("triggeri.txt");
		File messages = new File("poruke.txt");

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
		 
		 for(int i = 0; i < triggeri.size(); i++) {
			 kanal.sendMessage("--------------------\nPoruka: " + triggeri.get(i) + "\n" + poruke.get(i) + "\n").queue();
		 }
	
		 String vreme = "<t:" + (System.currentTimeMillis() / 1000L) + ":F>";
		 
		 kanal.sendMessage("Vreme koriscenja: " + vreme + "\nPoruke ce se nakon mesec dana poslati ponovo ovde.\n").queue();
		 
		 tR.close();
		 mR.close();
	}
	
	public static void INFO_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle("**INFORMACIJE O BOTU**");
		eb.setColor(Color.CYAN);
		String vreme = "<t:" + (System.currentTimeMillis() / 1000L) + ":f>";
		eb.setDescription("Komanda: **" + poruka[0] + "**. \nIskoriscenja od strane: " + event.getAuthor().getAsMention() + "\nVreme koriscenja: " + vreme);
		
		eb.addField("Namena", "- Discord Bot se koristi samo za zajebanciju...", false);
		eb.addField("Prikupljanje informacije", "- Discord Bot ne prikuplja informacije o korisnicima Discord servera", false);
		eb.addField("Kreator", "- Discord Bot je napravljen od strane Nenada Gvozdenca", false);
		eb.addField("Github", "- Ceo projekat se nalazi na Github, (https://github.com/NenadGvozdenac/DiscordBot)", false);
		
		eb.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Info_icon_002.svg/1200px-Info_icon_002.svg.png");
		
		event.getMessage().addReaction("✅").queue();
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}
	
	public static void HELP_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle("**KOMANDE BOTA**");
		eb.setColor(Color.CYAN);
		String vreme = "<t:" + (System.currentTimeMillis() / 1000L) + ":f>";
		eb.setDescription("Komanda: **" + poruka[0] + "**. \nIskoriscenja od strane: " + event.getAuthor().getAsMention() + "\nVreme koriscenja: " + vreme);
		
		eb.addField(prefix + "info", 					"- Pokazuje sve informacije o botu", false);
		eb.addField(prefix + "help", 					"- Pokazuje sve komande bota", false);
		eb.addField(prefix + "nick @user <nickname>", 	"- Menja nickname korisniku", false);
		eb.addField(prefix + "kick @user <razlog>", 	"- Izbacivanje korisnika, **ADMIN**", false);
		eb.addField(prefix + "purge <broj>",			"- Masivno brisanje poruka, **ADMIN**", false);
		eb.addField(prefix + "bot_invite",				"- Invite-ovanje bota u vas server", false);
		eb.addField(prefix + "korisni_linkovi",			"- Korisni linkovi za E2", false);
		eb.addField(prefix + "create_welcome",			"- Pravljenje dobrodoslice kanala", false);
		eb.addField(prefix + "create_admin_logs",		"- Pravljenje kanala za admine", false);
		eb.addField(prefix + "roast <osoba1>...<osobaN>", "- Roastovanje navedenih osoba (ili sebe)", false);
		eb.addField(prefix + "kalendar_ispita", 		"- Ispisivanje kalendara svih ispita 2021/2022 godine", false);
		eb.addField(prefix + "spamuj <poruka> <broj>", 	"- Spamovanje nekih poruka...", false);
		
		eb.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Info_icon_002.svg/1200px-Info_icon_002.svg.png");
		
		event.getMessage().addReaction("✅").queue();
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
		
		eb.clear();
		
		if(event.getGuild().getName().toLowerCase().equals("genggengv2")) {
			eb.setTitle("**RESPOND PORUKE**");
			eb.setColor(Color.CYAN);
			
			String specijalne_reci = "";
			List<String> triggeri = new ArrayList<String>();
			
			File fajl = new File("triggeri.txt");
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
				specijalne_reci += temp;
			}
			
			eb.addField(prefix + "dodaj_respondporuku", "Dodaj poruku na odgovaranje", false);
			eb.addField(prefix + "listaj_respondporuke", "Listaj poruke za odgovaranje", false);
			eb.addField(prefix + "obrisi_bot_rp", "Obrisi sve poruke za odgovaranja", false);
			eb.addField(prefix + "spamuj_bot_rp <#kanal>", "Listaj sve respond poruke u kanal", false);
			eb.addField("Cooldown za respond poruke", Cooldowns.cooldown_brojno_respond_poruke.toString(), false);
			eb.addField(prefix + "postavi_cooldown <int>", "Postavljanje cooldown-a ne odredjenu vrednost", false);
			
			eb.addField("Specijalne reci za genggengv2", specijalne_reci, false);
			
			eb.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Info_icon_002.svg/1200px-Info_icon_002.svg.png");
		
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
		}
	}
	
	public static void KORISNI_LINKOVI_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		EmbedBuilder eb = new EmbedBuilder();
		
		event.getMessage().addReaction("✅").queue();
		eb.setTitle("LINKOVI ZA E2");
		eb.setColor(Color.CYAN);
		
		String vreme = "<t:" + (System.currentTimeMillis() / 1000L) + ":f>";
		eb.setDescription("Komanda: **" + poruka[0] + "**. \nIskoriscenja od strane: " + event.getAuthor().getAsMention() + "\nVreme koriscenja: " + vreme);
		
		eb.addField("Dropbox Survival", 				"https://www.dropbox.com/sh/mkqwwg655ou36n8/AADv4xXVBdRgX63inKrmAegia?dl=0", false);
		eb.addField("Raspored Letnjeg Semestra", "http://www.ftn.uns.ac.rs/n893884880/racunarstvo-i-automatika", false);
		eb.addField("Studenski web servis", 			"https://ssluzba.ftn.uns.ac.rs/ssluzbasp/", false);
		eb.addField("Matematička Analiza 1", 			"https://manaliza1.wordpress.com/", false);
		eb.addField("Algebra", 							"http://imft.ftn.uns.ac.rs/~rade/elektro_diskretna_e2.html", false);
		eb.addField("Repozitorijum",					"http://www.acs.uns.ac.rs/sr/repozitorijum", false);
		eb.addField("OET",								"http://www.ktet.ftn.uns.ac.rs/index.php?option=com_content&task=view&id=31&Itemid", false);
		eb.addField("Fizika",							"http://www.ftn.uns.ac.rs/1321325633/racunarstvo-i-automatika", false);
		
		eb.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Info_icon_002.svg/1200px-Info_icon_002.svg.png");
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}
	
	public static void SHUTDOWN_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		if(event.getAuthor().getAsTag().equals(kreator)){
			event.getMessage().addReaction("✅").queue();
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage("Iskljucivanje bota...").queue();
			Main.jda.shutdownNow();
			System.exit(0);
		} else {
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage("Korisnik nema prava da ovo uradi.").queue();
		}
	}
	
	public static void NICK_KOMANDA(MessageReceivedEvent event, String[] poruka, Member autor) {
		try {
			  if(autor.hasPermission(Permission.NICKNAME_CHANGE)) {
				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage("Menjanje nickname-a...").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
				
				Boolean reset = false;
				
				int duzina = 0;
				int space = 0;
				
				for(int i = 2; i < poruka.length; i++) {
					duzina += poruka[i].length();
					space++;
				}
				
				if(duzina + space > 32) {
					event.getChannel().sendTyping().queue();
					event.getChannel().sendMessage("Predugacki nickname! \nGledajte da ne bude duzi od 32 karaktera...").queue(m -> m.delete().queueAfter(6, TimeUnit.SECONDS));
					
					return;
				}
				
				if(poruka.length == 2) {
					event.getChannel().sendTyping().queue();
					event.getChannel().sendMessage("Resetovanje nickname-a gotovo.").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
					
					reset = true;
				}
				
				if(poruka.length < 2) {
					event.getChannel().sendTyping().queue();
					event.getChannel().sendMessage("Losa sintaksa. Koriscenje: " + prefix + "nick @korisnik <nickname>").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
					return;
				}
				
				List<Member> mentioned = event.getMessage().getMentionedMembers();
				
				if(mentioned.size() > 1) {
					
					event.getChannel().sendTyping().queue();
					event.getChannel().sendMessage("Losa sintaksa, vise od 1 korisnik napisan. Koriscenje: " + prefix + "nick @korisnik <nickname>").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
					
					return;
				}
				
				String nick = "";
				if(reset != true) {
					if(poruka.length > 3) {
						for(int i = 2; i < poruka.length; i++) {
							nick = nick + poruka[i] + " ";
						}
					} else if(poruka.length == 3) nick = poruka[2];
				} 
				
				for(Member m : mentioned) {
					m.modifyNickname(nick).queue();
				}
					event.getMessage().addReaction("✅").queue();
					return;
			} else {
				throw new net.dv8tion.jda.api.exceptions.HierarchyException("#27836");
			}   

        } catch(net.dv8tion.jda.api.exceptions.HierarchyException e) {
      	  		event.getChannel().sendTyping().queue();
      	  		event.getChannel().sendMessage("Korisnik nema prava da ovo uradi.").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
				event.getMessage().addReaction("❌").queue();
				
				return;
        }
		
	}
	
	
	public static void KICK_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		if(event.getAuthor().isBot())	return;
		
		if(event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
			
			String s = "";
			
			if(poruka.length == 3)
				s = poruka[2];
			else if(poruka.length > 3)
				for(int i = 2; i < poruka.length; i++)
					s = s + poruka[i] + " ";
			else if(poruka.length < 3) {
				event.getChannel().sendMessage("Losa sintaksa. Koriscenje: " + prefix + "kick @korisnik <razlog>").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
				return;
			}
			
			List<Member> member = event.getMessage().getMentionedMembers();
			
			if(member.size() > 1) {
				event.getChannel().sendMessage("Pogresna sintaksa, vise od 1 korisnik selektovan.").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
				return;
			}
			
			TextChannel k = (TextChannel) event.getGuild().getGuildChannelById(ChannelType.TEXT, 893170494642225162l);
			k.sendMessage("Kickovan korisnik **" + poruka[1] + "**. Razlog: **" + s + "**.").queue();
			event.getChannel().sendMessage("Radnja izvrsena ✅").queue();

			event.getMessage().addReaction("✅").queue();
			event.getGuild().kick(member.get(0)).queue();
		}
	}
	
	
	public static void PING_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		
		long start = System.currentTimeMillis();
		event.getChannel().sendMessage("PING!").queue();
		long end = System.currentTimeMillis();
		event.getChannel().sendMessage("Vreme: *" + (end - start) + " ms*").queue();
	}
	
	
	public static Boolean PURGE_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		int brojPoruka = 0;
	
		if(poruka.length < 2 || poruka.length > 3) {
			event.getChannel().sendMessage("Pogresna sintaksa. Koriscenje: " + prefix + "purge <br poruka> ILI " + prefix + "purge <br poruka> <kanal>").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
			return false;
		}
	
	 	try{
            brojPoruka = Integer.parseInt(poruka[1]);
            
            if(poruka.length == 2) {
	            List<Message> messages = event.getChannel().getHistory().retrievePast(brojPoruka + 1).complete();
	            event.getTextChannel().deleteMessages(messages).queue();
            } else {
            	
            	String tC = poruka[2];
        		
        		tC = tC.substring(2, 20);
        		
        		TextChannel kanal = event.getGuild().getTextChannelById(tC);
        		
        		if(kanal == null) {
        			event.getChannel().sendMessage("Pogresna sintaksa. Koriscenje: " + prefix + "purge <br poruka> ILI " + prefix + "purge <br poruka> <kanal>").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
        			return false;
        		}
            	
            	List<Message> messages = kanal.getHistory().retrievePast(brojPoruka + 1).complete();
	            kanal.deleteMessages(messages).queue();
	            
	            event.getMessage().addReaction("✅").queue();
	            event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
            }
            
            return true;
        }
        catch (NumberFormatException ex){
        	event.getChannel().sendMessage("Pogresna sintaksa. Koriscenje: " + prefix + "purge <br poruka> ILI " + prefix + "purge <br poruka> <kanal>").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
			return false;
        }
	}
	
	
	public static void BOT_INVITE_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		event.getChannel().sendMessage("Mozete invite-ovati bota putem ovog linka: https://discord.com/api/oauth2/authorize?client_id=706482474200334366&permissions=8&scope=bot")
		.queue(m -> m.addReaction("😊").queueAfter(2, TimeUnit.SECONDS));
	}
	
	
	public static void CREATE_ADMIN_LOGS_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		if(event.getMember().hasPermission(Permission.ADMINISTRATOR) || event.getAuthor().getAsTag().equals("NenadG#0001")) {
			
			for(TextChannel channel : kanali) {
				if(channel.getName().equalsIgnoreCase(OtherEvents.admin_kanal)) {
					event.getMessage().addReaction("❌").queue();
					event.getChannel().sendTyping().queue();
					event.getChannel().sendMessage("Kanal vec postoji!").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
					return;
				}
			}
			
			event.getMessage().addReaction("✅").queue();
				
			EnumSet<Permission> permissions = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);
			
			event.getGuild().createCategory(OtherEvents.admin_kategorija).queue(
					m -> m.createTextChannel(OtherEvents.admin_kanal)
					.setTopic("Admin kanal za slanje logova")
					.setPosition(0)
					.addPermissionOverride(event.getGuild().getPublicRole(), null, permissions)
					.queue(
							n -> n.sendMessage("❗ Kanal napravljen za slanje admin logova.").queueAfter(2, TimeUnit.SECONDS)
						  )
				);
		} else {
			event.getMessage().addReaction("❌").queue();
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage("Korisnik nema prava da ovo uradi.").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
		}
	}
	
	
	public static void CREATE_WELCOME_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		if(event.getMember().hasPermission(Permission.ADMINISTRATOR) || event.getAuthor().getAsTag().equals("NenadG#0001")) {
			
			for(TextChannel channel : kanali) {
				if(channel.getName().equalsIgnoreCase(OtherEvents.dobrodoslica_kanal)) {
					event.getMessage().addReaction("❌").queue();
					event.getChannel().sendTyping().queue();
					event.getChannel().sendMessage("Kanal vec postoji!").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
					return;
				}
			}
			
			event.getMessage().addReaction("✅").queue();
			
			EnumSet<Permission> dobrodoslica_permissions = EnumSet.of(Permission.MESSAGE_SEND);
			
			event.getGuild().createCategory(OtherEvents.dobrodoslica_kategorija).setPosition(0).queue(
					m -> m.createTextChannel(OtherEvents.dobrodoslica_kanal)
					.setTopic("Kanal za slanje toplih poruka dobrodoslice za nove korisnike")
					.setPosition(0)
					.addPermissionOverride(event.getGuild().getPublicRole(), null, dobrodoslica_permissions)
					.queue (
							n -> n.sendMessage("❗ Kanal napravljen za dobrodoslicu novih ljudi.").queueAfter(2, TimeUnit.SECONDS)
						   )
				);
			
		} else {
			event.getMessage().addReaction("❌").queue();
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage("Korisnik nema prava da ovo uradi.").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
		}
	}
	
	
	public static void ROAST_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		int brojac = 0;
		File messages = new File("roasts.txt");
		List<String> poruke = new ArrayList<String>();
		
		try {
			Scanner mR = new Scanner(messages);
			 
			 while(mR.hasNextLine()) {
				 String data = mR.nextLine();
				 brojac++;
				 poruke.add(data);
			 }
			 mR.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Integer random_int = 0 + (int)(Math.random() * ((brojac) + 1));
		
		String poruka_za_slanje = "";
		
		if(poruka.length == 1) {
			poruka_za_slanje = event.getMessage().getAuthor().getAsMention() + ": " + poruke.get(random_int);
		} else if(poruka.length > 1){
			List<Member> mentioned = event.getMessage().getMentionedMembers();
			for(Member m : mentioned) {
				poruka_za_slanje += m.getAsMention();
			}
			
			poruka_za_slanje += ": " + poruke.get(random_int);
		}
		
		event.getChannel().sendMessage(poruka_za_slanje).queue(m -> m.addReaction("🍀").queueAfter(2, TimeUnit.SECONDS));
	}
	
	public static void KALENDAR_ISPITA_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle("KALENDAR ISPITA 2021/2022");
		eb.setColor(Color.CYAN);
		String vreme = "<t:" + (System.currentTimeMillis() / 1000L) + ":f>";
		eb.setDescription("Komanda: **" + poruka[0] + "**. \nIskoriscenja od strane: " + event.getAuthor().getAsMention() + "\nVreme koriscenja: " + vreme);
		
		eb.addField("Osnove Elektrotehnike", "● 1. kolokvijum **-** 7. maj u 11:00", false);
		eb.addField("Fizika", "● 1. kolokvijum **-** 30. april u 14:30\n● 2. kolokvijum **-** 11. juni u 11:00", false);
		eb.addField("Arhitektura", "● 1. kolokvijum (T12) **-** 5. april - 8. april\n● 2. kolokvijum (T34, PI1) **-** 3. maj - 6. maj\n● 3. kolokvijum (SOV, PI2) **-** 31. maj - 3. jun", false);
		
		eb.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Info_icon_002.svg/1200px-Info_icon_002.svg.png");
		
		eb.addBlankField(true);
		eb.addField("Raspored svih ispita", "Raspored svih ispita se nalazi na slici ispod.", false);
		
		eb.setImage("https://cdn.discordapp.com/attachments/864613055068635136/961591366322888714/unknown.png");
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}
	
	public static void DODAJ_RESPONDPORUKU_KOMANDA(MessageReceivedEvent event, String[] poruka) throws IOException {
		if(!event.getGuild().getName().toLowerCase().equals("genggengv2")) {
			return;
		}
		
		if(poruka.length <= 2) {
			event.getChannel().sendMessage("Pogresno koriscenje.").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
			return;
		}
		
		if(poruka[poruka.length - 1].contains("png") || poruka[poruka.length - 1].contains("jpg") || poruka[poruka.length - 1].contains("gif")) {
			
			File triggeri = new File("triggeri.txt");
			File poruke = new File("poruke.txt");
			
			FileWriter wr_triggeri = new FileWriter(triggeri, true);
			FileWriter wr_poruke = new FileWriter(poruke, true);
			
			String trigger = "";
			
			for(int i = 1; i < poruka.length - 2; i++) {
				trigger += poruka[i];
				trigger += " ";
			}
			
			trigger += poruka[poruka.length - 2];
			
			BufferedWriter b = new BufferedWriter(wr_triggeri);
			PrintWriter p = new PrintWriter(b);
			
			trigger = trigger.toLowerCase();
			
			p.println(trigger);
			
			b.close();
			p.close();
			
			b = new BufferedWriter(wr_poruke);
			p = new PrintWriter(b);
			
			p.println(poruka[poruka.length - 1]);
			
			b.close();
			p.close();
			
			wr_triggeri.close();
			wr_poruke.close();
			
			event.getMessage().addReaction("✅").queue();
		} else {
			event.getChannel().sendMessage("Pogresno koriscenje. Krajnji argument mora imati link do slike.").queue(m -> m.delete().queueAfter(10, TimeUnit.SECONDS));
			return;
		}
	}
	
	public static void LISTAJ_RESPONDPORUKE_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		if(!event.getGuild().getName().toLowerCase().equals("genggengv2")) {
			return;
		}
		
		File triggers = new File("triggeri.txt");
		File messages = new File("poruke.txt");
		
		event.getChannel().sendMessage("Triggeri").addFile(triggers).queue();
		event.getChannel().sendMessage("Poruke").addFile(messages).queue();
	}

	public static void SPAMUJ_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		
			int broj_poruka;
			
			if(Cooldowns.cooldown_spamovanje == true) {
				event.getMessage().addReaction("❌").queue();
				return;
			}
			
			try {
				broj_poruka = Integer.parseInt(poruka[poruka.length - 1]);
			} catch(java.lang.NumberFormatException e) {
				event.getChannel().sendMessage("Pogresno koriscenje. Krajnji argument mora biti broj.").queue(m -> m.delete().queueAfter(10, TimeUnit.SECONDS));
				event.getMessage().addReaction("❌").queue();
				return;
			}
	
		    if(broj_poruka > 20) {
		      broj_poruka = 20;
		    }
	
			String poruka_za_slanje = "";
			
			for(int i = 1; i < poruka.length - 2; i++) {
				poruka_za_slanje += poruka[i];
				poruka_za_slanje += " ";
			}
			
			List<Member> mentioned = event.getMessage().getMentionedMembers();
			
			if(!mentioned.isEmpty()) {
				broj_poruka = 3;
			}
			
			poruka_za_slanje += poruka[poruka.length - 2];
	    
			for(int i = 0; i < broj_poruka; i++) {
				event.getChannel().sendMessage(poruka_za_slanje).queue(m -> m.delete().queueAfter(15, TimeUnit.SECONDS));
			}
			
			event.getMessage().delete().queue();
	    
			Cooldowns.UKLJUCI_COOLDOWN(15);
	}
	
	public static void OBRISI_BOT_RESPONDPORUKE(MessageReceivedEvent event, String[] poruka) {
		if(!event.getGuild().getName().toLowerCase().equals("genggengv2")) {
			return;
		}
		
		if(Cooldowns.cooldown_brisanje_rp == true) {
			return;
		}
		
		if(!event.getAuthor().getAsTag().equals(kreator)){
			event.getChannel().sendTyping().queue();
  	  		event.getChannel().sendMessage("Korisnik nema prava da ovo uradi.").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
			event.getMessage().addReaction("❌").queue();
			return;
		}
		
		event.getChannel().sendMessage("Da li ste sigurni da zelite da obrisete sve respond poruke za server **" + event.getGuild().getName() + "**?\nSacekajte 15 sekundi nakon odgovora.\nDA za brisanje. Ovo ne moze biti prepravljeno!!").queue(n -> n.delete().queueAfter(30, TimeUnit.SECONDS));
		
		Cooldowns.UKLJUCI_COOLDOWN_ZA_BRISANJE_RP(event);
	}
	
	public static void POSTAVI_COOLDOWN(MessageReceivedEvent event, String[] poruka) {
		if(!event.getGuild().getName().toLowerCase().equals("genggengv2")) {
			return;
		}
		
		if(poruka.length != 2) {
			return;
		}
		
		Integer vrednost;
		
		vrednost = Integer.parseInt(poruka[1]);

    if(vrednost < 0 || vrednost > 120) {
      event.getChannel().sendMessage("Budite realisticni sa vremenom...").queue();
      return;
    }
		Cooldowns.cooldown_brojno_respond_poruke = vrednost;
		
		event.getChannel().sendMessage("Postavljen cooldown na " + vrednost.toString() + " sekundi.").queue();
	}
    
  }