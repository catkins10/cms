<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>

<ext:form action="/admin/doImportCards">
	充值卡文件：<html:file property="cardsFile"/>
</ext:form>