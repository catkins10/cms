function citationComment(commentId, applicationName, pageName, siteId) { //引用原来的评论
	var citationFrame = document.getElementById("citationFrame");
	if(!citationFrame) {
		try {
			citationFrame = document.createElement('<iframe id="citationFrame" name="citationFrame" onload="onCitationFrameLoad()"></iframe>');
		}
		catch(e) {
			citationFrame = document.createElement('iframe');
			citationFrame.id = citationFrame.name = 'citationFrame';
			citationFrame.onload = onCitationFrameLoad;
		}
		citationFrame.style.display = "none";
		document.body.appendChild(citationFrame);
	}
	citationFrame.src = RequestUtils.getContextPath() + "/cms/comment/citationComment.shtml" +
						"?commentId=" + commentId +
						"&applicationName=" + applicationName +
						"&pageName=" + pageName +
						"&siteId=" + siteId +
						"&seq=" + Math.random();
}
function onCitationFrameLoad() {
	//粘帖被引用的评论
	var editor = HtmlEditor.getEditor('content');
	var range = editor.getRange();
	if(!range) {
		range = DomSelection.createRange(editor.editorDocument, editor.editorDocument.body);
	}
	DomSelection.pasteHTML(editor.editorWindow, range, frames["citationFrame"].document.body.innerHTML);
	//oEditor.EditorDocument.body.innerHTML = frames["citationFrame"].document.body.innerHTML + oEditor.EditorDocument.body.innerHTML;
	//调整窗口位置
	var form = document.forms['cms/comment/comment'];
	var pos = DomUtils.getAbsolutePosition(form);
	if(document.body.scrollTop > pos.top || document.body.scrollTop + document.body.clientHeight < pos.top + form.offsetHeight) { 
		document.body.scrollTop = pos.top;
	}
}