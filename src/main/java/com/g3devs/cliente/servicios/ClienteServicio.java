package com.g3devs.cliente.servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.g3devs.cliente.juegos.Dado;

public class ClienteServicio extends Thread {

	private String nickName;

	// Cliente extends Thread ( para varios clientes 10 )
	// Pasar por parametros primera comunicacion para crear o unirse a partida
	// Espera a recibir respuesta (dormir conexion para ocupar menos memoria)
	// Recibir info de contrincante
	// Jugar partida contra contrincante usando clase Dados (Empieza visitante)
	// Pasar por parametros segunda comunicacion para finalizar partida (Solo Hosts)

	public ClienteServicio(String nombre) {
		nickName = nombre;
	}

	public void run() {
		try (Socket clientSocket = new Socket()) {
			InetSocketAddress addr = new InetSocketAddress("localhost", 5555);
			clientSocket.connect(addr);
			enviarMensaje(clientSocket);
			leerRespuesta(clientSocket);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void leerRespuesta(Socket clientSocket) {
		try {
			InputStream is = clientSocket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			String respuesta = reader.readLine();
			System.out.println(respuesta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void enviarMensaje(Socket clientSocket) {
		try {
			OutputStream os = clientSocket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			PrintWriter writer = new PrintWriter(osw);
			writer.println("crear:dados:" + nickName);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unused", "resource" })
	private void crearSocket(String ip, int puerto) {
		try (ServerSocket ss = new ServerSocket();) {
			InetSocketAddress add = new InetSocketAddress(ip, puerto);
			ss.bind(add);
			try (Socket socket = ss.accept();) {
				InputStream in = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(isr);
				String rp = br.readLine();
				String inf[] = rp.split(":");
				int num = Integer.parseInt(inf[0]);
				Dado dado = new Dado();
				int v = dado.roll();
				if (num < v) {
					System.out.println("Has ganado la partida");
					cerrarPartida(socket, ip, nickName);

				} else {
					System.out.println("Has perdido la partida");
					cerrarPartida(socket, ip, inf[1]);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void cerrarPartida(Socket clientSocket, String ip, String nickName) {
		try {
			OutputStream os = clientSocket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			PrintWriter w = new PrintWriter(osw);
			w.print("cerrar partida :" + ip + "El ganador de la partida es :" + nickName);
			w.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
