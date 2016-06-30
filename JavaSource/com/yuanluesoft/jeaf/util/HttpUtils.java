package com.yuanluesoft.jeaf.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartBase;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import com.yuanluesoft.jeaf.util.model.HttpResponse;

/**
 * 
 * @author linchuan
 *
 */
public class HttpUtils {
	
	/**
	 * 获取网页内容
	 * @param url
	 * @param charset
	 * @param followRedirects 是否自动重定向
	 * @param requestHeaders
	 * @param soTimeout 超时时间,以毫秒为单位
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getHttpContent(String url, String charset, boolean followRedirects, Map requestHeaders, int soTimeout) throws Exception {
		return getHttpContent(url, charset, followRedirects, requestHeaders, soTimeout, 0);
	}
	
	/**
	 * 获取网页内容
	 * @param url
	 * @param charset
	 * @param followRedirects
	 * @param requestHeaders
	 * @param soTimeout
	 * @param deep
	 * @return
	 * @throws Exception
	 */
	private static HttpResponse getHttpContent(String url, String charset, boolean followRedirects, Map requestHeaders, int soTimeout, int deep) throws Exception {
		if(url.indexOf("://")==-1) {
			url = "http://" + url;
		}
		//构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(Math.min(10000, Math.max(soTimeout / 5, 3000)));
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(Math.max(soTimeout, 10000));
		GetMethod getMethod = null;
		try {
			//重构URL,给URL中的中文编码
			url = retrieveUrl(url, httpClient);
			//创建GET方法的实例
			getMethod = new GetMethod(url);
			getMethod.setFollowRedirects(followRedirects); //是否自动重定向
			getMethod.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)");
			getMethod.setRequestHeader("Referer", url);
			setRequestHeaders(getMethod, requestHeaders);
			//使用系统提供的默认的恢复策略
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			getMethod.getParams().setSoTimeout(Math.max(soTimeout, 10000)); //最少10秒
			//执行getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			if(statusCode==HttpStatus.SC_MOVED_PERMANENTLY || statusCode==HttpStatus.SC_MOVED_TEMPORARILY) {
				throw new Exception("MOVED");
			}
			if(statusCode==HttpStatus.SC_NOT_FOUND) {
				throw new Exception("NOTFOUND");
			}
			if(statusCode!=HttpStatus.SC_OK && statusCode!=HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				throw new Exception("Request failed, url is " + url + ", status code is " + statusCode + ".");
			}
			//读取内容
			if(charset==null) {
				charset = getResponseCharset(getMethod);
			}
			String html = readResponseBody(getMethod, new String[]{charset==null ? getMethod.getResponseCharSet() : charset});
			//检查HTML是否有<META HTTP-EQUIV="REFRESH" CONTENT="0; URL=html/2011-10/21/node_2.htm">
			String refreshUrl = getRefreshUrl(url, html);
			if(refreshUrl!=null && deep<5 && followRedirects) {
				return getHttpContent(refreshUrl, charset, followRedirects, requestHeaders, soTimeout, deep+1);
			}
			HttpResponse httpResponse = new HttpResponse();
			httpResponse.setUrl(url);
			httpResponse.setStatusCode(statusCode); //状态码
			httpResponse.setResponseBody(html); //内容
			httpResponse.setResponseHeaders(getMethod.getRequestHeaders()); //头部
			Header header = getMethod.getResponseHeader("Cookie");
			httpResponse.setCookie(header==null ? null : header.getValue()); //COOKIE
			return httpResponse;
		}
		catch(Error e) {
			e.printStackTrace();
			throw new Exception("Request failed, url is " + url);
		}
		finally {
			try {
				getMethod.releaseConnection(); //释放连接
			}
			catch(Exception e) {
				
			}
			try {
				httpClient.getHttpConnectionManager().closeIdleConnections(0); //关闭连接
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 获取应答字符集
	 * @param httpMethod
	 * @return
	 */
	private static String getResponseCharset(HttpMethod httpMethod) {
		Header header = httpMethod.getResponseHeader("Content-Type");
		if(header==null) {
			return null;
		}
		int index = header.getValue().indexOf("encoding=");
		if(index!=-1) {
			return header.getValue().substring(index + "encoding=".length());
		}
		index = header.getValue().indexOf("charset=");
		if(index!=-1) {
			String charset = header.getValue().substring(index + "charset=".length());
			index = charset.indexOf(",");
			return (index==-1 ? charset : charset.substring(0, index)).trim();
		}
		return null;
	}
	
	/**
	 * 设置HTTP头
	 * @param httpMethod
	 * @param requestHeaders
	 */
	private static void setRequestHeaders(HttpMethod httpMethod, Map requestHeaders) {
		for(Iterator iterator = requestHeaders==null ? null : requestHeaders.keySet().iterator(); iterator!=null && iterator.hasNext();) {
			String name = (String)iterator.next();
			httpMethod.setRequestHeader(name, (String)requestHeaders.get(name)); 
		}
	}
	
	/**
	 * 重构URL,给URL中的中文编码
	 * @param url
	 * @return
	 */
	private static String retrieveUrl(String url, HttpClient httpClient) throws Exception {
		String newUrl = "";
		for(int i=0; i<url.length(); i++) {
			if(url.charAt(i)<=255) { //不是汉字
				newUrl += url.charAt(i);
			}
		}
		if(newUrl.equals(url)) { //URL没有变化
			return url;
		}
		//获取服务器编码
		String charsetEncoding = getCharsetEncoding(newUrl, httpClient, 60000);
		if(charsetEncoding==null) {
			charsetEncoding = "gbk";
		}
		//汉字编码
		newUrl = "";
		for(int i=0; i<url.length(); i++) {
			newUrl += url.charAt(i)<=255 ? url.charAt(i) + "" :  URLEncoder.encode(url.charAt(i) + "", charsetEncoding);
		}
		return newUrl;
	}
	
	/**
	 * 获取服务器编码
	 * @param url
	 * @param httpClient
	 * @param soTimeout
	 * @return
	 * @throws Exception
	 */
	private static String getCharsetEncoding(String url, HttpClient httpClient, int soTimeout) throws Exception {
		GetMethod getMethod = null;
		try {
			getMethod = new GetMethod(url);
			getMethod.setFollowRedirects(true); //是否自动重定向
			getMethod.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)");
			getMethod.setRequestHeader("Referer", url);
			//使用系统提供的默认的恢复策略
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			getMethod.getParams().setSoTimeout(Math.max(soTimeout, 10000)); //最少10秒
			//执行getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			if(statusCode!=HttpStatus.SC_OK && statusCode!=HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				throw new Exception("Request failed, url is " + url + ", status code is " + statusCode + ".");
			}
			//读取内容,同时解析出字符集
			String responseCharSet = getResponseCharset(getMethod);
			String[] charsetEncoding = {responseCharSet==null ? getMethod.getResponseCharSet() : responseCharSet};
			try {
				readResponseBody(getMethod, charsetEncoding);
			}
			catch(Exception e) {
				
			}
			return charsetEncoding[0];
		}
		finally {
			try {
				getMethod.releaseConnection(); //释放连接
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 获取重定向的URL
	 * @param url
	 * @param html
	 * @return
	 */
	private static String getRefreshUrl(String url, String html) {
		Pattern pattern = Pattern.compile("<META.*HTTP-EQUIV=\"REFRESH\".*CONTENT=\".*URL=([^\"]*)\".*>", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(html);
		String refreshUrl = null;
		if(matcher.find()) {
			refreshUrl = matcher.group(1);
        }
		else {
			pattern = Pattern.compile("<META.*CONTENT=\".*URL=([^\"]*)\".*HTTP-EQUIV=\"REFRESH\".*>", Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(html);
			if(matcher.find()) {
				refreshUrl = matcher.group(1);
	        }
		}
		if(refreshUrl==null || refreshUrl.isEmpty()) {
			return null;
		}
		int index = url.indexOf("/", url.indexOf("://") + 3);
		if(refreshUrl.startsWith("/")) {
			return index==-1 ? url + refreshUrl : url.substring(0, index) + refreshUrl;
		}
		else {
			return index==-1 ? url + "/" + refreshUrl : url.substring(0, url.lastIndexOf('/') + 1) + refreshUrl;
		}
	}
	
	/**
	 * 获取网页内容,如果失败,等待retryDelay毫秒后重试,最多重试retryTimes次
	 * @param url
	 * @param charset
	 * @param followRedirects 是否自动重定向
	 * @param requestHeaders
	 * @param soTimeout 超时时间,以毫秒为单位
	 * @param retryTimes
	 * @param retryDelay
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getHttpContent(String url, String charset, boolean followRedirects, Map requestHeaders, int soTimeout, int retryTimes, int retryDelay) throws Exception {
		HttpResponse httpResponse = null;
		for(int i=0; i<retryTimes; i++) {
			try {
				if((httpResponse = getHttpContent(url, charset, followRedirects, requestHeaders, Math.min(soTimeout * (i+1), 300000)))!=null) {
					break;
				}
			}
			catch(Exception e) {
				if("MOVED".equals(e.getMessage()) || "NOTFOUND".equals(e.getMessage())) {
					throw e;
				}
				httpResponse = null;
			}
			Thread.sleep(retryDelay); //等待
		}
		if(httpResponse==null) {
			throw new Exception("Request failed, url is " + url);
		}
		return httpResponse;
	}
	
	/**
	 * 提交,HTTPS双向认证方式
	 * @param url
	 * @param charset
	 * @param data Part[] 或者 NameValuePair
	 * @param requestHeaders
	 * @param serverKeyStoreFile
	 * @param serverKeyStoreType
	 * @param serverKeyStorePassword
	 * @param clientKeyStoreFile
	 * @param clientKeyStoreType
	 * @param clientKeyStorePassword
	 * @param soTimeout
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse doPost(String url, String charset, Object[] data, Map requestHeaders, String serverKeyStoreFile, String serverKeyStoreType, String serverKeyStorePassword, String clientKeyStoreFile, String clientKeyStoreType, String clientKeyStorePassword, int soTimeout) throws Exception {
		if(url.indexOf("://")==-1) {
			url = "http://" + url;
		}
		else if(url.toLowerCase().startsWith("https://")) { //注册https
			Protocol httpsProtocol = new Protocol("https", serverKeyStoreFile==null ? (ProtocolSocketFactory)new TrustingAllSocketFactory() : new X509SocketFactory(serverKeyStoreFile, serverKeyStoreType, serverKeyStorePassword, clientKeyStoreFile, clientKeyStoreType, clientKeyStorePassword, soTimeout), 443);
			Protocol.registerProtocol("https", httpsProtocol);
		}
		//构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(Math.min(10000, Math.max(soTimeout / 5, 3000)));
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(Math.max(soTimeout, 10000));
		PostMethod postMethod = new PostMethod(url);
		if(charset!=null) {
			/*if(data!=null && data instanceof Part[]) {
				postMethod.setRequestHeader("Content-Type", "multipart/form-data;charset=" + charset);
			}
			else {
				postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
			}*/
			postMethod.getParams().setContentCharset(charset);
		}
		postMethod.setRequestHeader("Referer", url);
		setRequestHeaders(postMethod, requestHeaders);
		postMethod.getParams().setSoTimeout(Math.max(soTimeout, 10000)); //最少30秒
		int statusCode = -1;
		try {
			//填充数据
			if(data!=null) {
				if(data instanceof Part[]) {
					postMethod.setRequestEntity(new MultipartRequestEntity((Part[])data, postMethod.getParams()));
				}
				else if(data instanceof NameValuePair[]) {
					NameValuePair[] values = (NameValuePair[])data;
					if(values.length==1 && values[0].getName()==null) {
						postMethod.setRequestEntity(new ByteArrayRequestEntity(charset==null ? values[0].getValue().getBytes() : values[0].getValue().getBytes(charset)));
					}
					else {
						postMethod.setRequestBody(values);
					}
				}
			}
			//执行postMethod
			statusCode = httpClient.executeMethod(postMethod);
			//HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
			HttpResponse httpResponse = new HttpResponse();
			httpResponse.setUrl(url);
			httpResponse.setStatusCode(statusCode); //状态码
			httpResponse.setResponseHeaders(postMethod.getRequestHeaders()); //头部
			Header header = postMethod.getResponseHeader("Cookie");
			httpResponse.setCookie(header==null ? null : header.getValue()); //COOKIE
			if(statusCode==HttpStatus.SC_OK) {
				//读取内容
				String responseCharSet = getResponseCharset(postMethod);
				httpResponse.setResponseBody(readResponseBody(postMethod, new String[]{responseCharSet==null ? postMethod.getResponseCharSet() : responseCharSet})); //内容
				return httpResponse;
			}
			else if(statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_TEMPORARY_REDIRECT) { //301/302/307
				// 从头中取出转向的地址
				Header locationHeader = postMethod.getResponseHeader("location");
				if(locationHeader != null) {
					httpResponse.setResponseBody("REDIRECT TO:" + locationHeader.getValue()); //内容
					return httpResponse;
				}
			}
			else {
				throw new Exception("Post failed, url is " + url + ", status code is " + statusCode + ".");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				postMethod.releaseConnection(); //释放连接
			}
			catch(Exception e) {
				
			}
			try {
				httpClient.getHttpConnectionManager().closeIdleConnections(0); //关闭连接
			}
			catch(Exception e) {
				
			}
		}
		return null;
	}
	
	/**
	 * 提交
	 * @param url
	 * @param charset
	 * @param data Part[] 或者 NameValuePair
	 * @param requestHeaders
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse doPost(String url, String charset, Object[] data, Map requestHeaders) throws Exception {
		return doPost(url, charset, data, requestHeaders, null, null, null, null, null, null, 60000);
	}
	
	/**
	 * 字节列表PART
	 * @author linchuan
	 *
	 */
	public static class ByteArrayPart extends PartBase {
		private byte[] data;
		private String name;

		public ByteArrayPart(byte[] data, String charset, String name, String type) {
			super(name, type, charset, "binary");
			this.name = name;
			this.data = data;
		}

		protected void sendData(OutputStream out) throws IOException {
			out.write(data);
		}

		protected long lengthOfData() throws IOException {
			return data.length;
		}

		protected void sendDispositionHeader(OutputStream out) throws IOException {
			super.sendDispositionHeader(out);
			StringBuilder buf = new StringBuilder();
			buf.append("; filename=\"").append(name).append("\"");
			out.write(buf.toString().getBytes());
		}
	}
	
	/**
	 * 读取返回值
	 * @param httpMethod
	 * @param responseCharSet
	 * @return
	 * @throws Exception
	 */
	private static String readResponseBody(HttpMethod httpMethod, String[] responseCharset) throws Exception {
		InputStream inputStream = null;
		BufferedReader reader = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			inputStream = httpMethod.getResponseBodyAsStream();
			int readlen;
			byte[] buffer = new byte[4096];
			while((readlen=inputStream.read(buffer))!=-1) {
				out.write(buffer, 0, readlen);
			}
			String response = out.toString(responseCharset[0]);
			if(!"ISO-8859-1".equalsIgnoreCase(responseCharset[0])) {
				return response;
			}
		    //获取实际的字符集,从meta获取
		    Pattern pattern = Pattern.compile("<meta([^>]*)http-equiv=['\"]?Content-Type['\"]?([^>]*)>", Pattern.CASE_INSENSITIVE); //不区分大小写
			Matcher matcher = pattern.matcher(response);
			String realCharset = null;
			while(realCharset==null && matcher.find()) {
    			pattern = Pattern.compile("charset=([^\"]*)[\"]?", Pattern.CASE_INSENSITIVE); //不区分大小写
    			Matcher charsetMatcher = pattern.matcher(matcher.group(1));
    			if(charsetMatcher.find()) {
    				realCharset = charsetMatcher.group(1);
    			}
    			else if((charsetMatcher=pattern.matcher(matcher.group(2))).find()) {
    				realCharset = charsetMatcher.group(1);
    			}
        	}
			if(realCharset==null || realCharset.equals("")) {
				pattern = Pattern.compile("<meta[^>]*charset=['\"]?([^'\"/>]*)['\"]?>", Pattern.CASE_INSENSITIVE); //不区分大小写
				matcher = pattern.matcher(response);
				if(matcher.find()) {
	    			realCharset = matcher.group(1);
	    		}
			}
			if(realCharset==null || realCharset.equals("")) { //默认字符集GBK
				realCharset = "gbk";
			}
			if(realCharset.equalsIgnoreCase(responseCharset[0]) ||
			   (realCharset.equalsIgnoreCase("gbk") && responseCharset[0].equalsIgnoreCase("gb2312")) ||
			   (realCharset.equalsIgnoreCase("gb2312") && responseCharset[0].equalsIgnoreCase("gbk"))) {
				return response;
		    }
			responseCharset[0] = realCharset;
			return out.toString(realCharset);
		}
		finally {
			try {
				reader.close();
			}
			catch(Exception e) {
				
			}
			try {
				inputStream.close();
			}
			catch(Exception e) {
				
			}
			try {
				out.close();
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * 获取对象
	 * @param url
	 * @param connectTimeout
	 * @param readTimeout
	 * @return
	 * @throws Exception
	 */
	public static Object doGetObject(String url, Map requestHeaders, int connectTimeout, int readTimeout) throws Exception {
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			connection = (HttpURLConnection)new URL(url).openConnection();
			connection.setConnectTimeout(Math.max(3000, connectTimeout));
			connection.setReadTimeout(Math.max(3000, readTimeout));
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			//设置头
			for(Iterator iterator = requestHeaders==null ? null : requestHeaders.keySet().iterator(); iterator!=null && iterator.hasNext();) {
				String name = (String)iterator.next();
				connection.setRequestProperty(name, "" + requestHeaders.get(name));
			}
			connection.connect();
			inputStream = connection.getInputStream();
			objectInputStream = new ObjectInputStream(inputStream);
			return objectInputStream.readObject();
		}
		finally {
			try {
				objectInputStream.close();
			}
			catch(Exception e) {
				
			}
			try {
				inputStream.close();
			}
			catch(Exception e) {
				
			}
			try {
				connection.disconnect();
			}
			catch(Exception e) {
				
			}
		}
	}
	
	/**
	 * POST数据
	 * @param url
	 * @param headers
	 * @param object
	 * @param connectTimeout
	 * @return
	 * @throws Exception
	 */
	public static int doPostObject(String url, Map requestHeaders, Serializable object, int connectTimeout) throws Exception {
		//发送数据
		HttpURLConnection connection = null;
		OutputStream outputStream = null;
		try {
			connection = (HttpURLConnection)new URL(url).openConnection();
			connection.setConnectTimeout(Math.max(3000, connectTimeout));
			connection.setDoOutput(true);
			connection.setDefaultUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("content-type", "text/html");
			//设置头
			for(Iterator iterator = requestHeaders==null ? null : requestHeaders.keySet().iterator(); iterator!=null && iterator.hasNext();) {
				String name = (String)iterator.next();
				connection.setRequestProperty(name, "" + requestHeaders.get(name));
			}
			connection.connect();
			outputStream = connection.getOutputStream();
			outputStream.write(ObjectSerializer.serialize(object)); //输出序列化后的对象
			outputStream.flush();
			outputStream.close();
			return connection.getResponseCode();
		}
		finally {
			try {
				outputStream.close();
			}
			catch(Exception e) {
		
			}
			try {
				connection.disconnect();
			}
			catch(Exception e) {
			
			}
		}
	}
	
	/**
	 * 
	 * @author linchuan
	 *
	 */
	public static class X509SocketFactory implements ProtocolSocketFactory {
		private String serverKeyStoreFile; //服务端证书文件
		private String serverKeyStoreType; //服务端证书类型,JKS/PKCS12等
		private String serverKeyStorePassword; //服务端证书密码
		private String clientKeyStoreFile; //客户端证书文件
		private String clientKeyStoreType; //客户端证书类型,JKS/PKCS12等
		private String clientKeyStorePassword; //客户端证书密码
		private int soTimeout; //超时时间

		public X509SocketFactory(String serverKeyStoreFile, String serverKeyStoreType, String serverKeyStorePassword, String clientKeyStoreFile, String clientKeyStoreType, String clientKeyStorePassword, int soTimeout) {
			super();
			this.serverKeyStoreFile = serverKeyStoreFile;
			this.serverKeyStoreType = serverKeyStoreType;
			this.serverKeyStorePassword = serverKeyStorePassword;
			this.clientKeyStoreFile = clientKeyStoreFile;
			this.clientKeyStoreType = clientKeyStoreType;
			this.clientKeyStorePassword = clientKeyStorePassword;
			this.soTimeout = soTimeout;
		}

		/* (non-Javadoc)
		 * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int, java.net.InetAddress, int, org.apache.commons.httpclient.params.HttpConnectionParams)
		 */
		public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
			return createSocket(host, port);
		}

		/* (non-Javadoc)
		 * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int, java.net.InetAddress, int)
		 */
		public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
			return createSocket(host, port);
		}

		/* (non-Javadoc)
		 * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int)
		 */
		public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
			try {
				//加载客户端证书
				KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
				KeyStore clientKeyStore = KeyStore.getInstance(clientKeyStoreType);
				clientKeyStore.load(new FileInputStream(clientKeyStoreFile), clientKeyStorePassword.toCharArray());
				keyManagerFactory.init(clientKeyStore, clientKeyStorePassword.toCharArray());

				//加载服务端证书
				TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
				KeyStore serverKeyStore = KeyStore.getInstance(serverKeyStoreType);  
				serverKeyStore.load(new FileInputStream(serverKeyStoreFile), serverKeyStorePassword.toCharArray());  
				trustManagerFactory.init(serverKeyStore);  //添加信任的证书  

				SSLContext sslContext = SSLContext.getInstance("TLS");
				sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
				SSLSocketFactory factory = sslContext.getSocketFactory();

				//建立连接
				SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
				socket.setSoTimeout(Math.max(soTimeout, 10000)); //最少10秒
				return socket;
			}
			catch(Exception e) {
				throw new IOException(e);
			}
		}
	}
	
	/**
	 * 信任所有证书的HTTPS请求
	 * @author linchuan
	 *
	 */
	public static class TrustingAllSocketFactory implements ProtocolSocketFactory {
		private SSLContext sslcontext = null;

		/**
		 * 创建SSL上下文
		 * @return
		 */
		private SSLContext createSSLContext() {
			SSLContext sslcontext=null;
			try {
				X509TrustManager trustManager = new X509TrustManager() {
					public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						
					}
					public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						
					}
					public X509Certificate[] getAcceptedIssuers() {
						return new X509Certificate[]{};
					}
				};
				sslcontext = SSLContext.getInstance("SSL");
				sslcontext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
			}
			catch(NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			catch(KeyManagementException e) {
				e.printStackTrace();
			}
			return sslcontext;
		}

		/**
		 * 
		 * @return
		 */
		private SSLContext getSSLContext() {
			if(this.sslcontext == null) {
				this.sslcontext = createSSLContext();
			}
			return this.sslcontext;
		}

		/*
		 * (non-Javadoc)
		 * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int)
		 */
		public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
			return getSSLContext().getSocketFactory().createSocket(host, port);
		}

		/*
		 * (non-Javadoc)
		 * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int, java.net.InetAddress, int)
		 */
		public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
			return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
		}

		/*
		 * (non-Javadoc)
		 * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String, int, java.net.InetAddress, int, org.apache.commons.httpclient.params.HttpConnectionParams)
		 */
		public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
			if(params == null) {
				throw new IllegalArgumentException("Parameters may not be null");
			}
			int timeout = params.getConnectionTimeout();
			SocketFactory socketfactory = getSSLContext().getSocketFactory();
			if (timeout == 0) {
				return socketfactory.createSocket(host, port, localAddress, localPort);
			}
			else {
				Socket socket = socketfactory.createSocket();
				SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
				SocketAddress remoteaddr = new InetSocketAddress(host, port);
				socket.bind(localaddr);
				socket.connect(remoteaddr, timeout);
				return socket;
			}
		}
	}
}