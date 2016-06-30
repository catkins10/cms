GraphicsEditor.DrawBar = function(graphicsEditor, drawBarContainer, drawBarButtons, disabled) {
	this.graphicsEditor = graphicsEditor;
	this.disabled = disabled;
	var drawBar = this;
	for(var i=0; i<drawBarButtons.length; i++) {
		var div = drawBarContainer.ownerDocument.createElement("div");
		div.className = "drawAction";
		div.elementType = drawBarButtons[i].elementType;
		div.title = drawBarButtons[i].name;
		div.onclick = function() {
			if(drawBar.disabled) {
				return;
			}
			if(this.className=="drawAction actionSelect") {
				return;
			}
			drawBar.cleanSelect();
			this.className = "drawAction actionSelect";
			drawBar.graphicsEditor.setToCreateElementType(this.elementType);
			drawBar.selectedButton = this;
		};
		div.onmouseover = function() {
			if(!drawBar.disabled && this.className!="drawAction actionSelect") {
				this.className = "drawAction actionOver";
			}
		};
		div.onmouseout = function() {
			if(!drawBar.disabled && this.className!="drawAction actionSelect") {
				this.className = "drawAction";
			}
		};
		var img = drawBarContainer.ownerDocument.createElement('img');
		img.src = RequestUtils.getContextPath() + drawBarButtons[i].iconUrl;
		img.alt = drawBarButtons[i].name;
		div.appendChild(img);
		if(drawBar.disabled) {
			CssUtils.setGray(img);
		}
		drawBarContainer.appendChild(div);
	}
};
GraphicsEditor.DrawBar.prototype.cleanSelect = function() {
	if(this.selectedButton) {
		this.selectedButton.className = "drawAction";
		this.selectedButton = null;
	}
};