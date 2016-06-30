/*
 * Created on 2005-11-27
 *
 */
package com.yuanluesoft.jeaf.messagecenter.sender.messenger;

import java.io.OutputStream;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.messagecenter.pojo.Message;
import com.yuanluesoft.jeaf.messagecenter.sender.SendException;
import com.yuanluesoft.jeaf.messagecenter.sender.Sender;
import com.yuanluesoft.jeaf.messagecenter.sender.messenger.pojo.MessengerOnline;

/**
 * 
 * @author linchuan
 *
 */
public class MessengerSender extends Sender implements Runnable {
	private DatabaseService databaseService;
    final private static int MESSENGER_PORT = 718;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#sendMessage(com.yuanluesoft.jeaf.messagecenter.pojo.Message)
	 */
	public boolean sendMessage(Message message, int feedbackDelay) throws SendException {
		//检查用户是否在线
		MessengerOnline pojoMessengerOnline = (MessengerOnline)databaseService.findRecordById(MessengerOnline.class.getName(), message.getReceivePersonId());
		if(pojoMessengerOnline==null) { //用户不在线
			return false;
		}
		Socket socket=null;
		OutputStream output=null;
		try {
		    socket=new Socket(InetAddress.getByName(pojoMessengerOnline.getIp()), pojoMessengerOnline.getPort());
			socket.setSoTimeout(5000);
			//send message
			output = socket.getOutputStream();
			SimpleDateFormat formatter=new java.text.SimpleDateFormat("MM-dd HH:mm");
			String msg = "id: " + message.getId() + "\0" + 
						 "send-time: " + formatter.format(Calendar.getInstance().getTime()) + "\0" +
						 "sender: " + message.getSenderName() + "\0" +
						 "content: " + message.getContent() + "\0" +
						 "url: " + (message.getWebLink()==null ? "" : message.getWebLink());
			output.write(msg.getBytes());
			return true;
		}
		catch(ConnectException ce) {
		    Logger.error("Send message to messenger " + pojoMessengerOnline.getIp() + ":" + pojoMessengerOnline.getPort() + " failed.");
			databaseService.deleteRecord(pojoMessengerOnline);
		}
		catch(Exception e) {
		    Logger.error("Send message to messenger " + pojoMessengerOnline.getIp() + ":" + pojoMessengerOnline.getPort() + " failed.");
		    Logger.exception(e);
		}
		finally {
			try {
				output.close();
			}
			catch(Exception ex){
				
			}
			try {
				socket.close();
			}
			catch(Exception ex) {
				
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#stopSender()
	 */
	public void stopSender() throws SendException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#loadPersonalCustom(long)
	 */
	public Object loadPersonalCustom(long personId) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.messagecenter.sender.Sender#savePersonalCustom(long, java.lang.Object)
	 */
	public void savePersonalCustom(long personId, Object config) throws ServiceException {
		
	}

	/**
	 * 启动即时消息服务器
	 * @throws Exception
	 */
	public void startMessengerServer() throws Exception {
	    new Thread(this).start();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run1() {
	    try {
	        DatagramSocket socket = new DatagramSocket(MESSENGER_PORT);
            byte buffer[] = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("Start messenger service........");
	        while (true) {
	            socket.receive(packet);
	            System.out.println("messenger receive data:" + new String(buffer) + ", port:" + packet.getPort());
	            //输出结果
	            DatagramPacket outPacket = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
	            socket.send(outPacket);
	        }
	    }
	    catch(Exception e) {
	        
	    }
	}
	public void run() {
	    if(true) {
	        return;
	    }
	    try {
	        Charset charset = Charset.forName("ISO-8859-1");
	        CharsetDecoder decoder = charset.newDecoder();
	        CharsetEncoder encoder = charset.newEncoder();

	        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
	        CharBuffer charBuffer = CharBuffer.allocate(1024);
	        InetSocketAddress socketAddress = new InetSocketAddress("www.sina.com.cn", 80);
	        
	        DatagramChannel channel = DatagramChannel.open();
	        channel.connect(socketAddress);
	        channel.configureBlocking(false);
	        
	        String request = "GET /" + " \r\n\r\n";
	       // for(int i=0; i<10000; i++) {
	        channel.write(encoder.encode(CharBuffer.wrap(request)));

	        while ((channel.read(buffer)) != -1) {
	          buffer.flip();
	          decoder.decode(buffer, charBuffer, false);
	          charBuffer.flip();
	          buffer.clear();
	          charBuffer.clear();
	        }
	        //}
	        
	        
	        
	    int channels = 0;
	    int nKeys = 0;
	    int currentSelector = 0;
	    
	    
	    //建立Channel 并绑定到9000端口
	    ServerSocketChannel ssc = ServerSocketChannel.open();
	    InetSocketAddress address = new InetSocketAddress("localhost",9000); 
	    ssc.socket().bind(address);
	    
	    //使设定non-blocking的方式。
	    ssc.configureBlocking(false);
	    
	    Selector selector = SelectorProvider.provider().openSelector(); 
	    
	    /*在服务器套接字上注册selector并设置为接受accept方法的通知。
	    这就告诉Selector，套接字想要在accept操作发生时被放在ready表
	    上，因此，允许多元非阻塞I/O发生。*/
	    SelectionKey acceptKey = ssc.register(selector,  SelectionKey.OP_ACCEPT);


	    
	    //向Selector注册Channel及我们有兴趣的事件
	    //System.out.println("NBTest: Starting selectr555555555555");
	    //printKeyInfo(s);
	    
	    for(;;) { //不断的轮询
	        if(selector.select()>0) {
	            for(Iterator iterator = selector.selectedKeys().iterator(); iterator.hasNext();) {
	                acceptKey = (SelectionKey)iterator.next();
	                //一个key被处理完成后,就都被从就绪关键字（ready keys）列表中除去
	                iterator.remove();
	                if(acceptKey.isAcceptable()) {
	                    //从channel()中取得我们刚刚注册的channel。
	                    Socket socket = ((ServerSocketChannel)acceptKey.channel()).accept().socket();
	                    SocketChannel sc = socket.getChannel();
	                    sc.configureBlocking(false);
	                    sc.register(selector, SelectionKey.OP_READ |SelectionKey.OP_WRITE);
	                    System.out.println(++channels);
	                }
	                else
	                {
	                    System.out.println("NBTest: Channel not acceptable");
	                }
	            }
	        }
	        else
	        {
	            System.out.println("NBTest: Select finished without any keys.");
	        }
	        
	    }
	    }
	    catch(Exception e) {
	        e.printStackTrace();
	    }
	    
	}

	/**
	 * @return Returns the databaseService.
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}
	/**
	 * @param databaseService The databaseService to set.
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
}
