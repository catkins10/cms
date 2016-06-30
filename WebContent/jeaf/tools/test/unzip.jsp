<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="java.io.File"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.FileOutputStream"%>
<html>
<body>
<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile("D:/web/xccms/cms/templates/200322963064270000/templateUpload/cvb_2014-11-27.zip");
	java.util.Enumeration e = zipFile.getEntries();
	org.apache.tools.zip.ZipEntry zipEntry = null;
	String outputDirectory = "D:/web/xccms/cms/templates/200322963064270000/templateUpload/";
	while(e.hasMoreElements()) {
		zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
		if (zipEntry.isDirectory()) {
			File f = new File(outputDirectory + zipEntry.getName());
			if(!f.exists()) {
				f.mkdir();
			}
		}
		else {
			String fileName = zipEntry.getName();
			fileName = fileName.replace('\\', '/');
			if(fileName.indexOf("/") != -1) {
				new File(outputDirectory + fileName.substring(0, fileName.lastIndexOf("/"))).mkdirs();
			}
			out.println(outputDirectory + zipEntry.getName());
			File f = new File(outputDirectory + zipEntry.getName());
			f.createNewFile();
			InputStream in = zipFile.getInputStream(zipEntry);
			FileOutputStream output = new FileOutputStream(f);
			byte[] buffer = new byte[81920];
			int readLen;
			while((readLen=in.read(buffer)) != -1) {
				output.write(buffer, 0, readLen);
			}
			out.close();
			in.close();
		}
	}
	zipFile.close();
%>