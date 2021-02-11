package com.g3devs.servidor.servicios;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import com.g3devs.servidor.entidades.TipoPartida;

public class ServidorReceptor {
	
	public static Semaphore mutex = new Semaphore(1);
	public static Semaphore dadosJugadores = new Semaphore(1-(TipoPartida.DADOS.getMaxJugadores()-1));
	
	//Servidor.
		// Recibe peticiones y las envia a la clase ServicioServidor.
		// Almacena info de las partidas esperando y en curso (metodos sincronizados para acceder a estas).
		// Almacenara los posibles semaforos para el servicio. (creo que no se necesitan semaforos)
	
	public void init() {
			System.out.println("Creando socket Servidor");
			try(ServerSocket serverSocket = new ServerSocket()) {
				InetSocketAddress addr = new InetSocketAddress("localhost",5555);
				System.out.println("Bindando el servidor");
				serverSocket.bind(addr);
				System.out.println("Aceptado peticiones");
				System.out.println("============================");
				while(true) {
					redirigirPeticion(serverSocket);
					
				}	
			}catch(Exception e) {
				e.printStackTrace();
		}
	}

	private void redirigirPeticion(ServerSocket serverSocket) {
		try{
			Socket newSocket = serverSocket.accept();
			ServidorServicio servService = new ServidorServicio(newSocket);
			servService.start();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
