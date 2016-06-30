<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%!
	void copyDirectory(String src, String dest, boolean overwrite) {
		if(!src.endsWith("/")) {
			src += "/";
		}
		if(!dest.endsWith("/")) {
			dest += "/";
		}
		File srcDir = new File(src);
		File[] files = srcDir.listFiles();
		for(int i=0; i<files.length; i++) {
			if(files[i].isDirectory()) { //目录
				File destDir = new File(dest + files[i].getName());
				if(!destDir.exists()) {
					destDir.mkdir();
				}
				copyDirectory(src + files[i].getName(), dest + files[i].getName(), overwrite);
			}
			else { //文件
				copyFile(src + files[i].getName(), dest + files[i].getName(), overwrite, false);
			}
		}
	}
	String copyFile(String src, String dest, boolean overwrite, boolean autoRename) {
		File destFile = new File(dest);
		if(destFile.isDirectory()) { //目录
			if(!dest.endsWith("/")) {
				dest += "/";
			}
			int index = src.lastIndexOf('/');
			if(index==-1) {
				index = src.lastIndexOf('\\');
			}
			dest += src.substring(index + 1);
			destFile = new File(dest);
		}
		File srcFile = new File(src);
		if(destFile.exists()) { //文件已存在,且不允许覆盖
			if(srcFile.lastModified()==destFile.lastModified()) { //文件最后修改时间相同,不需要拷贝
				return dest;
			}
			if(!overwrite) {
				if(!autoRename) {
					return null;
				}
				dest = autoRename(dest);
			}
		}
		byte[] buffer = new byte[4096];
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			int readLen;
			while((readLen=in.read(buffer))>0) {
				out.write(buffer, 0, readLen);
			}
			destFile.setLastModified(srcFile.lastModified());
		}
		catch(Exception e) {
			return null;
		}
		finally {
			try {
				in.close();
			}
			catch(Exception e) {
				
			}
			try {
				out.close();
			}
			catch(Exception e) {
				
			}
		}
		return dest;
	}
	String autoRename(String fileName) {
		File file = new File(fileName);
		int index = fileName.lastIndexOf('.');
		String prefix;
		String postfix;
		if(index!=-1) {
			prefix = fileName.substring(0, index);
			postfix = fileName.substring(index);
		}
		else {
			prefix = fileName;
			postfix = "";
		}
		for(int i=1; file.exists(); i++) {
			file = new File(prefix + "(" + i + ")" + postfix);
		}
		return file.getPath();
	}
%>
<%
	if(!request.getServerName().equals("localhost") &&
	   !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName"))) {
		out.print("failed");
		return;
	}
	Class.forName("org.gjt.mm.mysql.Driver");
	Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/cms?useUnicode=true&amp;characterEncoding=gbk", "root", "root");
	Statement statement = connection.createStatement();
	ResultSet resultSet = statement.executeQuery("select itemId from onlineservice_item_material where tableURL!=''");
	while(resultSet.next()) {
		long itemId = resultSet.getLong("itemId");
		String path = "e:/gzcms/cms/pages/" + itemId + "/";
		out.print(path + "</br>");
		new File("e:/update/" + itemId).mkdir();
		copyDirectory(path, "e:/update/" + itemId + "/", true);
	}
	resultSet.close();
	statement.close();
	connection.close();
%>