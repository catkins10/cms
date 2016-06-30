package com.yuanluesoft.jeaf.tools.deployment;

import com.yuanluesoft.jeaf.util.JsUtils;


/**
 * 
 * @author linchuan
 *
 */
public class JavaScriptCombine {
	
	public static void main(String[] args) throws Exception {
		//合并common.js
		String[] srcFiles = {"c:/workspace/cms/WebContent/jeaf/common/js/classes/common.js/eventutils.js",
							 "c:/workspace/cms/WebContent/jeaf/common/js/classes/common.js/formfield.js",
							 "c:/workspace/cms/WebContent/jeaf/common/js/classes/common.js/formfield.picker.js"};
		String[] srcFolders = {"c:/workspace/cms/WebContent/jeaf/common/js/classes/common.js/"};
		String destFile = "c:/workspace/cms/WebContent/jeaf/common/js/common.js";
		JsUtils.combine(srcFiles, srcFolders, destFile);

		//合并html编辑器脚本
		srcFiles = new String[] {"c:/workspace/cms/WebContent/jeaf/htmleditor/js/internals/htmleditor.js",
								 "c:/workspace/cms/WebContent/jeaf/htmleditor/js/commands/namedcommand.js",
								 "c:/workspace/cms/WebContent/jeaf/htmleditor/js/commands/menucommand.js"};
		srcFolders = new String[] {"c:/workspace/cms/WebContent/jeaf/htmleditor/js/internals/",
							   	   "c:/workspace/cms/WebContent/jeaf/htmleditor/js/commands/"};
		destFile = "c:/workspace/cms/WebContent/jeaf/htmleditor/js/htmleditor.js";
		JsUtils.combine(srcFiles, srcFolders, destFile);
		
		//合并模板配置plugin.js
		srcFiles = new String[] {"c:/workspace/cms/WebContent/cms/templatemanage/editorplugins/classes/template.js"};
		srcFolders = new String[]{"c:/workspace/cms/WebContent/cms/templatemanage/editorplugins/classes/"};
		destFile = "c:/workspace/cms/WebContent/cms/templatemanage/editorplugins/plugin.js";
		JsUtils.combine(srcFiles, srcFolders, destFile);
		
		//合并图形编辑
		srcFiles = new String[] {"c:/workspace/cms/WebContent/jeaf/graphicseditor/js/classes/graphicseditor.js",
								 "c:/workspace/cms/WebContent/jeaf/graphicseditor/js/classes/graphicseditor.shape.js"};
		srcFolders = new String[] {"c:/workspace/cms/WebContent/jeaf/graphicseditor/js/classes/"};
		destFile = "c:/workspace/cms/WebContent/jeaf/graphicseditor/js/graphicseditor.js";
		JsUtils.combine(srcFiles, srcFolders, destFile);
		
		//合并EAI配置
		srcFiles = new String[] {"C:/Workspace/cms/WebContent/eai/configure/js/classes/eaieditor.js"};
		srcFolders = new String[] {"C:/Workspace/cms/WebContent/eai/configure/js/classes/"};
		destFile = "C:/Workspace/cms/WebContent/eai/configure/js/eaieditor.js";
		JsUtils.combine(srcFiles, srcFolders, destFile);
		
		//合并流程配置
		srcFiles = new String[] {"C:/Workspace/cms/WebContent/workflow/configure/js/classes/workfloweditor.js"};
		srcFolders = new String[] {"C:/Workspace/cms/WebContent/workflow/configure/js/classes/"};
		destFile = "C:/Workspace/cms/WebContent/workflow/configure/js/workfloweditor.js";
		JsUtils.combine(srcFiles, srcFolders, destFile);
		
		//合并文件上传类
		srcFiles = new String[] {"C:/Workspace/cms/WebContent/jeaf/filetransfer/js/classes/fileuploadclient.js",
								 "C:/Workspace/cms/WebContent/jeaf/filetransfer/js/classes/fileuploadclient.uploader.js",
								 "C:/Workspace/cms/WebContent/jeaf/filetransfer/js/classes/fileuploadclient.htmluploader.js"};
		srcFolders = new String[] {"C:/Workspace/cms/WebContent/jeaf/filetransfer/js/classes/"};
		destFile = "C:/Workspace/cms/WebContent/jeaf/filetransfer/js/fileuploadclient.js";
		JsUtils.combine(srcFiles, srcFolders, destFile);
	}
}