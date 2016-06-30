function selectInquirySubject(width, height, multiSelect, param, scriptEndSelect, key, separator) {
   var url = RequestUtils.getContextPath() + "/cms/inquiry/selectInquirySubject.shtml";
   url += "?multiSelect=" + multiSelect; 
   url += "&param=" + StringUtils.utf8Encode(param); 
   url += (key && key!="" ? "&key=" + StringUtils.utf8Encode(key) : ""); 
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ",")); 
   url += "&selectNodeTypes=inquirySubject";
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : ""); 
   DialogUtils.openDialog(url, width, height);
}

//提交调查
function submitInquiry(formId) {
	var form = document.getElementById(formId);
	if(location.href.indexOf('submitInquiry.shtml')!=-1 || location.href.indexOf('inquiry.shtml')!=-1) {
		form.submit();
		return;
	}
	var result='', inquiryIds='', inputs = form.getElementsByTagName('input');
	for(var i=0; i<inputs.length; i++) {
		var values = inputs[i].name.split("_");
		if(values[0]!='inquiryOption' || result.indexOf(values[1])!=-1) {
			continue;
		}
		if(inquiryIds.indexOf(values[1])==-1) {
			inquiryIds += ',' + values[1];
		}
		var selected = '';
		var options = document.getElementsByName(inputs[i].name);
		var voteCount=0, minVote=1; maxVote=1;
		if(inputs[i].value && inputs[i].value!='') {
			minVote = Number(inputs[i].value.split('~')[0]);
			maxVote = Number(inputs[i].value.split('~')[1]);
		}
		isFillBlank=inputs[i].value.split('~')[2]=="isFillBlank=true";
		if(isFillBlank){
		   minVote=maxVote=options.length;//填空题必须全填
		}
		for(var j=0; j<options.length; j++) {
			if(options[j].checked) {
				selected += ',' + options[j].id;
				var supplement = DomUtils.getElement(form, 'textarea', 'supplement_' + options[j].id);
				if(!supplement){
				   supplement = DomUtils.getElement(form, 'input', 'supplement_' + options[j].id);
				}
				if(supplement && supplement.value!="") {
					selected += '@' + supplement.value.replace(/,/g, '[COMMA]').replace(/;/g, '[SEMICOLON]').replace(/@/g, '[AT]');
				}
				voteCount++;
			}
		}
		if(voteCount==0) {
			alert('调查“' + inputs[i].title + '”未完成选择！');
			return;
		}
		else if(voteCount<minVote) {
			alert('调查“' + inputs[i].title + '”选项不能少于' + minVote + '个，您已经选中' + voteCount + '个选项。');
			return;
		}
		else if(voteCount>maxVote) {
			alert('调查“' + inputs[i].title + '”选项不能超过' + maxVote + '个，您已经选中' + voteCount + '个选项。');
			return;
		}
		result += ';' + values[1] + ';' + selected.substring(1); //格式:[调查ID];[选中的选项ID1]@补充说明1,[选中的选项ID2]@补充说明2;...
	}
	/*if(form.onsubmit && !form.onsubmit()) {
		return;
	}*/
	form.inquiryResult.value = result.substring(1);
	form.inquiryIds.value = inquiryIds.substring(1);
	form.submit();
}
//设置选择框选中。当补充说明框有输入内容时，自动将对应的选择框打钩
function setSupplementInputBoxChecked(target){
  inputBox=document.getElementById(target.id.replace("supplement_",""));
  if(!inputBox){
    return;
  }
  if(target.value != ''){//有输入内容
     inputBox.checked=true;
  }
  else{
     inputBox.checked=false;
  }
}
//查看结果
function showInquiryResult(formId) {
	var form = document.getElementById(formId);
	var url = RequestUtils.getContextPath() + '/cms/inquiry/inquiryResult.shtml?inquiryIds=' + getInquiryIds(form) + '&siteId=' + form.siteId.value;
	/*if(location.href.indexOf('cms/inquiry')!=-1) {
		location.href = url;
	}
	else {
		window.open(url).focus();
	}*/
	window.open(url).focus();
}

//结果反馈
function showInquiryFeedback(formId) {
	var form = document.getElementById(formId);
	var url = RequestUtils.getContextPath() + '/cms/inquiry/inquiryFeedback.shtml?inquiryIds=' + getInquiryIds(form) + '&siteId=' + form.siteId.value;
	/*if(location.href.indexOf('cms/inquiry')!=-1) {
		location.href = url;
	}
	else {
		window.open(url).focus();
	}*/
	window.open(url).focus();
}

//获取调查ID
function getInquiryIds(form) {
	var inquiryIds = form.inquiryIds.value;
	if(inquiryIds=="") {
		var inputs = form.getElementsByTagName('input');
		for(var i=0; i<inputs.length; i++) {
			var values = inputs[i].name.split("_");
			if(values[0]!='inquiryOption' || inquiryIds.indexOf(values[1])!=-1) {
				continue;
			}
			inquiryIds += (inquiryIds=="" ? "" : ",") + values[1];
		}
	}
	return inquiryIds;
}