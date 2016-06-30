window.onload = function() {
	var recordElement = document.getElementById('record');
	recordElement.setAttribute("onclick", "openProject(this)");
	window.top.database.databaseQuery("select * from road_project order by created desc", 0, 20, recordElement, true);
	//获取当前路段ID
	window.top.writeCurrentSection = function(sectionId) {
		writeCurrentSection(sectionId);
	};
	window.top.client.callNativeMethod("getPreference('currentSection', '0')", function(returnValue) {
		writeCurrentSection(returnValue);
	});
};
function newProject() {
	window.top.client.openPage(window.location.href.replace("projectList", "project"), window);
}
function openProject(projectElement) {
	window.top.client.openPage(window.location.href.replace("projectList", "project") + "&id=" + projectElement.id + "&name=" + StringUtils.utf8Encode(DomUtils.getElement(projectElement, "span", "name").innerHTML), window);
}
function writeCurrentSection(sectionId) { //输出当前路段
	window.top.sectionId = sectionId;
	var projectNameElement = window.top.document.getElementById("projectName");
	if(sectionId==0) {
		if(projectNameElement.getAttribute("alt")) {
			projectNameElement.innerHTML = projectNameElement.getAttribute("alt");
		}
		return;
	}
	window.top.database.onAfterQuery = function(records) {
		if(!records || records.length<2) {
			return;
		}
		if(!projectNameElement.getAttribute("alt")) {
			projectNameElement.setAttribute("alt", projectNameElement.innerHTML);
		}
		projectNameElement.innerHTML = records[1].projectName + " " + records[1].sectionName;
		window.top.projectId = records[1].projectId;
		window.top.projectName = records[1].projectName;
		window.top.sectionName = records[1].sectionName;
		return true;
	};
	var sql = "select road_project.id projectId, road_project.name projectName, road_project_section.name sectionName" +
			  " from road_project_section left join road_project on road_project_section.projectId=road_project.id" +
			  " where road_project_section.id=" + sectionId;
	window.top.database.databaseQuery(sql, 0, 0, null, false);
}