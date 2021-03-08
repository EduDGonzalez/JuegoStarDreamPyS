package com.g3devs.cliente.servicios;

import com.g3devs.cliente.juegos.Dado;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

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
			crearSocket(player1[2], Integer.parseInt(player1[3]));
		}
		else if(player1[0].equals(nickName) && player1[1].equals("false"))
		{
			crearInvitado(player2[2], player2[3]);
		}
		else if (player2[0].equals(nickName) && player2[1].equals("true"))
		{
			crearSocket(player2[2], Integer.parseInt(player2[3]));
		}
		else if (player2[0].equals(nickName) && player2[1].equals("false"))
		{
			crearInvitado(player1[2], player1[3]);
		}
	}

	void crearSocket(String ip, int puerto)
	{

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
}
