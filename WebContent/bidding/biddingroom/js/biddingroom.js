function selectRoom(roomType, roomCity, freeRoomOnly, forProjectId, beginTime, endTime, width, height, multiSelect, param, scriptEndSelect, separator) {
   var left =(screen.width - width)/2;
   var top =(screen.height - height - 16)/2;
   var url = RequestUtils.getContextPath() + "/bidding/biddingroom/select.shtml";
   url += "?multiSelect=" + multiSelect;
   url += "&param=" + StringUtils.utf8Encode(param);
   url += (scriptEndSelect && scriptEndSelect!="" ? "&script=" + StringUtils.utf8Encode(scriptEndSelect) : "");
   url += ("&separator=" + (separator && separator!="" ?  StringUtils.utf8Encode(separator) : ","));
   url += "&roomType=" + StringUtils.utf8Encode(roomType);
   url += "&city=" + StringUtils.utf8Encode(roomCity);
   url += (freeRoomOnly ? "&freeRoomOnly=true" : "");
   url += "&forProjectId=" + forProjectId;
   url += (beginTime=="" ? "" : "&beginTime=" + beginTime);
   url += (endTime=="" ? "" : "&endTime=" + endTime);
   DialogUtils.openDialog(url, width, height);
}