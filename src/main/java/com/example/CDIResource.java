package com.example;

import java.io.Serializable;

import com.google.inject.Singleton;

@Singleton
public class CDIResource implements Serializable{
	private static final long serialVersionUID = -8870665774168328051L;

	public CDIResource() {
	}
	
	public String getTexto(){
		return "Sou um bean";
	}
}
