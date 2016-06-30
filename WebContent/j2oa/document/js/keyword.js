function parseKeyword(toParse, keywordFieldName) { //向服务器请求解析主题词
	ScriptUtils.appendJsFile(document, RequestUtils.getContextPath() + "/j2oa/document/parseKeyword.shtml?fieldName=" + keywordFieldName + "&text=" + StringUtils.utf8Encode(toParse) + "&seq=" + Math.random(), "scriptParseKeyword");
}