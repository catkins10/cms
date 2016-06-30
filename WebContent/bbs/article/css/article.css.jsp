<%@ page contentType="text/css; charset=UTF-8" %>

form, form td {
	font-size:12px;
	font-family:宋体;
	line-height: 16px
}

form input,.field {
	font-family: 宋体;
	font-size: 12px;
	color:#000000;
	width: 100%;
	height: 20px;
	background-color: #FFFFFF;
	border: 1px solid #909090;
	padding: 3px 3px 3px 3px;
	line-height: 12px;
}

.required {
	background-color: #fafdef !important;
}

form textarea {
	font-family: 宋体;
	font-size: 12px;
	overflow:auto;
	width:100%;
	padding: 3px 3px 3px 3px;
	border:1 solid #909090;
	line-height: 12px;
}
form pre {
	font-size:12px; 
	font-family:宋体;
	line-height:16px;
	margin:0;
	word-wrap:break-word
}

.button {
	color: #000000;
	padding: 3px 6px 3px 6px !important;
	padding: 3px 0px 0px 0px;
	width: auto;
	height: auto;
	border-width:1 solid #808080;
	background-color:buttonface;
}

.selectButton {
	background-repeat:no-repeat;
	cursor:pointer;
	width:13px;
	background-position: left center;
	background-image: url(<%=request.getContextPath()%>/jeaf/common/img/select.gif)
}

.dropDownButton {
	background-repeat:no-repeat;
	cursor:pointer;
	width:13px;
	background-position: left center;
	background-image: url(<%=request.getContextPath()%>/jeaf/common/img/dropdown.gif)
}

/*触摸屏选择器*/
.touchPicker {
	position: absolute;
	width: 308px; 
	top: 200px;
	left: 200px;
	border: 1px solid #f8f8f8;
	background-color: #ffffff;
	border-radius: 2px;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	color: #000000;
}
.touchPicker .touchPickerTitle {
	height: 30px;
	line-height: 30px;
	padding-left: 6px;
	border-bottom: 1px solid #33afdd;
}
.touchPicker .pickerBody {
 	line-height: 18px;
}
.touchPicker .touchPickerOkButton, .touchPicker .touchPickerCancelButton {
	 height: 32px;
	 line-height: 32px;
	 text-align: center;
	 border-top: 1px solid #f0f0f0;
}
.touchPicker .touchPickerOkButtonFocus, .touchPicker .touchPickerCancelButtonFocus {
	background-color: #f0f0f0;
}
.touchPicker .touchPickerCancelButton {
 	border-right: 1px solid #f0f0f0;
}

/*触摸屏选择器:列表条目*/
.touchPicker .listItem {
	border-bottom: 1px solid #f0f0f0;
	height: 30px;
	padding: 3px 5px 3px 5px;
}

/*触摸屏选择器:日期选择*/
.touchPicker .datePicker .yearMonthPicker { /*日期选择器:年月表格*/
	background: #fff;
	border: #f0f0f0 1px solid;
	border-bottom-style: none;
}
.touchPicker .datePicker .dayTable, .touchPicker .datePicker .dayTable td { /*日期选择器:“天”表格*/
	border: #f0f0f0 1px solid;
	border-collapse: collapse;
	background-color: #fff;
}
.touchPicker .datePicker .dayTable .selectedDay { /*日期选择器:选中的天*/
	background-color: #33afdd;
}
.touchPicker .datePicker .yearMonthPicker .yearPicker, .touchPicker .datePicker .yearMonthPicker .monthPicker { /*年月选择器*/
	width: 108px;
	height: 28px;
	border: #f0f0f0 1px solid;
}
.touchPicker .datePicker .yearMonthPicker .yearPicker {  /*日期选择器:年选择器*/
	width: 168px;
}
.touchPicker .datePicker .monthPicker .pickerItem { /*日期选择器:年选择器条目*/
	width: 36px;
}
.touchPicker .datePicker .pickerItem { /*日期选择器:年选择器条目*/
	line-height: 28px;
}
.touchPicker .datePicker .selectedPickerItem { /*选中的选择器条目*/
	text-align: center;
	background-image: none;
}

