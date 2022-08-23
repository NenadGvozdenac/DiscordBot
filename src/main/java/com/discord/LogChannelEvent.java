package com.discord;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;

public class LogChannelEvent {

    public static void POSALJI_BAN_LOG(TextChannel adminTextKanal, @NotNull GuildBanEvent event) {

        if(adminTextKanal == null){
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("NOVI BAN");
        eb.setDescription(event.getUser().getAsTag());
        eb.setColor(Color.RED);
        eb.addField("VREME", "<t:" + (System.currentTimeMillis() / 1000L) + ":f>", false);
        eb.setThumbnail("https://emojipedia-us.s3.amazonaws.com/source/skype/289/double-exclamation-mark_203c-fe0f.png");
        eb.setFooter("Banovan od strane ShruggoBot-a");

        adminTextKanal.sendMessageEmbeds(eb.build()).queue();
    }

    public static void POSALJI_KICK_LOG(TextChannel adminTextKanal, @NotNull GuildMemberRemoveEvent event) {

        if(adminTextKanal == null){
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("NOVI KICK");
        eb.setDescription(event.getUser().getAsTag());
        eb.setColor(Color.RED);
        eb.addField("VREME", "<t:" + (System.currentTimeMillis() / 1000L) + ":f>", false);
        eb.setThumbnail("https://emojipedia-us.s3.amazonaws.com/source/skype/289/double-exclamation-mark_203c-fe0f.png");
        eb.setFooter("Kickovan od strane ShruggoBot-a");

        adminTextKanal.sendMessageEmbeds(eb.build()).queue();
    }

    public static void POSALJI_KANAL_BRISANJE_LOG(TextChannel adminTextKanal, @NotNull ChannelDeleteEvent event) {
        if(adminTextKanal == null){
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("NOVO BRISANJE KANALA!");
        eb.setDescription("#" + event.getChannel().getName() + " je obrisan!");
        eb.setColor(Color.RED);
        eb.addField("VREME", "<t:" + (System.currentTimeMillis() / 1000L) + ":f>", false);
        eb.setThumbnail("https://emojipedia-us.s3.amazonaws.com/source/skype/289/double-exclamation-mark_203c-fe0f.png");
        eb.setFooter("Obrisan od strane ShruggoBot-a");

        adminTextKanal.sendMessageEmbeds(eb.build()).queue();
    }

    public static void POSALJI_KANAL_PRAVLJENJE_LOG(TextChannel adminTextKanal, @NotNull ChannelCreateEvent event) {
        if(adminTextKanal == null){
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("NOVO PRAVLJENJE KANALA!");
        eb.setDescription("#" + event.getChannel().getName() + " je napravljen!");
        eb.setColor(Color.RED);
        eb.addField("VREME", "<t:" + (System.currentTimeMillis() / 1000L) + ":f>", false);
        eb.setThumbnail("https://emojipedia-us.s3.amazonaws.com/source/skype/289/double-exclamation-mark_203c-fe0f.png");
        eb.setFooter("Napravljen od strane ShruggoBot-a");

        adminTextKanal.sendMessageEmbeds(eb.build()).queue();
    }
}
