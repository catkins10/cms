<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">短信内容</td>
		<td class="tdcontent"><ext:field property="contentName" onchange="onContentChanged()"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">描述</td>
		<td class="tdcontent"><ext:field property="description"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">订阅规则:前缀</td>
		<td class="tdcontent"><ext:field property="subscribePrefixRule"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">订阅规则:正文</td>
		<td class="tdcontent">
			<div id="divFields" style="display:none; margin-bottom: 3px;"></div>
			<ext:field property="subscribeBodyRule"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">退订规则</td>
		<td class="tdcontent"><ext:field property="unsubscribeRule"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">是否生效</td>
		<td class="tdcontent"><ext:field property="isValid"/></td>
	</tr>
</table>

<script>
	function onContentChanged() {
		var contentDefinition = DropdownField.getSelectedListValue('contentName');
		eval('contentDefinition=' + contentDefinition.substring(contentDefinition.indexOf('|') + 1));
		document.getElementsByName('contentServiceName')[0].value = contentDefinition.contentServiceName;
		var description = FormUtils.getField(document, 'description');
		if(description.value=="") {
	   		description.value = contentDefinition.description;
	   	}
	   	//更新字段列表
	 	var divFields = document.getElementById("divFields");
 		divFields.innerHTML = "";
 		divFields.style.display = "none";
	 	if(contentDefinition.contentFields=="") {
	 		return;
	 	}
	 	var contentFields = contentDefinition.contentFields.split(",");
	 	for(var j=0; j<contentFields.length; j++) {
	 		var button = document.createElement('button');
	 		button.className = "button";
	 		button.onclick = function() {
	 			FormUtils.pasteText('subscribeBodyRule', '<' + this.innerHTML.substring(2) + '>');
	 			return false;
	 		};
	 		button.innerHTML = '插入' + contentFields[j];
	 		divFields.appendChild(button);
	 		divFields.appendChild(document.createTextNode(" "));
	 	}
	 	divFields.style.display = "";
	}
</script>