package com.g3devs.servidor.entidades;

public enum TipoPartida {
	
	DADOS("dados",2);

	private String nombreTipoPartida;
	private int maxJugadores;
	
	
	private TipoPartida(String nombre, int jugadores) {
		this.nombreTipoPartida = nombre;
		this.maxJugadores = jugadores;
	}


	public String getNombreTipoPartida() {
		return nombreTipoPartida;
	}


	public void setNombreTipoPartida(String nombreTipoPartida) {
		this.nombreTipoPartida = nombreTipoPartida;
	}


	public int getMaxJugadores() {
		return maxJugadores;
	}


	public void setMaxJugadores(int maxJugadores) {
		this.maxJugadores = maxJugadores;
	}
	
}