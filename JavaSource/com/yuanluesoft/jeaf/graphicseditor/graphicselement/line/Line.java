/*
 * Created on 2004-10-10
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.graphicselement.line;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import com.yuanluesoft.jeaf.graphicseditor.graphicselement.Element;

/**
 * 
 * @author linchuan
 *
 */
public class Line extends Element {
	protected final int ARROWWIDTH = 8;
	protected final int ARROWCOVE = 6;
	protected final int TEXTDISTANCE = 3;
	protected final double TEXTPOSITION = 10;
	protected final double ARROWRADIAN = 0.45;
	protected int xArrow1,yArrow1,xArrow2,yArrow2,xArrow3,yArrow3;
	protected int xTitle,yTitle;
	private boolean drawArrow = true; //是否绘制箭头

	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.configure.Element#resetProperties()
	 */
	public void resetDisplayProperties() {
		//设置箭头信息
		setArrow(startX, startY, endX, endY);
	}
	
	/**
	 * 设置箭头信息
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 */
	protected void setArrow(int startX,int startY,int endX,int endY) {
		double radian;
		if(startX==endX) {
			radian = (startY>endY ? 1.5:0.5) * Math.PI;
		}
		else {
			radian=Math.atan2(endY-startY,endX-startX);
		}
		xArrow2=(int)Math.round(endX-ARROWCOVE*Math.cos(radian));
		yArrow2=(int)Math.round(endY-ARROWCOVE*Math.sin(radian));
		
		radian-=ARROWRADIAN;
		xArrow1=(int)(endX-ARROWWIDTH*Math.cos(radian));
		yArrow1=(int)(endY-ARROWWIDTH*Math.sin(radian));
		
		radian=Math.PI/2 - radian - 2*ARROWRADIAN;
		xArrow3=(int)(endX-ARROWWIDTH*Math.sin(radian));
		yArrow3=(int)(endY-ARROWWIDTH*Math.cos(radian));
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.configure.Element#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
		int[] xPoints={startX,endX,xArrow1,xArrow2,xArrow3};
		int[] yPoints={startY,endY,yArrow1,yArrow2,yArrow3};
		g.setColor(getLineColor());
		g.drawLine(xPoints[0],yPoints[0],xPoints[1],yPoints[1]);
		//显示箭头
		drawArrow(g,xPoints[1],yPoints[1],xPoints[2],yPoints[2],xPoints[3],yPoints[3],xPoints[4],yPoints[4]);
		//显示标题
		drawTitle(g);
	}
	
	/**
	 * 显示标题
	 * @param g
	 */
	protected void drawTitle(Graphics g) {
		setTitlePosition(g); //设置显示位置
		if(title!=null && xTitle!=-1) {
			int[] xPoints={xTitle};
			int[] yPoints={yTitle};
			g.setColor(getTextColor());
			g.drawString(title,xPoints[0],yPoints[0]);
		}
	}
	
	/**
	 * 显示箭头
	 * @param g
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param x3
	 * @param y3
	 * @param x4
	 * @param y4
	 */
	protected void drawArrow(Graphics g,int startX,int startY,int endX,int endY,int x3,int y3,int x4,int y4) {
	    if(drawArrow) {
	    	g.setColor(getLineColor());
			int[] xPolygon={startX,endX,x3,x4};
			int[] yPolygon={startY,endY,y3,y4};
			g.drawPolygon(xPolygon,yPolygon,4);
			g.fillPolygon(xPolygon,yPolygon,4);
	    }
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * 设置标题的显示位置
	 */
	protected void setTitlePosition(Graphics g) {
		if(title==null) {
			return;
		}
		Font font = new Font("宋体",Font.PLAIN,12); //字体
		Rectangle2D rect = getStringBounds(title, g, font);
		double width=rect.getWidth()/2;
		double height=rect.getHeight()/2;
		double radian;
		if(startX==endX) {
			xTitle=startX+TEXTDISTANCE;
			if(TEXTPOSITION>=1) {
				yTitle=(int)(startY+(endY>startY ? TEXTPOSITION+rect.getHeight() : -TEXTPOSITION));
			}
			else {
				yTitle=startY+(int)((endY-startY)*TEXTPOSITION+height);
			}
			return;
		}
		radian = Math.atan2(startY-endY,endX-startX);
		xTitle=-1;
		double distance;
		if(TEXTPOSITION>=1) {
			if(Math.abs(Math.sin(radian))>0.88) {
				distance = (width + height/Math.abs(Math.tan(radian)))*Math.abs(Math.cos(radian)) + height/Math.abs(Math.sin(radian)) + TEXTPOSITION;
			}
			else {
				distance = width*Math.abs(Math.cos(radian)) + TEXTPOSITION;
			}
		}
		else {
			distance = (int)(Math.sqrt(Math.pow(startX-endX,2)+Math.pow(startY-endY,2))*TEXTPOSITION);
		}
		xTitle=startX+(int)(distance*Math.cos(radian));
		yTitle=startY-(int)(distance*Math.sin(radian));
		if(radian<=0.8 && radian>-2.5) {
			distance=((width+TEXTDISTANCE)*Math.abs(Math.tan(radian))+height+TEXTDISTANCE)*Math.abs(Math.cos(radian));
			xTitle=xTitle-(int)(distance*Math.sin(radian)+width);
			yTitle=yTitle-(int)(distance*Math.cos(radian)-height);
			if(radian<-0.5*Math.PI) {
				yTitle-=TEXTDISTANCE;
			}
		}
		else {
			distance=((width+TEXTDISTANCE)*Math.abs(Math.tan(radian))+height+TEXTDISTANCE)*Math.abs(Math.cos(radian));
			xTitle=xTitle+(int)(distance*Math.sin(radian)-width);
			yTitle=yTitle+(int)(distance*Math.cos(radian)+height);
			if(radian<0.5*Math.PI) {
				yTitle-=TEXTDISTANCE;
			}
		}
	}
    /**
     * @return Returns the drawArrow.
     */
    public boolean isDrawArrow() {
        return drawArrow;
    }
    /**
     * @param drawArrow The drawArrow to set.
     */
    public void setDrawArrow(boolean drawArrow) {
        this.drawArrow = drawArrow;
    }
}