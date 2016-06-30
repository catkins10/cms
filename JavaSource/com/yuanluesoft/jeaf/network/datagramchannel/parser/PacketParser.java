/*
 * Created on 2006-10-20
 *
 */
package com.yuanluesoft.jeaf.network.datagramchannel.parser;

import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.ConnectAckPacket;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.ConnectCompletePacket;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.ConnectPacket;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.DataAckPacket;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.DataPacket;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.DatagramPacket;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.p2p.P2PConnectAckPacket;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.p2p.P2PConnectPacket;
import com.yuanluesoft.jeaf.network.datagramchannel.model.packet.p2p.P2PHolePacket;
import com.yuanluesoft.jeaf.util.BufferUtils;

/**
 * 
 * @author linchuan
 *
 */
public class PacketParser {
	
    /**
     * 解析UDP包
     * @param packetData
     * @param dataLen
     * @return
     * @throws PacketParseException
     */
    public DatagramPacket parsePacket(byte[] packetData, int dataLen) throws ParseException {
        //获取包类型
    	byte command = (byte)(Math.abs(packetData[0])); //第一字节
        int[] beginIndex = {1};
        boolean turn = (packetData[0]<0);
        String remoteIp = null;
        char remotePort = 0;
        if(turn) {
	        remoteIp = BufferUtils.getString(packetData, beginIndex, packetData.length);
	        remotePort = BufferUtils.getChar(packetData, beginIndex);
        }
        DatagramPacket packet = null;
        switch(command) {
        case DatagramPacket.CMD_DATA: //数据包
            packet = parseDataPacket(packetData, dataLen, beginIndex);
            break;
            
        case DatagramPacket.CMD_DATA_ACK: //应答包
            packet = parseDataAckPacket(packetData, dataLen, beginIndex);
            break;
        
        case DatagramPacket.CMD_CONNECT: //连接包
            packet = parseConnectPacket(packetData, dataLen, beginIndex);
            break;
        
        case DatagramPacket.CMD_CONNECT_ACK: //连接应答包
            packet = parseConnectAckPacket(packetData, dataLen, beginIndex);
            break;
        
        case DatagramPacket.CMD_CONNECT_COMPLETE: //连接确认包
            packet = parseConnectCompletePacket(packetData, dataLen, beginIndex);
            break;
        
        case DatagramPacket.CMD_P2P_CONNECT: //p2p连接请求包
        case DatagramPacket.CMD_P2P_FORWARD_CONNECT: //p2p(转发方式)连接请求包
            packet = parseP2PConnectPacket(command==DatagramPacket.CMD_P2P_FORWARD_CONNECT, packetData, dataLen, beginIndex);
            break;

        case DatagramPacket.CMD_P2P_CONNECT_ACK: //p2p连接请求应答包
        case DatagramPacket.CMD_P2P_FORWARD_CONNECT_ACK: //p2p(转发方式)连接请求应答包
            packet = parseP2PConnectAckPacket(command==DatagramPacket.CMD_P2P_FORWARD_CONNECT_ACK, packetData, dataLen, beginIndex);
            break;

        case DatagramPacket.CMD_P2P_HOLE: //p2p打洞
            packet = parseP2PHolePacket(packetData, dataLen, beginIndex);
            break;
        }
        if(packet==null) {
            throw new ParseException();
        }
        packet.setCommand(command); //包命令
    	packet.setRemoteIp(remoteIp); //转发时的数据接收人IP
    	packet.setRemotePort(remotePort); //转发时的数据接收人端口
    	if(turn) {
    		packet.setTurnIp("TURN");
    	}
    	return packet;
    }
        
