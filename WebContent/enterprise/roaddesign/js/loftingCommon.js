function saveLofting(pile, z) {
	//删除已经存在指定桩号的记录
	window.top.database.executeSQL("delete from road_lofting where pile='" + pile + "' and sectionId=" + window.top.sectionId);
	//添加新记录
	var lofting = {projectId: window.top.projectId,
				   sectionId: window.top.sectionId,
				   pile: pile,
				   z: z};
	window.top.database.saveRecord("road_lofting", lofting);
}
function resetLoftingResult() { //实测水平值变动后,重设放样结果
	var DL = toDouble(document.getElementsByName("horizontalDistance")[0].value) - toDouble(document.getElementsByName("actualHorizontalDistance")[0].value);
	var loftingResult = document.getElementsByName("loftingResult")[0];
    loftingResult.value = DL <= 0 ? "前进" + (0 - DL) + "米" : "后退" + DL + "米";
	loftingResult.style.display = '';
}