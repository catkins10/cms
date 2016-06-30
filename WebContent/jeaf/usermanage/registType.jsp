<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<html:html>
<head>
	<title>互动教育网 - 用户注册</title>
	<LINK href="css/usermanage.css" type="text/css" rel="stylesheet">
	<link type="text/css" href="<%=request.getContextPath()%>/jeaf/form/css/form.css" rel="stylesheet">
	<style>
		.STYLE1 {color: #FF6600;font-size: 14px;font-weight: bold;line-height: 25px;padding-top:10px}
		/*字体及链接*/
		td{font-family:"宋体";font-size:12px;line-height: 18px;}
		A{color:#0066FF;text-decoration: none;}
		A:hover{color:#FF6600}
	</style>
</head>
<body>
	<div id="header" style="width:100%;">
		<div class="inside">
			<table cellSpacing="0" cellPadding="0" border="0" style="width:100%; height:100%">
				<tr>
					<td class="title" valign="middle"></td>
					<td valign="bottom" align="right" style="padding-right:20px;padding-bottom:10px;font-size:14px">
						用户注册
					</td>
				</tr>
			</table>
		</div>
	</div>
	<table height="189" border="0" align="center" cellpadding="3" cellspacing="0" style="color:#000000; width:90%">
		<tr>
			<td height="6px" colspan="2" class="STYLE1">【互动教育网】选择注册用户类型：</td>
		</tr>
		<tr>
			<td width="44%" height="32" align="left" valign="middle" style="padding-top:8px">
				<input style="width:100px" type="button" class="button" value="点击注册教师" onclick="location.href='registTeacher.shtml'" />
			</td>
			<td width="44%" align="left" valign="top" style="padding-top:8px">
				<input style="width:100px" name="button" type="button" class="button" onclick="location.href='registStudent.shtml'" value="点击注册学生" />
			</td>
		</tr>
		<tr>
			<td height="119" valign="top">
				<p>教师注册步骤：<br>
				1、联系所在学校的管理员，索取注册验证码；<br>
				2、填写真实有效的注册信息；<br>
				3、完成注册；<br>
				4、帐号立即生效。<br>
				<span style="color:#ff6600">注：如密码丢失请联系所在学校的管理员。</span></p>
				<a href="http://www.9191edu.com/kd9191edu/jeaf/usermanage/demo/teacherRegist.html" target="_blank">教师注册及班级管理演示</a>
			</td>
			<td valign="top">
				<p>学生注册步骤：<br>
				1、填写真实有效的注册信息；<br>
				2、填写问卷；<br>
				3、完成注册；<br>
				4、帐号在班主任审核通过后生效。<br>
				<span style="color:#ff6600">注：如密码丢失请联系班主任。</span></p>
				<a href="http://www.9191edu.com/kd9191edu/jeaf/usermanage/demo/studentRegist.html" target="_blank">学生注册演示</a>
			</td>
		</tr>
	</table>
</body>
</html:html>