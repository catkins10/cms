window.onload = function() {
	var recordElement = document.getElementById('record');
	recordElement.setAttribute("onclick", "openBlock(this)");
	var sql = "select * from road_block" +
			  " where sectionId=" + window.top.sectionId +
			  " order by cast(lc as real)";
	window.top.database.databaseQuery(sql, 0, 20, recordElement, true);
};
function newBlock() {
	if(!window.top.sectionId || window.top.sectionId==0) {
		alert("未选定项目和路段，不允许添加资料。");
		return;
	}
	var inputs = [{name:"桩号", value:"", dataType:"number", maxLength:12},
				  {name:"左偏/右偏", value:"-1", dataType:"string", maxLength:2, inputMode:"radio", itemsText:"左偏|-1\0右偏|1"},
				  {name:"半径", value:"", dataType:"number", maxLength:12},
				  {name:"方位角", value:"", dataType:"number", maxLength:12},
				  {name:"N", value:"", dataType:"number", maxLength:12},
				  {name:"E", value:"", dataType:"number", maxLength:12}];
	var dialog = window.top.client.openInputDialog("添加资料", inputs, false, false, "确定", "取消");
	dialog.onOK = function(clientDialogBody) {
		var lc = clientDialogBody.getValue("桩号");
		if(lc!="") {
			var eta = clientDialogBody.getValue("左偏/右偏");
			var ra = toDouble(clientDialogBody.getValue("半径"));
			var fw = toDouble(clientDialogBody.getValue("方位角"));
			var x = toDouble(clientDialogBody.getValue("N"));
			var y = toDouble(clientDialogBody.getValue("E"));
			//保存记录
			var block = {projectId:window.top.projectId,
						 sectionId:window.top.sectionId,
						 lc:lc,
						 eta:eta,
						 ra:ra,
						 fw:fw,
						 x:x,
						 y:y};
			window.top.database.saveRecord("road_block", block);
			recomputeBlocks(); //更新交点的偏角和方位角
			window.location.reload(); //刷新页面
		}
		return true;
	};
}
function openBlock(blockElement) {
	var inputs = [{name:"桩号", value:DomUtils.getElement(blockElement, "span", "lc").innerHTML, dataType:"number", maxLength:12},
				  {name:"左偏/右偏", value:DomUtils.getElement(blockElement, "span", "eta").innerHTML, dataType:"string", maxLength:2, inputMode:"radio", itemsText:"左偏|-1\0右偏|1"},
				  {name:"半径", value:DomUtils.getElement(blockElement, "span", "ra").innerHTML, dataType:"number", maxLength:12},
				  {name:"方位角", value:DomUtils.getElement(blockElement, "span", "fw").innerHTML, dataType:"number", maxLength:12},
				  {name:"N", value:DomUtils.getElement(blockElement, "span", "x").innerHTML, dataType:"number", maxLength:12},
				  {name:"E", value:DomUtils.getElement(blockElement, "span", "y").innerHTML, dataType:"number", maxLength:12}];
	var dialog = window.top.client.openInputDialog("积木法资料", inputs, false, false, "保存", "取消", "删除");
	dialog.onOK = function(clientDialogBody) {
		var lc = clientDialogBody.getValue("桩号");
		if(lc!="") {
			var eta = clientDialogBody.getValue("左偏/右偏");
			var ra = toDouble(clientDialogBody.getValue("半径"));
			var fw = toDouble(clientDialogBody.getValue("方位角"));
			var x = toDouble(clientDialogBody.getValue("N"));
			var y = toDouble(clientDialogBody.getValue("E"));
			//更新记录
			var block = {id: blockElement.id,
						 lc:lc,
						 eta:eta,
						 ra:ra,
						 fw:fw,
						 x:x,
						 y:y};
			window.top.database.updateRecord("road_block", block);
			recomputeBlocks(); //更新交点的偏角和方位角
			window.location.reload(); //刷新页面
		}
		return true;
	};
	dialog.onOther = function(clientDialogBody) { //删除
		var confirm = window.top.client.openConfirmDialog("删除", "删除后不可恢复，是否确定删除？"); //打开确认对话框
		confirm.onOK = function(clientDialogBody) {
			//删除记录
			window.top.database.deleteRecord("road_block", blockElement.id);
			recomputeBlocks(); //更新交点的偏角和方位角
			window.location.reload(); //刷新页面
			//关闭对话框
			dialog.close();
			return true;
		};
		return false;
	};
}
function recomputeBlocks() { //重新计算转点
	window.top.database.onAfterQuery = function(records) {
		doRecomputeBlocks(records);
	};
    //获取积木资料列表
    var sql = "select * from road_block" +
    		  " where road_block.sectionId=" + window.top.sectionId +
    		  " order by cast(lc as real)"; 
    window.top.database.databaseQuery(sql, 0, 0, null, false);
}
function doRecomputeBlocks(blocks) {
    if(blocks.length < 3) { //小于2条记录,不处理
        return;
    }
    for(var i = 2; i<blocks.length; i++) {
        //转点计算
        var curve = computeTurningPoint(toDouble(blocks[i-1].ra), toDouble(blocks[i-1].eta), toDouble(blocks[i-1].x), toDouble(blocks[i-1].y), toDouble(blocks[i-1].lc), toDouble(blocks[i-1].fw), toDouble(blocks[i].ra), toDouble(blocks[i].eta), toDouble(blocks[i].lc));
        blocks[i].x = curve.Xb;
        blocks[i].y = curve.Yb;
        blocks[i].fw = curve.FWb;
        window.top.database.updateRecord("road_block", blocks[i]);
    }
}
function computeTurningPoint(Ra, ETa, Xa, Ya, LCa, FWa, Rb, ETb, LCb) { //计算转点位置的资料
    FWa = deg(FWa) * Math.PI / 180;
    //计算转点位置的坐标
    if(Ra == Rb) { 
        if(Ra == 0) { 
            Xb = Xa + (LCb - LCa) * Math.cos(FWa);
            Yb = Ya + (LCb - LCa) * Math.sin(FWa);
            FWB = FWa;
        }
        else {
            MB = (LCb - LCa) / Ra;
            IA = Ra * Math.sin(MB);
            Ja = ETa * (Ra * (1 - Math.cos(MB)));
            Xb = Xa + (IA) * Math.cos(FWa) - Ja * Math.sin(FWa);
            Yb = Ya + (IA) * Math.sin(FWa) + Ja * Math.cos(FWa);
            FWB = FWa + ETa * MB;
        }
    }
    else {
        if(Ra != 0) { 
            RAA = 1 / Ra;
        }
        else {
            RAA = 0;
        }
        if(Rb != 0) { 
            RBB = 1 / Rb;
        }
        else {
            RBB = 0;
        }
        if(LCb == LCa) { 
            X = 0;
        }
        else {
            X = ETa * (RBB - RAA) / (LCb - LCa);
        }
        T = Math.abs(LCb - LCa);
        i = X * T;
        FWB = FWa + (i + 2 * ETa * RAA) * T / 2;
        if(FWB < 0) { 
            FWB = 2 * Math.PI - FWB;
        }
        else if(FWB > 2 * Math.PI) { 
            FWB = FWB - 2 * Math.PI;
        }
        U = 0;
        V = 0;
        W = 0;
        Z = 0;
        for(S = 1; S<=12; S++) {
            U = U + Math.cos(FWa + (i * (2 * S - 1) / 24 + 2 * ETa * RAA) * (2 * S - 1) / 24 * T / 2);
            V = V + Math.sin(FWa + (i * (2 * S - 1) / 24 + 2 * ETa * RAA) * (2 * S - 1) / 24 * T / 2);
        }
        for(S = 1; S<=11; S++) {
            W = W + Math.cos(FWa + (i * S / 12 + 2 * ETa * RAA) * S / 12 * T / 2);
            Z = Z + Math.sin(FWa + (i * S / 12 + 2 * ETa * RAA) * S / 12 * T / 2);
        }
        Xb = Xa + T * (Math.cos(FWa) + 4 * U + 2 * W + Math.cos(FWB)) / 72;
        Yb = Ya + T * (Math.sin(FWa) + 4 * V + 2 * Z + Math.sin(FWB)) / 72;
    }
    return {Xb: round(Xb, 4),
    		Yb: round(Yb, 4),
    		FWb: round(dms(FWB * 180 / Math.PI), 4)};
}