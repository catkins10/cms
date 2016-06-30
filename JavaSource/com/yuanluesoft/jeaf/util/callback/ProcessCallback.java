package com.yuanluesoft.jeaf.util.callback;

/**
 * 
 * @author linchuan
 *
 */
public interface ProcessCallback {
	
	/**
	 * 处理输出的数据
	 * @param output
	 * @param isError
	 */
	public void processOutput(String output, boolean isError);
}
