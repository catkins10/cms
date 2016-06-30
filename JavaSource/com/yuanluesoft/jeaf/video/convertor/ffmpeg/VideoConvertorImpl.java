package com.yuanluesoft.jeaf.video.convertor.ffmpeg;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ProcessUtils;
import com.yuanluesoft.jeaf.util.callback.ProcessCallback;
import com.yuanluesoft.jeaf.video.convertor.VideoConvertor;
import com.yuanluesoft.jeaf.video.model.Video;

/**
 * 
 * @author linchuan
 *
 */
public class VideoConvertorImpl implements VideoConvertor {

	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.video.convertor.VideoConvertor#videoConvert(java.lang.String, java.lang.String, double, double, int, int, double, double)
	 */
	public synchronized boolean videoConvert(String sourceFileName, String targetFileName, double videoBitrate, double videoFps, int videoWidth, int videoHeight, double audioBitrate, double audioFreq) throws ServiceException {
		if(videoBitrate<30) {
			videoBitrate = 30;
		}
		//ffmpeg.exe -i test.mp3 -ab 56 -ar 22050 -b 500 -r 15 -s 320x240 f:\test.mp4
		if(Logger.isDebugEnabled()) {
			Logger.debug("Ffmpeg: convert video file " + sourceFileName + " to file " + targetFileName + ".");
		}
		List command = new ArrayList();
		command.add(Environment.getWebinfPath() + "jeaf/video/videoconvertor/ffmpeg.exe");
		command.add("-i");
		command.add("\"" + sourceFileName + "\"");
		command.add("-ab");
		command.add("" + (audioBitrate * 1000));
		command.add("-ar");
		command.add("" + audioFreq);
		command.add("-pix_fmt");
		command.add("yuv420p");
		command.add("-vcodec");
		command.add("libx264");
		command.add("-vprofile");
		command.add("baseline");
		command.add("-b");
		command.add("" + (videoBitrate * 1000)); //视频比特率,以kbps为单位
		command.add("-r");
		command.add("" + videoFps); //视频帧数,默认24
		if(videoWidth>0 && videoHeight>0) {
			command.add("-s");
			command.add(videoWidth + "x" + videoHeight);
		}
		command.add("-y"); //覆盖旧文件
		command.add("\"" + targetFileName + "\"");
		if(ProcessUtils.executeCommand(command, null, null)!=0) {
			return false;
		}
		//mp4box -hint file.mp4  为RTP准备，此指令将为文件创建RTP提示跟踪信息。这使得经典的流媒体服务器像darwinstreamingserver或QuickTime的流媒体服务器通过RTSP／RTP传输文件
		command.clear();
		command.add(Environment.getWebinfPath() + "jeaf/video/videoconvertor/MP4Box.exe");
		command.add("-hint");
		command.add("\"" + targetFileName + "\"");
		return ProcessUtils.executeCommand(command, null, null)==0;
		
		//qt-faststart转换,以实现实时播放
		/*command.clear();
		command.add(Environment.getWebinfPath() + "jeaf/video/videoconvertor/qt-faststart.exe");
		command.add("\"" + targetFileName + "\"");
		command.add("\"" + targetFileName + "_qt.mp4\"");
		if(ProcessUtils.executeCommand(command, null, null)!=0) {
			return false;
		}
		return FileUtils.renameFile(targetFileName + "_qt.mp4", targetFileName, true, false)!=null;*/
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.video.VideoConvertor#extractImage(java.lang.String, java.lang.String, int, int)
	 */
	public synchronized boolean extractImage(String videoFileName, String imageFileName, int imageWidth, int iamgeHeight) throws ServiceException {
		//ffmpeg -i "D:/workspace/cms/WebContent/cms/pages/990150377292650004/videos/播客视频-福州世茂天城3D片.mp4" -y -f image2  -t 0.001 -s 300*200  -ss 08.010  "D:/workspace/cms/WebContent/cms/pages/990150377292650004/videos/播客视频-福州世茂天城3D片.jpg" //获取静态图
		if(Logger.isDebugEnabled()) {
			Logger.debug("Ffmpeg: extract image from file " + videoFileName + " to " + imageFileName + ".");
		}
		Video video = getVideoInfo(videoFileName);
		List command = new ArrayList();
		command.add(Environment.getWebinfPath() + "jeaf/video/videoconvertor/ffmpeg.exe");
		command.add("-i");
		command.add("\"" + videoFileName + "\"");
		command.add("-f");
		command.add("image2");
		command.add("-t");
		command.add("1");
		if(video.getVideoLength()>10) {
			command.add("-ss");
			command.add("05.000"); //截取第5秒的图片
		}
		if(imageWidth>0 && iamgeHeight>0) {
			command.add("-s");
			command.add(imageWidth + "x" + iamgeHeight);
		}
		command.add("-y"); //覆盖旧文件
		command.add("\"" + imageFileName + "\"");
		return ProcessUtils.executeCommand(command, null, null)==0;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.video.VideoConvertor#getVideoInfo(java.lang.String)
	 */
	public synchronized Video getVideoInfo(String videoFileName) throws ServiceException {
		//ffmpeg -i "D:\Tomcat 5.0\webapps\cms\video_tcjd\tcjd.mp4"
		final Video video = new Video();
		List command = new ArrayList();
		command.add(Environment.getWebinfPath() + "jeaf/video/videoconvertor/ffmpeg.exe");
		command.add("-i");
		command.add("\"" + videoFileName + "\"");
		ProcessCallback callback = new ProcessCallback() {
			public void processOutput(String output, boolean isError) {
				int index;
				if((index=output.indexOf("Duration:"))!=-1) { //Duration: 00:00:07.42, start: 0.000000, bitrate: 1554 kb/s
					String[] values = output.substring(index + "Duration:".length()).split(",");
					//解析视频长度
					String[] timeValues = values[0].trim().split(":");
					video.setVideoLength(Double.parseDouble(timeValues[0]) * 3600 + Double.parseDouble(timeValues[1]) * 60 + Double.parseDouble(timeValues[2]));
					//解析视频比特率
					video.setVideoBitrate(Double.parseDouble(values[2].replaceAll("bitrate:|kb/s", "")));
				}
				else if((index=output.indexOf("Stream"))!=-1 && (index=output.indexOf("Video:", index))!=-1) { //Stream #0:0(eng): Video: h264 (Main) (avc1 / 0x31637661), yuv420p, 960x540 [SAR 1:1 DAR 16:9], 2489 kb/s, 29.97 fps, 29.97 tbr, 30k tbn, 59.94 tbc
					String[] values = output.substring(index + "Video:".length()).split(",");
					//视频格式
					video.setVideoFormat(values[0].trim());
					//解析视频宽度、高度
					values[2] = values[2].trim();
					index = values[2].indexOf(' ');
					String[] sizeValues = (index==-1 ? values[2] : values[2].substring(0, index)).split("x");
					video.setVideoWidth(Integer.parseInt(sizeValues[0]));
					video.setVideoHeight(Integer.parseInt(sizeValues[1]));
					//解析视频比特率、帧频
					for(int i=3; i<values.length; i++) {
						if((index=values[i].indexOf("kb/s"))!=-1) {
							video.setVideoBitrate(Double.parseDouble(values[i].substring(0, index))); //视频比特率
						}
						else if((index=values[i].indexOf("fps"))!=-1) {
							video.setVideoFps(Double.parseDouble(values[i].substring(0, index))); //帧频
						}
						else if(video.getVideoFps()==0 && (index=values[i].indexOf("tbr"))!=-1) {
							video.setVideoFps(Double.parseDouble(values[i].substring(0, index))); //帧频
						}
					}
				}
				else if((index=output.indexOf("Stream"))!=-1 && (index=output.indexOf("Audio:", index))!=-1) { //Stream #0:1(und): Audio: aac (mp4a / 0x6134706D), 22050 Hz, stereo, s16, 77 kb/s
					String[] values = output.substring(index + "Audio:".length()).split(",");
					//解析音频比特率、采样率
					for(int i=0; i<values.length; i++) {
						if((index=values[i].indexOf("kb/s"))!=-1) {
							video.setAudioBitrate(Double.parseDouble(values[i].substring(0, index))); //音频比特率
						}
						else if((index=values[i].indexOf("Hz"))!=-1) {
							video.setAudioFreq(Double.parseDouble(values[i].substring(0, index))); //采样率
						}
					}
				}
			}
		};
		ProcessUtils.executeCommand(command, null, callback);
		return video;
	}
}