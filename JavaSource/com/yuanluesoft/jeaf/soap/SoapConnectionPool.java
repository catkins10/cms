/*
 * Created on 2006-8-24
 *
 */
package com.yuanluesoft.jeaf.soap;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;

/**
 * SOAP连接池
 * @author linchuan
 * 
 */
public class SoapConnectionPool {
    private List connections = new ArrayList(); //soap连接列表
    private long lastCleanTime = System.currentTimeMillis(); //上次清理连接的时间
    private int cleanHours = 24; //清理周期
    private int maxIdle = 10; //最大空闲连接
    
    /**
     * 调用SOAP服务的方法
     * @param serviceName
     * @param method
     * @param soapPassport
     * @param args
     * @param typeMappings
     * @return
     * @throws Exception
     */
    public Object invokeRemoteMethod(String serviceName, String methodName, SoapPassport soapPassport, Object[] args, Class[] typeMappings) throws Exception {
        SoapConnection connection = getConnection(serviceName, soapPassport);
        setBusy(connection, true);
        try {
        	if(typeMappings!=null) {
        		//注册用户自定义的数据类型
        		for(int i=0; i<typeMappings.length; i++) {
        			String className = typeMappings[i].getName();
        			QName qn = new QName("urn:BeanService", className.substring(className.lastIndexOf('.') + 1));
        			connection.getCall().registerTypeMapping(typeMappings[i], qn, new BeanSerializerFactory(typeMappings[i], qn), new BeanDeserializerFactory(typeMappings[i], qn));
        		}
        	}
        	//设置方法名称
        	connection.getCall().setOperationName(methodName);
        	connection.setLastAccess(System.currentTimeMillis());
        	//调用远程方法
        	return connection.getCall().invoke(args);
        	/*
        	Call call = connection.getCall();
        	Object o = call.invoke(args);
        	System.out.println(call.getResponseMessage().getMimeHeaders().getHeader("content-type")[0]);
        	ByteArrayOutputStream a = new ByteArrayOutputStream(10000);
        	call.getResponseMessage().writeTo(a);
        	System.out.println(a.toString());
        	return o; */
        }
        finally {
        	setBusy(connection, false);
        }
    }
    
    /**
     * 调用.net远程方法
     * @param serviceName
     * @param methodName
     * @param returnType
     * @param soapPassport
     * @param args //参数列表
     * @param argNames //参数名称列表
     * @param argTypes //参数类型列表
     * @param mappingNames
     * @param mappingClasses
     * @return
     * @throws Exception
     */
    public Object invokeRemoteMethod(String serviceName, String methodName, QName returnType, Class returnClass, SoapPassport soapPassport, Object[] args, String[] argNames, QName[] argTypes, QName[] mappingNames, Class[] mappingClasses) throws Exception {
    	SoapConnection connection = getConnection(serviceName, soapPassport);
        setBusy(connection, true);
        try {
        	Call call = connection.getCall();
        	//设置方法名称
        	call.setOperationName(new QName(soapPassport.getNamespaceURI(), methodName));
        	call.removeAllParameters();
        	if(argNames!=null) { //设置参数列表
        		for(int i=0; i<argNames.length; i++) {
        			call.addParameter(new QName(soapPassport.getNamespaceURI(), argNames[i]), argTypes[i], javax.xml.rpc.ParameterMode.IN);
        		}
        	}
        	if(returnType!=null) {
        		call.setReturnType(returnType);
        	}
        	if(returnClass!=null) {
        		call.setReturnClass(returnClass);
        	}
        	if(mappingClasses!=null && mappingClasses.length>0) {
        		for(int i=0; i<mappingClasses.length; i++) {
        			call.registerTypeMapping(mappingClasses[i], mappingNames[i], new BeanSerializerFactory(mappingClasses[i], mappingNames[i]), new BeanDeserializerFactory(mappingClasses[i], mappingNames[i]));
        		}
        	}
			call.setUseSOAPAction(true);
			call.setSOAPActionURI(soapPassport.getNamespaceURI()+ methodName);
			connection.setLastAccess(System.currentTimeMillis());
			//调用远程方法
			return call.invoke(args);
			/*Object o = call.invoke(args);
        	System.out.println(call.getResponseMessage().getMimeHeaders().getHeader("content-type")[0]);
        	ByteArrayOutputStream a = new ByteArrayOutputStream(10000);
        	call.getResponseMessage().writeTo(a);
        	System.out.println(a.toString());
        	return o; 
        	*/
        }
        finally {
        	setBusy(connection, false);
        }
    }
    
