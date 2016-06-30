<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">报送级别</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">报送时间</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">采用时间</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">采用刊物名称</td>
	</tr>
	<ext:iterate id="use" indexId="useIndex" property="uses">
		<tr valign="top" align="center">
			<td class="tdcontent"><ext:writeNumber name="useIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="use" property="level"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="use" property="sendTime"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="use" property="useTime"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="use" property="magazine"/></td>
		</tr>
	</ext:iterate>
</table>