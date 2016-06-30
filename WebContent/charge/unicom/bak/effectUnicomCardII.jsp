<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html:html>
<head>
	<title>互动教育网 - 激活体验卡</title>
	<link href="css/reg_V2.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/edu/skins/v2/default/css/form.css.jsp" rel="stylesheet" type="text/css" />
	<script>
	var loginNameCheck = 0;
	function selectArea() {
		DialogUtils.selectOrg(600, 400, false, 'orgId{id},orgFullName{fullName}', '', 'area', '', ',', '<ext:write property="cardAreaId"/>', true, true);
	}
	function checkLoginNameInUse() {
		loginNameCheck = 0;
		var loginName = document.getElementsByName("loginName")[0].value;
		if(loginName!="") {
			ScriptUtils.appendJsFile(document, "<%=request.getContextPath()%>/jeaf/usermanage/isLoginNameInUse.shtml?loginName=" + StringUtils.utf8Encode(loginName) + "&seq=" + Math.random(), "scriptCheckLoginNameInUse");
		}
	}
	function loginNameInUse(inUse) {
		var obj = document.getElementById("loginNamePrompt");
		obj.innerHTML = (inUse ? "用户名已经被占用" : "");
		obj.style.display = (inUse ? "" : "none");
		loginNameCheck = inUse ? 1 : 2;
	}
	function validate() {
		if(!validateEffectUnicomCard(document.forms[0])) {
			return false;
		}
		if(loginNameCheck==0) {
			alert("正在校验用户名！");
			return false;
		}
		if(loginNameCheck==1) {
			alert("用户名已经被占用！");
			return false;
		}
		if(document.getElementsByName("identityCardConfirm")[0].value!=document.getElementsByName("identityCard")[0].value) {
			alert("身份证号码不一致！");
			return false;
		}
		var len = document.getElementsByName("identityCard")[0].value.length;
		if(len!=15 && len!=18) {
			alert("身份证号码输入有误！");
			return false;
		}
		if(document.getElementsByName("password")[0].value.length<6||document.getElementsByName("password")[0].value.length>16) {
			alert("密码长度不符合要求！");
			return false;
		}
		if(document.getElementsByName("passwordConfirm")[0].value!=document.getElementsByName("password")[0].value) {
			alert("密码不一致！");
			return false;
		}
		if(document.getElementsByName("telFamily")[0].value=="") {
			alert("电话不能为空！");
			return false;
		}
		return true;
	}
	</script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/common/js/ratePassword.js"></script>
