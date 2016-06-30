package com.yuanluesoft.jeaf.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.callback.ProcessCallback;

/**
 * 
 * @author linchuan
 *
 */
public class ProcessUtils {
	
	/**
	 * 运行命令
	 * @param commands 命令(字符串)列表
	 * @param environmentParameters 环境变量
	 * @param processCallback
	 * @return
	 */
	public static int executeCommand(List commands, Map environmentParameters, final ProcessCallback processCallback) {
		try {
			if(Logger.isTraceEnabled()) {
				Logger.trace(ListUtils.join(commands, " ", false));
			}
			ProcessBuilder builder = new ProcessBuilder(commands);
			if(environmentParameters!=null) {
				Map environment = builder.environment();
				for(Iterator iterator = environmentParameters.keySet().iterator(); iterator.hasNext();) {
					Object key = iterator.next();
					environment.put(key, environmentParameters.get(key));
				}
			}
			Process process = builder.start();
			//启动单独的线程来清空process.getInputStream()的缓冲区
			final InputStream stdout = process.getInputStream();
			new Thread(new Runnable() {
			    public void run() {
			    	outputProcessData(stdout, false, processCallback);
			    }
			}).start();
			//清空process.getErrorStream()的缓冲区
			outputProcessData(process.getErrorStream(), true, processCallback);
			return process.exitValue();
		}
		catch (Exception e) {
			Logger.exception(e);
			return -1;
		}
	}
	
	/**
	 * 输出进程数据
	 * @param input
	 * @param processCallback
	 */
	private static void outputProcessData(InputStream input, boolean isError, final ProcessCallback processCallback) {
		String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(input)); 
        try {
			while((line=reader.readLine())!=null) {
				if(Logger.isTraceEnabled()) {
					Logger.trace(line);
				}
				if(processCallback!=null) {
					processCallback.processOutput(line, isError);
				}
			}
		}
        catch (IOException e) {
        	Logger.exception(e);
		}
        finally {
        	try {
        		reader.close();
        	}
        	catch(Exception e) {

        	}
        }
	}
}