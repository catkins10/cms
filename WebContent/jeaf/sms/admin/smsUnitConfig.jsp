<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveSmsUnitConfig">
	<script>
		function resetUnitName(unitName) {
			var values = unitName.split("/");
			var postfix = ["县", "市", "州", "省", "自治区"];
			unitName = "";
			for(var i=values.length-1; i>=0; i--) {
				if(unitName.indexOf(values[i])!=-1) {
					break;
				}
				unitName = values[i] + unitName;
				var j=0;
				for(; j<postfix.length && values[i].lastIndexOf(postfix[j])!=values[i].length-postfix[j].length; j++);
				if(j<postfix.length) {
					break;
				}
			}
			document.getElementsByName('unitName')[0].value = unitName;
		}
	</script>
	<ext:tab>
		<ext:tabBody tabId="basic">
		   	<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
				<col>
				<col width="100%">
				<ext:empty property="businessConfigs">
					<tr>
						<td class="tdtitle" nowrap="nowrap">单位名称</td>
						<td class="tdcontent"><ext:field property="unitName" onchange="resetUnitName(value)"/></td>
					</tr>
					<tr>
						<td class="tdtitle" nowrap="nowrap">短信客户端</td>
						<td class="tdcontent"><ext:field property="smsClientName"/></td>
					</tr>
				</ext:empty>
				<ext:notEmpty property="businessConfigs">
					<tr>
						<td class="tdtitle" nowrap="nowrap">单位名称</td>
						<td class="tdcontent"><ext:field writeonly="true" property="unitName"/></td>
					</tr>
					<tr>
						<td class="tdtitle" nowrap="nowrap">短信客户端</td>
						<td class="tdcontent"><ext:field writeonly="true" property="smsClientName"/></td>
					</tr>
				</ext:notEmpty>
				<tr>
					<td class="tdtitle" nowrap="nowrap">是否启用</td>
					<td class="tdcontent"><ext:field property="enabled"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">最后修改时间</td>
					<td class="tdcontent"><ext:field property="lastModified"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">最后修改人</td>
					<td class="tdcontent"><ext:field property="lastModifier"/></td>
				</tr>
			</table>
		</ext:tabBody>
		<ext:tabBody tabId="businessConfigs">
			<script>
				function newBusinessConfig() {
					DialogUtils.openSelectDialog('jeaf/sms', 'admin/selectSmsBusiness', 500, 320, false, '', 'DialogUtils.openDialog("<%=request.getContextPath()%>/jeaf/sms/admin/smsUnitBusiness.shtml?id=<ext:write property="id"/>&unitBusiness.businessId={id}&unitId=' + document.getElementsByName('unitId')[0].value + '", 600, 400);');
				}
				function openBusinessConfig(businessConfigId) {
					DialogUtils.openDialog('<%=request.getContextPath()%>/jeaf/sms/admin/smsUnitBusiness.shtml?id=<ext:write property="id"/>&unitBusiness.id=' + businessConfigId, 600, 400);
				}
			</script>
			<div style="padding-bottom:5px">
				<input type="button" class="button" value="添加业务" onclick="newBusinessConfig()">
			</div>
			<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr align="center">
					<td class="tdtitle" nowrap="nowrap" width="36">序号</td>
					<td class="tdtitle" nowrap="nowrap" width="100%">业务名称</td>
					<td class="tdtitle" nowrap="nowrap" width="80px">短信号码</td>
					<td class="tdtitle" nowrap="nowrap" width="80px">价格</td>
					<td class="tdtitle" nowrap="nowrap" width="80px">是否启用</td>
				</tr>
				<ext:iterate id="businessConfig" indexId="businessConfigIndex" property="businessConfigs">
					<tr style="cursor:pointer" valign="top" onclick="openBusinessConfig('<ext:write name="businessConfig" property="id"/>')">
						<td class="tdcontent" align="center"><ext:writeNumber name="businessConfigIndex" plus="1"/></td>
						<td class="tdcontent"><ext:field writeonly="true" name="businessConfig" property="businessName"/></td>
						<td class="tdcontent" align="center"><ext:field writeonly="true" name="businessConfig" property="smsNumber"/></td>
						<td class="tdcontent" align="center"><ext:field writeonly="true" name="businessConfig" property="price"/></td>
						<td class="tdcontent" align="center"><ext:field writeonly="true" name="businessConfig" property="enabled"/></td>
					</tr>
				</ext:iterate>
			</table>
		</ext:tabBody>
	</ext:tab>
</ext:form>