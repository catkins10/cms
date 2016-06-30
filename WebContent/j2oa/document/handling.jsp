<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%
	String fieldName = request.getParameter("handlingFieldName");
	if(fieldName==null) {
		fieldName = "handling";
	}
%>
<div style="margin-bottom: 3px">
	<input value="打印办理单" onclick="printHandling(document.getElementById('divHandling').getElementsByTagName('iframe')[0].contentWindow)" type="button" class="button" style="width:72px">
</div>
<div id="divHandling">
	<ext:field property="<%=fieldName%>" readonly="true" style="height:430px;"/>
</div>
<script>
	FormUtils.getField(document, '<%=fieldName%>').value = "";
	function printHandling(win) { //打印办理单
		var doc = win.document;
		doc.body.focus();
		var css = doc.styleSheets["handlingStyle"];
		if(css) {
			win.print();
			return;
		}
		//创建style
	    style = doc.createElement("style");
	    style.type = "text/css";
	    style.id = "handlingStyle";
	    DomUtils.getHead(doc).appendChild(style);
	    css = doc.styleSheets[style.id];
		var cssIndex = 0;
		DomUtils.traversalChildElements(doc.body, function(element) { //遍历页面元素,callback=function(element),返回true时不遍历子元素
			if(element.id!="opinionArea") {
				return false;
			}
			var normalHeight = CssUtils.getElementComputedStyle(element, 'height');
			if(!normalHeight || normalHeight=="") {
				return true;
			}
			normalHeight = Number(normalHeight.replace("px", ""));
			if(normalHeight >= element.offsetHeight) { //预设的高度大等于实际高度
				return true;
			}
			//修改字体大小, 以保存预置的高度
			element.id = ("" + Math.random()).replace(/\./g, "");
		    var cssName = "#" + element.id + " *";
		    for(var fontSize = 14; fontSize>=11; fontSize--) {
			    var cssText = "font-size: " + fontSize + "px;  line-height: " + (fontSize + 2) + "px;";
			    if(css.cssRules) { //firefox
					css.insertRule(cssName + " {" + cssText + "}", cssIndex);
				}
				else {
					css.addRule(cssName, cssText);
				}
				if(fontSize==11 || normalHeight >= element.offsetHeight) { //调整后预设的高度大等于实际高度
					break;
				}
				if(css.cssRules) { //firefox
					css.deleteRule(cssIndex);
				}
				else {
					css.removeRule(cssIndex);
				}
			}
			cssIndex++;
			return true;
		});
		win.print();
	}
</script>