/*触摸屏选择器:数字选择*/
.touchPicker .numberPicker { /*数字选择器*/
	width: 50px;
	height: 105px;
	margin-top: 16px;
	margin-bottom: 16px;
	background-image: url(<%=request.getContextPath()%>/jeaf/common/img/number_picker.png);
	background-repeat: repeat-x;
	border: 1px solid #f0f0f0;
}
.touchPicker .numberPickerSeparator { /*数字分隔符*/
	font-size: 24px;
	padding-left: 6px;
	padding-right: 6px;
}
.touchPicker .numberPicker .pickerItem { /*数字选择器条目*/
	display: block;
	text-align: center;
	font-size: 24px;
	width: 50px;
	height: 35px;
	line-height: 35px;
	color: #ccc;
}
.touchPicker .numberPicker .selectedPickerItem{ /*选中的数字选择器条目*/
	font-size: 24px;
	color: #000;
}

/*日期选择*/
.datePicker .yearMonthPicker { /*日期选择器:年月表格*/
	background: #e6e6e6;
	border: #ddd 1px solid;
	border-bottom-style: none;
}
.datePicker .yearMonthPicker .yearPicker, .datePicker .yearMonthPicker .monthPicker { /*年月选择器*/
	width: 126px;
	height: 24px;
	margin-left: 3px;
	margin-top: 3px;
	margin-bottom: 2px;
	border: 1px solid #ddd;
	background: #fff;
	cursor: pointer;
}
.datePicker .yearMonthPicker .yearPicker {  /*日期选择器:年选择器*/
	width: 168px;
	margin-right: 6px;
}
.datePicker .pickerItem { /*日期选择器:年选择器条目*/
	font-size: 12px;
	line-height: 24px;
	text-align: center;
	width: 42px;
	color: #ccc;
}
.datePicker .yearPicker .pickerItem { /*日期选择器:年选择器条目*/
	width: 56px;
}
.datePicker .selectedPickerItem { /*选中的选择器条目*/
	font-size: 14px;
	color: #000;
}
.datePicker .selectedPickerItem .dropDownButton {
	display: inline-block;
	width: 16px;
	background-position: right center;
	background-repeat: no-repeat;
}
.datePicker .dayTable, .datePicker .dayTable td { /*日期选择器:“天”表格*/
	border: #ddd 1px solid;
	border-collapse: collapse;
	background-color: #fcfcfc;
}
.datePicker .dayTable .week { /*日期选择器:星期*/
	height: 30px;
	font-size: 12px;
	background-color: #eee;
}
.datePicker .dayTable .day { /*日期选择器:天*/
	height: 25px;
	cursor: pointer;
	padding: 3px 5px 3px 5px;
}
.datePicker .dayTable .selectedDay { /*日期选择器:选中的天*/
	background-color: #33afdd;
}
.datePicker .dayTable .today { /*日期选择器:今天*/
	color: red !important;
}
.datePicker .dayTable .weekend { /*日期选择器:周末*/
	color: #0174df;
}

.radio,.checkbox {
	border:none;
	width:13px;
	height:13px;
 	background:''
}
.menubar {
	font-size:12px;
	font-family:宋体;
	position:absolute;
	background-color:white;
	border:1 outset white;
	width:100px;
 	cursor:hand
}
 
.menunormal {
	font-size:12px;
	font-family:宋体;
	background:'';
	color:black;
	border:1 solid white;
	height:22
}

.menuover {
	font-size:12px;
	font-family:宋体;
	border:1 solid white;
	background:#647EB9;
	color:white;
	height:22
}

.listbar {
	background-color: white;
	border: #666 1px solid;
	cursor: pointer;
	margin: 0px;
	font-size: 12px;
	font-family: 宋体;
	display :block;
	position: absolute;
	outline:none;
	top: 0px;
    bottom: 0px;
    left: 0px;
    right: 0px;
    /*ie6*/
    width: expression(document.documentElement.clientWidth - 2);
    height: expression(document.documentElement.clientHeight - 2);
}

.listnormal {
	font-size: 12px;
	background:'';
	color:black;
	border:1 solid white;
	height:22
}
.listover {
	font-size: 12px;
	font-family:宋体;
	border:1 solid white;
	background:#647EB9;
	color:white;
	height:22
}