    /**
     * 获取连接
     * @param serviceName
     * @param soapPassport
     * @return
     * @throws Exception
     */
    private SoapConnection getConnection(String serviceName, SoapPassport soapPassport) throws Exception {
    	synchronized(connections) {
	    	for(Iterator iterator = connections.iterator(); iterator.hasNext();) {
	            SoapConnection connection = (SoapConnection)iterator.next();
	            //检查连接
	            if(connection.getServiceName().equals(serviceName) &&
	               connection.getSoapPassport().getUrl().equals(soapPassport.getUrl()) &&
				   !isBusy(connection)) {
	            	cleanTimeoutConnection(); //清除很久没使用过的连接
	                return connection;
	            }
	        }
    	}
        //创建新连接
        Service service = new Service();//建立请求服务框架实例。
		Call call = (Call)service.createCall(); //从框架中生成一个维护调用的实例。
		call.setTargetEndpointAddress(new URL(soapPassport.getUrl() + serviceName));
		call.setUsername(soapPassport.getUserName());
		call.setPassword(soapPassport.getPassword());
		SoapConnection connection = new SoapConnection();
		connection.setCall(call);
		connection.setLastAccess(0);
		connection.setServiceName(serviceName);
		connection.setSoapPassport(soapPassport);
		//加入连接队列
		synchronized(connections) {
			connections.add(connection);
		}
		return connection;
    }
    
    /**
     * 判断连接是否在用
     * @param connection
     * @return
     */
    private boolean isBusy(SoapConnection connection) {
    	synchronized(connection) {
    		return connection.isBusy();
    	}
    }
    
    /**
     * 将连接设置为忙或不忙
     * @param connection
     * @param isBusy
     */
    private void setBusy(SoapConnection connection, boolean isBusy) {
    	if(connections.size()>maxIdle) {
    		synchronized(connections) {
				connections.remove(connection);
				connection = null;
    		}
		}
    	else {
	    	synchronized(connection) {
	    		connection.setBusy(isBusy);
	    	}
    	}
    }
    
    /**
     * 清除很久没使用过的连接
     *
     */
    private void cleanTimeoutConnection() {
    	if(System.currentTimeMillis() < lastCleanTime + cleanHours * 3600000) { //检查是否达到一个清理周期
    		return;
    	}
    	synchronized(connections) {
	        for(Iterator iterator = connections.iterator(); iterator.hasNext();) {
	        	SoapConnection connection = (SoapConnection)iterator.next();
	        	if(!isBusy(connection) && connection.getLastAccess()<lastCleanTime) { //清除一个清理周期内未使用过的连接
	        		iterator.remove();
	        		connection = null;
	        	}
	        }
    	}
    	lastCleanTime = System.currentTimeMillis();
    }
    
    /**
     * SOAP连接
     * @author linchuan
     *
     */
    private class SoapConnection {
        private Call call; //SOAP客户端
        private long lastAccess; //最后访问时间
        private String serviceName; //服务名称
        private SoapPassport soapPassport; //soap身份验证
        private boolean isBusy; //是否正在使用
        
        /**
         * @return Returns the call.
         */
        public Call getCall() {
            return call;
        }
        /**
         * @param call The call to set.
         */
        public void setCall(Call call) {
            this.call = call;
        }
        /**
         * @return Returns the lastAccess.
         */
        public long getLastAccess() {
            return lastAccess;
        }
        /**
         * @param lastAccess The lastAccess to set.
         */
        public void setLastAccess(long lastAccess) {
            this.lastAccess = lastAccess;
        }
        /**
         * @return Returns the serviceName.
         */
        public String getServiceName() {
            return serviceName;
        }
        /**
         * @param serviceName The serviceName to set.
         */
        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }
        /**
         * @return Returns the soapPassport.
         */
        public SoapPassport getSoapPassport() {
            return soapPassport;
        }
        /**
         * @param soapPassport The soapPassport to set.
         */
        public void setSoapPassport(SoapPassport soapPassport) {
            this.soapPassport = soapPassport;
        }
		/**
		 * @return Returns the isBusy.
		 */
		public boolean isBusy() {
			return isBusy;
		}
		/**
		 * @param isBusy The isBusy to set.
		 */
		public void setBusy(boolean isBusy) {
			this.isBusy = isBusy;
		}
    }
}