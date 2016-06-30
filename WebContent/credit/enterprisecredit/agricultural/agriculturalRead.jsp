<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名（企业、个人)</td>
		<td class="tdcontent" ><ext:field property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">奖惩情况</td>
		<td class="tdcontent" ><ext:field property="summary"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">时间</td>
		<td class="tdcontent"><ext:field property="time"/></td>
		<td class="tdtitle" nowrap="nowrap">地点</td>
		<td class="tdcontent"><ext:field property="addr"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">事由</td>
		<td class="tdcontent"><ext:field property="reason"/></td>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field property="remark"/></td>
	</tr>
	
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>