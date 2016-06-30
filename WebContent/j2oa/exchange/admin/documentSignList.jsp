<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">单位名称</td>
		<td class="tdtitle" nowrap="nowrap" width="120px">签收时间</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">签收人</td>
	</tr>
	<ext:iterate id="exchangeUnit" indexId="exchangeUnitIndex" property="exchangeUnits">
		<tr style="cursor:pointer" valign="top">
			<td class="tdcontent" align="center"><ext:writeNumber name="exchangeUnitIndex" plus="1"/></td>
			<td class="tdcontent"><ext:write name="exchangeUnit" property="unitName" /></td>
			<td class="tdcontent" align="center"><ext:write name="exchangeUnit" property="signTime" format="yyyy-MM-dd HH:mm" /></td>
			<td class="tdcontent" align="center"><ext:write name="exchangeUnit" property="signPerson" /></td>
		</tr>
	</ext:iterate>
</table>