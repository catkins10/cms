function approval() {
	var inputs = [{name:'approvalPass', title:'审核结果', inputMode:'radio', itemsText:'通过|true\\0不通过|false', defaultValue:true},
				  {name:'failedReason', title:'未通过原因', inputMode:'textarea', rows:5}];
	DialogUtils.openInputDialog('人才审核', inputs, 430, 200, '', function(value) {
		if(value.approvalPass=='false' && value.failedReason=="") {
			alert('未通过原因不能为空');
			return;
		}
		FormUtils.doAction(RequestUtils.getContextPath() + '/job/talent/admin/approvalTalents.shtml', 'approvalPass=' + value.approvalPass + '&failedReason=' + StringUtils.utf8Encode(value.failedReason));
	});
}