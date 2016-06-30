/*
 * Created on 2006-8-23
 *
 */
package com.yuanluesoft.jeaf.soap;

import java.util.List;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 
 * @author linchuan
 *
 */
public class SoapPassport extends CloneableObject {
    private String url;
    private String userName;
    private String password;
    private List ips; //被允许访问的IP列表
    private String namespaceURI; //命名空间URI,在调用 .net web service 时必须设置
    
	/**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }
    /**
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * @return Returns the userName.
     */
    public String getUserName() {
        return userName;
    }
    /**
     * @param userName The userName to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }
    /**
     * @param url The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }
	/**
	 * @return the ips
	 */
	public List getIps() {
		return ips;
	}
	/**
	 * @param ips the ips to set
	 */
	public void setIps(List ips) {
		this.ips = ips;
	}
	/**
	 * @return the namespaceURI
	 */
	public String getNamespaceURI() {
		return namespaceURI;
	}
	/**
	 * @param namespaceURI the namespaceURI to set
	 */
	public void setNamespaceURI(String namespaceURI) {
		this.namespaceURI = namespaceURI;
	}
}
