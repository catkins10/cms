<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("editabled", "true");%>
<script>
	function selectFirstImage() {
		DialogUtils.selectAttachment('/cms/siteresource/admin/selectAttachment.shtml', '<ext:write property="id"/>', 'images', 680, 400, 'setFirstImageName("{URL}")');
	}
	function setFirstImageName(url) {
		document.getElementsByName("firstImageName")[0].value = StringUtils.utf8Decode(url.substring(url.lastIndexOf('/') + 1));
	}
</script>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td valign="top" class="tdtitle" nowrap="nowrap">标题</td>
		<td colspan="3" class="tdcontent"><ext:field property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">副标题</td>
		<td class="tdcontent"><ext:field property="subhead"/></td>
		<td class="tdtitle" nowrap="nowrap">文号</td>
		<td class="tdcontent"><ext:field property="mark"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属栏目</td>
		<td colspan="3" class="tdcontent"><ext:field property="columnName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属其它栏目</td>
		<td colspan="3" class="tdcontent"><ext:field property="otherColumnNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">来源</td>
		<td class="tdcontent"><ext:field property="source"/></td>
		<td class="tdtitle" nowrap="nowrap">作者</td>
		<td class="tdcontent"><ext:field property="author"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">关键字</td>
		<td class="tdcontent"><ext:field property="keyword"/></td>
		<td class="tdtitle" nowrap="nowrap">标题颜色</td>
		<td class="tdcontent"><ext:field property="subjectColor"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
		<td class="tdtitle" nowrap="nowrap">发布时间</td>
		<td class="tdcontent"><ext:field property="issueTime"/></td>
	</tr>
	<ext:notEmpty property="readers.visitorNames">
		<tr>
			<td class="tdtitle" nowrap="nowrap">访问者</td>
			<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="readers.visitorNames"/></td>
		</tr>
	</ext:notEmpty>
	<tr style="height:280px">
		<td valign="top" class="tdtitle" nowrap="nowrap">内容</td>
		<td colspan="3" class="tdcontent" class="tdcontent"><ext:field property="body"/></td>
	</tr>
</table>