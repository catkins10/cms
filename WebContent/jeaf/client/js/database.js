//数据库类
//数据库查询事件: window.top.database.onAfterQuery = function(records);
Database = function() {

};
Database.prototype.addDatabaseTableDefine = function(tableDefine) { //添加表定义
	if(!this.databaseTableDefines) {
		this.databaseTableDefines = new Array();
	}
	this.databaseTableDefines[this.databaseTableDefines.length] = tableDefine;
	//建表
	var sql = "create table " + tableDefine.name + "(";
	for(var i=0; i<tableDefine.columns.length; i++) {
		sql += (i>0 ? "," : "") + tableDefine.columns[i].name + 
			   " " + tableDefine.columns[i].type + (tableDefine.columns[i].length ? "(" + tableDefine.columns[i].length + ")" : "") +
			   (tableDefine.columns[i].name=="id" ? " primary key" : "");
	}
	sql += ")";
	window.client.callNativeMethod("createTable('" + sql + "')");
	//建索引
	for(var i=0; i<(tableDefine.indexes ? tableDefine.indexes.length : 0); i++) {
		sql = "create index " + tableDefine.indexes[i].name + " on " + tableDefine.name + " (" + tableDefine.indexes[i].columns + ")";
		window.client.callNativeMethod("createIndex('" + sql + "')");
	}
	tableDefine.indexes = null;
};
Database.prototype.getTableDefine = function(tableName) { //获取表定义
	for(var i=0; i<this.databaseTableDefines.length; i++) {
		if(this.databaseTableDefines[i].name==tableName) {
			return this.databaseTableDefines[i];
		}
	}
	return null;
};
Database.prototype.UUID = function() { //生成数字类型ID
	return new Date().valueOf() * 1000 + Math.floor(Math.random() * 1000);
};
Database.prototype.saveRecord = function(tableName, record) { //保存记录
	if(!record.id) {
		record.id = this.UUID();
	}
	//获取表定义
	var tableDefine = this.getTableDefine(tableName);
	//生成sql语句
	var columnNames = "";
	var columnValues = "";
	for(var i=0; i<tableDefine.columns.length; i++) {
		var value = this.generateSqlValue(tableDefine.columns[i], record);
		if(value) {
			columnNames += (columnNames=="" ? "" : ",") + tableDefine.columns[i].name;
			columnValues += (columnValues=="" ? "" : ",") + value;
		}
	}
	this.executeSQL("insert into " + tableDefine.name + "(" + columnNames + ") values (" + columnValues + ")");
};
Database.prototype.updateRecord = function(tableName, record) { //更新记录
	//获取表定义
	var tableDefine = this.getTableDefine(tableName);
	//生成sql语句
	var id;
	var columnValues = "";
	for(var i=0; i<tableDefine.columns.length; i++) {
		var value = this.generateSqlValue(tableDefine.columns[i], record);
		if(!value) {
			continue;
		}
		if(tableDefine.columns[i].name=="id") {
			id = value;
		}
		else {
			columnValues += (columnValues=="" ? "" : ",") + tableDefine.columns[i].name + "=" + value;
		}
	}
	this.executeSQL("update " + tableDefine.name + " set " + columnValues + " where id=" + id);
};
Database.prototype.generateSqlValue = function(tableColumnDefine, record) { //生成SQL值,用于生成SQL语句
	var value = eval("record." + tableColumnDefine.name);
	if(!value && value!=0) { //记录中没有设置值
		return null;
	}
	if(tableColumnDefine.type=="varchar") {
		return "'" + ("" + value).replace(/'/, "''") + "'";
	}
	if(tableColumnDefine.type=="date" || tableColumnDefine.type=="timestamp") {
		try {
			var timeValue = tableColumnDefine.type.toUpperCase() + "(" + 
							value.getFullYear() + "-" +
							(value.getMonth()<9 ? "0" : "") + (value.getMonth() + 1) + "-" +
							(value.getDate()<10 ? "0" : "") + value.getDate();
			if(tableColumnDefine.type=="timestamp") {
				timeValue += " " + (value.getHours()<10 ? "0" : "") + value.getHours() + ":" +
						 	 (value.getMinutes()<10 ? "0" : "") + value.getMinutes() + ":" +
						 	 (value.getSeconds()<10 ? "0" : "") + value.getSeconds();
			}
			return timeValue + ")";
		}
		catch(e) {
			return null;
		}
	}
	return "" + value;
};
Database.prototype.deleteRecord = function(tableName, recordId) { //删除记录
	//删除关联记录
	for(var i=0; i<this.databaseTableDefines.length; i++) {
		if(this.databaseTableDefines[i].name==tableName) {
			continue;
		}
		for(var j=0; j<this.databaseTableDefines[i].columns.length; j++) {
			var column = this.databaseTableDefines[i].columns[j];
			if(column.referenceTable==tableName) {
				this.executeSQL("delete from " + this.databaseTableDefines[i].name + " where " + column.name + "=" + recordId);
				break;
			}
		}
	}
	//删除主记录
	this.executeSQL("delete from " + tableName + " where id=" + recordId);
};
Database.prototype.executeSQL = function(sql) { //执行SQL
	window.client.callNativeMethod("executeSQL('" + sql.replace(/\"/gi, "\\\"").replace(/'/gi, "\\'").replace(/\r/gi, "").replace(/\n/gi, "\\n") + "')");
};
Database.prototype.databaseQuery = function(sql, offset, limit, sampleRecordElement, loadMoreEnabled) { //查询
	if(sampleRecordElement) {
		sampleRecordElement.style.display = 'none';
	}
	this.sampleRecordElement = sampleRecordElement;
	if(sampleRecordElement) {
		sampleRecordElement.offset = offset;
	}
	this.doDatabaseQuery(sql, offset, limit); //执行查询
	//需要加载更多
	if(loadMoreEnabled && sampleRecordElement) {
		sampleRecordElement.ownerDocument.loadMoreEnabled = true;
		sampleRecordElement.ownerDocument.loadMore = function() {
			window.top.database.sampleRecordElement = sampleRecordElement;
			window.top.database.doDatabaseQuery(sql, sampleRecordElement.offset, limit); //执行查询
		};
	}
};
Database.prototype.doDatabaseQuery = function(sql, offset, limit) { //执行查询
	sql = sql.replace(/'/gi, "\\'").replace(/\r/gi, "").replace(/\n/gi, "\\n");
	var callback;
	if(window.top.androidClient) { //安卓
		callback = function(returnValue) {
			window.top.database.writeDatabaseQueryResults(returnValue);
		};
	}
	else { //IOS
		callback = function(returnValue) {
			window.top.database.appendQueryResult(returnValue, offset, count);
		};
	}
	window.client.callNativeMethod("databaseQuery('" + sql + "', " + offset + "," + limit + ")", callback);
};
Database.prototype.appendQueryResult = function(queryResult, offset, count) { //追加查询结果(IOS调用),queryResult格式:{columnNames:字段名称列表} 或 {字段1名称:记录1字段1值, 字段2名称:记录1字段2值}
	//TODO
};
Database.prototype.writeDatabaseQueryResults = function(queryResults) { //输出查询结果,queryResults格式:[{columnNames:字段名称列表}, {字段1名称:记录1字段1值, 字段2名称:记录1字段2值},{字段1名称:记录2字段1值, 字段2名称:记录2字段2值}]
	var records;
	try {
		eval("records=" + queryResults + ";");
	}
	catch(e) {
	
	}
	if(this.onAfterQuery) {
		try {
			this.onAfterQuery.call(null, records);
		}
		catch(e) {

		}
		this.onAfterQuery = null;
	}
	if(!this.sampleRecordElement) {
		return;
	}
	this.sampleRecordElement.offset += records.length - 1;
	var columnNames = records[0].columnNames.split(",");
	for(var i=1; i<records.length; i++) {
		var newRecordElement = this.sampleRecordElement.cloneNode(true);
		newRecordElement.removeAttribute("id");
		newRecordElement.style.display = "";
		newRecordElement.style.visibility = "visible";
		newRecordElement.id = records[i].id;
		for(var j=0; j<columnNames.length; j++) {
			var fieldElement = DomUtils.getElement(newRecordElement, "span", columnNames[j]);
			if(fieldElement) {
				fieldElement.innerHTML = eval("records[i]." + columnNames[j]);
			}
		}
		this.sampleRecordElement.parentNode.insertBefore(newRecordElement, this.sampleRecordElement);
	}
};
Database.prototype.getRecordElement = function(sampleRecordElement, recordId) { //获取记录元素
	var childNodes = sampleRecordElement.parentNode.childNodes;
	for(var i=0; i<childNodes.length; i++) {
		if(childNodes[i].id==recordId) {
			return childNodes[i];
		}
	}
	return null;
};
Database.prototype.updateRecordProperty = function(sampleRecordElement, recordId, propertyName, propertyValue) { //更新记录属性
	var recordElement = this.getRecordElement(sampleRecordElement, recordId);
	if(!recordElement) {
		return;
	}
	var fieldElement = DomUtils.getElement(recordElement, "span", propertyName);
	if(fieldElement) {
		fieldElement.innerHTML = propertyValue;
	}
};
Database.prototype.removeRecordElement = function(sampleRecordElement, recordId) { //删除记录
	var recordElement = this.getRecordElement(sampleRecordElement, recordId);
	if(recordElement) {
		recordElement.parentNode.removeChild(recordElement);
	}
};