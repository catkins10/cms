<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>退订</title>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/common.js"></script>
	<link href="<%=request.getContextPath()%>/edu/skins/vnet/css/chargePrompt.css" rel="stylesheet" type="text/css" />
	<style>
		div{font-size:14px}
	</style>
</head>
<body>
<ext:form action="/doCancelVnetOrder">
<div id="cp_Container"><!--Container 这是保持背景及大框架居中-->
	<div id="cp_Head">
		<iframe src="<%=request.getContextPath()%>/edu/skins/vnet/header.html" width="100%" height="212px" scrolling="no" frameborder="0" allowtransparency="true"></iframe>
	</div>
	<div id="cp_Main" style="width:100%; text-align:center">
		<div style="background-color:#ffffff; text-align:left; width:1005px;">
			<div style="float:left; padding-left:100px; height:300px">
				<br/><br/>
		  		验证码：
				<img id="validateCodeImage"	src="<%=request.getContextPath()%>/jeaf/validatecode/generateValidateCodeImage.shtml"/>
				<html:text property="validateCode" maxlength="10" styleClass="field required" style="border:#41a8d7 1px solid"/>
				<a style="color:blue" href="javascript:FormUtils.reloadValidateCodeImage()">看不清，换一张</a>
				<ext:notEmpty property="errors">
					<br/><br/>系统提示 ：
					<span style="color:#ff0000">
						<ext:iterate property="errors" id="errorMessage" indexId="errorIndex">
							<pre style="margin:0;word-wrap:break-word"><ext:write name="errorMessage"/></pre>
						</ext:iterate>
					</span>
				</ext:notEmpty>
				<br/><br/>
				<button class="submit_cp" onclick="FormUtils.submitForm()">退订</button>
		  	</div>
	  	</div>
	</div>
</div><!--cp_Container End-->
<div id="foot">
	<iframe width="100%" height="45" scrolling="no" frameborder=0 marginheight=0 marginwidth=0 src="<%=request.getContextPath()%>/edu/skins/vnet/footer.htm" allowtransparency="true"></iframe>
</div>
</ext:form>
</body>
</html>