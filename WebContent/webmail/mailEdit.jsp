<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">收件人</td>
		<td class="tdcontent"><ext:field property="mailTo"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">抄送</td>
		<td class="tdcontent"><ext:field property="mailCc"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">密送</td>
		<td class="tdcontent"><ext:field property="mailBcc"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主题</td>
		<td class="tdcontent"><ext:field property="subject"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">重要等级</td>
		<td class="tdcontent"><ext:field property="priority"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">附件</td>
		<td class="tdcontent"><ext:field property="attachments"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">正文</td>
		<td class="tdcontent"><ext:field property="htmlBody"/></td>
	</tr>
</table>
<html:hidden property="mailboxId"/>
<script>
function afterSelectReceiver(type) {
	var fields = ["mailTo", "mailCc", "mailBcc"];
	var addresses = document.getElementsByName(fields[type])[0].value.split(";");
	var shortAddresses = "|";
	var receivers = "";
	for(var i=0; i<addresses.length; i++) {
		var address = StringUtils.trim(addresses[i]);
		if(address=="") {
			continue;
		}
		var shortAddress = address.split("<");
		if(shortAddress.length==2 && shortAddress[1].length>2) {
			shortAddress = shortAddress[1].substring(0, shortAddress[1].length - 1);
		}
		if(shortAddresses.indexOf("|" + shortAddress + "|")!=-1) {
			continue;
		}
		shortAddresses += shortAddress + "|";
		receivers += (receivers=="" ? "" : "; ") + address;
	}
	document.getElementsByName(fields[type])[0].value = receivers;
}
</script>