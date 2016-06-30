<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名／名称</td>
		<td class="tdcontent" ><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">类型</td>
		<td class="tdcontent" ><ext:field property="type"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">执行案号</td>
		<td class="tdcontent"><ext:field property="code"/></td>
		<td class="tdtitle" nowrap="nowrap">执行法院</td>
		<td class="tdcontent"><ext:field property="court"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布时间</td>
		<td class="tdcontent"><ext:field property="issueTime"/></td>
		<td class="tdtitle" nowrap="nowrap">最后更新时间</td>
		<td class="tdcontent"><ext:field property="updateTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">状态</td>
		<td class="tdcontent" colspan="3"><ext:field property="status"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>