<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">微博帐号</td>
		<td class="tdcontent"><ext:field property="accountIdArray"/></td>
	</tr>
	<ext:equal value="0" property="privateMessageId">
		<tr style="display:none">
			<td class="tdtitle" nowrap="nowrap">发送范围</td>
			<td class="tdcontent"><ext:field property="range" onclick="var tr=document.getElementById('trGroups');tr.style.display=tr.cells[0].style.display=tr.cells[1].style.display=document.getElementsByName('range')[2].checked ? '' : 'none';"/></td>
		</tr>
		<tr id="trGroups" style="<ext:notEqual value="groups" property="range">display:none</ext:notEqual>">
			<td class="tdtitle" nowrap="nowrap">分组</td>
			<td class="tdcontent"><ext:field property="groupNames"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">微博内容</td>
		<td class="tdcontent">
			<ext:field property="content" onchange="validateContentLength(true)" style="font-size: 14px; line-height: 18px;"/>
			<div style="padding: 6px 6px 6px 3px;" id="validateResult"/>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">图片</td>
		<td class="tdcontent"><ext:field property="image"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发布时间</td>
		<td class="tdcontent"><ext:field property="sendTime"/></td>
	</tr>
</table>
<script>
	var shortUrlMaxLength = Number(document.getElementsByName('shortUrlMaxLength')[0].value);
	var shortUrl = '';
	for(var i=0; i<shortUrlMaxLength; i++) {
		shortUrl += ' ';
	}
	function validateContentLength(writeResult) { //校验内容长度,返回还允许输入的字数
		var content = document.getElementsByName('content')[0].value;
		content = content.replace(/(http|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/gi, shortUrl);
		var len = 140;
		for(var i=0; i<content.length; i++) {
			var chr = content.charCodeAt(i);
			len -= chr==13 ? 0 : (chr>255 ? 1 : 0.5);
		}
		len = Math.floor(len);
		if(writeResult) {
			var html = (len>=0 ? '还可以输入' : '已经超过') +
					   '<span style="font-family:Constantia, Georgia; font-size:22px; font-style:italic; font-weight: 800;' + (len<0 ? 'color:red;' : '') + '">' + Math.abs(len) + '</span>' +
					   '个字';
			document.getElementById('validateResult').innerHTML = html;
		}
		return len;
	}
	function  formOnSubmit() {
		var len = validateContentLength(false);
		if(len<0) {
			alert('内容已经超过' + Math.abs(len) + '个字');
		}
		return len>=0;
	}
	validateContentLength(true);
</script>