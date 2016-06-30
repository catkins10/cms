/*
 * Created on 2004-10-9
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.graphicselement.node;

import java.awt.Color;
import java.awt.Graphics;

import com.yuanluesoft.jeaf.graphicseditor.graphicselement.Element;

/**
 * 
 * @author linchuan
 *
 */
public class Node extends Element {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.configure.Element#draw(java.awt.Graphics, java.awt.Color, java.awt.Color)
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
		g.setColor(getLineColor());
		g.drawRect(x,y,width,height);
		g.setColor(getFillColor()==null ? Color.WHITE : getFillColor());
		g.fillRect(x+1,y+1,width-1,height-1);
		if(title!=null) {
			g.setColor(getTextColor());
			drawString(g,title,x,y,width,height);
		}
	}
	
	/**
	 * 在元素位置变更时重置元素属性（包括焦点信息、连接点信息等）
	 */
	public void resetDisplayProperties() {
		
	}
}