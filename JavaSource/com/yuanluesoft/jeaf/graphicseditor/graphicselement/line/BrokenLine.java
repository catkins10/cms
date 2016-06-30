/*
 * Created on 2004-10-12
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.jeaf.graphicseditor.graphicselement.line;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LinChuan
 * 
 */
public class BrokenLine extends Line {
	protected List turningXPoints = new ArrayList(); //转折点X坐标
	protected List turningYPoints = new ArrayList(); //转折点Y坐标
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.configure.Element#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
		int size = turningXPoints.size();
		int[] xPoints=new int[size+5];
		int[] yPoints=new int[size+5];
		//起点
		xPoints[0]=startX;
		yPoints[0]=startY;
		//转折点
		int i;
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
		g.setColor(getLineColor());
		g.drawPolyline(xPoints,yPoints,size+2);
		//显示箭头
		drawArrow(g,xPoints[size+1],yPoints[size+1],xPoints[size+2],yPoints[size+2],xPoints[size+3],yPoints[size+3],xPoints[size+4],yPoints[size+4]);
		//显示标题
		drawTitle(g);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.graphicseditor.graphicselement.line.Line#resetDisplayProperties()
	 */
	public void resetDisplayProperties() {
		setArrow(((Number)turningXPoints.get(turningXPoints.size() - 1)).intValue(), ((Number)turningYPoints.get(turningYPoints.size() - 1)).intValue(), endX, endY);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.graphicseditor.graphicselement.line.Line#setTitlePosition(java.awt.Graphics)
	 */
	protected void setTitlePosition(Graphics g) {
		if(title==null) {
			return;
		}
		Font font = new Font("宋体",Font.PLAIN,12); //字体
		Rectangle2D rect = getStringBounds(title, g, font);
		int width=(int)rect.getWidth();
		int height=(int)rect.getHeight();
		xTitle=-1;
		int size = turningXPoints.size();
		if(size==0) {
			return;
		}
		int endX = ((Number)turningXPoints.get(0)).intValue();
		int endY = ((Number)turningYPoints.get(0)).intValue();
		int x3,y3,x4,y4;
		if(size==1) {
			x3=this.endX;
			y3=this.endY;
			x4=x3;
			y4=y3;
		}
		else {
			x3 = ((Number)turningXPoints.get(1)).intValue();
			y3 = ((Number)turningYPoints.get(1)).intValue();
			if(size==2) {
				x4=this.endX;
				y4=this.endY;
			}
			else {
				x4 = ((Number)turningXPoints.get(2)).intValue();
				y4 = ((Number)turningYPoints.get(2)).intValue();
			}
		}
		if(startX==endX) {
			xTitle=startX+TEXTDISTANCE;
			if(TEXTPOSITION<1 && Math.abs(endY-startY)<height) {
				yTitle=startY+(startY>endY ? -TEXTDISTANCE:TEXTDISTANCE+height);
			}
			else {
				if(TEXTPOSITION>=1) {
					yTitle=startY+(int)(endY>startY ? TEXTPOSITION+height:-TEXTPOSITION);
				}
				else {
					yTitle=startY+(int)((endY-startY)*TEXTPOSITION+height/2);
				}
			}
			if(x3>startX) {
				if(Math.abs(endY-startY)<height+(TEXTPOSITION<1 ? 0:TEXTPOSITION)+TEXTDISTANCE) {
					xTitle=startX-TEXTDISTANCE-width;
				}
				else if(x3<xTitle+width) {
					if( (startY<=endY && y4<yTitle) ||
						(startY>endY && y4>yTitle-height) ) {
						xTitle=startX-TEXTDISTANCE-width;
					}
				}
			}
		}
		else {
			yTitle=startY-TEXTDISTANCE;
			if(TEXTPOSITION<1 && Math.abs(endX-startX)<width) {
				xTitle=startX+(startX>endX ? -TEXTDISTANCE-width:TEXTDISTANCE);
			}
			else {
				if(TEXTPOSITION>=1) {
					xTitle=startX+(int)(endX>startX ? TEXTPOSITION:-TEXTPOSITION-width);
				}
				else {
					xTitle=startX+(int)((endX-startX)*TEXTPOSITION-width/2);
				}
			}
			if(y3<startY) {
				if(Math.abs(endX-startX)<width+(TEXTPOSITION<1 ? 0:TEXTPOSITION)+TEXTDISTANCE) {
					yTitle=startY+TEXTDISTANCE+height;
				}
				else if(y3>yTitle-height) {
					if( (startX<=endX && x4<xTitle+width) ||
						(startX>endX && x4>xTitle+width) ) {
						yTitle=startY+TEXTDISTANCE+height;
					}
				}
			}
		}
	}

	/**
	 * @return the turningXPoints
	 */
	public List getTurningXPoints() {
		return turningXPoints;
	}

	/**
	 * @param turningXPoints the turningXPoints to set
	 */
	public void setTurningXPoints(List turningXPoints) {
		this.turningXPoints = turningXPoints;
	}

	/**
	 * @return the turningYPoints
	 */
	public List getTurningYPoints() {
		return turningYPoints;
	}

	/**
	 * @param turningYPoints the turningYPoints to set
	 */
	public void setTurningYPoints(List turningYPoints) {
		this.turningYPoints = turningYPoints;
	}
}