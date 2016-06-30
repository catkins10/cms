<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col valign="middle" width="100%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">主题</td>
				<td class="tdcontent"><ext:write property="subject"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">访谈时间</td>
				<td class="tdcontent"><ext:write property="beginTime" format="yyyy-MM-dd HH:mm"/>&nbsp;至&nbsp;<ext:write property="endTime" format="yyyy-MM-dd HH:mm"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">嘉宾</td>
				<td class="tdcontent"><ext:write property="guests"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">主持人</td>
				<td class="tdcontent"><ext:write property="compereNames"/></td>
			</tr>
			<ext:iterate id="role" property="roles">
				<tr>
					<td class="tdtitle"><ext:write name="role" property="role"/></td>
					<td class="tdcontent"><ext:write name="role" property="roleMemberNames"/></td>
				</tr>
			</ext:iterate>
			<tr>
				<td class="tdtitle" nowrap="nowrap">网友发言审核顺序</td>
				<td class="tdcontent"><ext:write property="speakFlow"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">主持人发言审核顺序</td>
				<td class="tdcontent"><ext:write property="compereSpeakFlow"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">嘉宾发言审核顺序</td>
				<td class="tdcontent"><ext:write property="guestsSpeakFlow"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">创建人</td>
				<td class="tdcontent"><ext:write property="creator"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">创建时间</td>
				<td class="tdcontent"><ext:write property="created" format="yyyy-MM-dd HH:mm"/></td>
			</tr>
		</table>
	</ext:tabBody>

	<ext:tabBody tabId="interviewBackground">
		<ext:write property="background" filter="false"/>
	</ext:tabBody>

	<ext:tabBody tabId="interviewGuestsIntro">
		<ext:write property="guestsIntro" filter="false"/>
	</ext:tabBody>
</ext:tab>