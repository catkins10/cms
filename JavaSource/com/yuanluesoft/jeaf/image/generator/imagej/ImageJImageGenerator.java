package com.yuanluesoft.jeaf.image.generator.imagej;

import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageProcessor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.image.generator.ImageGenerator;
import com.yuanluesoft.jeaf.logger.Logger;

/**
 * 
 * @author linchuan
 *
 */
public class ImageJImageGenerator extends ImageGenerator {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.generator.ImageGenerator#getMaxMegaPixels()
	 */
	public int getMaxMegaPixels() {
		return 8; //最大8百万像素
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.generator.ImageGenerator#convertImage(java.lang.String, java.lang.String, int, int, int, int, int, int)
	 */
	public void convertImage(String sourceFilePath, String targetFilePath, int resizeWidth, int resizeHeight, int cropWidth, int cropHeight, int cropLeft, int cropTop) throws ServiceException {
		ImagePlus imagePlus = null;
		Graphics graphics = null;
		try {
			Opener opener = new Opener();
			imagePlus = opener.openImage(sourceFilePath);
			if(imagePlus==null) {
				return;
			}
			imagePlus.close();
			ImageProcessor imageProcessor = imagePlus.getProcessor();
			if(resizeWidth>0 && resizeHeight>0) {
				imageProcessor = imageProcessor.resize(resizeWidth, resizeHeight);
			}
			int width = cropWidth<=0 ? resizeWidth : cropWidth;
			int height = cropHeight<=0 ? resizeHeight : cropHeight;
			if(width<=0 || height<=0) {
				Dimension dimension = getDimension(sourceFilePath);
				width = width<=0 ? (int)dimension.getWidth() : width;
				height = height<=0 ? (int)dimension.getHeight() : height;
			}
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			graphics = bufferedImage.createGraphics();
			graphics.drawImage(imageProcessor.createImage(), -cropLeft, -cropTop, null);
			
			//输出文件
			saveImage(targetFilePath, bufferedImage);
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
		finally {
			try {
				graphics.dispose();
			}
			catch (Exception e) {
				
			}
			try {
				imagePlus.close();
			}
			catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 给图片加水印
	 * @param sourceImageFileName
	 * @param targetImageFileName
	 * @param pressImageFileName
	 * @param x
	 * @param y
	 * @throws ServiceException
	 */
	public void pressImage(String sourceImageFileName, String targetImageFileName, String pressImageFileName, int x, int y) throws ServiceException {
		try {
			Image sourceImage = ImageIO.read(new File(sourceImageFileName));
			int width = sourceImage.getWidth(null);
			int height = sourceImage.getHeight(null);
			BufferedImage bufferedImage =  new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics graphics = bufferedImage.createGraphics();
			graphics.drawImage(sourceImage, 0 , 0 , width, height, null);
	
			//水印文件
			Image pressImage = ImageIO.read(new File(pressImageFileName));
			int pressImageWidth = pressImage.getWidth(null);
			int pressImageHeight = pressImage.getHeight(null);
			graphics.drawImage(pressImage, x, y, pressImageWidth, pressImageHeight, null);
			graphics.dispose();
			
			//输出图片
			saveImage(targetImageFileName, bufferedImage);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/**
	 * 保存图片
	 * @param targetFilePath
	 * @param bufferedImage
	 * @throws ServiceException
	 */
	private void saveImage(String targetFilePath, BufferedImage bufferedImage) throws ServiceException {
		Iterator iterator = ImageIO.getImageWritersByFormatName(targetFilePath.substring(targetFilePath.lastIndexOf('.') + 1));  
		if(!iterator.hasNext()) {
			throw new ServiceException("format not support");
		}
		FileImageOutputStream out = null;
		ImageWriter writer = null;
		try {
		    writer = (ImageWriter)iterator.next();  
		    ImageWriteParam param = writer.getDefaultWriteParam();
		    try {
			    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);  
			    param.setCompressionQuality(0.95f);
		    }
		    catch(Exception e) {
		    	
		    }
		    out = new FileImageOutputStream(new File(targetFilePath));  
		    writer.setOutput(out);  
		    writer.write(null, new IIOImage(bufferedImage, null, null), param);
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
		finally {
			try {
				out.close();
			}
			catch (Exception e) {
				
			}
			try {
				writer.dispose();
			}
			catch (Exception e) {
				
			}
		}
	}
}