function editPerson(id, type, openMode) {
	switch(type) {
	case '1': 
		type = "employee";
		break;
	case '2': 
		type = "teacher";
		break;
	case '3': 
		type = "student";
		break;
	case '4': 
		type = "genearch";
		break;
	default:
		return;
	}
	var webApplicationSafeUrl = document.getElementsByName("webApplicationSafeUrl");
	if(webApplicationSafeUrl[0]) {
		PageUtils.openurl(webApplicationSafeUrl[0].value + '/jeaf/usermanage/admin/' + type + '.shtml?act=edit&id=' + id, 'width=720,height=480', id);
	}
	else {
		PageUtils.editrecord('jeaf/usermanage', 'admin/' + type, id, openMode);
	}
}