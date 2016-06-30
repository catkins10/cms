/*
 * Created on 2006-4-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.yuanluesoft.enterprise.iso.forms;

import com.yuanluesoft.jeaf.view.forms.ViewForm;

/**
 *
 * @author linchuan
 *
 */
public class DocumentView extends ViewForm {
    private long directoryId;

    /**
     * @return Returns the directoryId.
     */
    public long getDirectoryId() {
        return directoryId;
    }
    /**
     * @param directoryId The directoryId to set.
     */
    public void setDirectoryId(long directoryId) {
        this.directoryId = directoryId;
    }
}