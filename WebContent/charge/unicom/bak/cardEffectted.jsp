<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>

<html:html>
<head>
	<title>互动教育网 - 注册学生账号</title>
	<link href="css/reg_V2.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
</head>
<body>
<center>
    <div id="container">
		<div id="head">
	        <div id="user"> <img src="image/header_03.jpg"> <a href="http://www.9191edu.com/edu/manual/Login.html" class="STYLE3" target="_blank">使用帮助</a> </div>
	        <div id="help"> </div>
		</div> <!--head end-->
		<div id="main">
			<div id="main_rim_a">
				<div id="main_title">
					<div style="padding:30px; padding-left:100px; font-size:14px; text-align: left">
						* 注册成功！
						<ext:notEmpty name="effectUnicomCard" property="unicomPersonId">
							您的帐号是：<span style="color:#ff0000;  font-size:14px"><ext:write property="unicomPersonId"/></span>，请妥善保存。
							<br><br>
							* 如果帐号不甚遗忘，可以通过本站首页提示找回帐号。
						</ext:notEmpty>
						<ext:empty name="effectUnicomCard" property="unicomPersonId">
							联通帐号不能创建，请与客服联系。
						</ext:empty>
						<br><br>
						* 您可以免费体验至<ext:write property="tryEnd" format="yyyy年MM月dd日"/>。现在订购可享受30元包月、160包半年、280元包年的体验价，点击<a href="https://passport.9191edu.com/charge/order/personalWebOrder.shtml">此处</a>了解更多》》
					</div>
             	</div> <!--main_title end-->
				<div id="main_text">
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
</body>
</html:html>