<%@page import="java.sql.*"%>
<%@page import="java.io.*" %>
<%@page import="java.net.*" %>
<%@ page language="java" contentType="text/html; GBK"
    pageEncoding="GBK"%>
<%
	String atr=(String)request.getParameter("atr");
	System.out.println(atr);
	String[] id=atr.split(",");

	Class.forName("org.postgresql.Driver");
	Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/fzqicms", "postgres", "fzqicms@2012");
	Statement statement1 = connection.createStatement();
	for(int i=0;i<id.length;i++){
		System.out.println(id[i]+"");
	ResultSet rs1=statement1.executeQuery("select * from cms_resource where id="+id[i]);
	while(rs1.next()){
		String filename=rs1.getString("firstVideoName").substring(0,rs1.getString("firstVideoName").lastIndexOf("."));
		System.out.println(filename);
		Statement statement2 = connection.createStatement();
		statement2.execute("update cms_resource set firstVideoName='"+id[i]+".flv' where id="+id[i]);
		connection.commit();
		String dir="D:\\fzqicms\\cms\\pages\\"+id[i]+"\\videos\\";
		String imgdir=dir+filename+".jpg";
		String videodir=dir+filename+".flv";
		File img=new File(imgdir);
		img.renameTo(new File(dir+id[i]+".jpg"));
		File video=new File(videodir);
		video.renameTo(new File(dir+id[i]+".flv"));
		Statement statement3 = connection.createStatement();
		ResultSet rs2=statement3.executeQuery("select * from cms_resource_body where id="+id[i]);
		while(rs2.next()){
			String body=rs2.getString("body");
			if(URLEncoder.encode(filename,"UTF-8").toString().lastIndexOf("+")>0){
				filename=URLEncoder.encode(filename,"UTF-8").toString().replaceAll("+","%20");
			}else{
				filename=URLEncoder.encode(filename,"UTF-8");
			}
			System.out.println(filename);
			String newbody=body.replaceAll(filename,id[i]);
			if(newbody.lastIndexOf("%25")>0){
				newbody=newbody.replaceAll("%25","%");
				newbody=newbody.replaceAll(filename,id[i]);
			}
			System.out.println(newbody);
			Statement statement4 = connection.createStatement();
			statement4.execute("update cms_resource_body set body='"+newbody+"' where id="+id[i]);
			connection.commit();
			}
		}
	}
	connection.close();
%>