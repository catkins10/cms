function writeFields(fieldType, parentElementId) { //输出字段列表
	var fieldIds = document.getElementsByName(fieldType + "Ids")[0].value.split(",");
	var html = "";
	if(fieldIds[0]!="") {
		var fieldNames = document.getElementsByName(fieldType + "Names")[0].value.split(",");
		var fieldDirections = document.getElementsByName(fieldType + "Directions")[0].value.split(",");
		for(var i=0; i<fieldIds.length; i++) {
			html += '<span style="display:inline-block;">' + 
					fieldNames[i] + '&nbsp;' +
					'<input name="fieldDirection_' + fieldIds[i] + '"' + (fieldDirections[i]=='desc' ? ' checked' : '') + ' type="checkbox" onclick="generateFieldDirections(&quot;' + fieldType + '&quot;)" value="desc"/>降序' +
					(i==fieldIds.length-1 ? '&nbsp;&nbsp;' : '、&nbsp;') +
					'</span>';
		}
	}
	document.getElementById(parentElementId).innerHTML = html;
	DialogUtils.adjustDialogSize();
}
function generateFieldDirections(fieldType) {
	var fieldIds = document.getElementsByName(fieldType + "Ids")[0].value.split(",");
	var fieldDirections = "";
	if(fieldIds[0]!="") {
		for(var i=0; i<fieldIds.length; i++) {
			var field = document.getElementsByName('fieldDirection_' + fieldIds[i])[0];
			fieldDirections += (i==0 ? '' : ",") + (field && field.checked ? 'desc' : 'asc');
		}
	}
	document.getElementsByName(fieldType + "Directions")[0].value = fieldDirections;
}
function setFields(fieldType, formId, parentElementId) {
	DialogUtils.openSelectDialog('jeaf/application/builder', 'selectDbField', 550, 380, true, fieldType + 'Ids{id},' + fieldType + 'Names{name|字段名称|100%}', 'generateFieldDirections("' + fieldType + '");writeFields("' + fieldType + '", "' + parentElementId + '");', '', '', ',', false, 'formId=' + formId);
}