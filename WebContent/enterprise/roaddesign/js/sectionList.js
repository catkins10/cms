window.onload = function() {
	window.projectId = StringUtils.getPropertyValue(window.location.href, "projectId", "");
	var recordElement = document.getElementById('record');
	recordElement.setAttribute("onclick", "editSecton(this)");
	window.top.database.databaseQuery("select * from road_project_section where projectId=" + window.projectId + " order by created desc", 0, 0, recordElement);
};
function newSection() {
	window.top.client.openPage(window.location.href.replace("sectionList", "section"), window);
}
function editSecton(sectionElement) {
	window.top.client.openPage(window.location.href.replace("sectionList", "section") + "&id=" + sectionElement.id + "&name=" + StringUtils.utf8Encode(DomUtils.getElement(sectionElement, "span", "name").innerHTML), window);
}