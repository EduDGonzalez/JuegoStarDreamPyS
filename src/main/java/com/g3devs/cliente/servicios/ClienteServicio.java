package com.g3devs.cliente.servicios;

import com.g3devs.cliente.juegos.Dado;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClienteServicio extends Thread{
	
	private String nickName;
	
	public ClienteServicio(String nombre) {
		nickName = nombre;
	}

	public void run() {
		try(Socket clientSocket = new Socket()){
			InetSocketAddress addr = new InetSocketAddress("localhost",5555);
			clientSocket.connect(addr);
			enviarMensaje(clientSocket);
			leerRespuesta(clientSocket);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void leerRespuesta(Socket clientSocket) {
		try {
			InputStream is = clientSocket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			String respuesta = reader.readLine();
			String[] respuestaDiv = respuesta.split(":");
			gestionarRespuesta(respuestaDiv);

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	void gestionarRespuesta(String[] respuestaDiv)
	{
		String[] player1 = {respuestaDiv[2],respuestaDiv[3],respuestaDiv[4],respuestaDiv[5]};
		String[] player2 = {respuestaDiv[6],respuestaDiv[7],respuestaDiv[8],respuestaDiv[9]};

		if(player1[0].equals(nickName) && player1[1].equals("true"))
		{
			crearSocket(player1[2], Integer.parseInt(player1[3]),respuestaDiv[1]);
		}
		else if(player1[0].equals(nickName) && player1[1].equals("false"))
		{
			crearInvitado(player2[2], player2[3]);
		}
		else if (player2[0].equals(nickName) && player2[1].equals("true"))
		{
			crearSocket(player2[2], Integer.parseInt(player2[3]),respuestaDiv[1]);
		}
		else if (player2[0].equals(nickName) && player2[1].equals("false"))
		{
			crearInvitado(player1[2], player1[3]);
		}
	}
	
	@SuppressWarnings({ "unused", "resource" })
	void crearSocket(String ip, int puerto, String respuestaDiv){
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
					cerrarPartida(socket, respuestaDiv, nickName);

				} else {
					System.out.println("Has perdido la partida");
					cerrarPartida(socket, respuestaDiv, inf[1]);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void crearInvitado(String ip, String puerto)
	{
		try(Socket clientSocket = new Socket();
			OutputStream os = clientSocket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			PrintWriter writer = new PrintWriter(osw)){

			InetSocketAddress addr = new InetSocketAddress(ip, Integer.parseInt(puerto));
			send(addr,clientSocket,writer);

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	void send(InetSocketAddress addr,Socket clientSocket,PrintWriter writer)
	{
		while(!clientSocket.isConnected())
		{
			try
			{
				clientSocket.connect(addr);
				int num = roll();
				String resultado = num + ":" + nickName;
				writer.println(resultado);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	int roll()
	{
		Dado dado = new Dado();
		return dado.roll();
	}

	private void enviarMensaje(Socket clientSocket) {
		try{
			OutputStream os = clientSocket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			PrintWriter writer = new PrintWriter(osw);
			writer.println("crear:dados:"+nickName);
			writer.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unused")
	private void cerrarPartida(Socket clientSocket, String id, String nickName) {
		try {
			OutputStream os = clientSocket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			PrintWriter w = new PrintWriter(osw);
			w.print("cerrar:" + id + ":"+ nickName);
			w.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
