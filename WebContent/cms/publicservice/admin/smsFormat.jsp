<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveSmsFormat">
   	<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col>
		<col valign="middle" width="100%">
		<tr>
			<td valign="top" class="tdtitle" nowrap="nowrap"><br><br>短信格式</td>
			<td class="tdcontent">
				<input type="button" value="插入主题" onclick="FormUtils.pasteText('smsFormat', '&lt;主题&gt;')" class="button" style="width:60px">
				<input type="button" value="插入提交人" onclick="FormUtils.pasteText('smsFormat', '&lt;提交人&gt;')" class="button" style="width:72px">
				<input type="button" value="插入编号" onclick="FormUtils.pasteText('smsFormat', '&lt;编号&gt;')" class="button" style="width:60px">
				<input type="button" value="插入办理意见" onclick="FormUtils.pasteText('smsFormat', '&lt;办理意见&gt;')" class="button" style="width:86px">
				<input type="button" value="插入办结时间" onclick="FormUtils.pasteText('smsFormat', '&lt;办结时间&gt;')" class="button" style="width:86px">
				<html:textarea property="smsFormat" rows="6"/>
			</td>
		</tr>
	</table>
</ext:form>