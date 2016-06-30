<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="100%">语言类别</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">掌握程度</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">读写能力</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">听说能力</td>
	</tr>
	<ext:iterate id="speech" property="speechs">
		<tr align="center">
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="speech" property="language"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="speech" property="level"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="speech" property="literacy"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="speech" property="spoken"/></td>
		</tr>
	</ext:iterate>
</table>