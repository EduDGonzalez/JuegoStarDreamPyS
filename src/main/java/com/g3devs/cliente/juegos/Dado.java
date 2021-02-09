package com.g3devs.cliente.juegos;

import java.util.Random;

public class Dado {
	
	private Random rng = new Random();
	
	
	public int roll() {
		int valorDado = 1+rng.nextInt(6);
		
		
		return valorDado;
	
	}

}