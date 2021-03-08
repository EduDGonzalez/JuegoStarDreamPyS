package com.g3devs.servidor.main;

import com.g3devs.servidor.servicios.ServidorReceptor;

public class ServidorMain {

	public static void main(String[] args) {
		ServidorReceptor serv = new ServidorReceptor();
		serv.init();
	}

}
