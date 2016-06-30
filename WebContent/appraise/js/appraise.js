function submitInternetAppraise() {
	//检查是否已经完成评议
	var inputs = document.getElementsByTagName("input");
	var previousName = "";
	var unappraise = "";
	var firstUnappraiseRadio;
	for(var i=0; i<inputs.length; i++) {
		if(!inputs[i].name || inputs[i].name.indexOf("appraise_")!=0 || inputs[i].name==previousName) {
			continue;
		}
		previousName = inputs[i].name;
		//检查是否完成选择
		var radios = document.getElementsByName(previousName);
		var j = radios.length - 1;
		for(; j>=0 && !radios[j].checked; j--);
		if(j>=0) {
			continue;
		}
		unappraise = (unappraise=="" ? "" : unappraise + "、") + radios[0].title.substring(0, radios[0].title.indexOf('_'));
		if(!firstUnappraiseRadio) {
			firstUnappraiseRadio = radios[0];
		}
	}
	if(unappraise!="" && !confirm(unappraise + "未评议，是否继续提交？")) {
		try {
			firstUnappraiseRadio.focus();
		}
		catch(e) {
		
		}
		return;
	}
	FormUtils.submitForm(false, 'appraise/internetAppraise');
}