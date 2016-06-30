/*
 * Created on 2006-5-12
 *
 */
package com.yuanluesoft.jeaf.view.forms;

import com.yuanluesoft.jeaf.form.ActionForm;
import com.yuanluesoft.jeaf.view.model.ViewPackage;

/**
 * 
 * @author linchuan
 *
 */
public class ViewForm extends ActionForm {
    private ViewPackage viewPackage = new ViewPackage();
    
    /**
     * @return Returns the viewPackage.
     */
    public ViewPackage getViewPackage() {
        return viewPackage;
    }
    /**
     * @param viewPackage The viewPackage to set.
     */
    public void setViewPackage(ViewPackage viewPackage) {
        this.viewPackage = viewPackage;
    }
}