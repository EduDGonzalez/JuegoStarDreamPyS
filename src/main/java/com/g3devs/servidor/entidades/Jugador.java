package com.g3devs.servidor.entidades;

import java.net.InetAddress;

public class Jugador {
	 
	private String nickName;
	private int puerto;
	private InetAddress addres;
	private boolean host;

	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getPuerto() {
		return puerto;
	}
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	public InetAddress getAddres() {
		return addres;
	}
	public void setAddres(InetAddress addres) {
		this.addres = addres;
	}
	public boolean isHost() {
		return host;
	}
	public void setHost(boolean host) {
		this.host = host;
	}
	
}
