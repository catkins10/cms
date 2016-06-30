/*
 * Created on 2004-10-15
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
public class ArcRectNode extends Node {

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
		g.fillRoundRect(x,y,width,height,height,height);
		g.setColor(getLineColor());
		g.drawRoundRect(x,y,width,height,height,height);
		g.setColor(getTextColor());
		drawString(g,title,x,y,width,height);
	}
}
