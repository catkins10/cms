<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>

<%@page import="java.io.File"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<title>海事局删除从内网同步到外网的文章附件</title>
<style>
.table {border: 1px #2d5c7a solid;border-collapse:collapse;}
.tdtitle {border: 1px #2d5c7a solid;padding: 5px;background-color: rgb(236, 240, 249)}
.tdcontent {border: 1px #2d5c7a solid;padding: 5px;background-color: #FFF;}
input{width:100%}
</style>
<%request.setCharacterEncoding("utf-8"); %>
<form action="" method="post"/>
<table  width="80%" border="1" cellpadding="0" cellspacing="0" class="table" align="center">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
	   <td height="30px" style="font-size:20px;font-weight: bold" colspan="4" align="center">海事局删除从内网同步到外网的文章附件</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">pages目录路径</td>
		<td colspan="3" class="tdcontent" ><input type="text" name="pagesPath" value="<%=request.getParameter("pagesPath")==null?"":request.getParameter("pagesPath") %>"/></td>
	</tr>
    <tr>
		<td class="tdtitle" nowrap="nowrap">外网网站ID</td>
		<td class="tdcontent"><input type="text" name="internetSiteId" value="<%=request.getParameter("internetSiteId")==null?"":request.getParameter("internetSiteId").replaceAll("，",",") %>"/></td>
		<td class="tdtitle" nowrap="nowrap">JDBC URL</td>
		<td class="tdcontent" align="center"><input type="text" name="databaseUrl" value="<%=request.getParameter("databaseUrl")==null?"jdbc:postgresql://localhost:5432/admin":request.getParameter("databaseUrl") %>"/></td>
	</tr>
	 <tr>
		<td class="tdtitle" nowrap="nowrap">数据库用户名</td>
		<td class="tdcontent"><input type="text" name="databaseUser" value="<%=request.getParameter("databaseUser")==null?"postgres":request.getParameter("databaseUser") %>"/></td>
		<td class="tdtitle" nowrap="nowrap">数据库密码</td>
		<td class="tdcontent" align="center"><input type="text" name="databasePassword" value="<%=request.getParameter("databasePassword")==null?"":request.getParameter("databasePassword") %>"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否删除</td>
		<td class="tdcontent">否<input type="radio" name="deleteAble" value="false" checked="checked" style="width:20px"/>&nbsp;&nbsp;是<input type="radio" name="deleteAble" value="true" style="width:20px"/></td>
		<td class="tdtitle" nowrap="nowrap"></td>
		<td class="tdcontent" align="center"></td>
	</tr>
	<tr>
		<td class="tdcontent" align="center" colspan="4"><input type="submit" value="开始执行" style="width:100px"/> </td>
	</tr>
	
</table>
</form>
<div style='color:red;font-size:20px'>
<%  
	if (request.getMethod() == null || !"post".equals(request.getMethod().toLowerCase())) {
		return;
	}
	//表单提交，开始执行
	String pagesPath = request.getParameter("pagesPath");
	String internetSiteIdStr = request.getParameter("internetSiteId");
	String databaseUrl = request.getParameter("databaseUrl");
	String databaseUser = request.getParameter("databaseUser");
	String databasePassword = request.getParameter("databasePassword");
	String deleteAble = request.getParameter("deleteAble");
	if (pagesPath == null || pagesPath.length() == 0 || internetSiteIdStr == null || internetSiteIdStr.length() == 0
			||databaseUrl==null||databaseUrl.length()==0||databaseUser==null||databaseUser.length()==0||databaseUser==null||databaseUser.length()==0) {
		out.print("所有输入选项都不能为空");
		return;
	}
	File pageFiles = new File(pagesPath);
	if (!pageFiles.exists() || !pageFiles.isDirectory()) {
		out.print("pages目录路径不正确");
		return;
	}
	File[] pageDirectorys = pageFiles.listFiles();
	if (pageDirectorys == null || pageDirectorys.length == 0) {
		out.print("输入的pages路径为空目录，请确认输入是否正确");
		return;
	}
	Connection conn = null;
	Statement stat = null;
	ResultSet Rs = null;
	try {
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection(databaseUrl, databaseUser,databasePassword);//jdbc方式建立数据库链接
		stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		
		internetSiteIdStr=internetSiteIdStr.replaceAll("，",",");
		String[] siteIds=internetSiteIdStr.split(",");
		long siteId=0;
		for(int i=0;i<siteIds.length;i++){
		  try{
			siteId=Long.parseLong(siteIds[i]);
		  }catch(Exception e){
			  throw new Exception("外网站点ID:"+siteIds[i]+"不正确,必须是数字");
		  }
		  if(siteId<=0){
			throw new Exception("外网站点ID:"+siteIds[i]+"不正确");
		  }
		  Rs = stat.executeQuery("select id from cms_site where id="+ siteId);
		  if (Rs == null || !Rs.next()) {
			throw new Exception("外网站点ID"+siteId+"不存在");
		  }
		}
		
        StringBuffer successLog=new StringBuffer();//处理成功日志
        StringBuffer failLog=new StringBuffer();//处理失败日志
		for (int i = 0; i < pageDirectorys.length; i++) {//遍历每个目录
			File pageDirectory = pageDirectorys[i];
			if (!pageDirectory.isDirectory()) {
		        continue;
			}
			long resourceId = -1;//目录名以文章ID命名
			try {
		       resourceId = Long.parseLong(pageDirectory.getName());
			} 
			catch (Exception e) {
		       continue;
			}
			if (resourceId == -1) {
		       continue;
			}
			Rs = stat.executeQuery("select * from cms_resource where id="+ resourceId);
			if (Rs == null || !Rs.next()) {//查无数据，文章表没有该id对应的数据，即该ID不是文章ID，可能是信息公开
			   failLog.append("<br/>不存在的文章："+resourceId);
		       continue;
			}
//			判断该文章所属站点是否是外网站点。如果是内网站点的，删除该文章对应的pages目录
			Rs = stat.executeQuery("select id from cms_site where id in "+
					"(select parentdirectoryid from cms_directory_subjection where directoryid in"+
							"(select siteid from cms_resource_subjection where resourceid="+ resourceId+")"+
					  ")");
            if(Rs == null || !Rs.next()){
            	failLog.append("<br/>无站点文章ID："+resourceId);
            	continue;
            }
            else{
            	Rs.beforeFirst();
            }
			boolean isInternetResource=false;//是否外网文章
            while (Rs.next()) {
			    if((","+internetSiteIdStr+",").indexOf(","+Rs.getLong("id")+",")!=-1){//有在外网上发布
			    	isInternetResource=true;
			    	break;
			    }
			}
            if(isInternetResource){
            	continue;
            }
            if(deleteAble==null||deleteAble.equals("false")){//不删除
            	successLog.append((successLog.length()==0?"":"<br/>")+"需要删除的目录："+resourceId);
            }
            else{
//            	不是外网的文章附件，删除
    			if(deleteDirectory(pageDirectory)){
    				successLog.append((successLog.length()==0?"":"<br/>")+"成功删除目录："+resourceId);
    			}
    			else{
    				failLog.append((failLog.length()==0?"":"<br/>")+"删除目录："+resourceId+"失败");				
    			}
            }
			
		}
		out.print("操作日志：<br/>" + (successLog.length()!=0?successLog.toString():"未删除任何目录")
				+("<br/><br/>错误情况："+(failLog.length()!=0?failLog.toString():"无异常记录")));
	} 
	catch (Exception e) {
		out.print("错误：" + e.getMessage());
	} 
	finally {
		if (Rs != null ) {
			Rs.close();
		}
		if (stat != null ) {
			stat.close();
		}
		if (conn != null ) {
			conn.close();
		}
	}

%>
<%!
/**
 *删除目录
 */
 private  boolean deleteDirectory(File dir) {
		if(dir.exists()) {
			File[] files = dir.listFiles();
			for(int i=files.length - 1; i>=0; i--) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					deleteFile(files[i].getPath());
				}
			}
			dir.delete();
			
		}
		return true;
	}

//删除文件
/**
	 * 删除文件
	 * @param filePath
	 * @param warn
	 * @return
	 */
	private static boolean deleteFile(String filePath) {
		File curFile = new File(filePath);
		int i = 0;
		for(; i<20 && !curFile.delete(); i++) {
			try {
				System.gc();
				Thread.sleep(50);
			}
			catch (InterruptedException e) {
				
			}
		}
		return i<20;
	}
	
%>
</div>