    /**
     * 填充发送缓存
     * @param packet
     * @param buffer
     * @return
     * @throws ParseException
     */
    public int putPacketBuffer(DatagramPacket packet, byte[] buffer) throws ParseException {
        int beginIndex = BufferUtils.putByte((byte)(packet.getCommand() * (packet.getTurnPort()>0 ? -1 : 1)), buffer, 0);
        if(packet.getTurnPort()>0) { //转发包
	    	beginIndex = BufferUtils.putString(packet.getRemoteIp(), buffer, beginIndex); //对方主机IP
	    	beginIndex = BufferUtils.putChar(packet.getRemotePort(), buffer, beginIndex); //对方主机端口
        }
        switch(packet.getCommand()) {
        case DatagramPacket.CMD_DATA: //数据包
        	beginIndex = putDataPacket((DataPacket)packet, buffer, beginIndex);
        	break;
      
        case DatagramPacket.CMD_DATA_ACK: //应答包
        	beginIndex = putDataAckPacket((DataAckPacket)packet, buffer, beginIndex);
        	break;
        	
        case DatagramPacket.CMD_CONNECT: //连接包
        	beginIndex = putConnectPacket((ConnectPacket)packet, buffer, beginIndex);
        	break;
        	
        case DatagramPacket.CMD_CONNECT_ACK: //连接应答包
        	beginIndex = putConnectAckPacket((ConnectAckPacket)packet, buffer, beginIndex);
        	break;
        	
    	case DatagramPacket.CMD_CONNECT_COMPLETE: //连接应答包
    		beginIndex = putConnectCompletePacket((ConnectCompletePacket)packet, buffer, beginIndex);
        	break;
        	
        case DatagramPacket.CMD_P2P_CONNECT: //p2p连接请求包
        case DatagramPacket.CMD_P2P_FORWARD_CONNECT: //p2p(转发方式)连接请求包
        	beginIndex = putP2PConnectPacket((P2PConnectPacket)packet, buffer, beginIndex);
            break;

        case DatagramPacket.CMD_P2P_CONNECT_ACK: //p2p连接请求应答包
        case DatagramPacket.CMD_P2P_FORWARD_CONNECT_ACK: //p2p(转发方式)连接请求应答包
        	beginIndex = putP2PConnectAckPacket((P2PConnectAckPacket)packet, buffer, beginIndex);
            break;
            
        case DatagramPacket.CMD_P2P_HOLE: //p2p打洞
        	beginIndex = putP2PHolePacket((P2PHolePacket)packet, buffer, beginIndex);
            break;
        }
        return beginIndex;
    }
    
    /**
     * 解析数据包
     * @param packetData
     * @return
     * @throws ParseException
     */
    private DataPacket parseDataPacket(byte[] packetData, int dataLen, int[] beginIndex) throws ParseException {
        DataPacket dataPacket = new DataPacket();
        //解析顺序号
    	dataPacket.setSequence(BufferUtils.getChar(packetData, beginIndex));
    	byte values = BufferUtils.getByte(packetData, beginIndex);
    	dataPacket.setWindowSize((byte)(values & 0x0f)); //解析窗口大小
    	dataPacket.setFirstPacket((values & 0x80)!=0); //是否第一个数据包
    	dataPacket.setLastPacket((values & 0x40)!=0); //是否最后一个数据包
    	dataPacket.setAck((values & 0x20)!=0); //是否需要应答
    	dataPacket.setResponse((values & 0x10)!=0); //是否应答的数据
    	//解析校验码
    	dataPacket.setValidateCode(BufferUtils.getByte(packetData, beginIndex));
    	//解析数据
    	dataPacket.setData(BufferUtils.getBytes(packetData, beginIndex, dataLen - beginIndex[0]));
        return dataPacket;
    }
    
    /**
     * 解析应答包
     * @param packetData
     * @return
     * @throws ParseException
     */
    private DataAckPacket parseDataAckPacket(byte[] packetData, int dataLen, int[] beginIndex) throws ParseException {
        DataAckPacket ackPacket = new DataAckPacket();
        ackPacket.setNextReceiveSequence(BufferUtils.getChar(packetData, beginIndex));
        ackPacket.setRefused(BufferUtils.getBoolean(packetData, beginIndex)); //是否被拒绝
        return ackPacket;
    }
    
    /**
     * 解析连接包
     * @param packetData
     * @param dataLen
     * @return
     * @throws ParseException
     */
    private ConnectPacket parseConnectPacket(byte[] packetData, int dataLen, int[] beginIndex) throws ParseException {
    	ConnectPacket connectPacket = new ConnectPacket();
    	return connectPacket;
    }

    /**
     * 解析连接应答包
     * @param packetData
     * @param dataLen
     * @param beginIndex
     * @return
     * @throws ParseException
     */
    private ConnectAckPacket parseConnectAckPacket(byte[] packetData, int dataLen, int[] beginIndex) throws ParseException {
    	ConnectAckPacket connectAckPacket = new ConnectAckPacket();
    	connectAckPacket.setConnecterIp(BufferUtils.getString(packetData, beginIndex, dataLen));
    	connectAckPacket.setConnecterPort(BufferUtils.getChar(packetData, beginIndex));
        return connectAckPacket;
    }

    /**
     * 解析连接确认包
     * @param packetData
     * @param dataLen
     * @param beginIndex
     * @return
     * @throws ParseException
     */
    private ConnectCompletePacket parseConnectCompletePacket(byte[] packetData, int dataLen, int[] beginIndex) throws ParseException {
    	ConnectCompletePacket connectCompletePacket = new ConnectCompletePacket();
    	return connectCompletePacket;
    }
    
    /**
     * 解析p2p连接请求包
     * @param packetData
     * @param dataLen
     * @return
     * @throws ParseException
     */
    private P2PConnectPacket parseP2PConnectPacket(boolean forwardMode, byte[] packetData, int dataLen, int[] beginIndex) throws ParseException {
    	P2PConnectPacket packet = new P2PConnectPacket(forwardMode);
    	packet.setPeerIp(BufferUtils.getString(packetData, beginIndex, packetData.length)); //对方IP
    	packet.setPeerPort(BufferUtils.getChar(packetData, beginIndex)); //对方端口
        return packet;
    }
    
