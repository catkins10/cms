<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveApplicationForm">
	<script>
		function formOnSubmit() {
			if(FieldValidator.validateStringField(document.getElementsByName("name")[0], "&,?,=,\",'", true, "名称")=="NaN") {
				return false;
			}
			if(document.getElementsByName("englishName")[0]) {
				var englishName = document.getElementsByName("englishName")[0].value;
				if(englishName=="") {
					alert("英文名称不能为空");
					return false;
				}
				if(englishName.replace(/[\dA-Za-z]/gi, "")!="") {
					alert("英文名称必须由数字和字母组成");
					return false;
				}
				if(englishName.charAt(0)>='0' && englishName.charAt(0)<='9') {
					alert("英文名称不能以数字开头");
					return false;
				}
			}
			return true;
		}
	</script>
	<ext:tab>
		<ext:tabBody tabId="basic">
			<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
				<col>
				<col width="100%">
				<tr>
					<td nowrap="nowrap" class="tdtitle">名称</td>
					<td class="tdcontent"><ext:field property="name"/></td>
				</tr>
				<tr>
					<td nowrap="nowrap" class="tdtitle">英文名称</td>
					<td class="tdcontent">
						<ext:empty property="buildTime">
							<ext:field property="englishName"/>
						</ext:empty>
						<ext:notEmpty property="buildTime">
							<ext:field writeonly="true" property="englishName"/>
						</ext:notEmpty>
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap" class="tdtitle">新建操作名称</td>
					<td class="tdcontent"><ext:field property="newActionName"/></td>
				</tr>
				<tr>
					<td nowrap="nowrap" class="tdtitle">表单模板</td>
					<td class="tdcontent"><ext:field property="templateName"/></td>
				</tr>
				<tr>
					<td nowrap="nowrap" class="tdtitle">编辑权限</td>
					<td class="tdcontent"><ext:field property="editPrivilege"/></td>
				</tr>
				<tr>
					<td nowrap="nowrap" class="tdtitle">删除权限</td>
					<td class="tdcontent"><ext:field property="deletePrivilege"/></td>
				</tr>
				<tr>
					<td nowrap="nowrap" class="tdtitle">访问权限</td>
					<td class="tdcontent"><ext:field property="visitPrivilege"/></td>
				</tr>
			</table>
		</ext:tabBody>
		
		<ext:tabBody tabId="field">
			<script>
			function newField() {
				DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/application/builder/field.shtml?id=<ext:write property="id"/>', 550, 300);
			}
			function openField(fieldId, isPersistence) {
				DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/application/builder/field.shtml?id=<ext:write property="id"/>&field.id=' + fieldId, (isPersistence==0 ? 360 : 550), 300);
			}
			</script>
			<div style="padding-bottom:5px">
				<input type="button" class="button" value="添加字段" onclick="newField()">
				<input type="button" class="button" value="调整优先级" onclick="DialogUtils.adjustPriority('jeaf/application/builder', 'adjustFormFieldPriority', '字段', 600, 380, 'formId=<ext:write property="id"/>')">
			</div>
			<table id="tableFields" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr align="center">
					<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
					<td class="tdtitle" nowrap="nowrap" width="50%">名称</td>
					<td class="tdtitle" nowrap="nowrap" width="50%">英文名称</td>
					<td class="tdtitle" nowrap="nowrap" width="80px">数据类型</td>
					<td class="tdtitle" nowrap="nowrap" width="80px">字段长度</td>
					<td class="tdtitle" nowrap="nowrap"l width="120px">输入方式</td>
					<td class="tdtitle" nowrap="nowrap"l width="80px">必填</td>
					<td class="tdtitle" nowrap="nowrap"l width="80px">系统预设</td>
				</tr>
				<ext:iterate id="field" indexId="fieldIndex" property="fields">
					<tr style="cursor:pointer" onclick="openField('<ext:write name="field" property="id"/>', <ext:write name="field" property="isPersistence"/>)" id="<ext:write name="field" property="id"/>_<ext:write name="field" property="inputMode"/>_<ext:write name="field" property="isPersistence"/>">
						<td class="tdcontent" align="center"><ext:writeNumber name="fieldIndex" plus="1"/></td>
						<td class="tdcontent"><ext:field writeonly="true" name="field" property="name"/></td>
						<td class="tdcontent"><ext:field writeonly="true" name="field" property="englishName"/></td>
						<td class="tdcontent" align="center"><ext:field writeonly="true" name="field" property="fieldType"/></td>
						<td class="tdcontent" align="center"><ext:field writeonly="true" name="field" property="fieldLength"/></td>
						<td class="tdcontent" align="center"><ext:field writeonly="true" name="field" property="inputMode"/></td>
						<td class="tdcontent" align="center"><ext:equal name="field" property="required" value="1">√</ext:equal></td>
						<td class="tdcontent" align="center"><ext:equal name="field" property="isPresetting" value="1">√</ext:equal></td>
					</tr>
				</ext:iterate>
			</table>
		</ext:tabBody>
		
		<ext:tabBody tabId="index">
			<script>
			function newIndex() {
				DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/application/builder/index.shtml?id=<ext:write property="id"/>', 390, 300);
			}
			function openIndex(indexId) {
				DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/application/builder/index.shtml?id=<ext:write property="id"/>&index.id=' + indexId, 390, 300);
			}
			</script>
			<div style="padding-bottom:5px">
				<input type="button" class="button" value="添加索引" onclick="newIndex()">
			</div>
			<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr align="center">
					<td class="tdtitle" nowrap="nowrap" width="50px">序号</td>
					<td class="tdtitle" nowrap="nowrap" width="100%">字段列表</td>
				</tr>
				<ext:iterate id="index" indexId="indexIndex" property="indexes">
					<tr style="cursor:pointer" onclick="openIndex('<ext:write name="index" property="id"/>')">
						<td class="tdcontent" align="center"><ext:writeNumber name="indexIndex" plus="1"/></td>
						<td class="tdcontent"><ext:field writeonly="true" name="index" property="fieldNames"/></td>
					</tr>
				</ext:iterate>
			</table>
		</ext:tabBody>
		
		<ext:tabBody tabId="view">
			<script>
			function newView() {
				DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/application/builder/view.shtml?id=<ext:write property="id"/>', 520, 300);
			}
			function openView(viewId) {
				DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/application/builder/view.shtml?id=<ext:write property="id"/>&view.id=' + viewId, 520, 300);
			}
			function createViews() {
				DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/application/builder/createViews.shtml?id=<ext:write property="id"/>', 430, 300);
			}
			</script>
			<div style="padding-bottom:5px">
				<input type="button" class="button" value="创建视图" onclick="newView()"/>
				<input type="button" class="button" value="批量创建视图" onclick="createViews()"/>
			</div>
			<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr align="center">
					<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
					<td class="tdtitle" nowrap="nowrap" width="150px">名称</td>
					<td class="tdtitle" nowrap="nowrap" width="200px">英文名称</td>
					<td class="tdtitle" nowrap="nowrap" width="50%">字段列表</td>
					<td class="tdtitle" nowrap="nowrap" width="50%">排序字段列表</td>
				</tr>
				<ext:iterate id="view" indexId="viewIndex" property="views">
					<tr style="cursor:pointer" onclick="openView('<ext:write name="view" property="id"/>')">
						<td class="tdcontent" align="center"><ext:writeNumber name="viewIndex" plus="1"/></td>
						<td class="tdcontent"><ext:field writeonly="true" name="view" property="name"/></td>
						<td class="tdcontent"><ext:field writeonly="true" name="view" property="englishName"/></td>
						<td class="tdcontent"><ext:field writeonly="true" name="view" property="viewFieldNames"/></td>
						<td class="tdcontent"><ext:field writeonly="true" name="view" property="sortFieldNames"/></td>
					</tr>
				</ext:iterate>
			</table>
		</ext:tabBody>
		
		<ext:tabBody tabId="formCustom">
			<div>
				<ext:field property="custom"/>
			</div>
		</ext:tabBody>
	</ext:tab>
</ext:form>