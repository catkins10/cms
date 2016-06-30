function selectJobholder(projectJobholderId, jobholderCategory, jobholderQualifications, jobholderNumber) {
	DialogUtils.openSelectDialog('bidding/enterprise',
								 'selectFreeJobholder',
								 640,
								 400,
								 true, 'jobholderIds_' + projectJobholderId + '{id},jobholderNames_' + projectJobholderId + '{name|' + jobholderCategory + '|100%}',
								 'afterSelectJobholder("' + projectJobholderId + '", "' + jobholderCategory + '", ' + jobholderNumber + ')',
								 '',
								 '',
								 ',',
								 false,
								 'jobholderCategory=' + StringUtils.utf8Encode(jobholderCategory) + '&jobholderQualifications=' + StringUtils.utf8Encode(jobholderQualifications));
}
function afterSelectJobholder(projectJobholderId, jobholderCategory, jobholderNumber) {
	var jobholderIds = document.getElementsByName('jobholderIds_' + projectJobholderId)[0];
	var ids = jobholderIds.value.split(",");
	if(ids.length==jobholderNumber) {
		return;
	}
	if(ids.length<jobholderNumber) {
		alert(jobholderCategory + "人数不够");
		return;
	}
	var jobholderNames = document.getElementsByName('jobholderNames_' + projectJobholderId)[0];
	var names = jobholderNames.value.split(",");
	var newIds = "", newNames = "";
	for(var i=0; i<jobholderNumber; i++) {
		newIds += (i==0 ? "" : ",") + ids[i];
		newNames += (i==0 ? "" : ",") + names[i];
	}
	jobholderIds.value = newIds;
	jobholderNames.value = newNames;
}