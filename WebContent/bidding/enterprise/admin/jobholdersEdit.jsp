<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
function newJobholder(category) {
	if(document.getElementsByName("name")[0].value=="") {
		alert("企业名称不能为空");
		return;
	}
	if(document.getElementsByName("area")[0].value=="") {
		alert("企业所在地区不能为空");
		return;
	}
	DialogUtils.openDialog('<%=request.getContextPath()%>/bidding/enterprise/admin/jobholder.shtml?id=<ext:write property="id"/>&jobholder.category=' + StringUtils.utf8Encode(category) + "&jobholder.enterpriseName=" + StringUtils.utf8Encode(document.getElementsByName("name")[0].value), 600, 360);
}
function openJobholder(jobholderId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/bidding/enterprise/admin/jobholder.shtml?id=<ext:write property="id"/>&jobholder.id=' + jobholderId, 600, 360);
}
</script>
<div style="padding-bottom:8px">
	<input type="button" class="button" value="添加项目经理" style="width:90px" onclick="newJobholder('项目经理')">
	<input type="button" class="button" value="添加注册监理师" style="width:100px" onclick="newJobholder('注册监理师')">
	<input type="button" class="button" value="添加注册建造师" style="width:100px" onclick="newJobholder('注册建造师')">
	<input type="button" class="button" value="添加注册造价师" style="width:100px" onclick="newJobholder('注册造价师')">
	<input type="button" class="button" value="添加五大员" style="width:80px" onclick="newJobholder('五大员')">
</div>
<table id="tableJobholders" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
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