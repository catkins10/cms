<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<html:html>
<head>
	<title>互动教育网 - 用户注册</title>
	<LINK href="css/usermanage.css" type="text/css" rel="stylesheet">
	<link type="text/css" href="<%=request.getContextPath()%>/jeaf/form/css/form.css" rel="stylesheet">
	<style>
		.STYLE1 {color: #FF6600;font-size: 14px;font-weight: bold;line-height: 25px;padding-top:10px}
		/*用户注册页*/
		.reg_txt{
		    width: 720px;
		    height: 400px;	
		    font-family: "宋体";
			font-size: 12px;
			line-height: 18px;
			padding: 10px;
			border-style:1 solid #808080;
		}
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
	<table border="0" cellpadding="3" cellspacing="0" style="color:#000000; width:720px" align="center">
		<tr>
			<td height="6px" class="STYLE1">【互动教育网】用户注册协议</td>
		</tr>
		<tr>
			<td>
			<textarea name="textarea" class="reg_txt" readonly="readonly">
1、互动教育网服务条款的确认和接纳
　　互动教育网的各项电子服务的所有权和运作权归众互动教育网所有。 互动教育网提供的服务将完全按照其发布的章程、服务条款和操作规则严格执行。用户必须完全同意所有服务条款并完成注册程序，方可成为互动教育网的正式用户。
　
2、服务简介
　　互动教育网通过国际互联网为用户提供互联网络信息服务。同时，用户必须：
　　(1)提供详尽、准确的用户信息。
　　(2)不断更新注册资料，符合及时、详尽、准确的要求。
　　如果用户提供的资料包含有不正确的信息，互动教育网保留终止用户使用网络服务资格的权利。 
　
3、服务条款的修改和服务修订
　　互动教育网有权在必要时修改服务条款，互动教育网服务条款发生变动时，将会在重要页面上提示修改内容。如果不同意所改动的内容，用户可以向互动教育网提出，双方协商解决。

4、用户的帐号，密码和安全性
　　用户注册成功后，将成为互动教育网的合法用户，并得到一个密码和用户名。
　　用户将对用户名和密码安全负全部责任。此外，每个用户都要对以其用户名进行的所有活动和事件负全责。您可随时根据指示改变您的密码。用户若发现任何非法使用用户账号或存在安全漏洞的情况，请立即通知互动教育网。 

5、用户管理
　　用户必须遵循：
　　(1)使用服务不作非法用途。
　　(2)不干扰或混乱服务程序及内容。
　　(3)遵守所有使用服务的网络协议、规定、程序和惯例。
　　用户须承诺不传输任何包含非法的、骚扰性的、中伤他人的、辱骂性的、恐吓性的、伤害性的、庸俗淫秽的信息的资料。另外，用户也不能传输任何教唆他人构成犯罪行为的资料；不能传输涉及国家安全的资料；不能传输任何不符合当地法规、国家法律和国际法律的资料。禁止未经许可而非法进入其它电脑系统。
　　若用户的行为不符合以上提到的服务条款，互动教育网将做出独立判断并立即取消用户服务帐号。用户需对自己在网上的行为承担法律责任。用户若在互动教育网上散布和传播反动、色情或其它违反国家法律的信息，互动教育网的系统记录有可能作为用户违反法律的证据。

6、保障
　　用户同意保障和维护互动教育网全体成员的利益，负责支付由用户使用超出服务范围引起的诉讼费用，违反服务条款的损害补偿费用等。

7、通告
　　所有发给用户的通告都可通过重要页面的公告或电子邮件或常规的信件传送。服务条款的修改、服务变更、或其它重要事件的通告都会以此形式进行。
　
8、服务内容的所有权
　　互动教育网定义的服务内容包括：文字、软件、声音、图片、录像、图表、广告中的全部内容；电子邮件的全部内容；互动教育网为用户提供的其它信息。所有这些内容受版权、商标、标签和其它财产所有权法律的保护。所以，用户只能在互动教育网授权下才能使用这些内容，而不能擅自复制、再造这些内容或创造与内容有关的派生产品。互动教育网所有的内容版权归原作者和互动教育网共同所有，任何人需要转载互动教育网的内容，必须征得原文作者或互动教育网授权。欢迎各位经同意后，具名、具网址引用。
　
9、法律
　　网络服务条款要与中华人民共和国的法律解释相一致，用户和互动教育网一致同意服从高等法院所有管辖。如有互动教育网服务条款与中华人民共和国法律相抵触的情况发生，则该条款将完全按法律规定重新解释，而其它条款则依旧保持对用户产生法律效力和影响。
      </textarea>
			</td>
		</tr>
		<tr>
			<td align="center" style="padding-top:8px">
				<input type="button" class="button" value="同意" onclick="location.href='registType.jsp'">&nbsp;&nbsp;
				<input type="button" class="button" value="不同意" onclick="window.close();">
			</td>
		</tr>
	</table>
</body>
</html:html>