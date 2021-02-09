package com.g3devs.cliente.servicios;

public class ClienteServicio extends Thread{
	
	private String nickName;
	
	
	//Cliente extends Thread ( para varios clientes 10 )
		// Pasar por parametros  primera comunicacion para crear o unirse a partida
			//Espera a recibir respuesta (dormir conexion para ocupar menos memoria)
		// Recibir info de contrincante
		// Jugar partida contra contrincante usando clase Dados	(Empieza visitante)
		// Pasar por parametros segunda comunicacion para finalizar partida (Solo Hosts)
	
	public ClienteServicio(String nombre) {
		nickName = nombre;
	}

	public void run() {
		
	}

}