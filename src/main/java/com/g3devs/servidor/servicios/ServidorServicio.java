package com.g3devs.servidor.servicios;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
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
	
	public void run() {
		String [] info = leerPeticion();
		if(info[0].equalsIgnoreCase("crear")) {
			Jugador jugador = crearJugador(info);
			peticionPartida(jugador,info);
			EnviarInfo(jugador);
		}else {
			borrarPartida(info);
		}
		
	}

	private void borrarPartida(String[] info) {
		try {
			ServidorReceptor.mutex.acquire();
			System.out.println("Borrando parida con id: "+info[1]);
			System.out.println("Ganador de la partida: "+info[2]);
			listaPartidasJugando.remove(Integer.parseInt(info[1]));
			ServidorReceptor.mutex.release();
		}catch(Exception e) {
			e.printStackTrace();
		}
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
				System.out.println(ServidorReceptor.dadosJugadores.availablePermits());
				ServidorReceptor.dadosJugadores.acquire();
			}else {
				System.out.println("Buscando partida...");
				buscarPartida(jugador,info);
				ServidorReceptor.mutex.release();
				sleep(100);
				ServidorReceptor.dadosJugadores.release();	
			}
			 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	private void EnviarInfo(Jugador jugador) {
		try {
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			PrintWriter pWriter = new PrintWriter(osw);
			List<Jugador>jugadores = jugador.getPartida().getJugadores();
			String mensaje = jugador.getPartida().getTipo().getNombreTipoPartida()+":"+jugador.getPartida().getId();
			for (int i = 0; i < jugadores.size(); i++) {
				mensaje = mensaje.concat(":"+jugadores.get(i).getNickName()+":"+jugadores.get(i).isHost()+":"+jugadores.get(i).getAddres()+":"+jugadores.get(i).getPuerto());
			}
			pWriter.println(mensaje);
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
			Partida partida = new Partida();
			partida.setId(partidas);
			partida.getJugadores().add(jugador);
			partida.setTipo(tipo);
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
