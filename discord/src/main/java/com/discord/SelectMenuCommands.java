package com.discord;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SelectMenuCommands extends ListenerAdapter {
    
    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        if(event.getValues().get(0).equals("izvodi")) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("TABLICA IZVODA");
            eb.setFooter("Ako vam trebaju ove formule, srecno \u231B  :)");
            eb.setImage("https://trigidentities.net/wp-content/uploads/2020/06/formula.png");
            eb.setColor(Color.BLUE);
            eb.setThumbnail("https://w7.pngwing.com/pngs/434/317/png-transparent-integral-symbol-mathematics-cambio-de-variable-mathematics-text-line-lys.png");

            event.editMessage("").setEmbeds(eb.build()).queue();
        } else if(event.getValues().get(0).equals("integrali")) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("TABLICA INTEGRALA");
            eb.setFooter("Ako vam trebaju ove formule, srecno \u231B  :)");
            eb.setImage("http://2.bp.blogspot.com/-o5ox4nxjV8I/VaGY7TDAZlI/AAAAAAAAABI/8AGvv1R9N8E/s1600/TABLICA%2BINTEGRALA.png");
            eb.setColor(Color.BLUE);
            eb.setThumbnail("https://w7.pngwing.com/pngs/434/317/png-transparent-integral-symbol-mathematics-cambio-de-variable-mathematics-text-line-lys.png");

            event.editMessage("").setEmbeds(eb.build()).queue();
        }
    }
}
