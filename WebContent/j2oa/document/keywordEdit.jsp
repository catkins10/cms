<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">区域分类</td>
		<td class="tdcontent"><ext:field property="district"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">类别词</td>
		<td class="tdcontent"><ext:field property="category" /></td>
	</tr>
	<tr title="输入主题词,主题词之间用空格分隔">
		<td valign="top" class="tdtitle" nowrap="nowrap">主题词</td>
		<td class="tdcontent"><ext:field property="keywordList"/></td>
	</tr>
</table>
