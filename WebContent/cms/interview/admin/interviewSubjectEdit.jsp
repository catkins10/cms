<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col>
			<col valign="middle" width="100%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">主题</td>
				<td class="tdcontent"><ext:field property="subject"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">访谈时间</td>
				<td class="tdcontent">
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td width="50%"><ext:field property="beginTime"/></td>
							<td nowrap="nowrap">&nbsp;至&nbsp;</td>
							<td width="50%"><ext:field property="endTime"/></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">是否结束</td>
				<td class="tdcontent"><ext:field property="isEnding"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">嘉宾</td>
				<td class="tdcontent"><ext:field property="guests"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">嘉宾登录密码</td>
				<td class="tdcontent"><ext:field property="guestsPassword"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">主持人</td>
				<td class="tdcontent"><ext:field property="compereNames"/></td>
			</tr>
			<ext:iterate id="role" property="roles">
				<tr>
					<td class="tdtitle" nowrap="nowrap"><ext:write name="role" property="role"/></td>
					<td class="tdcontent">
						<input type="hidden" name="roleId" value="<ext:write name="role" property="id"/>"/>
						<input type="hidden" name="roleName_<ext:write name="role" property="id"/>" value="<ext:write name="role" property="role"/>"/>
						<input type="hidden" name="roleMemberIds_<ext:write name="role" property="id"/>" value="<ext:write name="role" property="roleMemberIds"/>"/>
						<script>new SelectField('<input type="text" name="roleMemberNames_<ext:write name="role" property="id"/>" value="<ext:write name="role" property="roleMemberNames"/>" readonly="readonly">', 'DialogUtils.selectPerson(600, 400, true, "roleMemberIds_<ext:write name="role" property="id"/>{id},roleMemberNames_<ext:write name="role" property="id"/>{name|人员|100%}")', 'field required', '', '', '')</script>
					</td>
				</tr>
			</ext:iterate>
			<tr>
				<td class="tdtitle" nowrap="nowrap">网友发言审核顺序</td>
				<td class="tdcontent"><ext:field property="speakFlow"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">主持人发言审核顺序</td>
				<td class="tdcontent"><ext:field property="compereSpeakFlow"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">嘉宾发言审核顺序</td>
				<td class="tdcontent"><ext:field property="guestsSpeakFlow"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">创建人</td>
				<td class="tdcontent"><ext:field property="creator"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">创建时间</td>
				<td class="tdcontent"><ext:field property="created"/></td>
			</tr>
		</table>
	</ext:tabBody>
	
	<ext:tabBody tabId="interviewBackground">	
		<div>
			<ext:field property="background"/>
		</div>
	</ext:tabBody>
	
	<ext:tabBody tabId="interviewGuestsIntro">	
		<div>
			<ext:field property="guestsIntro"/>
		</div>
	</ext:tabBody>
</ext:tab>