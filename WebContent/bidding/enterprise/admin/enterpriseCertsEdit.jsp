<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newCert(type) {
	if(document.getElementsByName("name")[0].value=="") {
		alert("企业名称不能为空");
		return;
	}
	if(document.getElementsByName("area")[0].value=="") {
		alert("企业所在地区不能为空");
		return;
	}
	DialogUtils.openDialog('<%=request.getContextPath()%>/bidding/enterprise/admin/enterpriseCert.shtml?id=<ext:write property="id"/>&cert.type=' + StringUtils.utf8Encode(type), 550, 320);
}
function openCert(certId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/bidding/enterprise/admin/enterpriseCert.shtml?id=<ext:write property="id"/>&cert.id=' + certId, 550, 320);
}
function newSurvey(certId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/bidding/enterprise/admin/enterpriseCertSurvey.shtml?id=<ext:write property="id"/>&survey.certId=' + certId, 450, 260);
}
function openSurvey(surveyId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/bidding/enterprise/admin/enterpriseCertSurvey.shtml?id=<ext:write property="id"/>&survey.id=' + surveyId, 450, 260);
}
</script>
<div style="padding-bottom:8px">
<input type="button" class="button" value="添加施工企业资质" style="width:120px" onclick="newCert('施工企业')">
<input type="button" class="button" value="添加监理企业资质" style="width:120px" onclick="newCert('监理企业')">
<input type="button" class="button" value="添加招标代理资质" style="width:120px" onclick="newCert('招标代理')">
<input type="button" class="button" value="添加房地产企业资质" style="width:120px" onclick="newCert('房地产企业')">
<input type="button" class="button" value="添加其它资质" style="width:120px" onclick="newCert('其它')">
</div>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom">
		<td align="center" class="tdtitle" width="100px" nowrap="nowrap">资质类型</td>
		<td align="center" class="tdtitle" width="100%" nowrap="nowrap">资质证书编号</td>
		<td align="center" class="tdtitle" width="100px" nowrap="nowrap">资质等级</td>
		<td align="center" class="tdtitle" width="70px" nowrap="nowrap">年检年份</td>
		<td align="center" class="tdtitle" width="100px" nowrap="nowrap">年检结果</td>
		<td align="center" class="tdtitle" width="100px" nowrap="nowrap">&nbsp;</td>
	</tr>
	<ext:iterate id="cert" property="certs">
		<tr style="cursor:pointer" align="center" valign="top">
			<td class="tdcontent" onclick="openCert('<ext:write name="cert" property="id"/>')" align="left"><ext:write name="cert" property="type"/></td>
			<td class="tdcontent" onclick="openCert('<ext:write name="cert" property="id"/>')"><ext:write name="cert" property="certificateNumber"/></td>
			<td class="tdcontent" onclick="openCert('<ext:write name="cert" property="id"/>')"><ext:write name="cert" property="level"/></td>
			<td class="tdcontent">
				<ext:iterate id="survey" name="cert" property="surveies">
					<pre onclick="openSurvey('<ext:write name="survey" property="id"/>')"><ext:write name="survey" property="surveyYear"/></pre>
				</ext:iterate>
			</td>
			<td class="tdcontent">
				<ext:iterate id="survey" name="cert" property="surveies">
					<pre onclick="openSurvey('<ext:write name="survey" property="id"/>')"><ext:write name="survey" property="surveyResult"/></pre>
				</ext:iterate>
			</td>
			<td class="tdcontent" onclick="newSurvey('<ext:write name="cert" property="id"/>')">添加年检记录</td>
		</tr>
	</ext:iterate>
</table>