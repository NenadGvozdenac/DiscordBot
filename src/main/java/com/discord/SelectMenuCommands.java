package com.discord;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

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

            SelectMenu meni = SelectMenu.create("formula")
			.addOptions(
				SelectOption.of("Izvodi", "izvodi")
					.withDescription("Formule za izvode")
					.withEmoji(Emoji.fromUnicode("\uD83D\uDCD3"))
                    .withDefault(true),
				SelectOption.of("Integrali", "integrali")
					.withDescription("Formule za integrale")
					.withEmoji(Emoji.fromUnicode("\uD83D\uDCD0")),
                SelectOption.of("Redovi", "redovi")
                    .withDescription("Formule za redove")
                    .withEmoji(Emoji.fromUnicode("\uD83D\uDCC9"))
			).build();

            event.editMessage("").setEmbeds(eb.build()).setActionRow(meni).queue();
        } else if(event.getValues().get(0).equals("integrali")) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("TABLICA INTEGRALA");
            eb.setFooter("Ako vam trebaju ove formule, srecno \u231B  :)");
            eb.setImage("http://2.bp.blogspot.com/-o5ox4nxjV8I/VaGY7TDAZlI/AAAAAAAAABI/8AGvv1R9N8E/s1600/TABLICA%2BINTEGRALA.png");
            eb.setColor(Color.BLUE);
            eb.setThumbnail("https://w7.pngwing.com/pngs/434/317/png-transparent-integral-symbol-mathematics-cambio-de-variable-mathematics-text-line-lys.png");

            SelectMenu meni = SelectMenu.create("formula")
			.addOptions(
				SelectOption.of("Izvodi", "izvodi")
					.withDescription("Formule za izvode")
					.withEmoji(Emoji.fromUnicode("\uD83D\uDCD3")),
                SelectOption.of("Integrali", "integrali")
                    .withDescription("Formule za integrale")
                    .withEmoji(Emoji.fromUnicode("\uD83D\uDCD0"))
                    .withDefault(true),
                SelectOption.of("Redovi", "redovi")
                    .withDescription("Formule za redove")
                    .withEmoji(Emoji.fromUnicode("\uD83D\uDCC9"))
			).build();

            event.editMessage("").setEmbeds(eb.build()).setActionRow(meni).queue();
        } else if(event.getValues().get(0).equals("redovi")) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("TABLICA REDOVA");
            eb.setFooter("Ako vam trebaju ove formule, srecno \u231B  :)");
            eb.setImage("https://i.pinimg.com/736x/23/ca/24/23ca246c8b42322e0a9c2599f5a1616c--power-series-menu.jpg");
            eb.setColor(Color.BLUE);
            eb.setThumbnail("https://w7.pngwing.com/pngs/434/317/png-transparent-integral-symbol-mathematics-cambio-de-variable-mathematics-text-line-lys.png");

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
                    .withDefault(true)
			).build();

            event.editMessage("").setEmbeds(eb.build()).setActionRow(meni).queue();
        }
    }
}
