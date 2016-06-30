package com.yuanluesoft.jeaf.image.generator.imagemagick;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.image.generator.ImageGenerator;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ProcessUtils;

/**
 * 
 * @author linchuan
 *
 */
public class ImageMagickImageGenerator extends ImageGenerator {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.generator.ImageGenerator#getMaxMegaPixels()
	 */
	public int getMaxMegaPixels() {
		return 0; //不限制像素
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.generator.ImageGenerator#convertImage(java.lang.String, java.lang.String, int, int, int, int, int, int)
	 */
	public void convertImage(String sourceFilePath, String targetFilePath, int resizeWidth, int resizeHeight, int cropWidth, int cropHeight, int cropLeft, int cropTop) throws ServiceException {
		List command = new ArrayList();
		command.add(Environment.getWebinfPath() +  "jeaf/image/imagemagick/convert.exe");
		command.add("+profile"); //+/-profile * 去掉/添加图片exif信息,必须使用，否则生成图片过大
		command.add("*");
		command.add("+repage"); //去掉图片裁减后的空白
		command.add(sourceFilePath + "[0]"); //如果是gif,只转换第一张图片
		if(resizeWidth>0 && resizeHeight>0) {
			command.add("-resize");
			command.add(resizeWidth + "x" + resizeHeight + "!");
		}
		if(cropWidth>0 && cropHeight>0) {
			command.add("-crop");
			command.add(cropWidth + "x" + cropHeight + "+" + cropLeft + "+" + cropTop);
		}
		command.add("-quality");
		command.add("95");
		command.add(targetFilePath);
		ProcessUtils.executeCommand(command, null, null);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.image.generator.ImageGenerator#pressImage(java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	public void pressImage(String sourceImageFileName, String targetImageFileName, String pressImageFileName, int x, int y) throws ServiceException {
		List command = new ArrayList();
		command.add(Environment.getWebinfPath() +  "jeaf/image/imagemagick/convert.exe");
		command.add(sourceImageFileName);
		command.add(pressImageFileName);
		command.add("-gravity");
		command.add("NorthWest");
		command.add("-geometry");
		command.add("+" + x + "+" + y);
		command.add("-composite");
		command.add(targetImageFileName);
		ProcessUtils.executeCommand(command, null, null);
	}
}