<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveApplication">
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
				<col align="right">
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
					<td nowrap="nowrap" class="tdtitle">生成时间</td>
					<td class="tdcontent"><ext:field property="buildTime"/></td>
				</tr>
				<tr>
					<td nowrap="nowrap" class="tdtitle">创建时间</td>
					<td class="tdcontent"><ext:field property="created"/></td>
				</tr>
				<tr>
					<td nowrap="nowrap" class="tdtitle">创建人</td>
					<td class="tdcontent"><ext:field property="creator"/></td>
				</tr>
			</table>
		</ext:tabBody>
		
		<ext:tabBody tabId="form">
			<script>
			function newForm() {
				PageUtils.newrecord('jeaf/application/builder', 'applicationForm', 'mode=fullscreen', 'applicationId=<ext:write property="id"/>');
			}
			function openForm(formId) {
				PageUtils.editrecord('jeaf/application/builder', 'applicationForm', formId, 'mode=fullscreen');
			}
			</script>
			<div style="padding-bottom:5px">
				<input type="button" class="button" value="添加表单" onclick="newForm()">
			</div>
			<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr align="center">
					<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
					<td class="tdtitle" nowrap="nowrap" width="120px">名称</td>
					<td class="tdtitle" nowrap="nowrap" width="100%">英文名称</td>
				</tr>
				<ext:iterate id="form" indexId="formIndex" property="forms">
					<tr style="cursor:pointer" onclick="openForm('<ext:write name="form" property="id"/>')">
						<td class="tdcontent" align="center"><ext:writeNumber name="formIndex" plus="1"/></td>
						<td class="tdcontent"><ext:field writeonly="true" name="form" property="name"/></td>
						<td class="tdcontent"><ext:field writeonly="true" name="form" property="englishName"/></td>
					</tr>
				</ext:iterate>
			</table>
		</ext:tabBody>
		
		<ext:tabBody tabId="navigator">
			<script>
			function newNavigator() {
				DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/application/builder/applicationNavigator.shtml?id=<ext:write property="id"/>', 390, 200);
			}
			function openNavigator(navigatorId) {
				DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/application/builder/applicationNavigator.shtml?id=<ext:write property="id"/>&navigator.id=' + navigatorId, 390, 200);
			}
			</script>
			<div style="padding-bottom:5px">
				<input type="button" class="button" value="添加导航条目" onclick="newNavigator()">
				<input type="button" class="button" value="批量创建导航条目" onclick="FormUtils.doAction('createApplicationNavigators')">
				<input type="button" class="button" value="调整优先级" onclick="DialogUtils.adjustPriority('jeaf/application/builder', 'adjustNavigatorPriority', '导航', 600, 380, 'applicationId=<ext:write property="id"/>')">
			</div>
			<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr align="center">
					<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
					<td class="tdtitle" nowrap="nowrap" width="100%">名称</td>
				</tr>
				<ext:iterate id="navigator" indexId="navigatorIndex" property="navigators">
					<tr style="cursor:pointer" onclick="openNavigator('<ext:write name="navigator" property="id"/>')">
						<td class="tdcontent" align="center"><ext:writeNumber name="navigatorIndex" plus="1"/></td>
						<td class="tdcontent"><ext:field writeonly="true" name="navigator" property="label"/></td>
					</tr>
				</ext:iterate>
			</table>
		</ext:tabBody>
	</ext:tab>
</ext:form>