package discord_bot;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
					event.getChannel().sendTyping().queue();
					event.getChannel().sendMessage(poruke.get(i)).
						queue();
					
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
		}
	}
	
	public static void POSALJI_LOG(TextChannel admin_logovi, MessageReceivedEvent event, Member autor) {
		if(admin_logovi == null) {
			return;
		}
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Koriscenje komande");
		eb.setDescription("Korisnik: " + autor.getAsMention());
		eb.addField("KOMANDA", event.getMessage().getContentRaw(), false);
		eb.addField("VREME", dtf.format(now), false);
		
		eb.setColor(Color.MAGENTA);
		
		eb.setThumbnail("https://emojipedia-us.s3.amazonaws.com/source/skype/289/double-exclamation-mark_203c-fe0f.png");
		
		admin_logovi.sendMessageEmbeds(eb.build()).queue();
	}
	
	public static void INFO_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle("**INFORMACIJE O BOTU**");
		eb.setColor(Color.CYAN);
		eb.setDescription("Komanda: **" + poruka[0] + "**. \nIskoriscenja od strane: " + event.getAuthor().getAsMention());
		
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
		eb.setDescription("Komanda: **" + poruka[0] + "**. \nIskoriscenja od strane: " + event.getAuthor().getAsMention());
		
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
		
		eb.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Info_icon_002.svg/1200px-Info_icon_002.svg.png");
		
		if(event.getGuild().getName().toLowerCase().equals("genggengv2")) {
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
			
			eb.addField("Dodaj poruku na odgovaranje", prefix + "dodaj_respondporuku", false);
			eb.addField("Listaj poruke za odgovaranje", prefix + "listaj_respondporuke", false);
			
			eb.addField("Specijalne reci za genggengv2", specijalne_reci, false);
		}
		
		event.getMessage().addReaction("✅").queue();
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}
	
	public static void KORISNI_LINKOVI_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		EmbedBuilder eb = new EmbedBuilder();
		
		event.getMessage().addReaction("✅").queue();
		
		eb.setTitle("LINKOVI ZA E2");
		eb.setColor(Color.CYAN);
		eb.setDescription("Komanda: **" + poruka[0] + "**. \nIskoriscenja od strane: " + event.getAuthor().getAsMention());
		
		eb.addField("Dropbox Survival", 				"http://bit.ly/E2surv", false);
		eb.addField("Studenski web servis", 			"https://ssluzba.ftn.uns.ac.rs/ssluzbasp/", false);
		eb.addField("Matematička Analiza 1", 			"https://manaliza1.wordpress.com/", false);
		eb.addField("Algebra", 							"http://imft.ftn.uns.ac.rs/~rade/elektro_diskretna_e2.html", false);
		eb.addField("PJiSP Vezbe", 						"https://github.com/randomCharacter/PJISP", false);
		eb.addField("Repozitorijum",					"http://www.acs.uns.ac.rs/sr/repozitorijum", false);
		eb.addField("Engleski jezik",					"http://branalicen.wix.com/engleski-obavestenja", false);
		eb.addField("OET",								"http://www.ktet.ftn.uns.ac.rs/index.php?option=com_content&task=view&id=31&Itemid", false);
		eb.addField("Fizika",							"http://www.ftn.uns.ac.rs/1321325633/racunarstvo-i-automatika", false);
		
		eb.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Info_icon_002.svg/1200px-Info_icon_002.svg.png");
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}
	
	public static void SHUTDOWN_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		if(event.getAuthor().getAsTag().equals("NenadG#0001")){
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
				event.getChannel().sendMessage("Menjanje nickname-a...").queue(m -> m.delete().queueAfter(2, TimeUnit.SECONDS));
				
				Boolean reset = false;
				
				if(poruka.length == 2) {
					event.getChannel().sendTyping().queue();
					event.getChannel().sendMessage("Resetovanje nickname-a gotovo.").queue(m -> m.delete().queueAfter(4, TimeUnit.SECONDS));
					
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
		event.getChannel().sendMessage("Pong!").queue();
		long end = System.currentTimeMillis();
		event.getChannel().sendMessage("Time:*" + (end - start) + " ms*").queue();
	}
	
	
	public static Boolean PURGE_KOMANDA(MessageReceivedEvent event, String[] poruka) {
		int brojPoruka = 0;
	
		if(poruka.length != 2) {
			event.getChannel().sendMessage("Pogresna sintaksa. Koriscenje: " + prefix + "purge <br poruka>").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
			return false;
		}
	
	 	try{
            brojPoruka = Integer.parseInt(poruka[1]);
            
            List<Message> messages = event.getChannel().getHistory().retrievePast(brojPoruka + 1).complete();
            event.getTextChannel().deleteMessages(messages).queue();
            
            return true;
        }
        catch (NumberFormatException ex){
        	event.getChannel().sendMessage("Pogresna sintaksa. Koriscenje: " + prefix + "purge <br poruka>").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS));
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
		
		System.out.print(random_int);
		
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
		eb.setDescription("Komanda: **" + poruka[0] + "**. \nIskoriscenja od strane: " + event.getAuthor().getAsMention());
		
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
		
		if(poruka[poruka.length - 1].contains(".png") || poruka[poruka.length - 1].contains(".jpg") || poruka[poruka.length - 1].contains(".gif")) {
			
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
			p.print("\n" + trigger);
			
			b.close();
			p.close();
			
			b = new BufferedWriter(wr_poruke);
			p = new PrintWriter(b);
			
			p.print("\n" + poruka[poruka.length - 1]);
			
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
		
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle("**AUTORESPOND PORUKE**");
		eb.setColor(Color.CYAN);
		eb.setDescription("Komanda: **" + poruka[0] + "**. \nIskoriscenja od strane: " + event.getAuthor().getAsMention());
		
		eb.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Info_icon_002.svg/1200px-Info_icon_002.svg.png");
		
		for(int i = 0; i < triggeri.size(); i++) {
			eb.addField(triggeri.get(i), poruke.get(i), false);
		}
		
		event.getMessage().addReaction("✅").queue();
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}
	
}