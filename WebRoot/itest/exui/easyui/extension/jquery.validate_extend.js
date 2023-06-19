/**
 * exui validate extend
 * @author LD
 * @since 2018-1-18
 * @class
 */
(function($){
	// ie6,7,8等日期解析
	function parseISO8601(dateStringInRange) {
	   dateStringInRange = $.trim(dateStringInRange);
	   var isoExp = /^\s*([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})\s*$/,
	       date = new Date(NaN), month,
	       parts = isoExp.exec(dateStringInRange);
	   if(parts) {
	     month = +parts[2];
	     date.setFullYear(parts[1], month - 1, parts[3]);
	     if(month != date.getMonth() + 1) {
	       date.setTime(NaN);
	     }
	   }
	   if(date) {
		   date = date.getTime();
	   } else {
		   date = -4000000000000;
	   }
	   return date;
	}
	var parseDateFunc;
	if(Date.parse && !isNaN(Date.parse('2014-10-10'))) {
		parseDateFunc = Date.parse;
	} else {
		parseDateFunc = parseISO8601;
	}
	// 扩展validatebox的validType属性
	$.extend($.fn.validatebox.defaults.rules, {
		rangDate: {
			validator: function(value, param){
		    	var t = parseDateFunc(value);
		    	var min = parseDateFunc(param[0]);
		    	var max = parseDateFunc(param[1]);
		    	if (!isNaN(t) && !isNaN(min) && !isNaN(max)){
		    		return t>=min&&t<=max;
		    	} else {
		    		return false;
		    	}
	        },
	        message: '请输入{0}和{1}之间的正确日期'
		},
	    minDate: {
	    	validator: function(value, param){
		    	var t = parseDateFunc(value);
		    	var d = parseDateFunc(param[0]);
		    	if (!isNaN(t) && !isNaN(d)){
		    		return t>=d;
		    	} else {
		    		return false;
		    	}
	        },  
	        message: '请输入  {0} 之后的正确日期'
	    }, 
	    maxDate: {
	    	validator: function(value, param){
	    		var t = parseDateFunc(value);
		    	var d = parseDateFunc(param[0]);
		    	if (!isNaN(t) && !isNaN(d)){
		    		return t<=d;
		    	} else {
		    		return false;
		    	}
	        },  
	        message: '请输入 {0} 之前的正确日期'
	    },
	    validDate: {
	    	validator: function(value, param){
		    	var t = parseDateFunc(value);
		    	if (/^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$/.test(value) && !isNaN(t)){
		    		return true;
		    	} else {
		    		return false;
		    	}
	        },  
	        message: '请输入正确的日期格式(年-月-日)'
	    }
	});
})(jQuery);
