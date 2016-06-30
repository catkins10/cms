<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom">
		<td align="center" class="tdtitle" width="50px" nowrap="nowrap">序号</td>
		<td align="center" class="tdtitle" width="40%" nowrap="nowrap">调查描述</td>
		<td align="center" class="tdtitle" width="60%" nowrap="nowrap">选项列表</td>
	</tr>
	<ext:iterate id="inquiry" indexId="inquiryIndex" property="inquiries">
		<tr align="center" valign="top">
			<td class="tdcontent" align="center"><ext:writeNumber name="inquiryIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:write name="inquiry" property="descriptionText" maxCharCount="100" ellipsis="..."/></td>
			<td class="tdcontent" align="left">
				<ext:iterate id="option" indexId="optionIndex" name="inquiry" property="options">
					<ext:notEqual value="0" name="optionIndex">、</ext:notEqual><ext:write name="option" property="inquiryOption"/>
				</ext:iterate>
			</td>
		</tr>
	</ext:iterate>
</table>