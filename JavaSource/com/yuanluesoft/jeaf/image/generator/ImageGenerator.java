package com.yuanluesoft.jeaf.image.generator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletResponse;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;

/**
 * 图像生成器
 * @author linchuan
 *
 */
public abstract class ImageGenerator {
	
	/**
	 * 获取能处理的最大像素,以百万像素为单位
	 * @return
	 */
	public abstract int getMaxMegaPixels();
	
	/**
	 * 图片转换,先按resizeWidth、resizeHeight调整尺寸,再按cropWidth、cropHeight、cropLeft、cropTop剪裁
	 * @param sourceFilePath 源文件路径
	 * @param targetFilePath 新文件路径
	 * @param resizeWidth 调整后的宽度,-1不调整
	 * @param resizeHeight 调整后的高度,-1不调整
	 * @param cropWidth 剪裁后的宽度,-1不剪裁
	 * @param cropHeight 剪裁后的高度,-1不剪裁
	 * @param cropLeft 剪裁左边距
	 * @param cropTop 剪裁上边距
	 * @throws ServiceException
	 */
	public abstract void convertImage(String sourceFilePath, String targetFilePath, int resizeWidth, int resizeHeight, int cropWidth, int cropHeight, int cropLeft, int cropTop) throws ServiceException;
	
	/**
	 * 给图片加水印
	 * @param sourceImageFileName
	 * @param targetImageFileName
	 * @param pressImageFileName
	 * @param x
	 * @param y
	 * @throws ServiceException
	 */
	public abstract void pressImage(String sourceImageFileName, String targetImageFileName, String pressImageFileName, int x, int y) throws ServiceException;
	
