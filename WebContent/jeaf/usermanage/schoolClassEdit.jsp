<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function initClassName() {
	var grade = document.getElementsByName("grade")[0];
	if(!grade) {
		return;
	}
	grade = grade.value;
	var classNumber = Number(document.getElementsByName("classNumber")[0].value);
	switch(grade.substring(0,2)) {
	case "小学":
		document.getElementsByName("name")[0].value = grade.substring(2, 4) + "（" + classNumber + "）班";
		break;
	
	case "初中":
	case "高中":
		document.getElementsByName("name")[0].value = grade.substring(0, 1) + grade.substring(2, 3) + "（" + classNumber + "）班";
	}
}
</script>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col valign="middle">
			<col valign="middle" width="100%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">入学年份</td>
				<td colspan="3" class="tdcontent"><html:text property="enrollTime" styleClass="field required"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">学制</td>
				<td colspan="3" class="tdcontent"><html:text property="lengthOfSchooling" styleClass="field required"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">班级编号</td>
				<td colspan="3" class="tdcontent"><html:text property="classNumber" styleClass="field required" onchange="initClassName()"/></td>
			</tr>
			<ext:notEmpty property="grades">
				<tr>
					<td class="tdtitle" nowrap="nowrap">年级</td>
					<td colspan="3" class="tdcontent">
						<ext:field property="grade" itemsProperty="grades" styleClass="field required" onchange="initClassName()"/>
					</td>
				</tr>
			</ext:notEmpty>
			<tr>
				<td class="tdtitle" nowrap="nowrap">班级名称</td>
				<td colspan="3" class="tdcontent"><html:text property="name" styleClass="field required"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">所在学校</td>
				<td colspan="3" class="tdcontent"><ext:write property="parentOrgFullName"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">是否毕业</td>
				<td colspan="3" class="tdcontent">
					<html:radio property="halt" value="1" styleClass="radio" styleId="haltEnable"/><label for="haltEnable">&nbsp;是</label>
					<html:radio property="halt" value="0" styleClass="radio" styleId="haltDisable"/><label for="haltDisable">&nbsp;否</label>
				</td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">注册人</td>
				<td colspan="3" class="tdcontent"><ext:write property="creator"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">注册时间</td>
				<td colspan="3" class="tdcontent"><ext:write property="created" format="yyyy-MM-dd HH:mm"/></td>
			</tr>
		</table>
	</ext:tabBody>

	<ext:tabBody tabId="teacher">
		<table border="0" cellpadding="2" cellspacing="0">
			<tr>
				<td nowrap="nowrap">选择教师:</td>
				<td nowrap="nowrap" width="100px">
					<html:hidden property="teacherId"/>
					<ext:field property="teacherName" selectOnly="true" onSelect="DialogUtils.selectPerson(520, 320, false, 'teacherId{id},teacherName{name}', '', 'teacher', '', ',')"/>
				</td>
				<td nowrap="nowrap">称谓:</td>
				<td nowrap="nowrap" width="160px">
					<ext:field property="teacherTitle" itemsProperty="teacherTitles"/>
				</td>
				<td nowrap="nowrap"><input type="button" class="button" value="添加" onclick="if(document.getElementsByName('teacherName')[0].value!='' && document.getElementsByName('teacherTitle')[0].value!='')FormUtils.doAction('addTeacher')"></td>
				<td nowrap="nowrap">&nbsp;<input type="button" class="button" value="删除" onclick="if(confirm('删除后不可恢复，是否确定删除选中的教师？'))FormUtils.doAction('deleteTeachers')"></td>
			</tr>
		</table>
		<table class="table" width="100%" border="0" cellpadding="3" cellspacing="1">
			<tr>
				<td align="center" nowrap="nowrap" class="tdtitle" width="32px">选择</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100px">教师姓名</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100%">称谓</td>
			</tr>
			<ext:iterate id="classTeacher" name="schoolClass" property="teachers">
				<tr bordercolor="E1E8F5" height="20px">
					<td class="tdcontent">
						<input name="classTeacherId" type="checkbox" class="checkbox" value="<ext:write name="classTeacher" property="id"/>">
					</td>
					<td class="tdcontent">
						<a href="javascript:PageUtils.editrecord('jeaf/usermanage', 'teacher', <ext:write name="classTeacher" property="teacher.id"/>, 'width=720,height=480')"><ext:write name="classTeacher" property="teacher.name"/></a>
					</td>
					<td class="tdcontent"><ext:write name="classTeacher" property="title"/></td>
				</tr>
			</ext:iterate>
		</table>
	</ext:tabBody>

	<ext:tabBody tabId="student">
		<table class="table" width="100%" border="0" cellpadding="3" cellspacing="1">
			<col valign="middle" align="center">
			<col valign="middle" class="tdcontent">
			<col valign="middle" class="tdcontent">
			<tr height="22px">
				<td align="center" nowrap="nowrap" class="tdtitle" width="50px">座号</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100px">姓名</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100%">电子邮件</td>
			</tr>
			<ext:iterate id="student" property="students">
				<tr>
					<td class="tdcontent"><ext:write name="student" property="seatNumber"/></td>
					<td class="tdcontent">
						<a href="javascript:PageUtils.editrecord('jeaf/usermanage','student','<ext:write name="student" property="id"/>','width=720,height=480')"><ext:write name="student" property="name"/></a>
					</td>
					<td class="tdcontent"><ext:write name="student" property="mobile"/></td>
					<td class="tdcontent"><ext:write name="student" property="mailAddress"/></td>
				</tr>
			</ext:iterate>
	</table>
	</ext:tabBody>
</ext:tab>
<html:hidden property="parentOrgId"/>