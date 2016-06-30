//输出监察记录列表
function listMonitorRecords(applicationName, sourceViewName, sourceColumnName, targetViewName, targetFormName, unitId, beginDate, endDate, categories, searchConditions) {
	var param = 'sourceViewName=' + sourceViewName +
				'&sourceColumnName=' + sourceColumnName +
				'&unitId=' + unitId +
				'&beginDate=' + StringUtils.utf8Encode(beginDate) +
				'&endDate=' + StringUtils.utf8Encode(endDate) +
				'&categories=' + StringUtils.utf8Encode(categories) +
				'&searchConditions=' + StringUtils.utf8Encode(searchConditions);
	DialogUtils.openSelectDialog(applicationName, targetViewName, '80%', '80%', false, '', 'PageUtils.editrecord("' + applicationName + '", "' + targetFormName + '", "{id}", "mode=fullscreen")', '', '', '', true, param);
}