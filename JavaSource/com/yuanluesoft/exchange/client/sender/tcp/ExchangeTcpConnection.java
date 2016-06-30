package com.yuanluesoft.exchange.client.sender.tcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.yuanluesoft.exchange.client.sender.ExchangeConnection;

/**
 * TCP连接
 * @author linchuan
 *
 */
public class ExchangeTcpConnection extends ExchangeConnection {
	private Socket socket;

	public ExchangeTcpConnection(InputStream inputStream, OutputStream outputStream, Socket socket) {
		super(inputStream, outputStream);
		this.socket = socket;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.exchange.client.sender.ExchangeConnection#close()
	 */
	public void close() {
		super.close();
		try {
			socket.close();
		}
		catch(Exception e) {
			
		}
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * @param socket the socket to set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