    /**
     * 解析p2p连接请求应答包
     * @param packetData
     * @param dataLen
     * @return
     * @throws ParseException
     */
    private P2PConnectAckPacket parseP2PConnectAckPacket(boolean forwardMode, byte[] packetData, int dataLen, int[] beginIndex) throws ParseException {
    	P2PConnectAckPacket packet = new P2PConnectAckPacket(forwardMode);
    	packet.setPeerIp(BufferUtils.getString(packetData, beginIndex, packetData.length)); //对方IP
    	packet.setPeerPort(BufferUtils.getChar(packetData, beginIndex)); //对方端口
    	return packet;
    }
    
    /**
     * 解析p2p打洞包
     * @param packetData
     * @param dataLen
     * @return
     * @throws ParseException
     */
    private P2PHolePacket parseP2PHolePacket(byte[] packetData, int dataLen, int[] beginIndex) throws ParseException {
    	P2PHolePacket packet = new P2PHolePacket();
        return packet;
    }
    
    /**
     * 填充数据包
     * @param packet
     * @param buffer
     * @return
     * @throws ParseException
     */
    private int putDataPacket(DataPacket packet, byte[] buffer, int beginIndex) throws ParseException {
    	beginIndex = BufferUtils.putChar(packet.getSequence(), buffer, beginIndex); //顺序号
        byte values = packet.getWindowSize(); //窗口尺寸
        if(packet.isFirstPacket()) { //是否第一个数据包
        	values |= 0x80; 
        }
        if(packet.isLastPacket()) { //是否最后一个数据包
        	values |= 0x40; 
        }
        if(packet.isAck()) { //是否需要应答
        	values |= 0x20; 
        }
        if(packet.isResponse()) { //是否应答的数据
        	values |= 0x10; 
        }
        beginIndex = BufferUtils.putByte(values, buffer, beginIndex);
    	//输出校验码
        beginIndex = BufferUtils.putByte(packet.getValidateCode(), buffer, beginIndex);
        return BufferUtils.putBytes(packet.getData(), buffer, beginIndex); //数据
    }
  
    /**
     * 填充应答包到缓存
     * @param packet
     * @param buffer
     * @return
     * @throws ParseException
     */
    private int putDataAckPacket(DataAckPacket packet, byte[] buffer, int beginIndex) throws ParseException {
    	beginIndex = BufferUtils.putChar(packet.getNextReceiveSequence(), buffer, beginIndex);
        return BufferUtils.putBoolean(packet.isRefused(), buffer, beginIndex);
    }
    
    /**
     * 填充连接包到缓存
     * @param packet
     * @param buffer
     * @return
     * @throws ParseException
     */
    private int putConnectPacket(ConnectPacket packet, byte[] buffer, int beginIndex) throws ParseException {
    	return beginIndex;
    }
    
    /**
     * 填充连接应答包到缓存
     * @param packet
     * @param buffer
     * @param beginIndex
     * @return
     * @throws ParseException
     */
    private int putConnectAckPacket(ConnectAckPacket packet, byte[] buffer, int beginIndex) throws ParseException {
    	beginIndex = BufferUtils.putString(packet.getConnecterIp(), buffer, beginIndex);
        return BufferUtils.putChar(packet.getConnecterPort(), buffer, beginIndex);
    }
    
    /**
     * 填充连接确认包到缓存
     * @param packet
     * @param buffer
     * @param beginIndex
     * @return
     * @throws ParseException
     */
    private int putConnectCompletePacket(ConnectCompletePacket packet, byte[] buffer, int beginIndex) throws ParseException {
        return beginIndex;
    }
    
    /**
     * 填充p2p连接请求包
     * @param packet
     * @param buffer
     * @return
     * @throws ParseException
     */
    private int putP2PConnectPacket(P2PConnectPacket packet, byte[] buffer, int beginIndex) throws ParseException {
    	beginIndex = BufferUtils.putString(packet.getPeerIp(), buffer, beginIndex);
    	return BufferUtils.putChar(packet.getPeerPort(), buffer, beginIndex);
    }
    
    /**
     * 填充p2p连接请求应答包
     * @param packet
     * @param buffer
     * @return
     * @throws ParseException
     */
    private int putP2PConnectAckPacket(P2PConnectAckPacket packet, byte[] buffer, int beginIndex) throws ParseException {
    	beginIndex = BufferUtils.putString(packet.getPeerIp(), buffer, beginIndex);
    	return BufferUtils.putChar(packet.getPeerPort(), buffer, beginIndex);
    }
    
    /**
     * 填充p2p打洞包
     * @param packet
     * @param buffer
     * @return
     * @throws ParseException
     */
    private int putP2PHolePacket(P2PHolePacket packet, byte[] buffer, int beginIndex) throws ParseException {
        return beginIndex;
    }
}