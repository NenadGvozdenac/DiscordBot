package com.discord;

import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {			

	public static JDA jda;					

	public static void main(String[] args) throws LoginException, InterruptedException {		
		
	   	try (Reader reader = new FileReader(new File(new File("jsonFolder"), "token.json"))) {

			Gson gson = new GsonBuilder()
						.disableHtmlEscaping()
						.setFieldNamingStrategy(FieldNamingPolicy.UPPER_CAMEL_CASE)
						.setPrettyPrinting()
						.serializeNulls()
						.create();

			Type founderTypeSet = new TypeToken<Token>(){}.getType();
			Token token = gson.fromJson(reader, founderTypeSet);

			jda = JDABuilder.createLight(token.getTokenValue(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS).addEventListeners(
				new Commands(),
				new OtherEvents(),
				new SlashCommands(),
				new Notifications(),
				new ModalCommands(),
				new SelectMenuCommands(),
				new HelpButton()
			).build();

			Logger logger = LoggerFactory.getLogger(Main.class);
			logger.info("Uzet token iz OS: " + token);

		} catch (IOException e) {
			
			Map<String, String> mapa = System.getenv();

			String token = null;

			if(mapa.containsKey("token")) {
				token = mapa.get("token");
			}

			jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS).addEventListeners(
				new Commands(),
				new OtherEvents(),
				new SlashCommands(),
				new Notifications(),
				new ModalCommands(),
				new SelectMenuCommands(),
				new HelpButton()
			).build();

			Logger logger = LoggerFactory.getLogger(Main.class);

			logger.info("Uzet token iz OS: " + token);
		}	
	
	    Thread threadAktivnosti = new Thread(new Runnable() {

			public int currentIndex = 0;		
			
			public String[] messages = {
					"https://bit.ly/shruggo",
					"Sada sa slash komandama!",
					"Novi updejt!",
					"Novi sajt!",
					"/help",
					"Koristi / za komande",
					"by NenadG"
			};	
			
			@Override
			public void run() {
				new Timer().schedule(new TimerTask() {	
					public void run() {	
					    jda.getPresence().setActivity(Activity.playing(messages[currentIndex]));	
					    currentIndex=(currentIndex+1)%messages.length;  

					}},0,30_000);	
				
				System.out.println("Poceo thread aktivnosti!");
			}
			
		});
		
		Thread threadServera = HttpSrv.getServerThread();
		
		Thread threadStatusa = new Thread(new Runnable() {

			public int currentIndex = 0;
			
			public OnlineStatus[] statusi = {
					// OnlineStatus.DO_NOT_DISTURB,
					// OnlineStatus.IDLE,
					OnlineStatus.ONLINE
			};
			
			@Override
			public void run() {
				new Timer().schedule(new TimerTask() {	
					public void run() {	
					    jda.getPresence().setStatus(statusi[currentIndex]);	
					    
					    currentIndex=(currentIndex+1)%statusi.length;  
					    
					}},0,50_000);	
				
				System.out.println("Poceo thread statusa!");
			}
		});

		threadServera.run();
		threadAktivnosti.run();
		threadStatusa.run();
		
		jda.awaitReady();

		Komanda[] komande = {
				new Komanda("posaljinoviupdate", "Posalji Novi Update", true),
				new Komanda("posaljiglobalnoporuku_id", "Posalji poruku preko ID.", true, new Opcija(OptionType.STRING, "id", "ID poruke za slanje", true)),
				new Komanda("trenutnovreme", "Dobij formatirano trenutno vreme.", false),
				new Komanda("napravi_json_rp", "Napravi JSON respond poruke", true),
				new Komanda("listaj_json_rp", "Listaj sve JSON respond poruke", false),
				new Komanda("obrisi_rp", "Obrisi respond poruku za bota", true, new Opcija(OptionType.STRING, "trigger", "Trigger za poruku koja treba da se obrise", true)),
				new Komanda("dodaj_rp", "Dodaj novu respond poruku", true, new Opcija(OptionType.STRING, "trigger", "Trigger za pisanje respond poruke.", true), new Opcija(OptionType.STRING, "response", "Response za pisanje respond poruke.", true)),
				new Komanda("ban", "Banuj korisnika sa servera", true, new Opcija(OptionType.USER, "korisnik", "Kojeg korisnika zelite da banujete?", true)),
				new Komanda("kick", "Kickuj korisnika sa servera", true, new Opcija(OptionType.USER, "korisnik", "Kojeg korisnika zelite da kickujete?", true)),
				new Komanda("analizaformule", "Ispisuje formule iz analize"),
				new Komanda("timeout", "Stavi neku osobu u timeout", true, new Opcija(OptionType.USER, "korisnik", "Izaberite korisnika za stavljanje u timeout.", true)),
				new Komanda("ping", "Odredjivanje pinga bota"),
				new Komanda("info", "Informacije o komandama bota"),
				new Komanda("say", "Govori u vase ime", new Opcija(OptionType.STRING, "izjava", "Sta zelite da kaze u vase ime", true)),
				new Komanda("shutdown", "Iskljucuje bota za odredjeno vreme", true),
				new Komanda("izadji", "Izlazi iz servera", true),
				new Komanda("help", "Registrovane komande bota"),
				new Komanda("github", "Vraca nesin github"),
				new Komanda("korisni_linkovi", "Korisni linkovi za FTN E2 smer, Racunarstvo i Automatika."),	
				new Komanda("nick", "Menjanje nickname korisnika", new Opcija(OptionType.USER, "korisnik", "Kojeg korisnika zelite promeniti ime?", true), new Opcija(OptionType.STRING, "novoime", "Novi nickname korisnika", true)),
				new Komanda("purge", "Brisanje broja poruka", new Opcija(OptionType.INTEGER, "brojporuka", "Broj poruka za brisanje.", true), new Opcija(OptionType.CHANNEL, "kanal", "U kom kanalu zelite da brisete poruke?", false)),
				new Komanda("bot_invite", "Invite-ovanje bota u server"),
				new Komanda("roast", "Roast-ovanje osobe", new Opcija(OptionType.USER, "korisnik", "Kojeg korisnika zelite roast-ovati?", true)),
				new Komanda("bitno_obavestenje", "Bitno Obavestenje za sve servere", true, new Opcija(OptionType.STRING, "izjava", "Koju poruku zelite da posaljete?", true)),
				new Komanda("spamuj", "Spamovanje poruke", new Opcija(OptionType.STRING, "poruka", "Ispisite poruku za spamovanje", true), new Opcija(OptionType.INTEGER, "brojporuka", "Broj poruka za spamovanje.", true)),
				new Komanda("glasanje", "Glasanje u kanalu", new Opcija(OptionType.STRING, "naslov", "Naslov", true), new Opcija(OptionType.STRING, "poruka", "Poruka za glasanje", true), new Opcija(OptionType.INTEGER, "trajanje_sekunde", "Koliko sekundi zelite da ovo glasanje bude dugacko?", true)),
				new Komanda("napravi_kanal", "Pravljenje novog kanala", true, new Opcija(OptionType.STRING, "kanal", "Naziv novog kanala.", true), new Opcija(OptionType.BOOLEAN, "isvoice", "Da li je kanal voice kanal?", true)),
				new Komanda("obrisi_kanal", "Brisanje kanala", true, new Opcija(OptionType.CHANNEL, "kanal", "Naziv kanala za brisanje.", true)),
				new Komanda("posalji_poruku", "Salje poruku glavnom developeru")
		};
		
		// DODAJ_KOMANDE_U_TEST_SERVER(jda.getGuildById("945790514605727755"), komande);
		// DODAJ_KOMANDE_GLOBALNO(jda, komande);

		JSONRespondPoruka.formatirajPorukeUJsonFajl(komande);
		
		Logger logger = LoggerFactory.getLogger(Main.class);
		logger.info("Uspesno pokrenut bot.");
	}

	private static void DODAJ_KOMANDE_GLOBALNO(JDA jda2, Komanda[] komande) {

		List<Komanda> listaKomandiZaNeDodavanje = new ArrayList<>();
		List<Komanda> listaZaDodati = new ArrayList<>();

		for(Command c : jda2.retrieveCommands().complete()) {
			for(Komanda c1 : Arrays.asList(komande)) {
				if(!c1.getNaziv().equals(c.getName())) {
					continue;
				} else {
					listaKomandiZaNeDodavanje.add(c1);
					break;
				}
			}
		}

		for(Komanda c : Arrays.asList(komande)) {
			if(listaKomandiZaNeDodavanje.contains(c)) {
				continue;
			} else {
				listaZaDodati.add(c);
			}
		}

		for(Komanda c : listaZaDodati) {
			c.dodajKomanduGlobalno(jda);
			System.out.println("Dodata komanda `/" + c.getNaziv() + "` globalno.");
		}
	}

	private static void DODAJ_KOMANDE_U_TEST_SERVER(Guild guildById, Komanda[] komande) {

		List<Komanda> listaKomandiZaNeDodavanje = new ArrayList<>();
		List<Komanda> listaZaDodati = new ArrayList<>();

		for(Command c : guildById.retrieveCommands().complete()) {
			for(Komanda c1 : Arrays.asList(komande)) {
				if(!c1.getNaziv().equals(c.getName())) {
					continue;
				} else {
					listaKomandiZaNeDodavanje.add(c1);
					break;
				}
			}
		}

		for(Komanda c : Arrays.asList(komande)) {
			if(listaKomandiZaNeDodavanje.contains(c)) {
				continue;
			} else {
				listaZaDodati.add(c);
			}
		}

		for(Komanda c : listaZaDodati) {
			c.dodajKomanduGlobalno(jda);
			System.out.println("Dodata komanda u server `" + guildById.getName() + "`, zvana `/" + c.getNaziv() + "`. (nije globalno).");
		}
	}
}