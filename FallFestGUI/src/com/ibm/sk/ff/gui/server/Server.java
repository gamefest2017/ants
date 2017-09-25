package com.ibm.sk.ff.gui.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.ibm.sk.ff.gui.Facade;
import com.ibm.sk.ff.gui.config.Config;
import com.sun.net.httpserver.HttpServer;

public class Server {
	
	private final HttpServer SERVER; 
	
	public Server(Facade facade) throws NumberFormatException, IOException {
		SERVER = HttpServer.create(new InetSocketAddress(Config.HOSTNAME.toString(), Integer.parseInt(Config.PORT.toString())), -1);
		SERVER.createContext("/", new RequestHandler(facade));
		SERVER.setExecutor(null);
		SERVER.start();
	}
	
}
