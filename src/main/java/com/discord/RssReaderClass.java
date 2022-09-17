package com.discord;

import java.awt.Color;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;

import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class RssReaderClass {
    
    public void ReadMatematickaAnaliza() throws InterruptedException {

        DiscordBot.jda.awaitReady();

        new Timer().schedule(new TimerTask() {

            Message latestMessage;
            MessageEmbed messageEmbed;
            Field mainField;
        
            RssReader reader;
            Item newestItem;
        
            SimpleDateFormat sdf;

            String url = "https://manaliza1.wordpress.com/oglasna/feed/";

            @Override
            public void run() {
                
                DiscordBot.jda.getTextChannelById("1017466916991946782").getIterableHistory().takeAsync(1).thenAccept(listOfMessages -> {

                try {
                    latestMessage = listOfMessages.get(0);
                    messageEmbed = latestMessage.getEmbeds().get(0);

                    mainField = null;
    
                    for(Field field : messageEmbed.getFields()) {
                        if(field.getName().equals("LINK")) {
                            mainField = field;
                            break;
                        }
                    }

                    reader = new RssReader();
                    ArrayList<Item> rssNewsItems = new ArrayList<>();
            
                    try (Stream<Item> rssFeed = reader.read(url)) {
                        rssFeed.forEach(item -> rssNewsItems.add(item));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    newestItem = rssNewsItems.get(0);

                    if(!newestItem.getLink().get().equals(mainField.getValue())) {

                        List<Item> listOfNewItems = new ArrayList<>();

                        for(Item t : rssNewsItems) {

                            System.out.println("ADDED " + t.getLink().get() + "\n");

                            if(!t.getLink().get().equals(mainField.getValue())) {
                                listOfNewItems.add(t);
                                break;
                            }
                        }

                        for(Item newItem : listOfNewItems) {
                            String updateLink = newItem.getLink().get();
                            String time = newItem.getPubDate().get();
        
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setColor(Color.PINK);
                                    
                            eb.setTitle(newItem.getTitle().get());                        
                            eb.setDescription("`Matematicka analiza 1`: novo obavestenje!");
                            eb.setThumbnail("https://cdn.iconscout.com/icon/free/png-256/math-1963506-1657007.png");
        
                            eb.addField("LINK", updateLink, false);
                                    
                            sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.getDefault());
        
                            Date d = null;
                            try {
                                d = sdf.parse(time);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                                    
                            String formattedTime = "<t:" + d.getTime() / 1000 + ":f>";
        
                            eb.addField("PUBLISHED", formattedTime, false);
        
                            DiscordBot.jda.getTextChannelById("1017466916991946782").retrieveWebhooks().queue(availableWebhooks -> {
                                Boolean needToCreateWebhook = true;
    
                                for(Webhook webhookName : availableWebhooks) {
                                    if(webhookName.getName().equals("Matematicka Analiza 1")) {
                                        WebhookClientBuilder builder = WebhookClientBuilder.fromJDA(webhookName);
                                
                                        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder();
                    
                                        WebhookEmbedBuilder embedBuilder = WebhookEmbedBuilder.fromJDA(eb.build());
                                        messageBuilder.addEmbeds(embedBuilder.build());
                    
                                        builder.build().send(messageBuilder.build());

                                        DiscordBot.jda.getTextChannelById("990153941369110568").sendMessageEmbeds(eb.build()).queue();
                                        return;
                                    }
                                }
            
                                if(needToCreateWebhook) {
                                    DiscordBot.jda.getTextChannelById("1017466916991946782").createWebhook("Matematicka Analiza 1").queue(newWebhook -> {
                                        WebhookClientBuilder builder = WebhookClientBuilder.fromJDA(newWebhook);
                                
                                        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder();
                    
                                        WebhookEmbedBuilder embedBuilder = WebhookEmbedBuilder.fromJDA(eb.build());
                                        messageBuilder.addEmbeds(embedBuilder.build());
                    
                                        builder.build().send(messageBuilder.build());

                                        DiscordBot.jda.getTextChannelById("990153941369110568").sendMessageEmbeds(eb.build()).queue();
                                    });
                                }
                            });
                        }
                    }

                } catch(NullPointerException | IndexOutOfBoundsException e) {

                    RssReader reader;
                    Item newestItem;
                
                    SimpleDateFormat sdf;
        
                    String url = "https://manaliza1.wordpress.com/oglasna/feed/";

                    reader = new RssReader();
                    ArrayList<Item> rssNewsItems = new ArrayList<>();
            
                    try (Stream<Item> rssFeed = reader.read(url)) {
                        rssFeed.forEach(item -> rssNewsItems.add(item));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    newestItem = rssNewsItems.get(0);

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.PINK);
                            
                    eb.setTitle(newestItem.getTitle().get());                        
                    eb.setDescription("`Matematicka analiza 1`: novo obavestenje!");
                    eb.setThumbnail("https://cdn.iconscout.com/icon/free/png-256/math-1963506-1657007.png");

                    String updateLink = newestItem.getLink().get();
                    String time = newestItem.getPubDate().get();

                    eb.addField("LINK", updateLink, false);
                            
                    sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.getDefault());

                    Date d = null;
                    try {
                        d = sdf.parse(time);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                            
                    String formattedTime = "<t:" + d.getTime() / 1000 + ":f>";

                    eb.addField("PUBLISHED", formattedTime, false);

                    DiscordBot.jda.getTextChannelById("1017466916991946782").retrieveWebhooks().queue(availableWebhooks -> {
                        Boolean needToCreateWebhook = true;

                        for(Webhook webhookName : availableWebhooks) {
                            if(webhookName.getName().equals("Matematicka Analiza 1")) {
                                WebhookClientBuilder builder = WebhookClientBuilder.fromJDA(webhookName);
                        
                                WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder();
            
                                WebhookEmbedBuilder embedBuilder = WebhookEmbedBuilder.fromJDA(eb.build());
                                messageBuilder.addEmbeds(embedBuilder.build());
            
                                builder.build().send(messageBuilder.build());

                                DiscordBot.jda.getTextChannelById("990153941369110568").sendMessageEmbeds(eb.build()).queue();
                                return;
                            }
                        }
    
                        if(needToCreateWebhook) {
                                DiscordBot.jda.getTextChannelById("1017466916991946782").createWebhook("Matematicka Analiza 1").queue(newWebhook -> {
                                WebhookClientBuilder builder = WebhookClientBuilder.fromJDA(newWebhook);
                            
                                WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder();
                
                                WebhookEmbedBuilder embedBuilder = WebhookEmbedBuilder.fromJDA(eb.build());
                                messageBuilder.addEmbeds(embedBuilder.build());
                
                                builder.build().send(messageBuilder.build());

                                DiscordBot.jda.getTextChannelById("990153941369110568").sendMessageEmbeds(eb.build()).queue();
                            });
                        }
                    });
                }
                });
            }
            
        }, 0 * 60 * 1000, 2 * 60 * 60 * 1000); // every 2 hours

    }
}
