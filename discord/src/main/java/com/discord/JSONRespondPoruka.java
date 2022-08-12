package com.discord;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONRespondPoruka {
    
    String naziv;
    RespondPoruka poruka;

    public JSONRespondPoruka() {

    }

    public JSONRespondPoruka(String naziv, RespondPoruka poruka) {
        this.naziv = naziv;
        this.poruka = poruka;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setRespondPoruka(RespondPoruka p) {
        this.poruka = p;
    }

    public String getNaziv() {
        return this.naziv;
    }

    public RespondPoruka getRespondPoruka() {
        return this.poruka;
    }

    public static class RespondPoruka {

        private String trigger, responseLatinica;

        public RespondPoruka() {
            trigger = "";
            responseLatinica = "";
        }
    
        public RespondPoruka(String trigger, String responseLatinica) {
            this.trigger = trigger;
            this.responseLatinica = responseLatinica;
        }
    
        public void setTrigger(String trigger) {
            this.trigger = trigger;
        }
    
        public void setResponseLatinica(String response) {
            this.responseLatinica = response;
        }
    
        public String getTrigger() {
            return this.trigger;
        }
    
        public String getResponseLatinica() {
            return this.responseLatinica;
        }
    
        @Override
        public String toString() {
            return "[trigger = \"" + trigger + "\", responseLatinica = \"" + responseLatinica + "\"]";
        }
    }

    public static void formatirajPorukeUJsonFajl(Komanda[] komande) {

        Gson gson = new GsonBuilder()
						.disableHtmlEscaping()
						.setFieldNamingStrategy(FieldNamingPolicy.UPPER_CAMEL_CASE)
						.setPrettyPrinting()
						.serializeNulls()
						.create();

			try (FileWriter file = new FileWriter(new File(new File("jsonFolder"), "komande.json"))) {

				String jsonString = gson.toJson(komande);

                

				file.append(jsonString);
				file.close();

                Logger logger = LoggerFactory.getLogger(JSONRespondPoruka.class);
                logger.info("Uspesno upisane sve poruke u .JSON fajl.");

			} catch(IOException e1) {
                Logger logger = LoggerFactory.getLogger(JSONRespondPoruka.class);
                logger.warn("Doslo je do greske pri upisu poruka u .JSON fajl.");
			}

    }
}
