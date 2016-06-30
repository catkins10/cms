<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveInquiryOption">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap" align="right" valign="top">选项：</td>
			<td width="100%"><ext:field property="option.inquiryOption"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">补充说明：</td>
			<td width="100%">
			 <div style="float:left">
			  <ext:field property="option.needSupplement" onclick="needSupplementChanged(this)"/>
			 </div>
			 <div id="supplementButtonDiv" style="display:<ext:equal property="option.needSupplement" value="0">none</ext:equal>;float:left;width:100px">
			  <input type="button" value="插入说明框" onclick="insertSupplementBox()"/>
			 </div>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right" valign="top">描述：</td>
			<td><ext:field property="option.description"/></td>
		</tr>
	</table>
</ext:form>
<script>
var range;
window.onload=function(){
	setTimeout("addBodyClick()",1000);
}
/*
	给编辑框加点击事件：获取光标位置用于插入说明框
*/
function addBodyClick(){
	var iframeDocument=window.frames[0].document;
	EventUtils.addEvent(iframeDocument.body, 'click', function(){
		range = DomSelection.getRange(iframeDocument.body);
	});
}
/*
	获取选中区域
*/
function getRange(window) { 
	var selection = DomSelection.getSelection(window);
	if(!selection) {
		return null;
	}
	var range;
	if(selection.createRange) {
		range = selection.createRange();
	}
	else if(selection.rangeCount>0) {
		range = selection.getRangeAt(0);
	}
	var element = DomSelection.getSelectedElement(range);
	return element && element.ownerDocument!=window.document ? null : range;
};
/*
	粘贴HTML
*/
function pasteHTML(document, range, html) { 
	DomSelection.selectRange(document, range);
	if(range && range.pasteHTML) {
		range.pasteHTML(html);
	}
	else if("" + document.queryCommandSupported('insertHTML')=="true") { //支持insertHTML命令
		document.execCommand('insertHTML', false, html);
	}
	else if(range.surroundContents) {
		var element = document.createElement("b");
        range.surroundContents(element);
        element.outerHTML = html;
	}
};
/*
	插入说明框按钮显示隐藏操作
*/
 function needSupplementChanged(object){
   iframeDocument=window.frames[0].document;
   supplementId="supplement_" + document.getElementsByName("option.id")[0].value;
   if(object.value==0){
     document.getElementById("supplementButtonDiv").style.display="none";
     supplementElement=iframeDocument.getElementById(supplementId);
     if(supplementElement){
        supplementElement.parentNode.removeChild(supplementElement);
     }
   }
   else{
     document.getElementById("supplementButtonDiv").style.display="";
   }
 }
 
 /*
	插入说明框操作
*/
 function insertSupplementBox(){
   iframeDocument=window.frames[0].document;
   supplementId="supplement_" + document.getElementsByName("option.id")[0].value;
   supplementElement=iframeDocument.getElementById(supplementId);
   DialogUtils.openInputDialog("插入补充说明框", [{name:"type", title:"类型", inputMode:'radio',itemsText:'文本框|text\\0文本域|textarea', defaultValue:'text'},{name:"width", title:"宽度", inputMode:'text'}], 360, 120, '', function(value) {
		  width=value.width==''?'':value.width+'px';
		  var supplementBox;
		  try{
		    supplementBox=iframeDocument.createElement(value.type=='textarea'?"<textarea onchange='setSupplementInputBoxChecked(this)' class='inquirySupplement' id='" +supplementId+ "' "+(width==''?'':" style='width:"+width+"'")+"></textarea>"
		                                               :"<input type='text' onchange='setSupplementInputBoxChecked(this)' class='inquirySupplement' id='" +supplementId+ "' "+(width==''?'':" style='width:"+width+"'")+"/>");
		  }catch(e){
		    supplementBox=iframeDocument.createElement(value.type=='textarea'?value.type:"INPUT");
		    if(value.type=='text'){
		      supplementBox.type='text';
		    }
		    supplementBox.id=supplementId;
		    supplementBox.className='inquirySupplement';
		    supplementBox.style.width=width;
		    supplementBox.setAttribute("onchange","setSupplementInputBoxChecked(this)");
		  }
		  if(!supplementElement){
		  		pasteHTML(iframeDocument, range, supplementBox.outerHTML);
          }
          else if(confirm("说明框已存在，是否替换？")){
            iframeDocument.body.innerHTML=iframeDocument.body.innerHTML.replace(supplementElement.outerHTML,supplementBox.outerHTML);
          }
          EventUtils.addEvent(supplementBox, 'onpropertychange', function(){alert(123);});
		});
  
 }
</script>