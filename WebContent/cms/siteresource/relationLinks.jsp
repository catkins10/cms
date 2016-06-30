<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newResourceLinks() {
	var keywords = document.getElementsByName('keyword')[0] ? document.getElementsByName('keyword')[0].value : '';
	DialogUtils.openSelectDialog('cms/sitemanage', 'selectRelationResources', '80%', '80%', true, 'relationResourceIds{id},relationResourceSubjects{subject|标题|100%}', 'doAddResourceLinks()', '', keywords, ',', true, 'currentResorceId=<ext:write property="id"/>&columnIds=<ext:write property="relationResourceColumnIds"/>');
}
function doAddResourceLinks() {
	if(document.getElementsByName("relationResourceIds")[0].value!="") {
		FormUtils.doAction('addRelationLinks');
	}
}
function newRelationLink() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/siteresource/admin/relationLink.shtml?id=<ext:write property="id"/>', 430, 300);
}
function openRelationLink(relationLinkId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/siteresource/admin/relationLink.shtml?id=<ext:write property="id"/>&relationLink.id=' + relationLinkId, 430, 300);
}
function adjustRelationLinkPriority() {
	DialogUtils.adjustPriority('cms/sitemanage', 'admin/adjustRelationLinkPriority', '调整优先级', '80%', '80%', 'resourceId=<ext:write property="id"/>');
}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加文章链接" onclick="newResourceLinks()">
		<input type="button" class="button" value="添加自定义链接" onclick="newRelationLink()">
		<input type="button" class="button" value="调整优先级" onclick="adjustRelationLinkPriority()">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="36">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">链接名称</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">链接URL</td>
	</tr>
	<ext:iterate id="relationLink" indexId="relationLinkIndex" property="relationLinks">
		<tr style="cursor:pointer" valign="top">
			<td class="tdcontent" align="center" onclick="openRelationLink('<ext:write name="relationLink" property="id"/>')"><ext:writeNumber name="relationLinkIndex" plus="1"/></td>
			<td class="tdcontent" onclick="openRelationLink('<ext:write name="relationLink" property="id"/>')"><ext:write name="relationLink" property="linkName"/></td>
			<td class="tdcontent"><a <ext:equal value="0" name="relationLink" property="relationResourceId">href="<ext:write name="relationLink" property="linkUrl"/>" target="_blank"</ext:equal><ext:notEqual value="0" name="relationLink" property="relationResourceId">href="javascript:PageUtils.editrecord('cms/siteresource', 'admin/article', '<ext:write  name="relationLink" property="relationResourceId"/>', 'mode=fullscreen')"</ext:notEqual>"><ext:write name="relationLink" property="linkUrl"/></a></td>
		</tr>
	</ext:iterate>
</table>