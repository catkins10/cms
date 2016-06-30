<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function openJobholder(jobholderId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/bidding/enterprise/admin/jobholder.shtml?id=<ext:write property="id"/>&jobholder.id=' + jobholderId, 600, 360);
}
</script>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom">
		<td width="36px" align="center" nowrap="nowrap" class="tdtitle">序号</td>
		<td width="100px" align="center" nowrap="nowrap" class="tdtitle">类别</td>
		<td width="80px" align="center" nowrap="nowrap" class="tdtitle">姓名</td>
		<td width="100%" align="center" nowrap="nowrap" class="tdtitle">等级/分类</td>
		<td width="160px" align="center" nowrap="nowrap" class="tdtitle">证书号码</td>
		<td width="80px" align="center" nowrap="nowrap" class="tdtitle">发证时间</td>
	</tr>
	<ext:iterate id="jobholder" indexId="jobholderIndex" property="jobholders">
		<tr style="cursor:pointer" align="center">
			<td class="tdcontent" onclick="openJobholder('<ext:write name="jobholder" property="id"/>')"><ext:writeNumber name="jobholderIndex" plus="1"/></td>
			<td class="tdcontent" onclick="openJobholder('<ext:write name="jobholder" property="id"/>')" align="left"><ext:write name="jobholder" property="category"/></td>
			<td class="tdcontent" onclick="openJobholder('<ext:write name="jobholder" property="id"/>')"><ext:write name="jobholder" property="name"/></td>
			<td class="tdcontent" onclick="openJobholder('<ext:write name="jobholder" property="id"/>')"><ext:write name="jobholder" property="qualification"/></td>
			<td class="tdcontent" onclick="openJobholder('<ext:write name="jobholder" property="id"/>')"><ext:write name="jobholder" property="certificateNumber"/></td>
			<td class="tdcontent" onclick="openJobholder('<ext:write name="jobholder" property="id"/>')"><ext:write name="jobholder" property="certificateCreated" format="yyyy-MM-dd"/></td>
		</tr>
	</ext:iterate>
</table>