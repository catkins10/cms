<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
//更新年级列表
function updateGrades(schoolCategory) {
	var table = document.getElementById("tabregistClass");
	for(var i=table.rows.length-1 ; i>0; i--) {
		table.deleteRow(i);
	}
	var gradeNames;
	var lengthOfSchooling = 3;
	if(schoolCategory=="小学") {
		gradeNames = "一年级,二年级,三年级,四年级,五年级,六年级";
		lengthOfSchooling = 6;
	}
	else if(schoolCategory=="初中") {
		gradeNames = "初一,初二,初三";
	}
	else if(schoolCategory=="高中") {
		gradeNames = "高一,高二,高三";
	}
	else if(schoolCategory=="完全中学") {
		gradeNames = "初一,初二,初三,高一,高二,高三";
	}
	else {
		return;
	}
	var names = gradeNames.split(",");
	var now = new Date();
	var enrollYear = now.getYear() - (now.getMonth()>6 ? 0 : 1); //8月之前注册班级,入学年度-1
	for(var i=0; i<names.length; i++) {
		//添加年级
		var tr = table.insertRow(-1);
		tr.insertCell(-1).innerHTML = names[i] + "<input type=\"hidden\" name=\"gradeName\" value=\"" + names[i] + "\"/>";
		tr.insertCell(-1).innerHTML = "<input type=\"text\" name=\"gradeClassCount\"/>";
		tr.insertCell(-1).innerHTML = "<input type=\"text\" onchange=\"updateEnrollYear(value)\" name=\"gradeEnrollYear\" " + (i==0 ? "" : "readonly=\"true\" style=\"border-left-style:none;border-top-style:none;border-right-style:none;border-bottom-style:none\" ") + "value=\"" + enrollYear + "\"/>";
		tr.insertCell(-1).innerHTML = "<input type=\"text\"" + (i==0 ? " onchange=\"updateLengthOfSchooling(value)\"" : "") + " name=\"lengthOfSchooling\" value=\"" + lengthOfSchooling + "\"/>";
		enrollYear--;
		if(schoolCategory=="完全中学" && i==2) {
			enrollYear += 3;
		}
	}
}

function updateEnrollYear(value) { //更新入学年度
	var	schoolCategory = document.getElementsByName("category")[0].value;
	var enrollYear = Number(value);
	var fields = document.getElementsByName("gradeEnrollYear");
	for(var i=0; i<fields.length; i++) {
		fields[i].value = enrollYear;
		enrollYear--;
		if(schoolCategory=="完全中学" && i==2) {
			enrollYear += 3;
		}
	}
}
function updateLengthOfSchooling(value) { //更新学制
	value = Number(value);
	var lengthOfSchoolings = document.getElementsByName("lengthOfSchooling");
	for(var i=0; i<lengthOfSchoolings.length; i++) {
		lengthOfSchoolings[i].value = value;
	}
}
function appendDepartment(name) {
	name = StringUtils.trim(name);
	if(name=="") {
		return;
	}
	var table = document.getElementById("tableDepartments");
	//检查是否重复
	for(var i=table.rows.length-1 ; i>0; i--) {
		var checkbox = DomUtils.getElement(table.rows[i].cells[0], "input", "department");
		if(checkbox.value==name) {
			checkbox.focus();
			checkbox.checked = true;
			return;
		}
	}
	var cell = table.insertRow(-1).insertCell(-1);
	cell.innerHTML = '<input type="checkbox" checked name="department" value="' + name + '" class="checkbox" id="departmentBox' + table.rows.length + '"/>&nbsp;<label for="departmentBox' + table.rows.length + '">' + name + '</label>';
	cell.focus();
}
//全选/全清部门列表
function selectAllDepartments(selected) {
	var departments = document.getElementsByName("department");
	for(var i=0; i<departments.length; i++) {
		departments[i].checked = selected;
	}
}
</script>

<ext:tab>
	<ext:tabItem id="basic" name="基本信息" selected="true"/>
	<ext:tabItem id="manager" name="管理员"/>
	<ext:tabItem id="registDepartment" name="注册教研组/部门"/>
	<ext:tabItem id="registClass" name="注册班级"/>
