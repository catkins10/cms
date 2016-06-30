package com.yuanluesoft.jeaf.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.LockInfo;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.management.ManagementFactory;
import sun.tools.attach.HotSpotVirtualMachine;
import sun.tools.attach.WindowsAttachProvider;

import com.yuanluesoft.jeaf.monitor.model.JvmClassInstance;
import com.yuanluesoft.jeaf.monitor.model.JvmInfo;
import com.yuanluesoft.jeaf.monitor.model.JvmMemoryUsage;
import com.yuanluesoft.jeaf.monitor.model.JvmThread;
import com.yuanluesoft.jeaf.util.ObjectSerializer;

/**
 * 
 * @author chuan
 *
 */
public class JvmMonitorServlet extends HttpServlet {
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!request.getServerName().equals("localhost") && !request.getServerName().equals("127.0.0.1")) { //只允许从本机调用
			return;
		}
		JvmInfo jvmInfo = new JvmInfo();
		jvmInfo.setMemoryUsages(listMemoryUsages()); //获取内存使用情况
		if("true".equals(request.getParameter("monitorThreads"))) {
			monitorThreads(jvmInfo); //监控线程
		}
		if("true".equals(request.getParameter("monitorClassInstances"))) {
			jvmInfo.setClassInstances(listInstances()); //获取类实例列表
		}
		response.getOutputStream().write(ObjectSerializer.serialize(jvmInfo));
	}
	
	/**
	 * 获取内存使用情况
	 * @return
	 */
	private List listMemoryUsages() {
		List memoryUsages = new ArrayList();
		
		//获取堆内存使用情况
		MemoryUsage memoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
		memoryUsages.add(new JvmMemoryUsage("heap", memoryUsage.getUsed(), (0.0 + memoryUsage.getUsed()) / memoryUsage.getMax()));
		
		//获取非堆内存使用情况
		memoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage(); 
		memoryUsages.add(new JvmMemoryUsage("NonHeap", memoryUsage.getUsed(), (0.0 + memoryUsage.getUsed()) / memoryUsage.getMax()));
		
		//获取明细项目
		List memoryPoolMXBeans =  ManagementFactory.getMemoryPoolMXBeans();
		for(Iterator iterator = memoryPoolMXBeans.iterator(); iterator.hasNext();) {
			MemoryPoolMXBean mxBean = (MemoryPoolMXBean)iterator.next();
			double ratio = (0.0 + mxBean.getUsage().getUsed()) / mxBean.getUsage().getMax();
			memoryUsages.add(new JvmMemoryUsage(mxBean.getName(), mxBean.getUsage().getUsed(), ratio));
		}
		return memoryUsages;
	}
	
	/**
	 * 监控线程
	 * @param jvmInfo
	 */
	private void monitorThreads(JvmInfo jvmInfo) {
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		jvmInfo.setThreadCount(threadMXBean.getThreadCount()); //总共线程数
		long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
		jvmInfo.setDeadlockedThreads(deadlockedThreads==null ? 0 : deadlockedThreads.length); //死锁线程数
		jvmInfo.setPeakThreadCount(threadMXBean.getPeakThreadCount()); //JVM从启动开始最大线程数量

		//获取线程列表
		jvmInfo.setThreads(new ArrayList());
		ThreadInfo[] threadInfo = threadMXBean.dumpAllThreads(true, true);
		for(int i = 0; i < threadInfo.length; i++) {
			JvmThread jvmThread = new JvmThread();
			jvmThread.setThreadName(threadInfo[i].getThreadName()); //名称
			jvmThread.setThreadId(threadInfo[i].getThreadId()); //ID
			jvmThread.setBlockedTime(threadInfo[i].getBlockedTime());
			jvmThread.setBlockedCount(threadInfo[i].getBlockedCount());
			jvmThread.setWaitedTime(threadInfo[i].getWaitedTime());
			jvmThread.setWaitedCount(threadInfo[i].getWaitedCount());
			LockInfo lockInfo = threadInfo[i].getLockInfo();
			if(lockInfo!=null) {
				jvmThread.setLockClassName(lockInfo.getClassName());
				jvmThread.setLockIdentityHashCode(lockInfo.getIdentityHashCode());
			}
			jvmThread.setLockName(threadInfo[i].getLockName());
			jvmThread.setLockOwnerId(threadInfo[i].getLockOwnerId());
			jvmThread.setLockOwnerName(threadInfo[i].getLockOwnerName());
			jvmThread.setInNative(threadInfo[i].isInNative());
			jvmThread.setSuspended(threadInfo[i].isSuspended());
			jvmThread.setThreadState(threadInfo[i].getThreadState().toString());
			
			//调用堆栈
			String stackTrace = null;
			StackTraceElement[] stackElement = threadInfo[i].getStackTrace();  
			for (int j = 0; j < stackElement.length; j++) {
				stackTrace = (stackTrace==null ? "" : stackTrace + "\r\n") +
							 stackElement[j].getClassName() + "." + stackElement[j].getMethodName() +
							 "(" + stackElement[j].getFileName() + ":" + stackElement[j].getLineNumber() + ")";
			}
			jvmThread.setStackTrace(stackTrace); //堆栈追踪
			jvmInfo.getThreads().add(jvmThread);
		}  
	}
	
	/**
	 * 获取类实例列表
	 * @return
	 */
	private List listInstances() {
		//获取PID
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		String name = runtimeMXBean.getName();
		String pid = name.substring(0, name.indexOf('@'));
		
		//这里要区分操作系统
		HotSpotVirtualMachine machine = null;
		InputStream inputStream = null;
		BufferedReader reader = null;
		List instances = new ArrayList();
		Pattern pattern = Pattern.compile(": *([^ ]*) *([^ ]*) *([^ ]*)");
		try {
			machine = (HotSpotVirtualMachine)new WindowsAttachProvider().attachVirtualMachine(pid);
			inputStream = machine.heapHisto(new String[]{"-all"});
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while((line = reader.readLine())!=null) {
				Matcher match = pattern.matcher(line);
				if(!match.find()) {
					continue;
				}
				JvmClassInstance classInstance = new JvmClassInstance();
				classInstance.setClassName(match.group(3));
				classInstance.setInstanceCount(Integer.parseInt(match.group(1)));
				classInstance.setMemoryUsed(Integer.parseInt(match.group(2)));
				instances.add(classInstance);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				inputStream.close();
			}
			catch(Exception e) {
				
			}
			try {
				reader.close();
			}
			catch(Exception e) {
				
			}
			try {
				machine.detach();
			}
			catch(Exception e) {
				
			}
		}
		return instances;
	}
}