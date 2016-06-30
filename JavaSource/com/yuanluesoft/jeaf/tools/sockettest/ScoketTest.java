package com.yuanluesoft.jeaf.tools.sockettest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author linchuan
 *
 */
public class ScoketTest {

	public static void main(String[] args) {
		new ScoketTest().test();
	}
	
	public void test() {
		ReceiveThread receiveThread = new ReceiveThread();
		receiveThread.start();
		try {
			Thread.sleep(1000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		Sender sender = new Sender();
		sender.send();
		try {
			receiveThread.receiverSocket.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		receiveThread.stopped = true;
		receiveThread.interrupt();
	}
	
	private class Sender {
		
		public void send() {
			try {
				Socket senderSocket = new Socket(InetAddress.getByName("127.0.0.1"), 8866);
				System.out.println("SEND: old buffer size is " + senderSocket.getSendBufferSize());
				senderSocket.setSendBufferSize(1000000);
				System.out.println("SEND: new buffer size is " + senderSocket.getSendBufferSize());
				try {
					Thread.sleep(1000);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				senderSocket.setTcpNoDelay(true); //禁用Nagle算法,保证传输的实时性
				ObjectOutputStream outputStream = new ObjectOutputStream(senderSocket.getOutputStream());
				//senderSocket.setTcpNoDelay(true); //禁用Nagle算法,保证传输的实时性
				for(int i=0; i<1; i++) {
					System.out.println("SEND: " + System.currentTimeMillis() + ", sending...");
					outputStream.writeObject(new String(i + ": 1"));
					outputStream.flush();
					System.out.println("SEND: " + System.currentTimeMillis() + ", sent.");
				}
				try {
					Thread.sleep(3000);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				senderSocket.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private class ReceiveThread extends Thread {
		private ServerSocket receiverSocket;
		private boolean stopped = false;

		public ReceiveThread() {
			
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			super.run();
			try {
				receiverSocket = new ServerSocket(8866);
				System.out.println("RECV: old buffer size is " + receiverSocket.getReceiveBufferSize());
				receiverSocket.setReceiveBufferSize(1000000);
				System.out.println("RECV: new buffer size is " + receiverSocket.getReceiveBufferSize());
			}
			catch (IOException e) {
				e.printStackTrace();
				return;
			}
			while(!stopped) {
				try {
					while (!stopped) {
						Socket socket = receiverSocket.accept();
						//修改接收缓存大小
						System.out.println("RECV: old buffer size is " + socket.getReceiveBufferSize());
						socket.setReceiveBufferSize(1000000);
						System.out.println("RECV: new buffer size is " + socket.getReceiveBufferSize());
						//socket.setTcpNoDelay(true); //禁用Nagle算法,保证传输的实时性
						//创建线程处理连接
						new ReceiveHandler(this, socket).start();
					}
				}
				catch (Exception e) {
					return;
				}
			}
		}
	}
	
	/**
	 * 接收处理
	 * @author linchuan
	 *
	 */
	private class ReceiveHandler extends Thread {
		private ObjectInputStream inputStream;
		private ReceiveThread receiveThread;
		
		public ReceiveHandler(ReceiveThread receiveThread, Socket socket) {
			try {
				this.receiveThread = receiveThread;
				inputStream = new ObjectInputStream(socket.getInputStream());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			while(!receiveThread.stopped) {
				Object obj;
				try {
					obj = inputStream.readObject();
					System.out.println("RECV:" + System.currentTimeMillis() + "," + obj);
				}
				catch(Exception e) {
					
				}
			}
		}
	}
}
