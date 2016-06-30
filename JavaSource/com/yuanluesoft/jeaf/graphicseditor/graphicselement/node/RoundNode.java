/*
 * Created on 2004-10-14
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.graphicselement.node;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 * @author linchuan
 *
 */
public class RoundNode extends Node {
	private int lineWeight = 1;
	
	public void draw(Graphics g) {
		int x=Math.min(startX,endX);
		int y=Math.min(startY,endY);
		int width=Math.abs(endX-startX);
		int height=Math.abs(endY-startY);
		int[] xPoints={x,x+width};
		int[] yPoints={y,y+height};
		x=xPoints[0];
		y=yPoints[0];
		g.setColor(getFillColor()==null ? Color.WHITE : getFillColor());
		g.fillArc(x,y,width,height,0,360);
		g.setColor(getLineColor());
		for(int i=0; i<lineWeight; i++) {
			g.drawArc(x+i, y+i, width-2*i ,height-2*i, 0, 360);
		}
		g.setColor(getTextColor());
		drawString(g,title,x,y,width,height);
	}
	
	/**
	 * @return Returns the lineWeight.
	 */
	public int getLineWeight() {
		return lineWeight;
	}
	/**
	 * @param lineWeight The lineWeight to set.
	 */
	public void setLineWeight(int lineWeight) {
		this.lineWeight = lineWeight;
	}
}
