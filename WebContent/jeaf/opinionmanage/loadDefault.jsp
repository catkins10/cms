<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/doLoadDefault">
	<html:hidden property="businessClassName"/>
	<div style="padding-top: 3px">　加载预设意见类型会删除当前配置的意见类型，是否继续加载？</div>
</ext:form>