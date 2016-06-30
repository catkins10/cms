<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/usermanage/js/usermanage.js"></script>
<ext:tab>
	<ext:tabBody tabId="basic">
		<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col valign="middle">
			<col valign="middle" width="100%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">入学年份</td>
				<td colspan="3" class="tdcontent"><ext:field property="enrollTime"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">学制</td>
				<td colspan="3" class="tdcontent"><ext:field property="lengthOfSchooling"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">班级编号</td>
				<td colspan="3" class="tdcontent"><ext:field property="classNumber"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">名称</td>
				<td colspan="3" class="tdcontent"><ext:field property="directoryName"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">所在学校</td>
				<td colspan="3" class="tdcontent"><ext:field property="parentDirectoryName"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">是否毕业</td>
				<td colspan="3" class="tdcontent"><ext:field property="halt"/></td>
			</tr>
			<jsp:include page="/jeaf/directorymanage/popedomConfigEdit.jsp"/>
			<tr>
				<td class="tdtitle" nowrap="nowrap">注册人</td>
				<td colspan="3" class="tdcontent"><ext:field property="creator"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">注册时间</td>
				<td colspan="3" class="tdcontent"><ext:field property="created"/></td>
			</tr>
		</table>
	</ext:tabBody>
		
	<ext:tabBody tabId="teacher">
		<table border="0" cellpadding="2" cellspacing="0">
			<tr>
				<td nowrap="nowrap">选择教师:</td>
				<td nowrap="nowrap" width="100px" class="tdcontent"><ext:field property="teacherName"/></td>
				<td nowrap="nowrap">称谓:</td>
				<td nowrap="nowrap" width="160px">
					<ext:field property="teacherTitle" itemsProperty="teacherTitles" />
				</td>
				<td nowrap="nowrap"><input type="button" class="button" value="添加" onclick="if(document.getElementsByName('teacherName')[0].value!='' && document.getElementsByName('teacherTitle')[0].value!='')FormUtils.doAction('addTeacher')"></td>
				<td nowrap="nowrap">&nbsp;<input type="button" class="button" value="删除" onclick="if(confirm('删除后不可恢复，是否确定删除选中的教师？'))FormUtils.doAction('deleteTeachers')"></td>
			</tr>
		</table>
		<table class="table" width="100%" border="0" cellpadding="3" cellspacing="1">
			<tr >
				<td align="center" nowrap="nowrap" class="tdtitle" width="32px">选择</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100px">教师姓名</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100%">称谓</td>
				<ext:iterate id="classTeacher" name="adminSchoolClass" property="teachers">
					<tr bordercolor="E1E8F5" height="20px">
						<td class="tdcontent">
							<input name="classTeacherId" type="checkbox" class="checkbox" value="<ext:write name="classTeacher" property="id"/>">
						</td>
						<td class="tdcontent">
							<a href="javascript:editPerson(<ext:write name="classTeacher" property="teacher.id"/>, '<ext:write name="classTeacher" property="teacher.type"/>', 'width=720,height=480')" class="tdcontent"><ext:write name="classTeacher" property="teacher.name"/></a>
						</td>
						<td class="tdcontent"><ext:write name="classTeacher" property="title"/></td>
					</tr>
				</ext:iterate>
			</tr>
		</table>
	</ext:tabBody>
</ext:tab>