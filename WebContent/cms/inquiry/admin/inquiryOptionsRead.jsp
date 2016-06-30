<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom">
		<td align="center" nowrap="nowrap" class="tdtitle" width="50px">序号</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="100%">选项主题</td>
	</tr>
	<ext:iterate id="option" indexId="optionIndex" property="options">
		<tr align="center" valign="top">
			<td class="tdcontent" align="center"><ext:writeNumber name="optionIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:write name="option" property="inquiryOption"/></td>
		</tr>
	</ext:iterate>
</table>