</head>
<body>
<ext:form action="/doEffectUnicomCard" onsubmit="return validate();">
<html:hidden property="cardNumber"/>
<html:hidden property="cardPassword"/>
<html:hidden property="unicomPersonId"/>
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
					<center>
						<div style="width:80%"><%@ include	file="/edu/skins/v2/default/detailFormPrompt.jsp"%></div>
					</center>
             		<table style="width:720px; color: #000000; table-layout:foxed" cellSpacing="0" cellPadding="3" align="center" border="0">
						<col width="120px">
						<col width="100%">
						<col width="300px">
						<tr>
							<td	style="FONT-SIZE: 14px; BORDER-BOTTOM: #a0c0ff 1px solid; HEIGHT: 30px"	vAlign=bottom align=left colSpan=3>
								<strong>基本信息<span style="color:#ff0000">（为了不影响您的使用，请仔细填写真实信息）</span></strong>
							</td>
						</tr>
						<tr>
							<td colSpan=3 height=6></td>
						</tr>
						<tr>
							<td height="24" align="right"><span style="color:#ff0000">*&nbsp;</span>所在地区：</td>
							<td align="left">
								<html:hidden property="orgId" />
								<html:text property="orgFullName" readonly="true" styleClass="needsclass" onclick="selectArea()"/>
		                    </td>
							<td width="254"  align="left" valign="top" style="font-size:12px"> 
								请点击左侧输入框选择所在区域,浏览器设置成允许弹出窗口 
							</td>
						</tr>
						<tr>
							<td height="24" align="right"><span style="color:#ff0000">*&nbsp;</span>所在年级：</td>
							<td align="left">
								<html:select property="grade">
								    <option>初一</option>
								    <option>初二</option>
								    <option>初三</option>
								</html:select>
							</td>
							<td width="254" align="left" valign="top" style="font-size:12px"> 
								请填写真实信息。
							</td>
						</tr>
						<tr>
							<td height="24" align="right"><span style="color:#ff0000">*&nbsp;</span>姓名：</td>
							<td align="left">
								<html:text property="name"  maxlength="4" styleClass="needsclass"/>
							</td>
							<td width="254" align="left" valign="top" style="font-size:12px"></td>
						</tr>
						<tr>
							<td height="24" align="right"><span style="color:#ff0000">*&nbsp;</span>身份证号码：</td>
							<td align="left">
								<html:text property="identityCard" maxlength="18" styleClass="needsclass"/>
							</td>
							<td width="254" align="left" valign="top" style="font-size:12px"> 
								请填写本人或者父母的身份证号码。
							</td>
						</tr>
						<tr>
							<td height="24" align="right"><span style="color:#ff0000">*&nbsp;</span>身份证号码确认：</td>
							<td align="left">
								<input type="text" name="identityCardConfirm" value="<ext:write property='identityCard'/>" maxlength="30" class="needsclass"/>
							</td>
							<td width="254" align="left" valign="top" style="font-size:12px"> 
								
							</td>
						</tr>
						<tr>
							<td height="24" align="right"><span style="color:#ff0000">*&nbsp;</span>身份证姓名：</td>
							<td align="left">
								<html:text property="identityCardName"  maxlength="4" styleClass="needsclass"/>
							</td>
							<td width="254" rowspan="3" align="left" valign="top" style="font-size:12px"> 
								请填写真实信息。
							</td>
						</tr>
						<tr>
							<td height="29" align="right">性别：</td>
							<td align="left">
								<html:radio style="border-style:none; width:20px" property="sex" value="M" styleClass="radio" styleId="male" /><label for="male"> &nbsp;男 </label>
								<html:radio style="border-style:none; width:20px" property="sex" value="F" styleClass="radio" styleId="female" /><label for="female"> &nbsp;女 </label>
							</td>
						</tr>
						<tr>
							<td height="24" align="right">电话：</td>
							<td align="left">
							<html:text property="telFamily" maxlength="16" styleClass="needsclass" />
							</td>
						</tr>
						<tr>
							<td height="24" align="right"><span style="color:#ff0000">*&nbsp;</span>电子邮箱：</td>
							<td align="left">
							<html:text property="mailAddress" maxlength="50" styleClass="needsclass"/>
							</td>
							<td rowspan="2" align="left" valign="top" style="font-size:12px"> 
								请填写正确的电子邮箱格式,不支持中文名。 例如: <span class="STYLE1">unicom@9191edu.com</span> 
							</td>
						</tr>
						<tr>
							<td height="24" align="right">家庭地址：</td>
							<td align="left">
							<html:text property="familyAddress" maxlength="50" styleClass="needsclass" />
							</td>
						</tr>
						<tr>
							<td height="37" colSpan=3 align=left vAlign=bottom style="FONT-SIZE: 14px; BORDER-BOTTOM: #a0c0ff 1px solid; HEIGHT: 30px"><strong>帐户信息</strong> </td>
							</tr>
						<tr>
							<td colSpan=3 height=6></td>
						</tr>
						<tr vAlign=center>
							<td height="24" align="right" valign="center"><span style="color:#ff0000">*&nbsp;</span>登录用户名：</td>
							<td align="left">
								<html:text onblur="checkLoginNameInUse()" property="loginName"  maxlength="20" styleClass="needsclass"/>
							</td>
							<td rowspan="3" align="left" valign="top" noWrap style="font-size:12px">
								<DIV id=loginNamePrompt style="DISPLAY: none; COLOR: #ff0000"></DIV>
								帐户名由字母 <span class="STYLE1">a</span>～<span class="STYLE1">z</span>(不区分大小写)、数字<span class="STYLE1">0</span>～<span class="STYLE1">9</span>、点、减号或下划线组成。 只能以字母开头，例如：<span class="STYLE1">beijing2008</span>
							</td>
						</tr>
						<tr>
							<td height="24" align="right"><span style="color:#ff0000">*&nbsp;</span>密码：</td>
							<td align="left">
								<html:password property="password" onkeyup="SetPwdStrengthEx(document.forms[0],this.value);" maxlength="20" />
							</td>
						</tr>
						<tr>
							<td height="24" noWrap align="right">密码安全等级：</td>
							<td align="left">
								<span style="text-align:center; height:20px; background-color:#EBEBEB">
									<div id="idSM1" style="float:left;width:30px;height:100%">
										<span id="idSMT1" style="display:none;height:100%;padding-top:3px">低</span>
									</div>
									<div id="idSM2" style="float:left;width:88px;height:100%"> 
										<span id="idSMT0" style="color:#666;padding-top:3px;height:100%">未能评级</span>
										<span id="idSMT2" style="display:none;padding-top:3px;height:100%">中</span>
									</div>
									<div id="idSM3" style="float:left;width:30px;height:100%">
										<span id="idSMT3" style="display:none;padding-top:3px;height:100%">高</span>
									</div>
								</span>
							</td>
						</tr>
						<tr>
							<td height="24" align="right"><span style="color:#ff0000">*&nbsp;</span>密码确认：</td>
							<td align="left">
								<input type="password" name="passwordConfirm" value="<ext:write property='password'/>" maxlength="20" class="needsclass"/>
							</td>
							<td rowspan="2" align="left" valign="top" noWrap style="font-size:12px">
								密码长度6～16位，字母区分大小写，不能包含字符：“ <span class="STYLE4">%</span>”“ <span class="STYLE4">&amp;</span>”“ <span class="STYLE4">?</span>”“ <span class="STYLE4">+</span>”。 
							</td>
						</tr>
						<tr>
							<td height="24" colspan="2" align="center" valign="top"></td>
						</tr>
						<tr>
							<td height="37" colSpan=3 align=left vAlign=bottom style="FONT-SIZE: 14px; BORDER-BOTTOM: #a0c0ff 1px solid; HEIGHT: 30px">
								<strong>注册</strong> 
							</td>
						</tr>
						<tr>
							<td colSpan=3 height=6></td>
						</tr>
						<tr>
							<td height="55" align="right"><span style="color:#ff0000">*&nbsp;</span>验证码：</td>
							<td align="left">
								<img id="validateCodeImage"	src="<%=request.getContextPath()%>/jeaf/validatecode/generateValidateCodeImage.shtml" align="bottom">
								<a style="color:blue" href="javascript:FormUtils.reloadValidateCodeImage()">看不清，换一张</a>
								<div style="font-size:3px">&nbsp;</div>
								<html:text property="validateCode" maxlength="10" styleClass="needsclass" />
							</td>
							<td></td>
						</tr>
					</table>
				</div> <!--main_title end-->
				<div id="main_text">
					<a href="#" onClick="FormUtils.submitForm();"><img src="image/enter.jpg" border="0"></a> &nbsp;&nbsp;&nbsp;&nbsp; 
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