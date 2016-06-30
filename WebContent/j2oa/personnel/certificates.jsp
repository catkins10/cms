<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newCertificate() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/personnel/certificate.shtml?id=<ext:write property="id"/>', 500, 290);
	}
	function openCertificate(certificateId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/personnel/certificate.shtml?id=<ext:write property="id"/>&certificate.id=' + certificateId, 500, 290);
	}
</script>
<%if(!"true".equals(request.getAttribute("readonly"))) { %>
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="登记" style="width:80px" onclick="newCertificate()">
	</div>
<%}%>
<table id="tableBill" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td width="120px" nowrap="nowrap" class="tdtitle">时间</td>
		<td width="160px" nowrap="nowrap" class="tdtitle">证书或者资质名称</td>
		<td nowrap="nowrap" class="tdtitle">备注</td>
	</tr>
	<ext:iterate id="certificate" indexId="certificateIndex" property="certificates">
		<tr onclick="openCertificate('<ext:write name="certificate" property="id"/>')" align="center">
			<td class="tdcontent"><ext:write name="certificate" property="issueDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent"><ext:write name="certificate" property="name"/></td>
			<td class="tdcontent" align="left"><ext:write name="certificate" property="remark"/></td>
		</tr>
	</ext:iterate>
</table>