package com.yuanluesoft.jeaf.report.word.poi;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.report.word.MsWordService;

/**
 * 
 * @author linchuan
 *
 */
public class MsWordServiceImpl implements MsWordService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.report.word.WordService#htmlToWordDocument(java.lang.String, java.io.OutputStream)
	 */
	public void htmlToWordDocument(String htmlFileName, OutputStream outputStream) throws ServiceException {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(htmlFileName);
			POIFSFileSystem poifs = new POIFSFileSystem();
			DirectoryEntry directory = poifs.getRoot();
			directory.createDocument("WordDocument", new FileInputStream(htmlFileName));
			poifs.writeFilesystem(outputStream);
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
		finally {
			try {
				inputStream.close();
			}
			catch(Exception e) {
				
			}
		}
	}
}