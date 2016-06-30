<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
function newOfficialDocument() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/officialDocument.shtml?id=<ext:write property="id"/>', 640, 480);
}
function openOfficialDocument(officialDocumentId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/officialDocument.shtml?id=<ext:write property="id"/>&officialDocument.id=' + officialDocumentId, 640, 480);
}
</script>
<ext:empty name="debrief"><ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加项目审批文件" style="width:150px" onclick="newOfficialDocument()">
	</div>
</ext:equal></ext:empty>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="100%">标题</td>
		<td class="tdtitle" nowrap="nowrap" title="批准该项目的公文文号" width="180px">批准文号</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">批准日期</td>
		<td class="tdtitle" nowrap="nowrap" width="50px">待审核</td>
	</tr>
	<ext:iterate id="officialDocument" property="officialDocuments">
<%		if(request.getAttribute("debrief")==null || ((com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent)pageContext.getAttribute("officialDocument")).getNeedApprovalTitle().equals("√")) { %>
			<tr style="cursor:pointer" valign="top" onclick="openOfficialDocument('<ext:write name="officialDocument" property="id"/>')">
				<td class="tdcontent"><ext:write name="officialDocument" property="subject"/></td>
				<td class="tdcontent"><ext:write name="officialDocument" property="documentNumber"/></td>
				<td class="tdcontent" align="center"><ext:write name="officialDocument" property="approvalDate" format="yyyy-MM-dd"/></td>
				<td class="tdcontent" align="center"><ext:write name="officialDocument" property="needApprovalTitle"/></td>
			</tr>
<%		} %>
	</ext:iterate>
</table>