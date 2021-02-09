package com.g3devs.servidor.servicios;

public class ServidorServicio extends Thread {
	
	//Servicio servidor extends Thread.
		// Recibe la petición del servidor.
			//Crear o unir partida (buscar en partidas en espera).
				//No hay partida (se crea partida) "crearPartida()" 
					//Se crea el Jugador (Host) 
					//Esperar wait()
					//Devuelve la info del jugador(1) a su jugador(0)
				//Si hay partida (se une a partida) "unirsePartida()"
					//Se crea el Jugador ( no Host )
					//notify()
					//Devuelve la info del jugador(0) a su jugador(1)
	
	public void run() {
		
	}

}