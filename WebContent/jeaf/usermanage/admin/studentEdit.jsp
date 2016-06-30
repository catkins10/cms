<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>


<script>
function onClickRegistNewGenearch(checked) { //点击注册家长
	var field = document.getElementById("divSelectGenearch");
	field.style.display = checked ? "none" : "";
	for(field=field.nextSibling; field; field=field.nextSibling) {
		if(field.id=="divRegistGenearch") {
			field.style.display = !checked ? "none" : "";
		}
	}
}
function addGenearch() { //添加家长
	if(document.getElementsByName('genearchTitle')[0].value=='') {
		return;
	}
	if(document.getElementsByName('registNewGenearch')[0].checked) { //注册新用户
		if(document.getElementsByName('genearch.name')[0].value=='' ||
		   document.getElementsByName('genearch.loginName')[0].value=='' ||
		   document.getElementsByName('genearch.password')[0].value=='') {
			return;
		}
	}
	else if(document.getElementsByName('selectedGenearchName')[0].value=='') {
		return;
	}
	FormUtils.doAction('addGenearch');
}
</script>

<ext:tab>
	<ext:tabBody tabId="basic">
		<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
			<col valign="middle">
			<col valign="middle" width="50%">
			<col valign="middle">
			<col valign="middle" width="50%">
			<tr>
				<td class="tdtitle" nowrap="nowrap">姓名</td>
				<td class="tdcontent"><ext:field property="name"/></td>
				<td class="tdtitle" nowrap="nowrap">登录用户名(英文)</td>
				<td class="tdcontent">
					<ext:equal value="1" property="preassign">
						<ext:field writeonly="true" property="loginName"/>
					</ext:equal>
					<ext:notEqual value="1" property="preassign">
						<ext:field property="loginName"/>
					</ext:notEqual>
				</td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">性别</td>
				<td class="tdcontent"><ext:field property="sex"/></td>
				<td class="tdtitle" nowrap="nowrap">口令</td>
				<td class="tdcontent"><ext:field property="password"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">所在班级</td>
				<td class="tdcontent" colspan="3"><ext:field property="orgFullName"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">座号</td>
				<td class="tdcontent"><ext:field property="seatNumber"/></td>
				<td class="tdtitle" nowrap="nowrap">电子邮箱</td>
				<td class="tdcontent"><ext:field property="mailAddress"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">家庭地址</td>
				<td class="tdcontent"><ext:field property="familyAddress"/></td>
				<td class="tdtitle" nowrap="nowrap">电话</td>
				<td class="tdcontent"><ext:field property="telFamily"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">手机</td>
				<td class="tdcontent"><ext:field property="mobile"/></td>
				<td class="tdtitle" nowrap="nowrap">是否停用</td>
				<td class="tdcontent"><ext:field property="halt"/></td>
			</tr>
			<tr>
				<td>注册人</td>
				<td><ext:field property="creator"/></td>
				<td>注册时间</td>
				<td><ext:field property="created"/></td>
			</tr>
			<tr>
				<td class="tdtitle" nowrap="nowrap">备注</td>
				<td class="tdcontent" colspan="3"><ext:field property="remark"/></td>
			</tr>
		</table>
	</ext:tabBody>

	<ext:tabBody tabId="genearch">
		<div style="float:left; height:23px; padding-top:4px">
			<html:checkbox onclick="onClickRegistNewGenearch(checked)" value="1" property="registNewGenearch" styleClass="checkbox" styleId="registNewGenearchBox"/>&nbsp;<label for="registNewGenearchBox">注册新的家长帐号</label>
		</div>
		<div style="float:left; height:23px; padding-top:4px; padding-left:5px">称谓</div>
		<div style="float:left; width:60px; height:23px; padding-left:2px">
			<ext:field property="genearchTitle" itemsText="父亲\0母亲\0祖父\0祖母\0外祖父\0外祖母" />
		</div>
		<div style="float:left; height:23px; padding-top:4px; padding-left:5px">姓名</div>
		<div id="divSelectGenearch" style="float:left; width:60px; height:23px; padding-left:2px; display:none">
			<html:hidden property="selectedGenearchId"/>
			<ext:field property="selectedGenearchName" selectOnly="true" onSelect="DialogUtils.selectPerson(520, 320, false, 'selectedGenearchId{id},selectedGenearchName{name}', '', 'teacher,employee,genearch', '', ',')"/>
		</div>
		<div id="divRegistGenearch" style="float:left; width:50px; height:23px; padding-left:2px">
			<html:text property="genearch.name"/>
		</div>
		<div id="divRegistGenearch" style="float:left; height:23px; padding-top:4px; padding-left:5px">登录用户名</div>
		<div id="divRegistGenearch" style="float:left; width:60px; height:23px; padding-left:2px">
			<html:text property="genearch.loginName"/>
		</div>
		<div id="divRegistGenearch" style="float:left; height:23px; padding-top:4px; padding-left:5px">口令</div>
		<div id="divRegistGenearch" style="float:left; width:60px; height:23px; padding-left:2px">
			<html:text property="genearch.password"/>
		</div>
		<div id="divRegistGenearch" style="float:left; width:75px; height:23px; padding-left:5px; padding-top:5px">
			<html:radio property="genearch.sex" value="M" styleClass="radio" styleId="male"/><label for="male">&nbsp;男</label>
			<html:radio property="genearch.sex" value="F" styleClass="radio" styleId="female"/><label for="female">&nbsp;女</label>
		</div>
		<div id="divRegistGenearch" style="float:left; height:23px; padding-top:4px; padding-left:5px">手机</div>
		<div id="divRegistGenearch" style="float:left; width:72px; height:23px; padding-left:2px">
			<html:text property="genearch.mobile"/>
		</div>
		<div style="float:left; height:23px; padding-left:5px">
			<input type="button" class="button" value="添加" onclick="addGenearch()">
		</div>
		<div style="float:left; height:23px; padding-left:5px">
			<input type="button" class="button" value="删除" onclick="if(confirm('删除后不可恢复，是否确定删除选中的家长？'))FormUtils.doAction('deleteGenearches')">
		</div>
		<table class="table" width="100%" border="0" cellpadding="3" cellspacing="1">
			<tr height="22px">
				<td align="center" nowrap="nowrap" class="tdtitle" width="32px">选择</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100px">姓名</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100px">称呼</td>
				<td align="center" nowrap="nowrap" class="tdtitle" width="100%">手机</td>
				<ext:iterate id="genearch" name="adminStudent" property="genearches">
					<tr bordercolor="E1E8F5" height="20px">
						<td class="tdcontent" align="center">
							<input name="studentGenearchId" type="checkbox" class="checkbox" value="<ext:write name="genearch" property="id"/>">
						</td>
						<td class="tdcontent">
							<a href="javascript:editPerson(<ext:write name="genearch" property="genearch.id"/>, '<ext:write name="genearch" property="genearch.type"/>', 'width=720,height=480')"><ext:write name="genearch" property="genearch.name"/></a>
						</td>
						<td class="tdcontent"><ext:write name="genearch" property="relation"/></td>
						<td class="tdcontent"><ext:write name="genearch" property="genearch.mobile"/></td>
					</tr>
				</ext:iterate>
			</tr>
		</table>
	</ext:tabBody>
</ext:tab>