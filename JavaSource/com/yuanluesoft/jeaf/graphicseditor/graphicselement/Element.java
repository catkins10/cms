/*
 * Created on 2004-10-8
 *
 */
package com.yuanluesoft.jeaf.graphicseditor.graphicselement;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author linchuan
 *
 */
public abstract class Element {
	public static final int GRIDSIZE = 8; //网格大小
	
	private Color fillColor = null; //填充颜色
	private Color lineColor = null; //边框颜色
	private Color textColor = null; //文本颜色
	//位置和尺寸属性
	protected int startX,startY,endX,endY;
	protected String title; //标题
	//abstract functions
	public abstract void draw(Graphics g); //绘制
	public abstract void resetDisplayProperties(); //在元素位置变更时重置元素显示属性（包括焦点信息、连接点信息等）
	
	/**
	 * 获取字符串输出时的尺寸
	 * @param str
	 * @param g
	 * @return
	 */
	protected Rectangle2D getStringBounds(String str, Graphics g, Font font) {
		FontRenderContext frc = ((Graphics2D)g).getFontRenderContext();
		return font.getStringBounds(str,frc);
	}
	
	/**
	 * 在居中位置显示字符串
	 * @param g
	 * @param str
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void drawString(Graphics g,String str,int x,int y,int width,int height) {
		if(str==null) {
			return;
		}
		Font font = new Font("宋体",Font.PLAIN,12); //字体
		Rectangle2D rc = getStringBounds(str, g, font);
		if(height < rc.getHeight()) {
			return; //高度不足一行
		}
		if(width<=4*GRIDSIZE && height/width>2) { //竖向显示
		    int heightTotal = 0;
		    int i = 0;
		    for(; i<str.length(); i++) {
			    rc = getStringBounds(str.substring(i, i+1), g, font);
			    if(width<rc.getWidth()) {
			        return; //宽度不足一个字
			    }
			    if(heightTotal + rc.getHeight() > height) {
			        break;
			    }
			    heightTotal += rc.getHeight();
			}
			y += (height - heightTotal)/2;
			for(int k=0; k<i; k++) {
			    rc = getStringBounds(str.substring(k, k+1), g, font);
			    g.drawString(str.substring(k, k+1), (int)(x+(width-rc.getWidth())/2)+1, y + (int)rc.getHeight());
			    y += rc.getHeight();
			}
		    return;
		}
		
		if(width-2>rc.getWidth()) {
			g.drawString(title,(int)(x+(width-rc.getWidth())/2),(int)(y+(height-rc.getHeight())/2+rc.getHeight()-1));
			return;
		}
		//分行显示
		List list = new ArrayList();
		String lineStr = "";
		int heightTotal = 0;
		int i = 0;
		for(; i<str.length(); i++) {
		    lineStr += str.substring(i, i+1);
		    rc = getStringBounds(lineStr, g, font);
		    if(heightTotal + rc.getHeight() > height) {
		        lineStr = (String)list.get(list.size()-1);
		        for(int j=1; j<=Math.min(lineStr.length(),3); j++) {
		            String newStr = lineStr.substring(0, lineStr.length()-j) + "...";
		            rc = getStringBounds(newStr, g, font);
		            if(rc.getWidth()<width) {
		                list.set(list.size()-1, newStr);
		                break;
		            }
		        }
		        break;
		    }
		    if(rc.getWidth()>width) {
		        if(lineStr.length()==1) {
		            return; //宽度不足一列
		        }
		        lineStr = lineStr.substring(0, lineStr.length()-1);
		        heightTotal += getStringBounds(lineStr, g, font).getHeight();
		        list.add(lineStr);
		        lineStr = "";
		        i--;
		    }
		}
		if(i>=str.length()) {
		    heightTotal += getStringBounds(lineStr, g, font).getHeight();
		    list.add(lineStr);
		}
		y += (height - heightTotal)/2;
		for(i=0; i<list.size(); i++) {
		    lineStr = (String)list.get(i);
		    rc = getStringBounds(lineStr, g, font);
		    g.drawString(lineStr, (int)(x+(width-rc.getWidth())/2)+1, y + (int)rc.getHeight());
		    y += rc.getHeight();
		}
		
	}
	/**
	 * 获取元素左端坐标
	 * @return
	 */
	public int getLeft() {
		return Math.min(startX,endX);
	}
	/**
	 * 获取元素顶端坐标
	 * @return
	 */
	public int getTop() {
		return Math.min(startY,endY);
	}
	/**
	 * 获取元素右端坐标
	 * @return
	 */
	public int getRight() {
		return Math.max(startX,endX);
	}
	/**
	 * 获取元素底端坐标
	 * @return
	 */
	public int getBottom() {
		return Math.max(startY,endY);
	}
	/**
	 * 获取元素宽度
	 * @return
	 */
	public int getWidth() {
		return Math.abs(startX-endX);
	}
	/**
	 * 获取元素高度
	 * @return
	 */
	public int getHeight() {
		return Math.abs(startY-endY);
	}
	
	/**
	 * @return Returns the fillColor.
	 */
	public Color getFillColor() {
		return fillColor;
	}
	/**
	 * @param fillColor The fillColor to set.
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return Returns the endX.
	 */
	public int getEndX() {
		return endX;
	}
	/**
	 * @param endX The endX to set.
	 */
	public void setEndX(int endX) {
		this.endX = endX;
	}
	/**
	 * @return Returns the endY.
	 */
	public int getEndY() {
		return endY;
	}
	/**
	 * @param endY The endY to set.
	 */
	public void setEndY(int endY) {
		this.endY = endY;
	}
	/**
	 * @return Returns the startX.
	 */
	public int getStartX() {
		return startX;
	}
	/**
	 * @param startX The startX to set.
	 */
	public void setStartX(int startX) {
		this.startX = startX;
	}
	/**
	 * @return Returns the startY.
	 */
	public int getStartY() {
		return startY;
	}
	/**
	 * @param startY The startY to set.
	 */
	public void setStartY(int startY) {
		this.startY = startY;
	}
	/**
	 * @return the lineColor
	 */
	public Color getLineColor() {
		return lineColor;
	}
	/**
	 * @param lineColor the lineColor to set
	 */
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}
	/**
	 * @return the textColor
	 */
	public Color getTextColor() {
		return textColor;
	}
	/**
	 * @param textColor the textColor to set
	 */
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
}