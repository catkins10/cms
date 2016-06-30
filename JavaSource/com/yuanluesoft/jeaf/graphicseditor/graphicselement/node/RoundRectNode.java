/*
 * Created on 2004-10-14
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.graphicselement.node;

import java.awt.Graphics;

/**
 * 
 * @author linchuan
 *
 */
public class RoundRectNode extends Node {
	private int arcWidth = 10;
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.graphicseditor.graphicselement.node.Node#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
	    int x=Math.min(startX,endX);
		int y=Math.min(startY,endY);
		int width=Math.abs(endX-startX);
		int height=Math.abs(endY-startY);
		int[] xPoints={x,x+width};
		int[] yPoints={y,y+height};
		x=xPoints[0];
		y=yPoints[0];
		g.setColor(getFillColor());
		g.fillRoundRect(x,y,width,height,arcWidth,arcWidth);
		g.setColor(getLineColor());
		g.drawRoundRect(x,y,width,height,arcWidth,arcWidth);
		g.setColor(getTextColor());
		drawString(g,title,x,y,width,height);
	}
	
	/**
	 * @return Returns the arcWidth.
	 */
	public int getArcWidth() {
		return arcWidth;
	}
	/**
	 * @param arcWidth The arcWidth to set.
	 */
	public void setArcWidth(int arcWidth) {
		this.arcWidth = arcWidth;
	}
}
