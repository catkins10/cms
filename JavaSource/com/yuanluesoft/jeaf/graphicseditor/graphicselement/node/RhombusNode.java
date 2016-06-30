/*
 * Created on 2004-10-14
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.graphicselement.node;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * 
 * @author linchuan
 *
 */
public class RhombusNode extends Node {
		
	public void draw(Graphics g) {
		int x=Math.min(startX,endX);
		int y=Math.min(startY,endY);
		int width=Math.abs(endX-startX);
		int height=Math.abs(endY-startY);
		int[] xPoints={x,x+width/2,x+width,x+width/2};
		int[] yPoints={y+height/2,y+height,y+height/2,y};
		x=xPoints[0];
		y=yPoints[3];
		Polygon polygon=new Polygon(xPoints,yPoints,4);
		g.setColor(getFillColor()==null ? Color.WHITE : getFillColor());
		g.fillPolygon(polygon);
		g.setColor(getLineColor());
		g.drawPolygon(polygon);
		g.setColor(getTextColor());
		drawString(g,title,x,y,width,height);
	}
}