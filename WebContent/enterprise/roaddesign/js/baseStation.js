window.onload = function() {
	setTraverseListItems("surveyStation,backsightSurveyStation");
	if(window.top.baseStation) {
		var surveyStation = document.getElementsByName('surveyStation')[0]; //测站
		var backsightSurveyStation = document.getElementsByName('backsightSurveyStation')[0]; //后视测站
		DropdownField.setValue('surveyStation', window.top.baseStation.surveyStation);
		surveyStation.setAttribute("x", window.top.baseStation.surveyStationX);
		surveyStation.setAttribute("y", window.top.baseStation.surveyStationY);
		surveyStation.setAttribute("z", window.top.baseStation.surveyStationZ);
		DropdownField.setValue('backsightSurveyStation', window.top.baseStation.backsightSurveyStation);
		backsightSurveyStation.setAttribute("x", window.top.baseStation.backsightSurveyStationX);
		backsightSurveyStation.setAttribute("y", window.top.baseStation.backsightSurveyStationY);
		backsightSurveyStation.setAttribute("z", window.top.baseStation.backsightSurveyStationZ);
		document.getElementsByName('backHeight')[0].value = window.top.baseStation.backHeight;
		document.getElementsByName('backMirrorHeight')[0].value = window.top.baseStation.backMirrorHeight;
	}
};
function setBaseStation() { //设置基站
	var surveyStation = document.getElementsByName('surveyStation')[0]; //测站
	var backsightSurveyStation = document.getElementsByName('backsightSurveyStation')[0]; //后视测站
	window.top.baseStation = {surveyStation: surveyStation.value,
							  surveyStationX: toDouble(surveyStation.getAttribute("x")),
							  surveyStationY: toDouble(surveyStation.getAttribute("y")),
							  surveyStationZ: toDouble(surveyStation.getAttribute("z")),
							  backsightSurveyStation: backsightSurveyStation.value,
							  backsightSurveyStationX: toDouble(backsightSurveyStation.getAttribute("x")),
							  backsightSurveyStationY: toDouble(backsightSurveyStation.getAttribute("y")),
							  backsightSurveyStationZ: toDouble(backsightSurveyStation.getAttribute("z")),
							  backHeight: toDouble(document.getElementsByName('backHeight')[0].value),
							  backMirrorHeight: toDouble(document.getElementsByName('backMirrorHeight')[0].value)};
	window.closeWindow();
}