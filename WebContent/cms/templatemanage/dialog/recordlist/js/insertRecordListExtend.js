//显示扩展属性,由继承者实现
/*function showExtendProperties(recordListProperties, recordListTextContent);*/

//获取配置元素标题,由继承者实现
/*function getElementTitle(isSearchResults, isRssChannel);*/

//获取默认的左侧记录格式,由继承者实现
/*function getDeafultRecordLeftFormat();*/

//获取默认的右侧记录格式,由继承者实现
/*function getDeafultRecordRightFormat();*/

//保存扩展属性,如果校验出错返回"ERROR",由继承者实现
/*function getExtendPropertiesAsText();*/

var editor = parent.dialogArguments.editor;
var recordListDocument = window.frameElement.ownerDocument;
window.onload = function() { //扩展的页面加载完成
	window.setTimeout(function() {
		CssUtils.cloneStyle(parent.document, document);
		document.body.style.margin = '0px';
		document.body.style.padding = '0px';
		document.body.style.overflow = 'hidden';
		var form = document.body.getElementsByTagName("form")[0];
		window.frameElement.style.height = document.body.scrollHeight;
		parent.setTimeout('DialogUtils.adjustDialogSize();', 10);
		var extendTable = document.getElementsByTagName("table")[0];
		var mainTable = parent.document.getElementById("mainTable");
		if(!extendTable || !mainTable) {
			return;
		}
		extendTable.cellSpacing  = mainTable.cellSpacing;
		extendTable.cellPadding  = mainTable.cellPadding;
		var mainTd = mainTable.rows[0].cells[0];
		var extendTd = extendTable.rows[0].cells[0];
		mainTd.style.width = extendTd.style.width = Math.max(mainTd.offsetWidth, extendTd.offsetWidth);
	}, 10);
};