<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function onItemTypeChanged() {
		var value = document.getElementsByName('itemTypeShortName')[0].value;
		if(value!="") {
			var rule = document.getElementsByName('rule')[0];
			rule.value = rule.value.replace(/\[类型简称\]/, value);
		}
	}
</script>
<ext:form action="/admin/saveServiceItemCodeRule">
   	<table border="0" width="100%" cellspacing="0" cellpadding="5px">
		<col>
		<col width="100%">
		<tr>
			<td nowrap="nowrap" align="right">事项类型：</td>
			<td>
				<input type="hidden" name="itemTypeShortName"/>
				<ext:field property="itemType" onchange="onItemTypeChanged()"/>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right" style="padding-top: 27px;">编号规则：</td>
			<td>
				<div style="padding-bottom:3px">
					<input type="button" class="button" value="插入目录编号" style="width:88px" onclick="FormUtils.pasteText('rule', '&lt;目录编号&gt;')">
					<input type="button" class="button" value="插入序号" style="width:72px" onclick="FormUtils.pasteText('rule', '&lt;序号&gt;')">
				</div>
				<ext:field property="rule"/>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">手工编号：</td>
			<td><ext:field property="manualCodeEnabled"/></td>
		</tr>
	</table>
</ext:form>