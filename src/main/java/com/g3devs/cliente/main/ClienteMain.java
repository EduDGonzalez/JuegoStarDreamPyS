package com.g3devs.cliente.main;

import com.g3devs.cliente.servicios.ClienteServicio;

public class ClienteMain {

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			ClienteServicio cliente = new ClienteServicio("Jugador "+(i+1));
			cliente.start();
		}
	}

}