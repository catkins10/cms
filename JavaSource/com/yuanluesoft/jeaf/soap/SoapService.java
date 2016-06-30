/*
 * Created on 2006-8-25
 *
 */
package com.yuanluesoft.jeaf.soap;


/**
 * 
 * @author linchuan
 *
 */
public interface SoapService {
	
    /**
     * 获取服务端SOAP身份验证对象
     * @return
     */
    public SoapPassport getServiceSoapPassport();
}
