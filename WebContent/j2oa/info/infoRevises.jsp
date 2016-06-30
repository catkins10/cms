<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">退改稿意见</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">退改稿人</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">退改稿时间</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">修改人</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">修改时间</td>
	</tr>
	<ext:iterate id="revise" indexId="reviseIndex" property="revises">
		<tr valign="top" align="center">
			<td class="tdcontent"><ext:writeNumber name="reviseIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="revise" property="reviseOpinion"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="revise" property="revisePerson"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="revise" property="reviseTime"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="revise" property="editor"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="revise" property="editTime"/></td>
		</tr>
	</ext:iterate>
</table>