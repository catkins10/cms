<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">反馈信息</td>
		<td class="tdtitle" nowrap="nowrap" width="110px">反馈时间</td>
		<td class="tdtitle" nowrap="nowrap" width="160px">单位</td>
		<td class="tdtitle" nowrap="nowrap" width="50px">答复</td>
	</tr>
	<ext:iterate id="exchangeMessage" indexId="exchangeMessageIndex" property="exchangeMessages">
		<ext:equal value="0" name="exchangeMessage" property="replyMessageId">
			<tr style="cursor:pointer" valign="top" onclick="PageUtils.editrecord('j2oa/exchange', 'admin/message', '<ext:write name="exchangeMessage" property="id" />', 'width=500,height=400,mode=dialog')">
				<td class="tdcontent" align="center"><ext:writeNumber name="exchangeMessageIndex" plus="1"/></td>
				<td class="tdcontent"><ext:write name="exchangeMessage" property="subject" /></td>
				<td class="tdcontent" align="center"><ext:write name="exchangeMessage" property="created" format="yyyy-MM-dd HH:mm" /></td>
				<td class="tdcontent"><ext:write name="exchangeMessage" property="creatorUnit" /></td>
				<td class="tdcontent" align="center"><ext:notEmpty name="exchangeMessage" property="reply">√</ext:notEmpty></td>
			</tr>
		</ext:equal>
	</ext:iterate>
</table>