form a:link {
	font-size: 12px;
	font-family:宋体;
	text-decoration: none;
	font-weight: none;
	color: #000000;
}

form a:hover {
	font-size: 12px;
	font-family:宋体;
	font-weight: none;
	text-decoration: none;
	color: #000000;
}

form a:visited {
	font-size: 12px;
	font-family:宋体;
	font-weight: none;
	text-decoration: none;
	color: #000000;
}

/*图片按钮*/
.imgButtonLeftNormal, .imgButtonLeftOver {
	cursor:pointer;
	width:4px;
	height:24px;
	line-height:12px;
	background-image:url(<%=request.getContextPath()%>/jeaf/form/img/button_left.gif);
	background-repeat:no-repeat;
	background-position: right top;
	border-width: 0px;
	font-size: 12px;
	font-weight:bold;
	padding-top:6px;
}
.imgButtonMiddleNormal, .imgButtonMiddleOver {
	cursor:pointer;
	height:24px;
	line-height:12px;
	background-image:url(<%=request.getContextPath()%>/jeaf/form/img/button_center.gif);
	background-repeat:repeat-x;
	color:#ffffff;
	font-size: 12px;
	font-weight:bold;
	border-width:0px;
	padding-top:6px;
	padding-left:5px;
	padding-right:5px;
}
.imgButtonRightNormal, .imgButtonRightOver {
	cursor:pointer;
	width:4px;
	height:24px;
	line-height:12px;
	background-image:url(<%=request.getContextPath()%>/jeaf/form/img/button_right.gif);
	background-repeat:no-repeat;
	border-width: 0px;
	font-size: 12px;
	font-weight:bold;
	padding-top:6px;
}

.forumTable {
	border: #9db3c5 1px solid;
	line-height: 20px;
}

.categoryHeader {
	background-color: #30589c;
	line-height: 20px;
	font-weight: bold;
	color: #ffffff;
}

.forumHeader {
	background-color: #e8f3fd;
	line-height: 20px;
	border-top: #9db3c5 1px solid;
	font-weight: bold;
}

.forumName {
	font-weight: bold;
}

.forum {
	line-height: 20px;
	border-top: #9db3c5 1px solid;
}

.loginTable {
	border: #9db3c5 1px solid;
	background-color: #f5faff;
}

