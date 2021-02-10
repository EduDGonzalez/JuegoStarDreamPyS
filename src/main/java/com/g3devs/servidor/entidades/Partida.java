package com.g3devs.servidor.entidades;

import java.util.ArrayList;
import java.util.List;

public class Partida {
	
	private int id;
	private List<Jugador> jugadores = new ArrayList<>();
	private TipoPartida tipo;
	
	public Partida(int id,Jugador jugador, TipoPartida tipo) {
		this.id = id;
		this.jugadores.add(jugador);
		this.tipo = tipo;
	}
	public Partida() {
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Jugador> getJugadores() {
		return jugadores;
	}
	public void setJugadores(List<Jugador> jugadores) {
		this.jugadores = jugadores;
	}
	public TipoPartida getTipo() {
		return tipo;
	}
	public void setTipo(TipoPartida tipo) {
		this.tipo = tipo;
	}
			
}
