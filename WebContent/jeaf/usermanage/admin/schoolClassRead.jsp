<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col valign="middle">
			<col valign="middle" width="100%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">入学年份</td>
				<td colspan="3" class="tdcontent"><ext:write property="enrollTime"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">学制</td>
				<td colspan="3" class="tdcontent"><ext:write property="lengthOfSchooling"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">班级编号</td>
				<td colspan="3" class="tdcontent"><ext:write property="classNumber"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">名称</td>
				<td colspan="3" class="tdcontent"><ext:write property="directoryName"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">所在学校</td>
				<td colspan="3" class="tdcontent"><ext:write property="parentDirectoryName"/></td>
			</tr>
		</table>
	</ext:tabBody>

	<ext:tabBody tabId="teacher">
		<table class="table" width="100%" border="0" cellpadding="3" cellspacing="1">
			<tr>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100px">教师姓名</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100%">称谓</td>
				<ext:iterate id="classTeacher" name="adminSchoolClass" property="teachers">
					<tr bordercolor="E1E8F5" height="20px">
						<td class="tdcontent">
							<a href="javascript:PageUtils.editrecord('jeaf/usermanage', 'admin/teacher', <ext:write name="classTeacher" property="teacher.id"/>, 'width=720,height=480')"><ext:write name="classTeacher" property="teacher.name"/></a>
						</td>
						<td class="tdcontent"><ext:write name="classTeacher" property="title"/></td>
					</tr>
				</ext:iterate>
			</tr>	
		</table>
	</ext:tabBody>
</ext:tab>