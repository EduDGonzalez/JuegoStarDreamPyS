package com.g3devs.servidor.servicios;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.g3devs.servidor.entidades.Jugador;
import com.g3devs.servidor.entidades.Partida;
import com.g3devs.servidor.entidades.TipoPartida;

public class ServidorServicio extends Thread {
	
	private Socket socket;
	private static Map<TipoPartida,Partida> listaPartidasEsperando = new HashMap<>();
	private static Map<Integer, Partida> listaPartidasJugando = new HashMap<>();
	private static int partidas = 1;

	
	public ServidorServicio (Socket socket) {
		this.socket = socket;
	}
	
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
		String [] info = leerPeticion();
		
		Jugador jugador = crearJugador(info);
			peticionPartida(jugador,info);
	}

	private void peticionPartida(Jugador jugador, String[] info) {
		try {
			ServidorReceptor.mutex.acquire();
			if(listaPartidasEsperando.isEmpty()) {
				System.out.println("creando partida "+partidas);
				jugador.setHost(true);
				crearPartida(jugador,info);
				partidas++;
				ServidorReceptor.mutex.release();
				ServidorReceptor.dadosJugadores.acquire();
			}else {
				System.out.println("Buscando partida...");
				buscarPartida(jugador,info);
				ServidorReceptor.mutex.release();
				ServidorReceptor.dadosJugadores.release();
			}
			 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(listaPartidasJugando.size());
		EnviarInfo(jugador);
	}

	private void EnviarInfo(Jugador jugador) {
		try {
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			PrintWriter pWriter = new PrintWriter(osw);
			
			pWriter.println("Recibido");
			pWriter.flush();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void buscarPartida(Jugador jugador, String[] info) {
		for (Map.Entry<TipoPartida, Partida> game : listaPartidasEsperando.entrySet()) {
			if(game.getKey().getNombreTipoPartida().equalsIgnoreCase(info[1])) {
				game.getValue().getJugadores().add(jugador);
				jugador.setPartida(game.getValue());
				if(game.getValue().getJugadores().size()==game.getValue().getTipo().getMaxJugadores()) {
						Partida partida = listaPartidasEsperando.remove(game.getKey());
						listaPartidasJugando.put(partida.getId(), partida);
					}
				}
			}
		
	}

	private void crearPartida(Jugador jugador, String[] info) {
		switch (info[1]) {
		case "dados":
			TipoPartida tipo = TipoPartida.DADOS;
			Partida partida = new Partida(partidas,jugador,tipo);
			jugador.setPartida(partida);
			listaPartidasEsperando.put(TipoPartida.DADOS, partida);
			break;
		default:
			//Sin implementar
			break;
		}
		
	}

	private Jugador crearJugador(String[] info) {
		Jugador player = new Jugador();
		player.setNickName(info[2]);
		player.setAddres(socket.getInetAddress());
		player.setPuerto(socket.getPort());
		player.setHost(false);
		return player;
	}

	private String[] leerPeticion() {
		String texto="";
		try{
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			texto = reader.readLine();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return texto.split(":");	
	}

}
