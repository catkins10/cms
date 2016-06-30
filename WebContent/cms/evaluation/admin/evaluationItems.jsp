<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newItem() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/cms/evaluation/admin/evaluationItem.shtml?id=<ext:write property="id"/>', 500, 300);
	}
	function openItem(evaluationItemId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/cms/evaluation/admin/evaluationItem.shtml?id=<ext:write property="id"/>&item.id=' + evaluationItemId, 500, 300);
	}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加测评项目" onclick="newItem()"/>
		<input type="button" class="button" value="调整优先级" onclick="DialogUtils.adjustPriority('cms/evaluation', 'admin/evaluationItem', '测评项目优先级', 600, 400, 'topicId=<ext:write property="id"/>')"/>
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">测评项目名称</td>
		<td class="tdtitle" nowrap="nowrap" width="120px">分类</td>
		<td class="tdtitle" nowrap="nowrap" width="60px">分值</td>
	</tr>
	<ext:iterate id="evaluationItem" indexId="evaluationItemIndex" property="items">
		<tr style="cursor:pointer" align="center" onclick="openItem('<ext:write name="evaluationItem" property="id"/>')">
			<td class="tdcontent"><ext:writeNumber name="evaluationItemIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:write name="evaluationItem" property="name"/></td>
			<td class="tdcontent"><ext:write name="evaluationItem" property="category"/></td>
			<td class="tdcontent"><ext:write name="evaluationItem" property="score" format="###"/></td>
		</tr>
	</ext:iterate>
</table>