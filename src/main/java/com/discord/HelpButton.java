package com.discord;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;

public class HelpButton extends ListenerAdapter {

    private List<EmbedBuilder> listaEmbeda;
    private EmbedBuilder selectovanEmbed;

    public HelpButton() {
        listaEmbeda = ISPISI_SVE_KOMANDE();
        selectovanEmbed = listaEmbeda.get(0);
    }

    public static List<EmbedBuilder> ISPISI_SVE_KOMANDE() {
        List<EmbedBuilder> eb = new ArrayList<>();
        try(Reader reader = new FileReader(new File(new File("jsonFolder"), "komande.json"))) {

			Gson gson = new GsonBuilder()
						.disableHtmlEscaping()
						.setFieldNamingStrategy(FieldNamingPolicy.UPPER_CAMEL_CASE)
						.setPrettyPrinting()
						.serializeNulls()
						.create();

			Komanda[] komande = gson.fromJson(reader, Komanda[].class);

			reader.close();

            for(int i = 0; i < Math.ceil(komande.length / 10f); i++) {
                eb.add(new EmbedBuilder());
            }

            for(EmbedBuilder b : eb) {
                b.setTitle("**KOMANDE BOTA**");
                b.setColor(Color.CYAN);
                b.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Info_icon_002.svg/1200px-Info_icon_002.svg.png");
                b.setFooter("ShruggoBot by Nenad Gvozdenac");
            }

			int brojac = 0, brojac2 = 0;

			for(int i = 0; i < komande.length; i++) {
				eb.get(brojac2).addField("/" + komande[i].getNaziv(), "\u2192 " + komande[i].getDeskripcija() + ((komande[i].getAdminOnly().equals(true)) ? " *(ADMIN)*" : ""), false);
				brojac++;

				if(brojac == 10) {
					brojac = 0;
					brojac2++;
				}
			}

        } catch(IOException e) {
            
        }

        return eb;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent e) {

		if(e.getComponentId().equals("desno")) {

            for(int i = 0; i < listaEmbeda.size(); i++) {
                if(listaEmbeda.get(i).getFields().get(0).equals(e.getMessage().getEmbeds().get(0).getFields().get(0))) {
                    selectovanEmbed = listaEmbeda.get(i);
                }
            }

            selectovanEmbed = listaEmbeda.get(listaEmbeda.indexOf(selectovanEmbed) + 1);

            if(selectovanEmbed.equals(listaEmbeda.get(listaEmbeda.size() - 1))) {
                e.getHook().editMessageComponentsById(
                    e.getMessageId(), 
                    ActionRow.of(
                        e.getMessage().getButtonById("levo").asEnabled(),
                        e.getMessage().getButtonById("desno").asDisabled()
                    )
                    ).queue();

            } else {
                e.getHook().editMessageComponentsById(e.getMessageId(),
                    ActionRow.of(
                        e.getMessage().getButtonById("levo").asEnabled(),
                        e.getMessage().getButtonById("desno").asEnabled()
                    )
                ).queue();
            }

            e.deferEdit().setEmbeds(selectovanEmbed.build()).queue();
        } else if(e.getComponentId().equals("levo")) {

            for(int i = 0; i < listaEmbeda.size(); i++) {
                if(listaEmbeda.get(i).getFields().get(0).equals(e.getMessage().getEmbeds().get(0).getFields().get(0))) {
                    selectovanEmbed = listaEmbeda.get(i);
                }
            }

            selectovanEmbed = listaEmbeda.get(listaEmbeda.indexOf(selectovanEmbed) - 1);

            if(selectovanEmbed.equals(listaEmbeda.get(0))) {
                e.getHook().editMessageComponentsById(
                    e.getMessageId(), 
                        ActionRow.of(
                            e.getMessage().getButtonById("levo").asDisabled(),
                            e.getMessage().getButtonById("desno").asEnabled()
                        )
                    ).queue();
            } else {
                e.getHook().editMessageComponentsById(e.getMessageId(),
                    ActionRow.of(
                        e.getMessage().getButtonById("levo").asEnabled(),
                        e.getMessage().getButtonById("desno").asEnabled()
                    )
                ).queue();
            }

            e.deferEdit().setEmbeds(selectovanEmbed.build()).queue();
        }
    }
}
