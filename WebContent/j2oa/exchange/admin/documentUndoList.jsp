<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">撤销时间</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">撤销发布的原因</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">撤销人</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">重新签收</td>
	</tr>
	<ext:iterate id="exchangeUndo" indexId="exchangeUndoIndex" property="exchangeUndos">
		<tr style="cursor:pointer" valign="top">
			<td class="tdcontent" align="center"><ext:writeNumber name="exchangeUndoIndex" plus="1"/></td>
			<td class="tdcontent" align="center"><ext:write name="exchangeUndo" property="undoTime" format="yyyy-MM-dd HH:mm" /></td>
			<td class="tdcontent"><ext:write name="exchangeUndo" property="undoReason" /></td>
			<td class="tdcontent" align="center"><ext:write name="exchangeUndo" property="undoPerson" /></td>
			<td class="tdcontent" align="center"><ext:equal value="1" name="exchangeUndo" property="resign">√</ext:equal></td>
		</tr>
	</ext:iterate>
</table>