package com.yuanluesoft.jeaf.util;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author linchuan
 *
 */
public class NetworkUtils {
	
	/**
	 * 获取本机IP,在linux下也有效
	 * @return
	 */
	public static String getLocalHostIP() {
		return getLocalHostIP(null);
	}
	
	/**
	 * 获取本机IP,在linux下也有效
	 * @return
	 */
	public static String getLocalHostIP(String prefix) {
		Enumeration netInterfaces;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
		}
		catch (SocketException e) {
			throw new Error(e);
		}
		while(netInterfaces.hasMoreElements()) { 
			NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();
			Enumeration addresses = ni.getInetAddresses();
			while(addresses.hasMoreElements()) {
				InetAddress ip = (InetAddress)addresses.nextElement();
				if(!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":")==-1) { 
					String address = ip.getHostAddress(); 
					if(prefix==null || prefix.isEmpty() || address.startsWith(prefix)) {
						return address;
					}
				}
			}
		}
		throw new Error();
	}
	
	/**
	 * 获取MAC地址列表
	 * @return
	 *
	public static List listMacAddresses() {
		InputStream input = null;
		try {
			Process process = Runtime.getRuntime().exec("ipconfig /all");
			input = process.getInputStream();
			byte[] buffer = new byte[4096];
			int readLen = input.read(buffer);
			String ipconfig = new String(buffer, 0, readLen);
			List macs = new ArrayList();
			int beginIndex = 0, endIndex;
			for(beginIndex=ipconfig.indexOf("Physical Address"); beginIndex!=-1; beginIndex=ipconfig.indexOf("Physical Address", beginIndex)) {
				endIndex = beginIndex + "Physical Address".length();
				beginIndex = ipconfig.indexOf('-', endIndex);
				if(beginIndex==-1) {
					break;
				}
				endIndex = ipconfig.indexOf('\n', beginIndex);
				if(endIndex==-1) {
					break;
				}
				macs.add(ipconfig.substring(beginIndex - 2, endIndex).trim());
				beginIndex = endIndex + 1;
			}
			return macs.isEmpty() ? null : macs;
		}
		catch(Exception e) {
			return null;
		}
		finally {
			try {
				input.close();
			}
			catch(Exception e) {
				
			}
		}
	}*/
	
	/**
	 * 获取MAC地址列表
	 */
	public static List listMacAddresses() {
		String[] commands = {"ipconfig /all", "ifconfig -a"};
		for(int i=0; i<commands.length; i++) {
			InputStreamReader input = null;
			LineNumberReader reader = null;
			try {
				Process process = Runtime.getRuntime().exec(commands[i]);
				input = new InputStreamReader(process.getInputStream());
				reader = new LineNumberReader(input);
				String line;
				List macs = new ArrayList();
				while((line = reader.readLine()) != null) {
					System.out.println(line);
					if((line.indexOf("Physical Address")>0 || line.indexOf("物理地址")>0) && line.indexOf('-')!=-1) {
						macs.add(line.substring(line.indexOf("-") - 2));
					}
					else if((line.indexOf("ether")>0 || line.indexOf("lladdr")>0) && line.indexOf(':')!=-1) {
						macs.add(line.substring(line.indexOf(":") - 2));
					}
				}
				if(!macs.isEmpty()) {
					return macs;
				}
			}
			catch(Exception e) {
				Logger.exception(e);
				return null;
			}
			finally {
				try {
					reader.close();
				}
				catch(Exception e) {
					
				}
				try {
					input.close();
				}
				catch(Exception e) {
					
				}
			}
		}
		return null;
	}
}