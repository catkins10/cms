<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>


<html:html>
<head>
	<title>互动教育网 - 注册学生账号</title>
	<LINK href="css/usermanage.css" type="text/css" rel="stylesheet">
	<link type="text/css" href="<%=request.getContextPath()%>/jeaf/form/css/form.css" rel="stylesheet">
	<script>
	function validate() {
		var hasComputer = getSelectedRedioValue("hasComputer");
		if(hasComputer==null) {
			alert("有没有电脑尚未选择");
			return false;
		}
		if(hasComputer=="0") {
			return true;
		}
		var connectInternet = getSelectedRedioValue("connectInternet");
		if(connectInternet==null) {
			alert("可不可以上网尚未选择");
			return false;
		}
		if(document.getElementsByName("whoUseInternet")[0].value=="") {
			alert("主要使用人尚未填写");
			return false;
		}
		if(document.getElementsByName("internetPurpose")[0].value=="") {
			alert("用途尚未填写");
			return false;
		}
		if(connectInternet=="1") { //可以上网
			if(getSelectedRedioValue("connectMode")==null) {
				alert("接入方式尚未选择");
				return false;
			}
			if(document.getElementsByName("bandwidth")[0].value=="") {
				alert("接入带宽尚未填写");
				return false;
			}
			if(getSelectedRedioValue("connectLimit")==null) {
				alert("接入期限尚未选择");
				return false;
			}
			if(getSelectedRedioValue("carrier")==null) {
				alert("所属网络尚未选择");
				return false;
			}
			if(getSelectedRedioValue("eduContent")==null) {
				alert("上网最想看的有关教育教学内容尚未选择");
				return false;
			}
		}
		return true;
	}
	function getSelectedRedioValue(fieldName) {
		var radios = document.getElementsByName(fieldName);
		for(var i=0; i<radios.length; i++) {
			if(radios[i].checked) {
				return radios[i].value;
			}
		}
		return null;
	}
	</script>