.viewPackage{line-height:12px}
.viewActionAndSearchBar{height:36px; 
						width:100%;
						background-color: #f5faff;
						border:1 solid #dbdce3;}
.viewPageAndCategoryBar{height:32px; width:100%; padding-top:5px; border:1 solid #dbdce3; border-top-style:none; border-bottom-style:none;}
.viewPageAndCategoryBottomBar{}
.articleSwitchBar{height:28px; padding-left:5px;padding:8px}

/*视图操作相关*/
.viewActionBar{padding-left:5px; float:left; margin-top: 5px}
.viewActionTextNormal, .viewActionTextOver {
	cursor: pointer;
	padding: 4px 4px 4px 4px;
	border: #9db3c5 1px outset;
	color: #ffffff;
	background-color: #3970c0;
	display: inline-block;
}
.viewActionSpace{width:10px}
.printAsExcelImage, .viewCustomImage, .printAsExcelText, .viewCustomText {display: none;}

/*视图定位相关*/
.viewLocationBar{height:32px; width:100%; padding-top:5px; border:1 solid #dbdce3; border-top-style:none; border-bottom-style:none;}
.viewLocationHeader{padding-left:3px;}
.viewLocationText{color:#000000}
.viewLocationSeparateImage{}
.viewLocationSeparateText{padding-left:1px; padding-right:1px;}

/*视图相关*/
.view{border: #9db3c5 1px solid;}
.view pre{font-size:12px; font-family:宋体;line-height:16px;margin:0;word-wrap:break-word}
.viewHeader{height:30px; background-color:#30589c; color:#ffffff; font-weight:bold}

/*页面信息相关*/
.viewPageBar{padding:3px 0px 0px 5px;}
.viewRecordCount{padding-top:5px; padding-right:5px;}
/*第一页*/
.firstPageLinkImage{width:16px;
					height:22px;
					background-image: url(<%=request.getContextPath()%>/jeaf/view/images/firstpage.gif);
					background-repeat:no-repeat;
					background-position:3px 5px;}
.firstPageLinkText{padding-top:5px; display:none}
.firstPageLinkImageDisable{width:16px;
						   height:22px;
						   background-image: url(<%=request.getContextPath()%>/jeaf/view/images/firstpage_disable.gif);
						   background-repeat:no-repeat;
						   background-position:3px 5px;}
.firstPageLinkTextDisable{padding-top:5px; display:none}
/*上一页*/
.previousPageLinkImage{width:16px;
					height:22px;
					background-image: url(<%=request.getContextPath()%>/jeaf/view/images/previouspage.gif);
					background-repeat:no-repeat;
					background-position:3px 5px;}
.previousPageLinkText{padding-top:5px; display:none}
.previousPageLinkImageDisable{width:16px;
					height:22px;
					background-image: url(<%=request.getContextPath()%>/jeaf/view/images/previouspage_disable.gif);
					background-repeat:no-repeat;
					background-position:3px 5px;}
.previousPageLinkTextDisable{padding-top:5px; display:none}
/*下一页*/
.nextPageLinkImage{width:16px;
					height:22px;
					background-image: url(<%=request.getContextPath()%>/jeaf/view/images/nextpage.gif);
					background-repeat:no-repeat;
					background-position:3px 5px;}
.nextPageLinkText{padding-top:5px;display:none}
.nextPageLinkImageDisable{width:16px;
					height:22px;
					background-image: url(<%=request.getContextPath()%>/jeaf/view/images/nextpage_disable.gif);
					background-repeat:no-repeat;
					background-position:3px 5px;}
.nextPageLinkTextDisable{padding-top:5px; display:none}
/*最后一页*/
.lastPageLinkImage{width:16px;
					height:22px;
					background-image: url(<%=request.getContextPath()%>/jeaf/view/images/lastpage.gif);
					background-repeat:no-repeat;
					background-position:3px 5px;}
.lastPageLinkText{padding-top:5px; display:none}
.lastPageLinkImageDisable{width:16px;
					height:22px;
					background-image: url(<%=request.getContextPath()%>/jeaf/view/images/lastpage_disable.gif);
					background-repeat:no-repeat;
					background-position:3px 5px;}
.lastPageLinkTextDisable{padding-top:5px; display:none}
/*页跳转*/
.pageNumber{}
.pageNumberInput{width:50px;height:18px;text-align:center;padding-top:2px;border-style:solid; border-width:1px ;border-left-color:#dbdce3;border-top-color:#dbdce3;border-right-color:#dbdce3;border-bottom-color:#dbdce3}

.authorCol {
	width:200px;
	background-color: #e8f3fd;
	padding-left:10px;
	padding-right:10px;
	padding-top:10px;
	padding-bottom:10px;
}

.bodyCol {
	width:100%;
	padding-left:10px;
	padding-right:10px;
	padding-top:10px;
	padding-bottom:10px;
}

.authorLine {
	height:1px;
	color: #9db3c5;
}

.bodyLine {
	height:1px;
	color: #9db3c5;
}

.contentTable {
	background: #dbdce3;
}
.tdtitle {
	font-size: 12px;
	font-family:宋体;
	background-color:#E9F3FC;
	padding-left:5px;
	font-weight:bold;
	padding-top:5px;
	padding-bottom:5px;
	padding-right:5px;
}
.tdcontent {
	font-size: 12px;
	font-family:宋体;
	background-color:#ffffff;
	padding-left:3px;
	padding-right:3px;
	padding-top:3px;
	padding-bottom:3px;
}

.row {
	padding:3px
}

.readonlyrow {
	padding:5px
}

.content {
	height:150px;
	font-size: 14px;
	line-height: 24px;
}

.content a:link {
	font-size: 14px;
	line-height: 24px;
}

.content a:hover {
	font-size: 14px;
	line-height: 24px;
}

.content a:visited {
	font-size: 14px;
	line-height: 24px;
}

.articleSubject {
	font-weight:bold;
	padding-bottom:8px;
}

.htmleditor {
	border: #cccccc 1px solid;
}
.htmleditor .toolbarSet { /*编辑器工具栏集合*/
	background-color: #f0f0ee;
	border-bottom: #cccccc 1px solid;
	cursor: arrow;
}
.htmleditor .toolbar { /*编辑器工具栏*/
	padding: 1px 1px 1px 1px;
	display: inline-block;
	overflow: hidden;
	height: 22px !important;
	height: 24px;
}
.htmleditor .toolbar .toolbarSeparatorLine { /*编辑器工具栏分隔线*/
	display: inline-block;
	width: 5px;
	height: 22px;
	background-image: url(../../../jeaf/htmleditor/images/toolbar.start.gif);
	background-position: center center;
	background-repeat: no-repeat;
}
.htmleditor .toolbar input, .htmleditor .toolbar .field { /*编辑器工具栏分隔线*/
	height: 20px;
	padding: 0px 2px 0px 2px;
	border: 1px solid #BBBBBB;
	line-height: 18px;
	font-size: 12px;
}
.htmleditor .toolbarButtonSeparatorBar { /*编辑器工具栏按钮分隔栏*/
	display: inline-block;
	width: 1px;
	height: 18px;
	margin-left: 3px;
	margin-right: 3px;
}
.htmleditor .toolbarButtonSeparatorBar .toolbarButtonSeparatorLine { /*编辑器工具栏按钮分隔线*/
	display: inline-block;
	width: 1px;
	height: 22px;
	background-image: url(../../../htmleditor/images/toolbar.group.start.gif);
	background-position: center 1px;
	background-repeat: no-repeat;
}
.htmleditor .toolbarButton { /*编辑器工具栏按钮*/
	display: inline-block;
	height: 16px;
	padding: 1px 2px 1px 2px;
	margin-right: 1px;
	border: #f0f0ee 1px solid;
	cursor: pointer;
}
.htmleditor .toolbarButtonActive { /*编辑器工具栏按钮:选中*/
	border: #cccccc 1px solid;
	background-color: #f6f6f0;
}
.htmleditor .toolbarButtonDisabled { /*编辑器工具栏按钮:禁用*/
	opacity: 0.30; /* Safari, Opera and Mozilla */
    filter: gray() alpha(opacity=30); /* IE */
}
.htmleditor .toolbarButtonOver { /*编辑器工具栏按钮:鼠标经过时*/
	border: #cccccc 1px solid;
	background-color: #f5f5f0;
}
.htmleditor .toolbarButton .toolbarButtonIcon { /*编辑器工具栏按钮:图标*/
	display: inline-block;
	width: 16px;
	height: 16px;
	float: left;
}
.htmleditor .toolbarButton .toolbarButtonTitle { /*编辑器工具栏按钮:标题*/
	display: inline-block;
	white-space: nowrap !important;
	padding: 0px 1px 0px 2px;
	height: 16px;
	line-height: 16px;
	font-size: 12px;
	float: left;
}
.htmleditor .toolbarButton .toolbarButtonDropButton { /*编辑器工具栏按钮:下拉按钮*/
	display: inline-block;
	width: 8px;
	padding: 0px 0px 0px 2px;
	float: left;
}
.htmleditor .sourceCodeTextArea { /*源代码文本框*/
	border-style: none;
	width: 100% !important;
	height: 100% !important;
	outline-style: none;
}
.htmleditor .editorarea body, .htmleditor .editorarea td, .htmleditor .editorarea th { /*编辑区域内容样式*/
	font-family: 宋体;
	font-size: 14px;
	line-height: 1.6em;
}
.htmleditor .editorarea body { /*编辑区域BODY样式*/
	padding: 5px 5px 5px 5px;
	margin: 0px;
	border-style: none;
	background-color: #ffffff;
}
.htmleditor .editorarea a { /*编辑区域链接样式*/
	color: #0000FF !important;	/* For Firefox... mark as important, otherwise it becomes black */
}
.htmleditor .editorarea p, .htmleditor .editorarea ul, .htmleditor .editorarea li {  /*编辑区域P、UL、LI样式*/
	margin-top: 3px;
	margin-bottom: 0px;
}