<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<b>图纸：</b>
<ext:field writeonly="true" property="biddingDrawing"/>
&nbsp;&nbsp;<b>其它招标文件：</b>
<ext:field writeonly="true" property="otherBiddingDocuments"/>
<br><br>
<input type="button" class="button" style="width:160px" value="下载招标文件并加载阅读器" onclick="loadReader()">
<input type="button" class="button" style="width:100px" value="打开招标文件" onclick="openGef()">
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/filetransfer/js/fileTransfer.js"></script>
<div id="divFileDownload" style="display:none">
	<table border="0" cellpadding="0" cellspacing="2" width="100%" style="table-layout:fixed">
		<tr>
			<td nowrap="nowrap">
				下载URL：<span id="downloadURL"></span>
			</td>
			<td align="right" nowrap="nowrap">
				保存位置：<span id="downloadSavePath"></span>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div style="height:16px; border:#808080 1px solid; padding:1px; width:100%;">
					<div id="downloadProgress" style="background-color:#00008B; height:100%; width:0px; font-size:1px">&nbsp;</div>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				传输速度：<span id="downloadSpeed"></span>
			</td>
			<td align="right">
				已完成：<span id="downloadCompleted">&nbsp;</span>
			</td>
		</tr>
	</table>
	<input type="button" class="button" value="放弃" onclick="if(fileDownloader)fileDownloader.cancelDownload()">
	<input type="button" class="button" value="停止" onclick="if(fileDownloader)fileDownloader.stopDownload()">
</div>
<div style="width:100%; height:500px" id="divReader"></div>
<script>
var biddingDocumentFileName;
var fileDownloader;
function loadReader() {
	var url = '<ext:iterateAttachment id="biddingDocuments" propertyRecordId="id" applicationName="bidding/project" attachmentType="biddingDocuments" length="1"><ext:write name="biddingDocuments" property="urlInline"/></ext:iterateAttachment>';
	if(url=='') {
		alert('招标文件不存在');
		return;
	}
	if(!fileDownloader) {
		fileDownloader = new FileDownloader();
		fileDownloader.onDownloadComplete = function(downloadURL, downloadedFilePath) { //事件处理:下载完毕
			if(!document.getElementById('biddingDocumentsReader')) {
				return;
			}
			biddingDocumentFileName = downloadedFilePath;
			//document.getElementById('biddingDocumentsReader').OpenFile(biddingDocumentFileName);
		}
		fileDownloader.onDownloading = function(downloadURL, savePath, fileSize, complete, speed, usedTime, threads, percent) { //事件处理:下载中
			document.getElementById('divFileDownload').style.display = '';
			document.getElementById('downloadURL').innerHTML = downloadURL;
			document.getElementById('downloadSavePath').innerHTML = savePath;
			document.getElementById('downloadSpeed').innerHTML = convertBytes(speed) + '/秒' + '，耗时：'  + convertTime(usedTime) + (fileSize<=0 || speed==0 ? '' : '，预计剩余时间：' + convertTime((fileSize - complete) / speed));
			if(fileSize>-1) {
				var percent = fileSize==0 ? '100%' : percent + '%';
				document.getElementById('downloadCompleted').innerHTML = percent + "，&nbsp;&nbsp;" + convertBytes(complete) + ' / ' + convertBytes(fileSize);
				document.getElementById('downloadProgress').style.width = percent;
			}
			else {
				document.getElementById('downloadCompleted').innerHTML = convertBytes(complete);
			}
		};
	}
	//下载招标文件
	fileDownloader.downloadWithoutFileDialog(url, 'c:\\gef\\<ext:write property="id"/>\\', 3);
	//打开阅读器
	if(document.getElementById('biddingDocumentsReader')) {
		return;
	}
	var readerHTML = '<object id="biddingDocumentsReader" classid="clsid:D0A93F0E-6BE2-4068-A770-D811DC3FE155" width="100%" height="500" align="center" hspace="0" vspace="0"></object>'
	document.getElementById('divReader').innerHTML = readerHTML;
}
function openGef() {
	if(!document.getElementById('biddingDocumentsReader')) {
		return;
	}
	document.getElementById('biddingDocumentsReader').OpenFile(biddingDocumentFileName);
}
</script>