</head>
<body>
<ext:form action="/saveQuestionnaire" onsubmit="return validate();">
	<div id="header" style="width:100%;">
		<div class="inside">
			<table cellSpacing="0" cellPadding="0" border="0" style="width:100%; height:100%">
				<tr>
					<td class="title" valign="middle"></td>
					<td valign="bottom" align="right" style="padding-right:20px;padding-bottom:10px;font-size:14px">
						注册学生账号
					</td>
				</tr>
			</table>
		</div>
	</div>
	<table border="0" cellpadding="3" cellspacing="0" style="color:#000000; width:80%" align="center">
		<col align="left">
		<col width="100%">
		<col>
		<tr>
			<td colspan="3" align="left" style="font-size:14px; height:30px; border-bottom:1 solid #a0c0ff" valign="bottom">家庭电脑配备情况</td>
		</tr>
		<tr>
			<td colspan="3" height="6px"></td>
		</tr>
		<tr>
			<td nowrap>有没有电脑：</td>
			<td>
				<html:radio style="border-style:none; width:20px" property="hasComputer" value="1" styleClass="radio" styleId="hasComputerYes" /><label for="hasComputerYes">&nbsp;有　</label>
				<html:radio style="border-style:none; width:20px" property="hasComputer" value="0" styleClass="radio" styleId="hasComputerNo" /><label for="hasComputerNo">&nbsp;没有</label>
			</td>
			<td nowrap></td>
		</tr>
		<tr>
			<td nowrap>可不可以上网：</td>
			<td>
				<html:radio style="border-style:none; width:20px" property="connectInternet" value="1" styleClass="radio" styleId="connectInternetYes" /><label for="connectInternetYes">&nbsp;可以</label>
				<html:radio style="border-style:none; width:20px" property="connectInternet" value="0" styleClass="radio" styleId="connectInternetNo" /><label for="connectInternetNo">&nbsp;不可以</label>
			</td>
			<td nowrap></td>
		</tr>
		<tr>
			<td nowrap>主要使用人：</td>
			<td><ext:field property="whoUseInternet" itemsText="我\0家长" maxlength="10"/></td>
			<td nowrap></td>
		</tr>
		<tr>
			<td nowrap>用　　途：</td>
			<td><html:text property="internetPurpose" maxlength="50"/></td>
			<td nowrap></td>
		</tr>
		<tr>
			<td nowrap>接入方式：</td>
			<td>
				<html:radio style="border-style:none; width:20px" property="connectMode" value="ADSL" styleClass="radio" styleId="connectModeAdsl" /><label for="connectModeAdsl">&nbsp;ADSL</label>
				<html:radio style="border-style:none; width:20px" property="connectMode" value="LAN" styleClass="radio" styleId="connectModeLan" /><label for="connectModeLan">&nbsp;小区宽带</label>
			</td>
			<td nowrap></td>
		</tr>
		<tr>
			<td nowrap>接入带宽(M)：</td>
			<td><ext:field property="bandwidth" value="" itemsText="1\02\010\0100"/></td>
			<td nowrap>以兆为单位</td>
		</tr>
		<tr>
			<td nowrap>接入限期：</td>
			<td>
				<html:radio style="border-style:none; width:20px" property="connectLimit" value="3" styleClass="radio" styleId="connectLimit1" /><label for="connectLimit1">&nbsp;3个月</label>
				<html:radio style="border-style:none; width:20px" property="connectLimit" value="6" styleClass="radio" styleId="connectLimit2" /><label for="connectLimit2">&nbsp;半年</label>
				<html:radio style="border-style:none; width:20px" property="connectLimit" value="12" styleClass="radio" styleId="connectLimit3" /><label for="connectLimit3">&nbsp;全年</label>
			</td>
			<td nowrap></td>
		</tr>
		<tr>
			<td nowrap>所属网络：</td>
			<td>
				<html:radio style="border-style:none; width:20px" property="carrier" value="电信" styleClass="radio" styleId="carrier1" /><label for="carrier1">&nbsp;电信&nbsp;</label>
				<html:radio style="border-style:none; width:20px" property="carrier" value="网通" styleClass="radio" styleId="carrier2" /><label for="carrier2">&nbsp;网通</label>
				<html:radio style="border-style:none; width:20px" property="carrier" value="铁通" styleClass="radio" styleId="carrier3" /><label for="carrier3">&nbsp;铁通</label>
				<html:radio style="border-style:none; width:20px" property="carrier" value="广电" styleClass="radio" styleId="carrier4" /><label for="carrier4">&nbsp;广电</label>
				<html:radio style="border-style:none; width:20px" property="carrier" value="其它" styleClass="radio" styleId="carrier5" /><label for="carrier5">&nbsp;其它</label>
			</td>
			<td nowrap></td>
		</tr>
		<tr>
			<td colspan="3" align="left" style="font-size:14px; height:30px; border-bottom:1 solid #a0c0ff" valign="bottom">电脑应用调查</td>
		</tr>
		<tr style="padding-top:10px;">
			<td colspan="3">1、平常在家里，您的孩子都利用什么时间上网？
				<select name="connectBegin" style="width:50px">
					<%for(int i=0; i<24; i++) { %>
						<option><%=i%>
					<%} %>
				</select>点
				到
				<select name="connectEnd" style="width:50px">
					<%for(int i=0; i<24; i++) { %>
						<option><%=i%>
					<%} %>
				</select>点
			</td>
		</tr>
		<tr style="padding-top:10px;">
			<td colspan="3">2、平均一周约有多少时间使用电脑(包含上网)？ <html:text property="timesPerWeek" style="width:36px"/>&nbsp;小时</td>
		</tr>
		<tr style="padding-top:10px;">
			<td colspan="3">3、对家里电脑的使用率如何？</td>
		</tr>
		<tr>
			<td colspan="3">
				<html:radio style="border-style:none; width:20px" property="useRate" value="很高" styleClass="radio" styleId="useRate1" /><label for="useRate1">&nbsp;很高</label>
				<html:radio style="border-style:none; width:20px" property="useRate" value="一般" styleClass="radio" styleId="useRate2" /><label for="useRate2">&nbsp;一般</label>
				<html:radio style="border-style:none; width:20px" property="useRate" value="不高" styleClass="radio" styleId="useRate3" /><label for="useRate3">&nbsp;不高</label>
				<html:radio style="border-style:none; width:20px" property="useRate" value="很低" styleClass="radio" styleId="useRate4" /><label for="useRate4">&nbsp;很低</label>
			</td>
		</tr>
		<tr style="padding-top:10px;">
			<td colspan="3">4、如还没接入宽带，是否会因为一个好的教育网站选择家庭宽带接入服务？</td>
		</tr>
		<tr>
			<td colspan="3">
				<html:radio style="border-style:none; width:20px" property="connectInternetForSite" value="1" styleClass="radio" styleId="connectInternetForSiteYes" /><label for="connectInternetForSiteYes">&nbsp;愿意</label>
				<html:radio style="border-style:none; width:20px" property="connectInternetForSite" value="0" styleClass="radio" styleId="connectInternetForSiteNo" /><label for="connectInternetForSiteNo">&nbsp;不愿意</label>
			</td>
		</tr>
		<tr style="padding-top:10px;">
			<td colspan="3">5、上网最想看的有关教育教学内容是什么？</td>
		</tr>
		<tr>
			<td colspan="3">
				<html:radio style="border-style:none; width:20px" property="eduContent" value="教育资讯" styleClass="radio" styleId="eduContent1" /><label for="eduContent1">&nbsp;教育资讯</label>
				<html:radio style="border-style:none; width:20px" property="eduContent" value="教育方法" styleClass="radio" styleId="eduContent2" /><label for="eduContent2">&nbsp;教育方法</label>
				<html:radio style="border-style:none; width:20px" property="eduContent" value="网上课堂" styleClass="radio" styleId="eduContent3" /><label for="eduContent3">&nbsp;网上课堂</label>
				<html:radio style="border-style:none; width:20px" property="eduContent" value="网上测验" styleClass="radio" styleId="eduContent4" /><label for="eduContent4">&nbsp;网上测验</label>
				<html:radio style="border-style:none; width:20px" property="eduContent" value="中、高考试题" styleClass="radio" styleId="eduContent5" /><label for="eduContent5">&nbsp;中、高考试题</label>
				<html:radio style="border-style:none; width:20px" property="eduContent" value="其他" styleClass="radio" styleId="eduContent6" /><label for="eduContent6">&nbsp;其他</label>
			</td>
		</tr>
		<tr style="padding-top:10px;">
			<td colspan="3">6、上网时遇到的主要问题有哪些？</td>
		</tr>
		<tr>
			<td colspan="3">
				<html:text property="internetProblem" maxlength="50"/>
			</td>
		</tr>
		<tr style="padding-top:10px;">
			<td colspan="3">7、最让你想访问的教育网站应该是怎么的，具备什么（如：界面、内容、功能等）？</td>
		</tr>
		<tr>
			<td colspan="3">
				<html:text property="whatEduSite" maxlength="50"/>
			</td>
		</tr>
		<tr>
			<td colspan="3" align="center" style="padding-top:8px">
				<input type="button" class="button" value="注册" onclick="FormUtils.submitForm();">&nbsp;&nbsp;
				<input type="button" class="button" value="取消" onclick="window.close();">
			</td>
		</tr>
	</table>
</ext:form>
</body>
</html:html>