package com.yuanluesoft.jeaf.video.convertor.handbrake;

import java.util.ArrayList;
import java.util.List;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ProcessUtils;
import com.yuanluesoft.jeaf.video.convertor.VideoConvertor;
import com.yuanluesoft.jeaf.video.model.Video;

/**
 * 
 * @author linchuan
 *
 */
public class VideoConvertorImpl implements VideoConvertor {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.video.convertor.VideoConvertor#video2mp4(java.lang.String, java.lang.String, int, int, int, int, int, int)
	 */
	public boolean videoConvert(String sourceFileName, String targetFileName, double videoBitrate, double videoFps, int videoWidth, int videoHeight, double audioBitrate, double audioFreq) throws ServiceException {
		//HandBrakeCLI -i /Volumes/MyBook/BLURAY_DISC -o /Volumes/MyBook/Bluray.mkv -m -E copy --audio-copy-mask ac3,dts,dtshd --audio-fallback ffac3 -e x264 -q 20 -x level=4.1:ref=4:b-adapt=2:direct=auto:me=umh:subq=8:rc-lookahead=50:psy-rd=1.0,0.15:deblock=-1,-1:vbv-bufsize=30000:vbv-maxrate=40000:slices=4
		if(Logger.isDebugEnabled()) {
			Logger.debug("HandBrakeCLI: convert video file " + sourceFileName + " to mp4 file " + targetFileName + ".");
		}
		List command = new ArrayList();
		command.add(Environment.getWebinfPath() + "jeaf/video/videoconvertor/HandBrakeCLI.exe");
		command.add("-i"); //Set input device
		command.add("\"" + sourceFileName + "\"");
		command.add("-o"); //Set output file name
		command.add("\"" + targetFileName + "\"");
		command.add("-e"); //Set video library encoder Options: x264/ffmpeg4/ffmpeg2/theora (default: ffmpeg4)
		command.add("x264");
		command.add("--x264-preset"); //When using x264, selects the x264 preset: ultrafast/superfast/veryfast/faster/fast/medium/slow/slower/veryslow/placebo
		command.add("superfast");
		command.add("-f"); //Set output format (mp4/mkv, default: autodetected from file name)
		command.add("mp4");
		command.add("-O"); //Optimize mp4 files for HTTP streaming
		command.add("-b"); //Set video bitrate (default: 1000) <kb/s>
		command.add("" + videoBitrate); //视频比特率,以kbps为单位
		command.add("-B"); //Set audio bitrate(s) <kb/s> (default: depends on the selected codec, mixdown and samplerate) Separated by commas for more than one audio track.
		command.add("" + audioBitrate);
		command.add("-R"); //Set audio samplerate(s) (22.05/24/32/44.1/48 kHz) Separated by commas for more than one audio track.
		command.add("" + audioFreq);
		command.add("-r"); //Set video framerate (5/10/12/15/23.976/24/25/29.97) Be aware that not specifying a framerate lets HandBrake preserve a source's time stamps, potentially creating variable framerate video
		command.add("" + videoFps); //视频帧数,默认24
		if(videoWidth>0 && videoHeight>0) {
			command.add("-w"); //Set picture width
			command.add("" + videoWidth);
			command.add("-l"); //Set picture height
			command.add("" + videoHeight);
		}
		return ProcessUtils.executeCommand(command, null, null)==0;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.video.VideoConvertor#extractImage(java.lang.String, java.lang.String, int, int)
	 */
	public synchronized boolean extractImage(String videoFileName, String imageFileName, int imageWidth, int iamgeHeight) throws ServiceException {
		//ffmpeg -i "D:/workspace/cms/WebContent/cms/pages/990150377292650004/videos/播客视频-福州世茂天城3D片.mp4" -y -f image2  -t 0.001 -s 300*200  -ss 08.010  "D:/workspace/cms/WebContent/cms/pages/990150377292650004/videos/播客视频-福州世茂天城3D片.jpg" //获取静态图
		if(Logger.isDebugEnabled()) {
			Logger.debug("Ffmpeg: extract image from file " + videoFileName + " to " + imageFileName + ".");
		}
		List command = new ArrayList();
		command.add(Environment.getWebinfPath() + "jeaf/video/videoconvertor/ffmpeg.exe");
		command.add("-i");
		command.add("\"" + videoFileName + "\"");
		command.add("-f");
		command.add("image2");
		command.add("-t");
		command.add("1");
		command.add("-ss");
		command.add("05.000"); //截取第5秒的图片
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
		return null;
	}
}