<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:iterate id="inquiry" indexId="inquiryIndex" property="inquiries">
	<ext:equal value="1" property="isQuestionnaire">
		&nbsp;<ext:writeNumber name="inquiryIndex" plus="1"/>、<ext:write name="inquiry" property="descriptionText" maxCharCount="100" ellipsis="..."/>
	</ext:equal>
	<table width="100%" class="table" style="margin-bottom: 8px" border="1" cellpadding="0" cellspacing="0">
		<tr height="23px" valign="bottom">
			<td align="center" nowrap="nowrap" class="tdtitle" width="50px">序号</td>
			<td align="center" nowrap="nowrap" class="tdtitle" width="100%">投票人</td>
			<td align="center" nowrap="nowrap" class="tdtitle" width="60px">匹配数</td>
			<td align="center" nowrap="nowrap" class="tdtitle" width="300px">匹配率</td>
		</tr>
		<ext:iterate id="match" indexId="matchIndex" name="inquiry" property="matchs">
			<tr align="center">
				<td class="tdcontent" align="center"><ext:writeNumber name="matchIndex" plus="1"/></td>
				<td class="tdcontent" align="left"><ext:write name="match" property="personName"/></td>
				<td class="tdcontent"><ext:write name="match" property="matchNumber"/></td>
				<td class="tdcontent" align="left">
					<table border="0" width="100%" cellspacing="0" cellpadding="3px" style="table-layout:fixed">
						<tr>
							<td style="width:43px"><ext:write name="match" property="matchRate" format="###.##%"/></td>
							<td>
								<div style="background:red;width:<ext:write name="match" property="matchRate" format="###.##%"/>"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</ext:iterate>
	</table>
</ext:iterate>