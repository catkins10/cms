<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dialog/video">
<script type="text/javascript" src="<%=request.getContextPath()%>/jeaf/video/player/js/videoplayer.js" charset="utf-8"></script>
<script>
	window.onload = function() {

	}
	function doOK() {
		var url = document.getElementsByName('url')[0].value;
		if(url=='') {
			alert('源文件不能为空');
			return;
		}
		var width = document.getElementsByName('width')[0].value;
		if(width=='' || isNaN(Number(width))) {
			alert('视频宽度不正确');
			return;
		}
		var height = document.getElementsByName('height')[0].value;
		if(height=='' || isNaN(Number(height))) {
			alert('视频高度不正确');
			return;
		}
		var dialogArguments = DialogUtils.getDialogArguments();
		dialogArguments.editor.saveUndoStep();
		var video = DomUtils.getParentNode(dialogArguments.selectedElement, 'image');
		if(!video || video.id!='video') {
			video = DomUtils.createElement(dialogArguments.window, dialogArguments.range, 'image'); //在指定区域创建HTML元素
			if(!video) {
				alert('视频创建失败');
				return;
			}
			if(document.getElementsByName('center')[0].checked) { //自动居中
				var p = dialogArguments.document.createElement('p');
				p.align = "center";
				video.parentNode.replaceChild(p, video);
				p.appendChild(video);
			}
		}
		video.id = 'video';
		video.width = width;
		video.height = height;
		video.src = window.previewImageUrl;
		var properties = 'applicationName=' + window.applicationName +
						 '&recordId=' + window.recordId +
						 '&videoType=' + window.videoType +
						 '&videoName=' + StringUtils.encodePropertyValue(window.videoName) +
						 '&width=' + width +
						 '&height=' + height +
						 '&autoStart=' + document.getElementsByName("autoStart")[0].checked;
		video.setAttribute('alt', properties);
		video.title = window.videoName;
		DialogUtils.closeDialog();
	}
	function selectVideo() {
		var dialogArguments = DialogUtils.getDialogArguments();
		var selectorURL = dialogArguments.editor.getAttachmentSelectorURL("videos", 'setUrl("{URL}", "{previewImageUrl}", "{applicationName}", "{id}", "{attachmentType}", "{attachmentName}", {WIDTH}, {HEIGHT})');
		DialogUtils.openDialog(selectorURL, 640, 400);
	}
	function setUrl(url, previewImageUrl, applicationName, recordId, videoType, videoName, width, height) {
		window.previewImageUrl = previewImageUrl;
		window.applicationName = applicationName;
		window.recordId = recordId;
		window.videoType = videoType;
		window.videoName = videoName;
		document.getElementsByName('url')[0].value = url;
		document.getElementsByName('width')[0].value = width==0 ? 400 : width;
		document.getElementsByName('height')[0].value = height==0 ? 300 : height;
		preview();
	}
	function preview() {
		var previewDiv = document.getElementById('previewDiv');
		previewDiv.style.width = previewDiv.offsetWidth + "px";
		var url = document.getElementsByName('url')[0].value;
		previewDiv.innerHTML = "";
		if(url=="") {
			return;
		}
		var videoUrl = document.getElementsByName("url")[0].value;
		var videoWidth = document.getElementsByName("width")[0].value;
		var videoHeight = document.getElementsByName("height")[0].value;
		var autoStart = document.getElementsByName("autoStart")[0].checked;
		new VideoPlayer(previewDiv, url, window.previewImageUrl, null, videoWidth, videoHeight, false, false);
	}
</script>
<table border="0" width="100%" cellspacing="0" cellpadding="3px">
	<tr>
		<td nowrap="nowrap" align="right">源文件：</td>
		<td><ext:field property="url" onchange="preview()"/></td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">宽度：</td>
		<td>
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="60px"><ext:field property="width" onchange="preview()"/></td>
					<td>&nbsp;高度：</td>
					<td width="60px"><ext:field property="height" onchange="preview()"/></td>
					<td nowrap="nowrap">&nbsp;<ext:field property="autoStart"/>&nbsp;<ext:field property="center"/></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right" valign="top">预览：</td>
		<td>
			<div id="previewDiv" style="text-align:center; border:#909090 1px solid; overflow: auto; height: 400px; padding: 1px 1px 1px 1px;"></div>
		</td>
	</tr>
</table>
</ext:form>