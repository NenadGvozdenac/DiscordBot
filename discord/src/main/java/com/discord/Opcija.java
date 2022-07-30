package com.discord;

import net.dv8tion.jda.api.interactions.commands.OptionType;

public class Opcija {
	
	private OptionType optionType;
	private String optionTypeName = "";
	private String optionTypeDesc = "";
	private Boolean obaveznaOpcija;
	
	Opcija(OptionType type, String optionTypeName, String optionTypeDesc) {
		this.optionType = type;
		this.optionTypeName = optionTypeName;
		this.optionTypeDesc = optionTypeDesc;
		this.obaveznaOpcija = false;
	}
	
	Opcija(OptionType type, String optionTypeName, String optionTypeDesc, Boolean obaveznaOpcija) {
		this.optionType = type;
		this.optionTypeName = optionTypeName;
		this.optionTypeDesc = optionTypeDesc;
		this.obaveznaOpcija = obaveznaOpcija;
	}

	public String getOptionTypeName() {
		return this.optionTypeName;
	}
	
	public String getOptionTypeDesc() {
		return this.optionTypeDesc;
	}
	
	public OptionType getOptionType() {
		return this.optionType;
	}
	
	public void setOptionTypeName(String optionTypeName) {
		this.optionTypeName = optionTypeName;
	}
	
	public void setOptionTypeDesc(String optionTypeDesc) {
		this.optionTypeDesc = optionTypeDesc;
	}
	
	public void setOptionType(OptionType optionType) {
		this.optionType = optionType;
	}

	public Boolean getObaveznaOpcija() {
		return obaveznaOpcija;
	}

	public void setObaveznaOpcija(Boolean obaveznaOpcija) {
		this.obaveznaOpcija = obaveznaOpcija;
	}
	
};