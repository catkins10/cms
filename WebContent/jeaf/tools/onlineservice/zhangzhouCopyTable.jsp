<%@page import="com.yuanluesoft.jeaf.database.DatabaseService"%>
<%@page import="com.yuanluesoft.jeaf.util.UUIDLongGenerator"%>
<%@page import="com.yuanluesoft.jeaf.util.Environment"%>
<%@page import="com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemMaterial"%>
<%@page import="com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItem"%>
<%@page import="com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemSubjection"%>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collection"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<%!
	public void copyDirectory(String src, String dest, boolean overwrite) {
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
					destDir.mkdirs();
				}
				copyDirectory(src + files[i].getName(), dest + files[i].getName(), overwrite);
			}
			else { //文件
				copyFile(src + files[i].getName(), dest + files[i].getName());
			}
		}
	}
	public String copyFile(String src, String dest) {
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
		if(destFile.exists()) { //文件已存在
			return dest;
		}
		byte[] buffer = new byte[Math.max(1, Math.min((int)srcFile.length(), 81920))];
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			int readLen;
			while((readLen=in.read(buffer))>0) {
				out.write(buffer, 0, readLen);
			}
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
		destFile.setLastModified(srcFile.lastModified());
		return dest;
	}
	
	public Object findObjectByProperty(Collection listToFind, String propertyName, Object propertyValue) {
		if(listToFind==null) {
			return null;
		}
		for(Iterator iterator=listToFind.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			try {
				if((propertyName==null ? object : PropertyUtils.getProperty(object, propertyName)).equals(propertyValue)) {
					return object;
				}
			} catch(Exception e) {
			    
			}
		}
		return null;
	}
%>

<%
	if(!request.getServerName().equals("localhost") &&
	   (request.getSession().getAttribute("SessionInfo")==null || 
	    !"admin".equals(PropertyUtils.getProperty(request.getSession().getAttribute("SessionInfo"), "loginName")))) {
		out.print("failed");
		return;
	}
	String path = (String)Environment.getService("sitePagePath");
	if(!new File(path).exists()) {
		out.print("path failed");
		return;
	}
	DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
	//查询有表格的事项
	String hql = "select distinct OnlineServiceItem" +
				 " from OnlineServiceItem OnlineServiceItem, OnlineServiceItemMaterial OnlineServiceItemMaterial" +
				 " where OnlineServiceItemMaterial.itemId=OnlineServiceItem.id" +
				 " and OnlineServiceItemMaterial.tableURL like '%/%'"; // + " and OnlineServiceItem.id=1069";
	List lazyProperties = new ArrayList();
	lazyProperties.add("materials");
	lazyProperties.add("subjections");
	List items = databaseService.findRecordsByHql(hql, lazyProperties);
	for(Iterator iterator = items.iterator(); iterator.hasNext();) {
		OnlineServiceItem item = (OnlineServiceItem)iterator.next();
		if(item.getSubjections()==null || item.getSubjections().isEmpty()) {
			continue;
		}
		//获取名称带“标”的同名事项
		hql = "select OnlineServiceItem" +
			  " from OnlineServiceItem OnlineServiceItem, OnlineServiceItemSubjection OnlineServiceItemSubjection" +
			  " where OnlineServiceItemSubjection.itemId=OnlineServiceItem.id" +
			  " and OnlineServiceItemSubjection.directoryId=" + ((OnlineServiceItemSubjection)item.getSubjections().iterator().next()).getDirectoryId() +
			  " and OnlineServiceItem.name like '" + item.getName() + "%标%'";
		OnlineServiceItem newItem = (OnlineServiceItem)databaseService.findRecordByHql(hql, lazyProperties);
		if(newItem==null) {
			continue;
		}
		if(newItem.getName().trim().length()!=item.getName().trim().length()+3) {
			continue;
		}
		System.out.println("process " + newItem.getName());
		out.println("process " + newItem.getName() + ", id:" + newItem.getId() + "<br>");
		//目录复制
		copyDirectory(path + item.getId() + "/", path + newItem.getId() + "/", false);
		for(Iterator iteratorMaterial = item.getMaterials().iterator(); iteratorMaterial.hasNext();) {
			OnlineServiceItemMaterial material = (OnlineServiceItemMaterial)iteratorMaterial.next();
			if(material.getTableName()==null || material.getTableName().isEmpty()) {
				continue;
			}
			//检查新的事项中是否已经有当前表格
			if(findObjectByProperty(newItem.getMaterials(), "tableName", material.getTableName())!=null) {
				continue;
			}
			if(material.getName()==null || material.getName().isEmpty()) { //没有材料名称,纯粹的表格
				//复制记录
				OnlineServiceItemMaterial newMaterial = new OnlineServiceItemMaterial();
				PropertyUtils.copyProperties(newMaterial, material);
				newMaterial.setId(UUIDLongGenerator.generateId());
				newMaterial.setItemId(newItem.getId());
				if(material.getTableURL()!=null) {
					newMaterial.setTableURL(material.getTableURL().replaceAll("/" + item.getId() + "", "/" + newItem.getId() + ""));
				}
				if(material.getExampleURL()!=null) {
					newMaterial.setExampleURL(material.getExampleURL().replaceAll("/" + item.getId() + "", "/" + newItem.getId() + ""));
				}
				databaseService.saveRecord(newMaterial);
				System.out.println("append table " + material.getTableName());
				out.println("append table " + material.getTableName() + "<br>");
			}
			else { //有材料名称
				//检查是否有同名材料
				for(Iterator iteratorNewMaterial = newItem.getMaterials().iterator(); iteratorNewMaterial.hasNext();) {
					OnlineServiceItemMaterial newMaterial = (OnlineServiceItemMaterial)iteratorNewMaterial.next();
					if(newMaterial.getName()==null || (newMaterial.getTableName()!=null && !newMaterial.getTableName().isEmpty())) {
						continue;
					}
					if(!newMaterial.getName().replaceAll("[；;，。,\\. 　]", "").equals(material.getName().replaceAll("[；;，。,\\. 　]", ""))) {
						continue;
					}
					newMaterial.setTableName(material.getTableName());
					if(material.getTableURL()!=null) {
						newMaterial.setTableURL(material.getTableURL().replaceAll("/" + item.getId() + "", "/" + newItem.getId() + ""));
					}
					if(material.getExampleURL()!=null) {
						newMaterial.setExampleURL(material.getExampleURL().replaceAll("/" + item.getId() + "", "/" + newItem.getId() + ""));
					}
					databaseService.updateRecord(newMaterial);
					System.out.println("update table " + material.getTableName());
					out.println("update table " + material.getTableName() + "<br>");
					break;
				}
			}
		}
	}
	out.println("complete");
%>