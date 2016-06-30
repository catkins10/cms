package com.yuanluesoft.jeaf.network.datagramchannel.serverchannel.listener;

import com.yuanluesoft.jeaf.network.datagramchannel.model.DatagramConnection;
import com.yuanluesoft.jeaf.network.datagramchannel.serverchannel.DatagramServerChannel;

/**
 * 数据报通道侦听器
 * @author linchuan
 *
 */
public interface DatagramChannelListener {
	
	/**
	 * 有数据到达
	 * @param channel
	 * @param connection
	 */
	public void onDataArrive(DatagramServerChannel channel, DatagramConnection connection);
}