	/**
	 * 获取图片尺寸
	 * @param imageFileName
	 * @return
	 */
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.service.ImageGenerator#getDimension(java.lang.String)
	 */
	public Dimension getDimension(String imageFileName) throws ServiceException {
		File imageFile = new File(imageFileName);
		if(!imageFile.exists()) {
			return null;
		}
		Dimension dimension = null;
		ImageInputStream imageInputStream = null;
		try {
			imageInputStream = ImageIO.createImageInputStream(imageFile); 
			Iterator imageReaders = ImageIO.getImageReadersByFormatName(imageFileName.substring(imageFileName.lastIndexOf('.') + 1)); 
			ImageReader imageReader = (ImageReader)imageReaders.next(); 
			imageReader.setInput(imageInputStream);
			dimension = new Dimension(imageReader.getWidth(0), imageReader.getHeight(0));
			imageReader.dispose(); 
		}
		catch(Exception e) {
			//Logger.exception(e);
		}
		finally {
			try {
				imageInputStream.close(); 
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		return dimension;
	}
	
	/**
	 * 生成文本图片
	 * @param text
	 * @param response
	 * @throws ServiceException
	 */
	public void writeValidateCodeImage(String text, int fontSize, HttpServletResponse response) throws ServiceException {
        //在内存中创建图象
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics2D g = (Graphics2D)image.getGraphics();
        
        //获取字体宽度,高度
        Font font = new Font("Tahoma", Font.PLAIN, fontSize);
        FontRenderContext frc = g.getFontRenderContext();
        double fontWidth = font.getStringBounds("D", frc).getWidth();
        double fontHeight = font.getStringBounds(text, frc).getHeight();
        
        //计算图片尺寸
        int length = text.length();
        int imageWidth = (int)(fontWidth * 1.2 * length + fontWidth);
        int imageHeight = (int)(fontHeight * 1.2);

        //根据新尺寸创建图片
        image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        //获取图形上下文
        g = (Graphics2D)image.getGraphics();
        g.setFont(font); //字体
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //消除锯齿
        
        //设定背景色
        g.setColor(new Color(240, 243, 248));
        g.fillRect(0, 0, imageWidth, imageHeight);
        
        //画边框
        g.setColor(new Color(228, 238, 249));
        g.drawRect(0, 0, imageWidth-1, imageHeight-1);

        Random random = new Random();
        // 随机产生29条干扰线，使图象中的认证码不易被其它程序探测到
        /*g.setColor(new Color(80, 120 + random.nextInt(30), 160 + random.nextInt(50)));
        for (int i=0; i<29; i++) {
            int x = random.nextInt(imageWidth);
            int y = random.nextInt(imageHeight);
            int xl = random.nextInt(3);
            int yl = random.nextInt(3);
            g.drawLine(x, y, x+xl, y+yl);
        }*/
        
        //输出干扰Bézier曲线
        g.setColor(new Color(random.nextInt(158), random.nextInt(158), random.nextInt(158))); //颜色
        BasicStroke stroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f); //设置线条粗细   
        g.setStroke(stroke);   
        Path2D curve = new Path2D.Double();
        double y = imageHeight * 0.75;
        double step = (imageWidth - 5) / 3.0;
        for(double x=2; x<imageWidth-3; x+=step) {
        	curve.moveTo(x, y); 
        	int rand = random.nextInt(imageHeight/2);
            curve.curveTo(x + 0.33 * step, y + rand, x + 0.67 * step, y-rand, x + step, y);   
	    }
        g.draw(curve);
        
        //输出文本
        g.translate(-fontWidth*0.2, fontHeight * 0.8);
        //g.setColor(new Color(random.nextInt(128), random.nextInt(128), random.nextInt(128)));
    	for(int i=0; i<text.length(); i++) {
            int k = random.nextInt(16);
        	g.translate(fontWidth, 0);
        	g.rotate(k * 0.05); //旋转文本
        	g.drawString(text.substring(i, i+1), 0, 0);
        	g.rotate(-k * 0.05);
        }
    	// 图象生效
        g.dispose();
        
        // 输出图象到页面
		response.setContentType("image/jpeg");
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires", 0);
        
        try {
        	JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
    		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
    		param.setQuality(1.0f, false); //0.885f, true);
    		encoder.encode(image, param);
		}
        catch (IOException e) {
			if(e.getMessage().indexOf("Connection reset by peer")==-1) { //客户端连接被关闭
				throw new ServiceException(e);
			}
		}
    }
	
	/**
	 * 生成文本图片,JPG格式
	 * @param text
	 * @param widht
	 * @param height
	 * @param fontName
	 * @param color
	 * @param bold
	 * @param imagePath
	 * @throws ServiceException
	 */
	public void generateTextImage(String text, int imageWidth, int imageHeight, String fontName, boolean bold, boolean italic, String textColor, String backgroundColor, String imagePath) throws ServiceException {
		 //在内存中创建图象
		Graphics2D g;
		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        if(backgroundColor!=null && !backgroundColor.isEmpty()) {
        	g = (Graphics2D)image.getGraphics();
        }
        else {
        	//获取Graphics2D
    		g = image.createGraphics();
    		//使得背景透明
    		image = g.getDeviceConfiguration().createCompatibleImage(imageWidth, imageHeight, Transparency.TRANSLUCENT);
    		g.dispose();
    		g = image.createGraphics();
        }
        // 获取图形上下文
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //消除锯齿
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY); //控制颜色着色的方式
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE); //控制如何处理抖动
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON); //控制显示文本的质量
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); //确定着色技术，在速度和质量之间进行权衡
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); //确定对文本着色时是否抗锯齿
        
        //获取字体宽度,高度
        Font font;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File(Environment.getWebinfPath() + "jeaf/fonts/" + fontName + ".ttf"));
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage());
		}
		font = font.deriveFont((bold ? Font.BOLD : 0) + (italic ? Font.ITALIC : 0), imageHeight * 1.0f);
		//获取使用当前字体时，文本的高度和宽度
		FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D textBounds = font.getStringBounds(text, frc);
        
        //根据宽度和高度自动缩放
        double widthScale = (imageWidth-8.0)/textBounds.getWidth(); //横向缩放比例
        double heightScale = imageHeight/textBounds.getHeight(); //纵向缩放比例
        int flag = 0;
        for(;;) {
	        textBounds = font.deriveFont(AffineTransform.getScaleInstance(widthScale, heightScale)).getStringBounds(text, frc);
	        //System.out.println(imageHeight + "," + widthScale + ":" + textBounds);
	        if(textBounds.getWidth()==imageWidth-8) {
	        	break;
	        }
	        else if(textBounds.getWidth()<imageWidth-8) {
	        	if(flag==0) {
	        		flag = 1;
	        	}
	        	else if(flag==-1) { //原来是在缩小
	        		break;
	        	}
	        	widthScale += 0.001; //放大
	        }
	        else {
	        	widthScale -= 0.001; //缩小
	        	if(flag==0) {
	        		flag = -1;
	        	}
	        	else if(flag==1) { //原来是在放大
	        		break;
	        	}
	        }
        }
        //设置最后的缩放比例
        font = font.deriveFont(AffineTransform.getScaleInstance(widthScale, heightScale));
        //textBounds = font.getStringBounds(text, frc);
        
        g.setFont(font); //设置字体
	    //设定背景色
        if(backgroundColor!=null && !backgroundColor.isEmpty()) {
	        g.setColor(backgroundColor.equals("#") ? Color.WHITE : new Color(Integer.parseInt(backgroundColor.replace("#", ""), 16)));
	        g.fillRect(0, 0, imageWidth, imageHeight);
        }
        //输出文本
        g.setColor(textColor==null || textColor.isEmpty() || textColor.equals("#") ? Color.BLUE : new Color(Integer.parseInt(textColor.replace("#", ""), 16)));
        g.drawString(text, 4.0f, (float)(imageHeight - (textBounds.getHeight() + textBounds.getY()) * heightScale));
    	//使图象生效
        g.dispose();
        
        //输出图象到文件
        FileOutputStream out = null;
        try {
        	out = new FileOutputStream(imagePath);
        	ImageIO.write(image, "png", out);
		}
        catch (IOException e) {
			if(e.getMessage().indexOf("Connection reset by peer")==-1) { //客户端连接被关闭
				Logger.exception(e);
				throw new ServiceException();
			}
		}
        finally {
        	try {
        		out.close();
        	}
        	catch(Exception e) {
        		
        	}
        }
	}
}