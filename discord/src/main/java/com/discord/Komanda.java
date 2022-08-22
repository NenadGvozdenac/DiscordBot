package com.discord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class Komanda {
	
		private String naziv, deskripcija;
		private List<Opcija> opcija = new ArrayList<>();
		private Boolean adminOnly;
		
		public Komanda(String naziv, String deskripcija) {
			this.naziv = naziv;
			this.deskripcija = deskripcija;
			this.adminOnly = false;
		}
	
		public Komanda(String naziv, String deskripcija, Opcija... opcije) {
			this.naziv = naziv;
			this.deskripcija = deskripcija;
			this.adminOnly = false;
			
			for(Opcija o : opcije) {
				opcija.add(o);
			}
		}

		public Komanda(String naziv, String deskripcija, Boolean adminOnly, Opcija... opcije) {
			this.naziv = naziv;
			this.deskripcija = deskripcija;
			this.adminOnly = adminOnly;
			
			for(Opcija o : opcije) {
				opcija.add(o);
			}
		}

		public void dodajKomanduUServer(Guild guild) {
			if(opcija.size() == 0) {

				SlashCommandData data = Commands.slash(naziv, deskripcija);
		
				if(adminOnly) {
					data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
				}
		
				guild.upsertCommand((CommandData) data).queue();
			} else {
				SlashCommandData data = Commands.slash(naziv, deskripcija);
							
				for(Opcija o : opcija) {
					data.addOption(o.getOptionType(), o.getOptionTypeName(), o.getOptionTypeDesc(), o.getObaveznaOpcija());
				}
							
				if(adminOnly) {
					data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
				}
							
				guild.upsertCommand((CommandData) data).queue();
			}
		}

		public void dodajKomanduGlobalno(JDA jda) {
			if(opcija.size() == 0) {

				SlashCommandData data = Commands.slash(naziv, deskripcija);

				if(adminOnly) {
					data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
				}

				jda.upsertCommand((CommandData) data).queue();
			} else {
				SlashCommandData data = Commands.slash(naziv, deskripcija);
				
				for(Opcija o : opcija) {
					data.addOption(o.getOptionType(), o.getOptionTypeName(), o.getOptionTypeDesc(), o.getObaveznaOpcija());
				}
				
				if(adminOnly) {
					data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
				}
				
				jda.upsertCommand((CommandData) data).queue();
			}
		}
		
		@Override
		public String toString() {
			
			String opcijaString = "[";
			
			for(Opcija o : opcija) {
				opcijaString += o.getOptionTypeName() + ", ";
			}
			
			opcijaString = opcijaString.substring(0,  opcijaString.length() - 2);
			opcijaString += "]";
			
			return opcijaString;
		}
	
		public String getNaziv() {
			return this.naziv;
		}
		
		public String getDeskripcija() {
			return this.deskripcija;
		}

		public Boolean getAdminOnly() {
            return this.adminOnly;
		}

		public void setNaziv(String naziv) {
			this.naziv = naziv;
		}
		
		public void setDeskripcija(String desc) {
			this.deskripcija = desc;
		}

        public void setAdminOnly(Boolean adminOnly) {
			this.adminOnly = adminOnly;
		}

		public static void DODAJ_KOMANDE_GLOBALNO(JDA jda2, Komanda[] komande) {
			List<Komanda> listaKomandiZaNeDodavanje = new ArrayList<>();
			List<Komanda> listaZaDodati = new ArrayList<>();
	
			for(Command c : jda2.retrieveCommands().complete()) {
				for(Komanda c1 : Arrays.asList(komande)) {
					if(c1.getNaziv().equals(c.getName())) {
						listaKomandiZaNeDodavanje.add(c1);
						break;
					} 
				}
			}
	
			for(Komanda c : Arrays.asList(komande)) {
				if(!listaKomandiZaNeDodavanje.contains(c)) {
					listaZaDodati.add(c);
				}
			}
	
			for(Komanda c : listaZaDodati) {
				c.dodajKomanduGlobalno(jda2);
				System.out.println("Dodata komanda `/" + c.getNaziv() + "` globalno.");
			}
		}
	
		public static void DODAJ_KOMANDE_U_SERVER(Guild guildById, Komanda[] komande) {
			List<Komanda> listaKomandiZaNeDodavanje = new ArrayList<>();
			List<Komanda> listaZaDodati = new ArrayList<>();
	
			for(Command c : guildById.retrieveCommands().complete()) {
				for(Komanda c1 : Arrays.asList(komande)) {
					if(c1.getNaziv().equals(c.getName())) {
						listaKomandiZaNeDodavanje.add(c1);
						break;
					}
				}
			}
	
			for(Komanda c : Arrays.asList(komande)) {
				if(!listaKomandiZaNeDodavanje.contains(c)) {
					listaZaDodati.add(c);
				}
			}
	
			for(Komanda c : listaZaDodati) {
				c.dodajKomanduUServer(guildById);
				System.out.println("Dodata komanda u server `" + guildById.getName() + "`, zvana `/" + c.getNaziv() + "`. (nije globalno).");
			}
		}
}
