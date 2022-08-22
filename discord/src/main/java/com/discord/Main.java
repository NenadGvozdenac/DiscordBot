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
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

public class Main {			

	public static JDA jda;					
	public static Komanda[] globalneKomande, gengengKomande, privatneKomande;

	static {
		globalneKomande = new Komanda[] {
			new Komanda("randomcat", "Salje sliku nasumicno izabrane macke.", false),
			new Komanda("website", "Salje vam link do sajta za ShruggoBot-a.", false),
			new Komanda("trenutnovreme", "Dobij formatirano vreme, kao i trenutno formatirano vreme.", false),
			new Komanda("ping", "Komanda za odredivanje i ispisivanje latency-ja bota.", false),
			new Komanda("info", "Komanda koja ispisuje osnovne informacije o botu.", false),
			new Komanda("say", "Komanda za govor u vase ime od strane bota.", false, new Opcija(OptionType.STRING, "izjava", "Sta zelite da kaze u vase ime", true)),
			new Komanda("analizaformule", "Komanda za ispisivanje formula za analizu.", false),
			new Komanda("help", "Komanda za ispisivanje svih komandi bota.", false),
			new Komanda("github", "Komanda za ispisivanje source-a ovog bota.", false),
			new Komanda("korisni_linkovi", "Komanda za ispisivanje korisnih linkova za RA, E2, FTN.", false),	
			new Komanda("nick", "Komanda za menjanje nickname-a korisnika.", false, new Opcija(OptionType.USER, "korisnik", "Kojeg korisnika zelite promeniti ime?", true), new Opcija(OptionType.STRING, "novoime", "Novi nickname korisnika", true)),
			new Komanda("purge", "Komanda za brisanje odredjenog broja poruka.", false, new Opcija(OptionType.INTEGER, "brojporuka", "Broj poruka za brisanje.", true), new Opcija(OptionType.CHANNEL, "kanal", "U kom kanalu zelite da brisete poruke?", false)),
			new Komanda("bot_invite", "Komanda koja vraca invite link za bota.", false),
			new Komanda("spamuj", "Komanda koja spamuje odredjenu poruku, odredjeni broj puta (obrise je posle).", false, new Opcija(OptionType.STRING, "poruka", "Ispisite poruku za spamovanje", true), new Opcija(OptionType.INTEGER, "brojporuka", "Broj poruka za spamovanje.", true)),
			new Komanda("posalji_poruku", "Komanda za komunikaciju sa glavnim developerom.", false),
			new Komanda("roast", "Komanda koja roast-uje korisnika.", false, new Opcija(OptionType.USER, "korisnik", "Kojeg korisnika zelite roast-ovati?", true)),
			new Komanda("glasanje", "Komanda za ostvarivanje glasanja.", false, new Opcija(OptionType.STRING, "naslov", "Naslov", true), new Opcija(OptionType.STRING, "poruka", "Poruka za glasanje", true), new Opcija(OptionType.INTEGER, "trajanje_sekunde", "Koliko sekundi zelite da ovo glasanje bude dugacko?", true)),
			new Komanda("ban", "Komanda za banovanje korisnika sa servera.", true, new Opcija(OptionType.USER, "korisnik", "Kojeg korisnika zelite da banujete?", true)),
			new Komanda("kick", "Komanda za kickovanje korisnika sa servera.", true, new Opcija(OptionType.USER, "korisnik", "Kojeg korisnika zelite da kickujete?", true)),
			new Komanda("timeout", "Komanda za stavljanje korisnika u timeout.", true, new Opcija(OptionType.USER, "korisnik", "Izaberite korisnika za stavljanje u timeout.", true)),
			new Komanda("napravi_kanal", "Komanda za pravljenje novog kanala.", true, new Opcija(OptionType.STRING, "kanal", "Naziv novog kanala.", true), new Opcija(OptionType.BOOLEAN, "isvoice", "Da li je kanal voice kanal?", true)),
			new Komanda("obrisi_kanal", "Komanda za brisanje vec postojeceg kanala.", true, new Opcija(OptionType.CHANNEL, "kanal", "Naziv kanala za brisanje.", true)),
			new Komanda("unmute", "Komanda za unmutovanje korisnika sa servera.", true, new Opcija(OptionType.USER, "korisnik", "Korisnik kojega zelite unmutovati.", true)),
			new Komanda("mute", "Komanda za mutovanje korisnika sa servera.", true, new Opcija(OptionType.USER, "korisnik", "Korisnik kojega zelite mutovati.", true)),
			new Komanda("izadji", "Komanda koja izbacuje bota sa servera.", true)
		};

		gengengKomande = new Komanda[]{
			new Komanda("listaj_json_rp", "Ispisuje sve dodate respond poruke.", false),
			new Komanda("obrisi_rp", "Brise respond poruku koja je izabrana.", false, new Opcija(OptionType.STRING, "trigger", "Trigger za poruku koja treba da se obrise", true)),
			new Komanda("dodaj_rp", "Dodaje novu respond poruku.", false, new Opcija(OptionType.STRING, "trigger", "Trigger za pisanje respond poruke.", true), new Opcija(OptionType.STRING, "response", "Response za pisanje respond poruke.", true)),
		};

		privatneKomande = new Komanda[]{
			new Komanda("posaljinoviupdate", "Posalji Novi Update", true),
			new Komanda("posaljiglobalnoporuku_id", "Posalji poruku preko ID.", true, new Opcija(OptionType.STRING, "id", "ID poruke za slanje", true)),
			new Komanda("shutdown", "Iskljucuje bota za odredjeno vreme", true),
			new Komanda("bitno_obavestenje", "Bitno Obavestenje za sve servere", true, new Opcija(OptionType.STRING, "izjava", "Koju poruku zelite da posaljete?", true)),
			new Komanda("napravi_roast", "Napravi novi roast .json fajl.", true)
		};
	}
	
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

		JSONRespondPoruka.formatirajPorukeUJsonFajl(globalneKomande);
		
		Logger logger = LoggerFactory.getLogger(Main.class);
		logger.info("Uspesno pokrenut bot.");
	}
}