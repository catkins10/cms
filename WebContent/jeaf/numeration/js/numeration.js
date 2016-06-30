var newCodeFieldValue;
var oldCodeFieldValue;
var currentCodeFieldName;
function insertField(fieldName, isNumberField, codeFieldName) { //插入字段
	currentCodeFieldName = codeFieldName;
	oldCodeFieldValue = document.getElementsByName(codeFieldName)[0].value;
	clipboardData.setData("Text", "<" + fieldName + (isNumberField ? "#####" : "") + ">");
	document.getElementsByName(codeFieldName)[0].focus();
	var script = "document.execCommand('Paste');";
	if(isNumberField) {
		script += "newCodeFieldValue=document.getElementsByName('" + codeFieldName + "')[0].value;";
		script += "document.getElementsByName('" + codeFieldName + "')[0].value=oldCodeFieldValue;";
	 	script += "setNumberLength()";
	}
	window.setTimeout(script, 100);
}
function setNumberLength() { //设置数字长度
	DialogUtils.openInputDialog('数字长度', [{name:'length', title:'数字长度', inputMode:'text'}], 320, 130, 'doInsertNumberField({value}.length)');
}
function doInsertNumberField(numberLength) {
	numberLength = new Number(numberLength);
	document.getElementsByName(currentCodeFieldName)[0].value = newCodeFieldValue.replace(new RegExp("#####", "g"), (numberLength+""=="NaN" || numberLength==0 ? "" : "*" + numberLength));
}