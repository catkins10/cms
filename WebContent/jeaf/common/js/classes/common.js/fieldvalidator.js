FieldValidator = function() {

};
//校验是否必须输入
FieldValidator.validateFieldRequired = function(src, required, fieldName) {
   if(!src) {
	  return "";
   }
   if(src.value=="" && required) {
      alert((fieldName ? fieldName : "内容") + "不能为空！");
      src.focus();
      return "NaN";
   }
   return src.value;
};
//校验字符串
FieldValidator.validateStringField = function(src, mask, required, fieldName) {
   var value = FieldValidator.validateFieldRequired(src, required, fieldName);
   if(value == "" || value == "NaN") {
      return value;
   }
   if(mask && mask != "") {
      var newMask = mask.replace(new RegExp("\\x27", "g"), "\\x27").replace(new RegExp(",", "g"), "");
      if(value.search(new RegExp("[" + newMask + "]")) != - 1) {
         alert((fieldName ? fieldName : "输入内容") + "不能包含" + mask + "等字符！");
         src.focus();
         src.select();
         return "NaN";
      }
   }
   return value;
};
//校验数字
FieldValidator.validateNumberField = function(src, required, fieldName) {
   var value = FieldValidator.validateFieldRequired(src, required, fieldName);
   if(value == "" || value == "NaN") {
      return value;
   }
   var value = new Number(value);
   if(isNaN(value)) {
      alert((fieldName ? fieldName : "您") + "输入的数字不正确！");
      src.focus();
      src.select();
      return "NaN";
   }
   return value;
};
//校验日期
FieldValidator.validateDateField = function(src, required, fieldName) {
   var value = FieldValidator.validateFieldRequired(src, required, fieldName);
   if(value == "" || value == "NaN") {
      return value;
   }
   var dateValue = new Date(value.replace(new RegExp("-", "g"), "/"));
   if(isNaN(dateValue)) {
      alert((fieldName ? fieldName : "您") + "输入的日期格式不正确！");
      src.focus();
      src.select();
      return "NaN";
   }
   return value;
};