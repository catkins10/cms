<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>温馨提示</title>
	<link href="<%=request.getContextPath()%>/edu/skins/vnet/css/chargePrompt.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="cp_Container"><!--Container 这是保持背景及大框架居中-->
	<div id="cp_Head">
		<iframe src="<%=request.getContextPath()%>/edu/skins/vnet/header.html" width="100%" height="212px" scrolling="no" frameborder="0" allowtransparency="true"></iframe>
	</div>
	<div id="cp_Main" style="width:100%; text-align:center">
		<div style="background-color:#ffffff; text-align:left; width:1005px;">
			<div style="float:left; padding-left:100px">
		  		<img src="<%=request.getContextPath()%>/edu/skins/vnet/images/userico_01.jpg"/>
		  	</div>
		  	<div style="float:left; padding-top:60px">
			  	<span class="CP_STYLE9" style="font-size:18px; font-weight:bold">
			  		<ext:write property="actionResult"/>
			  	</span>
		  	</div>
	  	</div>
	</div>
</div><!--cp_Container End-->
<div id="foot">
	<iframe width="100%" height="45" scrolling="no" frameborder=0 marginheight=0 marginwidth=0 src="<%=request.getContextPath()%>/edu/skins/vnet/footer.htm" allowtransparency="true"></iframe>
</div>
</body>
</html>