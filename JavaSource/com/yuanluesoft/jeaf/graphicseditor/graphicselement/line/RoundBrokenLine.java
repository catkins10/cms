/*
 * Created on 2004-10-15
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.graphicselement.line;

import java.awt.Graphics;
import java.awt.Point;

/**
 * 
 * @author linchuan
 *
 */
public class RoundBrokenLine extends BrokenLine {
	protected int radii = 3;
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.configure.Element#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
		int size = turningXPoints.size();
		int[] xPoints = new int[size+5];
		int[] yPoints = new int[size+5];
		//起点
		xPoints[0]=startX;
		yPoints[0]=startY;
		//转折点
		int i;
		Point point;
		for(i=1; i<=size; i++) {
			xPoints[i] = ((Number)turningXPoints.get(i-1)).intValue();
			yPoints[i] = ((Number)turningYPoints.get(i-1)).intValue();
		}
		//终点
		xPoints[i]=endX;
		yPoints[i]=endY;
		//箭头
		xPoints[++i]=xArrow1;
		yPoints[i]=yArrow1;
		xPoints[++i]=xArrow2;
		yPoints[i]=yArrow2;
		xPoints[++i]=xArrow3;
		yPoints[i]=yArrow3;
		//画线
		g.setColor(getLineColor());
		int x=xPoints[0],y=yPoints[0];
		for(i=1;i<=size;i++) {
			point=drawAngle(g,x,y,xPoints[i],yPoints[i],xPoints[i+1],yPoints[i+1]);
			x=point.x;
			y=point.y;
		}
		g.drawLine(x,y,xPoints[size+1],yPoints[size+1]);
		//显示箭头
		drawArrow(g,xPoints[size+1],yPoints[size+1],xPoints[size+2],yPoints[size+2],xPoints[size+3],yPoints[size+3],xPoints[size+4],yPoints[size+4]);
		//显示标题
		drawTitle(g);
	}
	
	/**
	 * 绘制拐角
	 * @param g
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param x3
	 * @param y3
	 * @return
	 */
	protected Point drawAngle(Graphics g,int startX,int startY,int endX,int endY,int x3,int y3) {
		if((startX==endX && endX==x3) ||
		   (startY==endY && endY==y3) ||
		   (startX==endX && startY==endY) ||
		   (endX==x3 && endY==y3)) {
			g.drawLine(startX,startY,endX,endY);
			return new Point(endX,endY);
		}
			
		int x,y;
		int angle;
		int radii=this.radii;
		if(startX==endX) {
			if(Math.abs(endY-startY)<radii) {
				radii=Math.abs(endY-startY);
			}
			if(Math.abs((x3-endX)/2)<radii) {
				radii=Math.abs((x3-endX)/2);
			}
			if(endY>startY) {
				y=endY-radii*2;
				if(x3>endX) {
					x=endX;
					angle=3;
				}
				else {
					x=endX-radii*2;
					angle=4;
				}
			}
			else {
				y=endY;
				if(x3>endX) {
					x=endX;
					angle=2;
				}
				else {
					x=endX-radii*2;
					angle=1;
				}
			}
			g.drawLine(startX,startY,startX,endY>startY ? endY-radii:endY+radii);
			g.drawArc(x,y,radii*2,radii*2,(angle-1)*90,90);
			return new Point((x3>endX ? endX+radii:endX-radii),endY);
		}
		else {
			if(Math.abs(endX-startX)<radii) {
				radii=Math.abs(endX-startX);
			}
			if(Math.abs((y3-endY)/2)<radii) {
				radii=Math.abs((y3-endY)/2);
			}
			if(endX>startX) {
				x=endX-radii*2;
				if(y3>endY) {
					y=endY;
					angle=1;
				}
				else {
					y=endY-radii*2;
					angle=4;
				}
			}
			else {
				x=endX;
				if(y3>endY) {
					y=endY;
					angle=2;
				}
				else {
					y=endY-radii*2;
					angle=3;
				}
			}
			g.drawLine(startX,startY,endX>startX ? endX-radii:endX+radii,endY);
			g.drawArc(x,y,radii*2,radii*2,(angle-1)*90,90);
			return new Point(endX,(y3>endY ? endY+radii:endY-radii));
		}
	}
}