</ext:tab>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="100%">
	<tr>
		<td>名称</td>
		<td><html:text property="directoryName" styleClass="field required"/></td>
	</tr>
	<tr>
		<td>全称</td>
		<td><html:text property="fullName"/></td>
	</tr>
	<tr>
		<td>所在区域</td>
		<td><ext:write property="parentDirectoryName"/></td>
	</tr>
	<tr>
		<td>学校类别</td>
		<td><ext:field onchange="updateGrades(value);" selectOnly="true" property="category" styleClass="field required" itemsText="小学\0初中\0高中\0完全中学"/></td>
	</tr>
	<jsp:include page="/jeaf/directorymanage/popedomConfigEdit.jsp"/>
	<tr>
		<td>注册人</td>
		<td><ext:write property="creator"/></td>
	</tr>
	<tr>
		<td>注册时间</td>
		<td><ext:write property="created" format="yyyy-MM-dd HH:mm"/></td>
	</tr>
	<tr>
		<td>备注</td>
		<td><html:text property="remark"/></td>
	</tr>
</table>

<table id="tabmanager" width="100%" style="table-layout:fixed;display:none" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle" width="90px">
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent"><html:text property="managerName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent">
			<html:radio property="managerSex" value="M" styleClass="radio" styleId="male"/><label for="male">&nbsp;男</label>
			<html:radio property="managerSex" value="F" styleClass="radio" styleId="female"/><label for="female">&nbsp;女</label>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所在部门</td>
		<td class="tdcontent">
			<ext:field property="managerDepartment" itemsProperty="departmentNames"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">电话</td>
		<td class="tdcontent"><html:text property="managerTel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">手机</td>
		<td class="tdcontent"><html:text property="managerMobile"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
		<td class="tdcontent"><html:text property="managerMail"/></td>
	</tr>
</table>

<table id="tabregistdepartment" border="0" height="320px" width="100%" cellspacing="0" cellpadding="0px" style="table-layout:fixed; display:none">
    <tr valign="bottom">
    	<td>
    		教研组/部门：<input type="text" id="newDepartmentName" style="width:180px" onkeydown="if(window.event.keyCode==13){appendDepartment(value);value='';focus();}">
    		<input type="button" value="添加" class="button" style="width:38px" onclick="appendDepartment(document.getElementById('newDepartmentName').value);document.getElementById('newDepartmentName').value='';">
    		<input type="button" value="全部选中" class="button" style="width:68px" onclick="selectAllDepartments(true)">
    		<input type="button" value="全部删除" class="button" style="width:68px" onclick="selectAllDepartments(false)">
    	</td>
	</tr>
    <tr height="100%" valign="top">
       	<td style="padding:5px">
			<div style="float:left;padding:2px;background:white;border-width:1;border-style:solid;border-color:gray;width:100%;height:100%;overflow:auto">
				<table id="tableDepartments" border="0" cellpadding="1">
					<ext:iterate id="departmentName" indexId="departmentIndex" property="departmentNames">
						<tr><td><input type="checkbox" name="department" value="<ext:write name="departmentName"/>" class="checkbox" id="departmentBox<ext:write name="departmentIndex"/>"/>&nbsp;<label for="departmentBox<ext:write name="departmentNameIndex"/>"><ext:write name="departmentName"/></label></td></tr>
					</ext:iterate>
				</table>
	  		</div>
       	</td>
	</tr>
</table>

<table id="tabregistClass" class="table" style="display:none" width="100%" border="0" cellpadding="3" cellspacing="1">
	<col valign="middle" class="tdcontent">
	<col valign="middle" class="tdcontent">
	<col valign="middle" class="tdcontent">
	<col valign="middle" class="tdcontent">
	<tr height="22px">
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">年段</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="100%">班级数量</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">入学年度</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">学制</td>
	</tr>	
</table>

<html:hidden property="parentDirectoryId"/>