<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100px">领导姓名</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">领导级别</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">批示内容</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">批示时间</td>
	</tr>
	<ext:iterate id="instruct" indexId="instructIndex" property="instructs">
		<tr valign="top" align="center">
			<td class="tdcontent"><ext:writeNumber name="instructIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="instruct" property="leader"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="instruct" property="level"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="instruct" property="instruct"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="instruct" property="instructTime"/></td>
		</tr>
	</ext:iterate>
</table>