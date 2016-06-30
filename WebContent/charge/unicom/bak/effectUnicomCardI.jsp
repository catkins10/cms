<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html:html>
<head>
	<title>互动教育网 - 激活体验卡</title>
	<link href="css/reg_V2.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
	<script>
	function validate() {
		if(document.getElementsByName("cardNumber")[0].value=='') {
			alert("请输入卡号！");
			return false;
		}
		if(document.getElementsByName("cardPassword")[0].value=='') {
			alert("请输入密码！");
			return false;
		}
		return true;
	}
	</script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/ratePassword.js"></script>
</head>
<body>
<ext:form action="/doEffectUnicomCard" onsubmit="return validate();">
<html:hidden property="step"/>
<center>
    <div id="container">
		<div id="head">
	        <div id="user"> <img src="image/header_03.jpg"> <a href="http://www.9191edu.com/edu/manual/Login.html" class="STYLE3" target="_blank">使用帮助</a> </div>
	        <div id="help"> 请注意：带有<span class="STYLE13">*</span>的项目必须填写。&nbsp;&nbsp;</div>
		</div> <!--head end-->
		<div id="main">
			<div id="main_rim_a">
				<div id="main_title">
					<table style="width:720px; color: #000000; table-layout:foxed" cellSpacing="0" cellPadding="3" align="center" border="0">
						<col width="30%">
						<col width="35%">
						<col width="35%">
						<tr>
							<td	style="FONT-SIZE: 14px; BORDER-BOTTOM: #a0c0ff 1px solid; HEIGHT: 30px"	vAlign=bottom align=left colSpan=3>
								<strong>体验卡信息<span style="color:#ff0000"></span></strong>
							</td>
						</tr>
						<tr>
							<td colSpan=3 height="20"></td>
						</tr>
						<tr>
							<td height="24" align="right"><span style="color:#ff0000">*&nbsp;</span>卡号 ：</td>
							<td align="left">
								<html:text property="cardNumber"/>
		                    </td>
							<td align="left" valign="top" style="font-size:12px"> 
								请输入体验卡卡号
							</td>
						</tr>
						<tr>
							<td height="24" align="right"><span style="color:#ff0000">*&nbsp;</span>密码 ：</td>
							<td align="left">
								<html:password property="cardPassword" maxlength="20" value=""/>
		                    </td>
							<td align="left" valign="top" style="font-size:12px"> 
								请输入体验卡密码
							</td>
						</tr>
						<ext:notEmpty property="errors">
							<tr>
								<td height="24" align="right"><span style="color:#ff0000">&nbsp;</span>系统提示 ：</td>
								<td align="left" colspan="2" style="color:#ff0000">
									<ext:iterate property="errors" id="errorMessage" indexId="errorIndex">
										<pre style="margin:0;word-wrap:break-word"><ext:write name="errorMessage"/></pre>
									</ext:iterate>
			                    </td>
							</tr>
						</ext:notEmpty>
					</table>
				</div> <!--main_title end-->
				<div id="main_text">
					<a href="#" onClick="FormUtils.submitForm();"><img src="image/next.jpg" border="0"></a> &nbsp;&nbsp;&nbsp;&nbsp; 
					<a href="#" onClick="window.close();"> <img src="image/close.jpg" border="0" /></a>
				</div> <!--main_rim_a end-->
			</div>  <!--main end-->
			<div id="foot" class="style6">
				尊重网上道德，遵守《全国人大常委会关于维护互联网安全的决定》及中华人民共和国其他各项有关法律法规 <br>
          		<a href="http://www.9191edu.com/edu/about/aboutUs.html" class="STYLE6">关于我们</a> | <a href="http://www.9191edu.com/edu/about/contactUs.html"	class="STYLE6">联系我们</a> | <a	href="http://www.9191edu.com/edu/resources/handbook/flash.html" class="STYLE6">宣传手册</a> &nbsp;&nbsp;&nbsp;福建省中小学网络互动平台示范应用项目&nbsp;&nbsp;福建省电化教育馆"十一.五"重点规划课题
          	</div>
		</div>
	</div>
</center>
</ext:form>
</body>
</html:html>