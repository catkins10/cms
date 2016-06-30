<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function openCert(certId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/bidding/enterprise/admin/enterpriseCert.shtml?id=<ext:write property="id"/>&cert.id=' + certId, 500, 290);
}
function openSurvey(surveyId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/bidding/enterprise/admin/enterpriseCertSurvey.shtml?id=<ext:write property="id"/>&survey.id=' + surveyId, 450, 260);
}
</script>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom">
		<td align="center" width="100px" class="tdtitle" nowrap="nowrap">资质类型</td>
		<td align="center" width="100%" class="tdtitle" nowrap="nowrap">资质证书编号</td>
		<td align="center" width="100px" class="tdtitle" nowrap="nowrap">资质等级</td>
		<td align="center" width="70px" class="tdtitle" nowrap="nowrap">年检年份</td>
		<td align="center" width="100px" class="tdtitle" nowrap="nowrap">年检结果</td>
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
		</tr>
	</ext:iterate>
</table>