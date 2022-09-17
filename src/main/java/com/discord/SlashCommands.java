package com.discord;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.internal.utils.PermissionUtil;

public class SlashCommands extends ListenerAdapter {	
	
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        
		if(event.getGuild() == null) {
			return;
		}
		
		LOGUJ_KOMANDU(event, event.getChannel(), event.getGuild(), event.getMember(), event.getCommandString());

		switch(event.getName()) {
			case "shutdown":
				SHUTDOWN_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "say":
				RECI_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "info":
				INFO_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "ping": 
				PING_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "izadji":
				IZADJI_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "preimenuj_kanal":
				PREIMENUJ_KANAL_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "help":
				HELP_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "korisni_linkovi":
				KORISNI_LINKOVI_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "nick":
				NICK_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "purge":
				PURGE_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "bot_invite":
				BOT_INVITE_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "roast":
				ROAST_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;

			case "napravi_roast":
				NAPRAVI_ROAST_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "bitno_obavestenje":
				BITNO_OBAVESTENJE_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "spamuj":
				SPAMUJ_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "glasanje":
				GLASANJE_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "napravi_kanal":
				NAPRAVI_KANAL_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "obrisi_kanal":
				OBRISI_KANAL_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "github":
				GITHUB_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "posalji_poruku":
				POSALJI_TIKET(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;
			
			case "ban":
				BAN_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;

			case "kick":
				KICK_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;

			case "timeout":
				TIMEOUT_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;

			case "analizaformule":
				ANALIZA_FORMULE_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;

			case "napravi_json_rp":
				NAPRAVI_JSON_RESPOND_PORUKE(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;

			case "listaj_json_rp":
				LISTAJ_JSON_RESPOND_PORUKE(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;

			case "obrisi_rp":
				OBRISI_RESPOND_PORUKU_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;

			case "dodaj_rp":
				DODAJ_RESPOND_PORUKU_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;

			case "trenutnovreme":
				TRENUTNO_VREME_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;

			case "posaljiglobalnoporuku_id":
				POSALJI_GLOBALNO_PORUKU_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;

			case "posaljinoviupdate":
				POSALJI_NOVI_UPDATE_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;

			case "website":
				POSALJI_WEBSITE_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;

			case "unmute":
				UNMUTE_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;

			case "algebra":
				ALGEBRA_OCENA(event);
			break;

			case "randomcat":
				RANDOM_CAT_KOMANDA(event);
				// ISKLJUCENA_KOMANDA_ODGOVORI(event);
			break;

			default:
				event.deferReply(true).queue();
				InteractionHook hook = event.getHook();
				
				hook.sendMessage("Ne mogu vam to sada uraditi...").queue();
		}
    }

	private void ALGEBRA_OCENA(@NotNull SlashCommandInteractionEvent event) {

		event.deferReply(false).queue();

		Double bodovi1 = event.getOption("bodovi1").getAsDouble();
		Double bodovi2 = event.getOption("bodovi2").getAsDouble();
		Double bodovi3 = event.getOption("bodovi3").getAsDouble();
		Double bodovi4 = event.getOption("bodovi4").getAsDouble();

		if(bodovi1 < 45 || bodovi1 > 100 | bodovi2 < 45 || bodovi2 > 100 || bodovi3 < 45 || bodovi3 > 100 | bodovi4 < 45 || bodovi4 > 100) {
			event.getHook().sendMessage("`Pogresan unos... Broj bodova moze biti samo broj izmedju 45 i 100...`").queue();
			return;
		}

		Double prosek = (bodovi1 + bodovi2 + bodovi3 + bodovi4) / 4 * 49 / 60f;

		prosek += 55 / 3;

		Double ocena = prosek / 10;

		DecimalFormat ocenaFormater = new DecimalFormat("##");
		DecimalFormat brojBodovaFormater = new DecimalFormat("##.##");

		event.getHook().sendMessage("Broj bodova: " + brojBodovaFormater.format(prosek) + ((prosek <= 50) ? "! Niste jos polozili." : "! Ocena: " + ocenaFormater.format(ocena))).queue();
	}

	private void LOGUJ_KOMANDU(@NotNull SlashCommandInteractionEvent event, MessageChannelUnion channel, Guild guild, Member member, String commandString) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Nova komanda iskoriscenja!");
		eb.setThumbnail(guild.getIconUrl());
		eb.setFooter("ShruggoBot");
		eb.addField("Server: ", guild.getName(), true);
		eb.addField("Kanal: ", channel.getName(), true);
		eb.addBlankField(false);
		eb.addField("Korisnik: ", member.getEffectiveName(), true);
		eb.addField("Komanda: ", commandString, true);

		Message message = new MessageBuilder().setEmbeds(eb.build()).build();
		event.getJDA().getGuildById("945790514605727755").getTextChannelById("1002918401750085643").sendMessage(message).queue();
	}

	private void RANDOM_CAT_KOMANDA(@NotNull SlashCommandInteractionEvent event) {
		event.deferReply(false).queue();
		try {
			HttpResponse<String> response = Unirest.get("https://api.thecatapi.com/v1/images/search")
				.asString();

			JsonElement je = JsonParser.parseString(response.getBody().toString());
			JsonElement jobject = je.getAsJsonArray().get(0);
			String url = jobject.getAsJsonObject().get("url").getAsString();

			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.black);
			eb.setFooter("by ShruggoBot");
			eb.setImage(url);

			event.getHook().sendMessageEmbeds(eb.build()).queue();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	private void UNMUTE_KOMANDA(@NotNull SlashCommandInteractionEvent event) {
		Member korisnik = event.getOption("korisnik").getAsMember();
		korisnik.deafen(false).queue();
		event.getHook().sendMessage("Unmutovan korisnik").setEphemeral(true).queue();
	}

	// private void MUTE_KOMANDA(@NotNull SlashCommandInteractionEvent event) {

	// 	Member korisnik = event.getOption("korisnik").getAsMember();

	// 	TextInput razlog = TextInput.create("razlog", "Razlog", TextInputStyle.SHORT)
	// 			.setPlaceholder("Unesite kratak razlog")
	// 			.setMinLength(5)
	// 			.setMaxLength(50)
	// 			.setRequired(true)
	// 			.build();

	// 	Modal modal = Modal.create("muteTiket", "Mute Tiket")
	// 			.addActionRows(
	// 				ActionRow.of(razlog)
	// 			).build();

		
	// 	ModalCommands.m = korisnik;
	// 	event.replyModal(modal).queue();
	// }

	private void NAPRAVI_ROAST_KOMANDA(@NotNull SlashCommandInteractionEvent event) {

		List<String> poruke = new ArrayList<>();

		try (Scanner roastsReader = new Scanner(new File(new File("jsonFolder"), "roasts.txt"))) {
			
			while (roastsReader.hasNextLine()) {
				String poruka = roastsReader.nextLine();
				poruke.add(poruka);
			}

			 roastsReader.close();
		} catch (FileNotFoundException e) {
			event.getHook().sendMessage("Fajlovi za ucitavanje poruka nisu nadjeni.").queue();
			return;
		}

		try (FileWriter roastsWriter = new FileWriter(new File(new File("jsonFolder"), "roasts.json"))) {

			Gson gson = new GsonBuilder()
						.disableHtmlEscaping()
						.setFieldNamingStrategy(FieldNamingPolicy.UPPER_CAMEL_CASE)
						.setPrettyPrinting()
						.serializeNulls()
						.create();
			
			String jsonString = gson.toJson(poruke);

			roastsWriter.write(jsonString);
			roastsWriter.close();

		} catch(IOException e) {
			event.getHook().sendMessage("Fajlovi za upisivanje poruka nisu nadjeni.").queue();
			return;
		}
	}

	private void POSALJI_WEBSITE_KOMANDA(@NotNull SlashCommandInteractionEvent event) {

		event.deferReply(true).queue();

		EmbedBuilder eb = new EmbedBuilder();

		eb.setTitle("Website");
		eb.setFooter("by Nenad Gvozdenac");
		eb.addField("LINK DO SAJTA", "Website se moze naci na sledecem linku: \nhttps://bit.ly/shruggo", false);
		eb.setColor(Color.cyan);
		eb.setThumbnail(event.getGuild().getIconUrl());

		Message message = new MessageBuilder().setEmbeds(eb.build()).setActionRows(ActionRow.of(Button.link("https://bit.ly/shruggo", "WEBSITE"))).build();

		event.getHook().sendMessage(message).queue();
	}

	// private void ISKLJUCENA_KOMANDA_ODGOVORI(@NotNull SlashCommandInteractionEvent event) {
	// 	event.deferReply(true).queue();
	// 	event.getHook().sendMessage("Nazalost, ova komanda je trenutno iskljucena.\nBice ukljucena uskoro.").queue();
	// }

	private void POSALJI_NOVI_UPDATE_KOMANDA(@NotNull SlashCommandInteractionEvent event) {
		event.deferReply(false).queue();

		EmbedBuilder eb = new EmbedBuilder();

		eb.setTitle("Novi Update - 12.08.2022");
		eb.setDescription("Novi update je live. Dodate nove funkcije.");
		eb.setColor(Color.CYAN);
		eb.setThumbnail("https://pngimg.com/uploads/confetti/confetti_PNG86957.png");

		eb.addField("STA JE URADJENO?", 
		"\u27A1 `/mute` komanda i dalje iskljucena.\n" + 
		"\u27A1 Pri ulasku u server, nekad se znalo da se obrise mogucnost ulaska u druge servere. Popravljeno.\n" +
		"\u27A1 Dodata komanda `/randomcat`.\n\n" +
		"\u27A1 Pristupanje API se sada radi veoma lako.\n" +
		"\u27A1 Ako imate sugestije, DM @NenadG#1022\n\n", false);

		eb.setFooter("\u2764\uFE0F Hvala sto koristite ShruggoBot-a. - Nenad Gvozdenac, autor \u2764\uFE0F");
		event.getHook().sendMessageEmbeds(eb.build()).queue();
	}

	private void POSALJI_GLOBALNO_PORUKU_KOMANDA(@NotNull SlashCommandInteractionEvent event) {

		event.deferReply(true).queue();

		String id = event.getOption("id").getAsString();
		for(Guild guild : event.getJDA().getGuilds()) {
			event.getChannel().retrieveMessageById(id).queue(k -> {
				guild.getDefaultChannel().asTextChannel().sendMessage(k).queue();
			});
		}

		event.getHook().sendMessage("Poslata poruka svim guildovima!").queue();
	}

	private void TRENUTNO_VREME_KOMANDA(@NotNull SlashCommandInteractionEvent event) {

		String trenutnoVreme = "<t:" + (System.currentTimeMillis() / 1000L) + ":f>";

		event.reply("Trenutno vreme: " + trenutnoVreme + "\nFormatirano vreme: `" + trenutnoVreme + "`.").queue();
	}

	private void TIMEOUT_KOMANDA(@NotNull SlashCommandInteractionEvent event) {

		Member member = event.getOption("korisnik").getAsMember();

		TextInput razlog = TextInput.create("razlog", "Razlog", TextInputStyle.SHORT)
				.setPlaceholder("Unesite kratak razlog")
				.setMinLength(5)
				.setMaxLength(50)
				.setRequired(true)
				.build();

		TextInput vreme = TextInput.create("vreme", "Vreme u minutima", TextInputStyle.SHORT)
				.setPlaceholder("Unesite duzinu timeout-a u minutima.")
				.setRequired(true)
				.setValue("15")
				.build();

		Modal modal = Modal.create("timeoutTiket", "Timeout Tiket")
				.addActionRows(
					ActionRow.of(razlog),
					ActionRow.of(vreme)
				).build();

		ModalCommands.m = member;
		event.replyModal(modal).queue();
	}

	private void DODAJ_RESPOND_PORUKU_KOMANDA(@NotNull SlashCommandInteractionEvent event) {

		event.deferReply(false).queue();

		String trigger = event.getOption("trigger").getAsString();
		String response = event.getOption("response").getAsString();

		try (Reader reader = new FileReader(new File(new File("jsonFolder"), "respondPoruke.json"))) {

			Gson gson = new GsonBuilder()
						.disableHtmlEscaping()
						.setFieldNamingStrategy(FieldNamingPolicy.UPPER_CAMEL_CASE)
						.setPrettyPrinting()
						.serializeNulls()
						.create();

			Type founderTypeSet = new TypeToken<Set<JSONRespondPoruka>>(){}.getType();
			Set<JSONRespondPoruka> set = gson.fromJson(reader, founderTypeSet);

			set.add(new JSONRespondPoruka(trigger, new JSONRespondPoruka.RespondPoruka(trigger, response)));

			FileWriter file = new FileWriter(new File(new File("jsonFolder"), "respondPoruke.json"));

			String jsonString = gson.toJson(set);
			file.write(jsonString);
			event.getHook().sendMessage("Uspesno upisani novi podaci za respond-poruke u .JSON fajl.\nTrigger: " + trigger + "\nPoruka: " + response).setEphemeral(false)
				.queue(k -> k.delete().queueAfter(20, TimeUnit.SECONDS));	

			file.close();
			reader.close();

		} catch(IOException e) {
			event.getHook().sendMessage("Nije uspesno dodavanje date komande. Najvernovatnije jer ona ne postoji?").setEphemeral(false)
				.queue(k -> k.delete().queueAfter(20, TimeUnit.SECONDS));
		}
	}

	private void OBRISI_RESPOND_PORUKU_KOMANDA(@NotNull SlashCommandInteractionEvent event) {
		event.deferReply(false).queue();

		String triggerZaBrisanje = event.getOption("trigger").getAsString();

		try (Reader reader = new FileReader(new File(new File("jsonFolder"), "respondPoruke.json"))) {

			Gson gson = new GsonBuilder()
						.disableHtmlEscaping()
						.setFieldNamingStrategy(FieldNamingPolicy.UPPER_CAMEL_CASE)
						.setPrettyPrinting()
						.serializeNulls()
						.create();

			Type founderTypeSet = new TypeToken<Set<JSONRespondPoruka>>(){}.getType();
			Set<JSONRespondPoruka> set = gson.fromJson(reader, founderTypeSet);

			JSONRespondPoruka porukaZaBrisanje = null;

			for(JSONRespondPoruka p : set) {
				if(p.getRespondPoruka().getTrigger().equals(triggerZaBrisanje)) {
					event.getHook().sendMessage("Izbrisana poruka: " + p.getRespondPoruka().getTrigger() + "\nResponse: " + p.getRespondPoruka().getResponseLatinica())
						.queue();
					porukaZaBrisanje = p;
					break;
				}
			}

			if(porukaZaBrisanje != null) {
				set.remove(porukaZaBrisanje);

				FileWriter file = new FileWriter(new File(new File("jsonFolder"), "respondPoruke.json"));

				String jsonString = gson.toJson(set);
				file.write(jsonString);
				event.getHook().sendMessage("Uspesno upisani novi podaci za respond-poruke u .JSON fajl.\n").setEphemeral(false)
					.queue(k -> k.delete().queueAfter(20, TimeUnit.SECONDS));	

				file.close();
			} else {
				event.getHook().sendMessage("Nije uspesno brisanje date komande. Najvernovatnije jer ona ne postoji?").setEphemeral(false)
					.queue(k -> k.delete().queueAfter(20, TimeUnit.SECONDS));
			}

			reader.close();

		} catch (IOException e) {
            e.printStackTrace();
        }
	}

	private void LISTAJ_JSON_RESPOND_PORUKE(@NotNull SlashCommandInteractionEvent event) {

		event.deferReply(true).queue();

		try (Reader reader = new FileReader(new File(new File("jsonFolder"), "respondPoruke.json"))) {

			Gson gson = new GsonBuilder()
						.disableHtmlEscaping()
						.setFieldNamingStrategy(FieldNamingPolicy.UPPER_CAMEL_CASE)
						.setPrettyPrinting()
						.serializeNulls()
						.create();

			Type founderTypeSet = new TypeToken<Set<JSONRespondPoruka>>(){}.getType();
			Set<JSONRespondPoruka> set = gson.fromJson(reader, founderTypeSet);
			
			int brojac = 0;

			String poruka = "```c\n";

			List<String> poruke = new ArrayList<>();

			for(JSONRespondPoruka p : set) {
				poruka += p.getRespondPoruka().toString() + "\n";
				brojac++;

				if(brojac == 5) {
					brojac = 0;
					poruka += "```";

					poruke.add(poruka);
					poruka = "```c\n";
				}
			}

			if(brojac != 0) {
				poruka += "```";
				poruke.add(poruka);
			}

			event.getUser().openPrivateChannel().queue(k -> {
				for(String p : poruke) {
					k.sendMessage(p).queue();
				}
			});

			event.getHook().sendMessage("Uspesno je sve proslo. Trebalo bi da ste dobili sve poruke u privatnom kanalu (DM).").setEphemeral(true).queue();

        } catch (IOException e) {
            event.getHook().sendMessage("Nije bilo moguce otvoriti fajl za citanje respond poruka.").setEphemeral(true).queue();
        } catch(ErrorResponseException e1) {
			event.getHook().sendMessage("Nije bilo moguce otvoriti privatni kanal (DM) sa Vama. Omogucite ovu funkciju, i ponovo pozovite ovu komandu").setEphemeral(true).queue();
		}
	}

	private void NAPRAVI_JSON_RESPOND_PORUKE(@NotNull SlashCommandInteractionEvent event) {
			event.deferReply(true).queue();

			File triggers = new File("triggeri.txt");
			File messages = new File("poruke.txt");
			
			List<JSONRespondPoruka.RespondPoruka> poruke = new ArrayList<>();

			try {
				Scanner tR = new Scanner(triggers);
				Scanner mR = new Scanner(messages);
				
				while (tR.hasNextLine() && mR.hasNextLine()) {
				    String data1 = tR.nextLine();
					String data2 = mR.nextLine();

					JSONRespondPoruka.RespondPoruka p = new JSONRespondPoruka.RespondPoruka(data1, data2);

					poruke.add(p);
				 }

				 tR.close();
				 mR.close();
			} catch (FileNotFoundException e) {
				event.getHook().sendMessage("Fajlovi za ucitavanje poruka: " + triggers.getName() + " i " + messages.getName() + " nisu nadjeni.").queue();
				return;
			}

			Gson gson = new GsonBuilder()
						.disableHtmlEscaping()
						.setFieldNamingStrategy(FieldNamingPolicy.UPPER_CAMEL_CASE)
						.setPrettyPrinting()
						.serializeNulls()
						.create();

			try (FileWriter file = new FileWriter(new File(new File("jsonFolder"), "respondPoruke.json"))) {
				
				HashSet<JSONRespondPoruka> set = new HashSet<>();

				for(JSONRespondPoruka.RespondPoruka p : poruke) {
					JSONRespondPoruka novo = new JSONRespondPoruka(p.getTrigger(), p);
					set.add(novo);
				}

				String jsonString = gson.toJson(set);
				file.write(jsonString);
				event.getHook().sendMessage("Upisani svi podaci u .JSON fajl.").setEphemeral(true).queue();	
				file.close();

			} catch(IOException e1) {
				event.getHook().sendMessage("Nije bilo moguce otvoriti .JSON fajl.").setEphemeral(true).queue();
			}

			
	}

	private void ANALIZA_FORMULE_KOMANDA(@NotNull SlashCommandInteractionEvent event) {

		SelectMenu meni = SelectMenu.create("formula")
			.addOptions(
				SelectOption.of("Izvodi", "izvodi")
					.withDescription("Formule za izvode")
					.withEmoji(Emoji.fromUnicode("\uD83D\uDCD3")),
				SelectOption.of("Integrali", "integrali")
					.withDescription("Formule za integrale")
					.withEmoji(Emoji.fromUnicode("\uD83D\uDCD0")),
				SelectOption.of("Redovi", "redovi")
                    .withDescription("Formule za redove")
                    .withEmoji(Emoji.fromUnicode("\uD83D\uDCC9"))
			).build();

		event.reply("Izaberite koje formule zelite: ")
			.addActionRow(meni).queue();	
	}

	private void KICK_KOMANDA(@NotNull SlashCommandInteractionEvent event) {

		Member korisnik = event.getOption("korisnik").getAsMember();

		TextInput razlog = TextInput.create("razlog", "Razlog", TextInputStyle.SHORT)
				.setPlaceholder("Unesite kratak razlog")
				.setMinLength(5)
				.setMaxLength(50)
				.setRequired(true)
				.build();

		Modal modal = Modal.create("kickTiket", "Kick Tiket")
				.addActionRows(
					ActionRow.of(razlog)
				).build();

		ModalCommands.m = korisnik;
		event.replyModal(modal).queue();
	}

	private void BAN_KOMANDA(SlashCommandInteractionEvent event) {

		Member korisnik = event.getOption("korisnik").getAsMember();

		TextInput razlog = TextInput.create("razlog", "Razlog", TextInputStyle.SHORT)
				.setPlaceholder("Unesite kratak razlog")
				.setMinLength(5)
				.setMaxLength(50)
				.setRequired(true)
				.build();

		TextInput vreme = TextInput.create("vreme", "Vreme brisanja poruka", TextInputStyle.SHORT)
				.setPlaceholder("Unesite vreme u danima")
				.setRequired(true)
				.setMaxLength(10)
				.setValue(String.valueOf(0))
				.build();

		Modal modal = Modal.create("banTiket", "Ban Tiket")
				.addActionRows(
					ActionRow.of(razlog), 
					ActionRow.of(vreme)
				).build();

		
		ModalCommands.m = korisnik;
		event.replyModal(modal).queue();
	}

	private void POSALJI_TIKET(SlashCommandInteractionEvent event) {
		
		TextInput naslov = TextInput.create("naslov", "Naslov", TextInputStyle.SHORT)
				.setPlaceholder("Naslov tiketa")
				.setMinLength(10)
				.setMaxLength(100)
				.build();
		
		TextInput upozorenje = TextInput.create("paragraf", "Paragraf", TextInputStyle.PARAGRAPH)
				.setPlaceholder("Paragraf tiketa")
				.setMinLength(30)
				.setMaxLength(1000)
				.build();
		
		Modal modal = Modal.create("tiket", "Tiket")
				.addActionRows(ActionRow.of(naslov), ActionRow.of(upozorenje)).build();
		
		event.replyModal(modal).queue();
	}

	private void GITHUB_KOMANDA(SlashCommandInteractionEvent event) {
		
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("GITHUB");
		eb.setDescription("Najjaci github profil ikada napravljen.");
		eb.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Octicons-mark-github.svg/600px-Octicons-mark-github.svg.png");
		eb.setFooter("Nenad Gvozdenac");
		
		hook.sendMessageEmbeds(eb.build())
			.addActionRow(Button.link("https://github.com/NenadGvozdenac/DiscordBot", "GITHUB")).queue();
	}

	private void OBRISI_KANAL_KOMANDA(SlashCommandInteractionEvent event) {
		
		Channel channel = event.getOption("kanal").getAsChannel();
		
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		
		channel.delete().queue();
		hook.sendMessage("Uspesno obrisan kanal!").queue();
	}

	private void NAPRAVI_KANAL_KOMANDA(SlashCommandInteractionEvent event) {
		
		String nazivKanala = event.getOption("kanal").getAsString();
		Boolean isVoice = event.getOption("isvoice").getAsBoolean();
		
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		
		if(isVoice)
			event.getGuild().createVoiceChannel(nazivKanala).queue();
		else
			event.getGuild().createTextChannel(nazivKanala).queue(k -> k.sendMessage("! Napravljen kanal od strane " + event.getMember().getAsMention() + ".").queue());
		
		hook.sendMessage("Uspesno napravljen kanal!").queue();
	}
					
	private void GLASANJE_KOMANDA(SlashCommandInteractionEvent event) {
		
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		
		String naslov = event.getOption("naslov").getAsString();
		String deskripcija = event.getOption("poruka").getAsString();
		Integer trajanje = event.getOption("trajanje_sekunde").getAsInt();
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				JDA jda = event.getJDA();
				
				List<Object> listaListenera = jda.getRegisteredListeners();
				
				for(Object o : listaListenera) {
					if(o instanceof KomandaGlasanje || o instanceof ButtonClick) {
						hook.sendMessage("Vec postoji aktivno glasanje, sacekajte da ono istekne!!").queue();
						return;
					}
				}

				Member member = event.getMember();
				System.out.println(member.getAsMention());

				hook.sendMessage("Naslov: " + naslov + ", deskripcija: " + deskripcija).queue();
				hook.sendMessage("Napisite opciju za glasanje (ili stop za prestanak, maksimalno 5): ").queue();

				KomandaGlasanje glasanje = new KomandaGlasanje(event.getMember(), naslov, deskripcija, event, trajanje);
				
				jda.addEventListener(glasanje);
				
				new Timer().schedule(new TimerTask() {	
					public void run() {	
						
						List<Object> listaListenera = jda.getRegisteredListeners();
						
						if(listaListenera.contains(glasanje)) {
							hook.sendMessage("Isteklo je vreme za ovo glasanje. Prestanak akcije.").queue();
							jda.removeEventListener(glasanje);
						}
						
						this.cancel();
						
					}}, 150_000, 1);
			}
		});
		thread.run();
	}

	private void SPAMUJ_KOMANDA(SlashCommandInteractionEvent event) {
		event.deferReply().queue();
		String poruka = event.getOption("poruka").getAsString();
		Integer brojPoruka = event.getOption("brojporuka").getAsInt();
		
		if(brojPoruka < 1 || brojPoruka > 15) {
			brojPoruka = 15;
		}

		for(int i = 0; i < brojPoruka; i++) {
			event.getHook().sendMessage(poruka).queue(m -> m.delete().queueAfter(10, TimeUnit.SECONDS));
		}
	}

	private void BITNO_OBAVESTENJE_KOMANDA(SlashCommandInteractionEvent event) {
		
		String izjava = event.getOption("izjava").getAsString();
		String vreme = "<t:" + (System.currentTimeMillis() / 1000L) + ":f>";
		
		for(Guild g : event.getJDA().getGuilds()) {
			g.getDefaultChannel().asTextChannel().sendMessage(izjava + " - " + "**" + vreme + "**").queue();
		}
	}

	private void ROAST_KOMANDA(SlashCommandInteractionEvent event) {
		event.deferReply(false).queue();
		InteractionHook hook = event.getHook();
		
		try(Reader roastReader = new FileReader(new File(new File("jsonFolder"), "roasts.json"))) {

			Gson gson = new GsonBuilder()
						.disableHtmlEscaping()
						.setFieldNamingStrategy(FieldNamingPolicy.UPPER_CAMEL_CASE)
						.setPrettyPrinting()
						.serializeNulls()
						.create();
			
			Type founderTypeSet = new TypeToken<List<String>>(){}.getType();
			List<String> poruke = gson.fromJson(roastReader, founderTypeSet);

			Integer random_int = (int)(Math.random() * ((poruke.size() - 1)));
		
			String poruka_za_slanje = "";
		
			Member member = event.getOption("korisnik").getAsMember();
		
			poruka_za_slanje += member.getAsMention();
			
			poruka_za_slanje += ", " + poruke.get(random_int);
		
			hook.sendMessage(poruka_za_slanje).allowedMentions(null).queue();

		} catch(IOException e) {
			event.getHook().sendMessage("Nazalost, .json fajl se nije mogao ucitati.").queue();
		}
	}

	private void BOT_INVITE_KOMANDA(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle("**INVITE BOTA**");
		eb.setColor(Color.CYAN);
		
		eb.addField("LINK ZA INVITE", "https://bit.ly/shruggo", false);
		
		eb.setThumbnail(event.getGuild().getIconUrl());
		
		hook.sendMessageEmbeds(eb.build()).queue();
	}

	private void PURGE_KOMANDA(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		
		Integer brojPoruka = event.getOption("brojporuka").getAsInt();

		try {
			Optional<TextChannel> kanal = Optional.of(event.getOption("kanal").getAsChannel().asTextChannel());
				
			if(kanal.isPresent()) {
				if(brojPoruka == 1) {
					String message = kanal.get().getLatestMessageId();
					kanal.get().deleteMessageById(message).queue();
					return;
				} else {
					List<Message> poruke = kanal.get().getHistory().retrievePast(brojPoruka).complete();
					kanal.get().deleteMessages(poruke).queue();
				}
			} else {
				kanal = Optional.of(event.getChannel().asTextChannel());

				if(brojPoruka == 1) {
					String message = kanal.get().getLatestMessageId();
					kanal.get().deleteMessageById(message).queue();
				} else {
					List<Message> poruke = kanal.get().getHistory().retrievePast(brojPoruka).complete();
					kanal.get().deleteMessages(poruke).queue();
				}
			}
		} catch(Exception e) {
				TextChannel kanal = event.getChannel().asTextChannel();

				if(brojPoruka == 1) {
					String message = kanal.getLatestMessageId();
					kanal.deleteMessageById(message).queue();
				} else {
					List<Message> poruke = kanal.getHistory().retrievePast(brojPoruka).complete();
					kanal.deleteMessages(poruke).queue();
				}
		} finally {
			hook.sendMessage("Uspesno obrisano " + brojPoruka + " poruka.").queue();
		}
	}

	private void NICK_KOMANDA(SlashCommandInteractionEvent event) {
		
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		
		Member korisnik = event.getOption("korisnik").getAsMember();
		String novoIme = event.getOption("novoime").getAsString();
		
		try {
			korisnik.modifyNickname(novoIme).queue();
			
		} catch(NullPointerException e) {
			hook.sendMessage("Nije moguce ovo uraditi.").queue();
			return;
		} catch(HierarchyException e1) {
			hook.sendMessage("Bot nema mogucnost da ovo uradi.").queue();
			return;
		}
		
		hook.sendMessage("Uspesno promenjen nickname!").queue();
	}

	private void KORISNI_LINKOVI_KOMANDA(SlashCommandInteractionEvent event) {
		
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle("**KORISNI LINKOVI E2**");
		eb.setColor(Color.CYAN);
		String vreme = "<t:" + (System.currentTimeMillis() / 1000L) + ":f>";
		eb.setDescription("Iskoriscenjo od strane: " + event.getUser().getAsMention() + "\nVreme koriscenja: " + vreme);
		
		eb.addField("Dropbox Survival", 				"https://www.dropbox.com/sh/mkqwwg655ou36n8/AADv4xXVBdRgX63inKrmAegia?dl=0", false);
		eb.addField("Raspored Zimskog Semestra", "BICE DODATO", false);
		eb.addField("Studenski web servis", 			"https://ssluzba.ftn.uns.ac.rs/ssluzbasp/", false);
		
		eb.addField("Modeliranje i simulacija sistema", 			"BICE DODATO", false);
		eb.addField("Logičko projektovanje računarskih sistema 1", 		"BICE DODATO", false);
		eb.addField("Matematička analiza 2",					"BICE DODATO", false);
		eb.addField("Objektno programiranje",								"BICE DODATO", false);
		
		eb.setThumbnail(event.getGuild().getIconUrl());
		
		hook.sendMessageEmbeds(eb.build()).queue();
	}

	private void HELP_KOMANDA(SlashCommandInteractionEvent event) {
		
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();

		Message message = new MessageBuilder().setActionRows(
			ActionRow.of(
				Button.primary("levo", "\uD83D\uDCDA LISTAJ LEVO \uD83D\uDCDA").asDisabled(),
				Button.secondary("desno", "\uD83D\uDCDA LISTAJ DESNO \uD83D\uDCDA")
			)
		).setEmbeds(HelpButton.ISPISI_SVE_KOMANDE().get(0).build()).build();

		hook.sendMessage(message).queue();
	}

	private void SHUTDOWN_KOMANDA(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		
		if(event.getUser().getAsTag().equals(Commands.kreator)) {
			hook.sendMessage("Iskljucivanje za 5 sekundi...").queue();
			
			try {
				Thread.sleep(5000);
				System.exit(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			event.getJDA().shutdownNow();
		} else {
			hook.sendMessage("Nemate pravo uraditi ovu komandu...").queue();
		}
	}
	
	private void RECI_KOMANDA(SlashCommandInteractionEvent event) {
		try {
			String content = event.getOption("izjava").getAsString();
			
			event.reply(event.getUser().getAsTag() + ": " + content).queue();
		} catch(NullPointerException e) {
			event.deferReply(true).queue();
			InteractionHook hook = event.getHook();
			
			hook.sendMessage("Morate dostaviti drugi argument.").queue();
		}
	}
	
	private void INFO_KOMANDA(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle("**INFORMACIJE O BOTU**");
		eb.setColor(Color.CYAN);
		String vreme = "<t:" + (System.currentTimeMillis() / 1000L) + ":f>";
		eb.setDescription("Iskoriscenjo od strane: " + event.getUser().getAsMention() + "\nVreme koriscenja: " + vreme);
		
		eb.addField("Namena", "- Discord Bot se koristi samo za zajebanciju...", false);
		eb.addField("Prikupljanje informacije", "- Discord Bot ne prikuplja informacije o korisnicima Discord servera", false);
		eb.addField("Website", "https://bit.ly/shruggo", false);
		eb.addField("Kreator", "- Discord Bot je napravljen od strane Nenada Gvozdenca", false);
		eb.addField("Github", "- Ceo projekat se nalazi na Github, (https://github.com/NenadGvozdenac/DiscordBot)", false);
		
		eb.setThumbnail(event.getGuild().getIconUrl());
		
		hook.sendMessageEmbeds(eb.build()).addActionRows(ActionRow.of(
			Button.link("https://github.com/NenadGvozdenac/DiscordBot", "GITHUB"),
			Button.link("https://bit.ly/shruggo", "WEBSITE")
		)).queue();
	}
	
	private void PING_KOMANDA(SlashCommandInteractionEvent event) {
		long start = System.currentTimeMillis();
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		
		hook.sendMessage("🏓🏓🏓 `PONG!` 🏓🏓🏓").queue(k -> {
			
			long end = System.currentTimeMillis();
			
			EmbedBuilder pingRespond = new EmbedBuilder();
			pingRespond.addField("\uD83E\uDD16 BOT PING \uD83E\uDD16", String.valueOf((end - start)) + " ms.", true);
			pingRespond.addField("\uD83E\uDD16 WEBSOCKET PING \uD83E\uDD16", String.valueOf(DiscordBot.jda.getGatewayPing()) + " ms.", true);

			k.editMessage(k.getContentRaw()).setEmbeds(pingRespond.build()).queue();
		});
		
		}
	
	private void IZADJI_KOMANDA(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		
		if(!PermissionUtil.checkPermission(event.getMember(), Permission.ADMINISTRATOR)) {
			hook.sendMessage("Ne mozes me izbaciti iz servera!").queue();
			return;
		}
		
		hook.sendMessage("Mozete invite-ovati bota putem ovog linka: https://bit.ly/shruggo").queue();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		event.getGuild().leave().queue();
	}
	
	private void PREIMENUJ_KANAL_KOMANDA(SlashCommandInteractionEvent event) {
		event.deferReply(true).queue();
		InteractionHook hook = event.getHook();
		
		try {
			GuildChannel kanal = event.getOption("origkanal").getAsChannel();
			
			String naziv = event.getOption("novikanal").getAsString();
		
			kanal.getManager().setName(naziv).queue();
			
			hook.sendMessage("Preimenovao sam kanal...").queue();
			
		} catch(NullPointerException e) {
			
			hook.sendMessage("Morate dostaviti drugi argument.").queue();
		}
	}
}
