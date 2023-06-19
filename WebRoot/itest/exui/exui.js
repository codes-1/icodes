/*！
 * exui v1.5.4.1
 * 
 * Released under the MIT licenced
 */
(function(global, factory) {
	/*typeof exports === 'object' && typeof module !== 'undefined' ?
		factory(exports, require('jquery'), require('easyui/exuiloader.js')) :
		typeof define === 'function' && define.amd ?
			define(['exports', 'jquery', 'easyui/exuiloader.js'], factory) :
			(factory((global.exports = {}), global.jQuery, global.exuiloader));*/
			
	if (typeof exports === 'object' && typeof module !== 'undefined') {
		factory(exports, require('jquery'), require('exuiloader.js'))
	} else if (typeof define === 'function' && define.amd) {
		define(['exports', 'jquery', 'exuiloader.js'], factory)
	} else {
		factory(global.exports, global.jQuery, global.exuiloader)
	}
} (this, (function(exports, $, exuiloader) {
	'use strict';
	jQuery = jQuery && jQuery.hasOwnProperty('default') ? jQuery['default'] : jQuery;
	exuiloader = exuiloader && exuiloader.hasOwnProperty('default') ? exuiloader['default'] : exuiloader;
	
	(function($, exuiloader) {
		if (typeof $ === 'undefined') {
			throw new TypeError('jQuery must be included before this js file.');
		}
		if (typeof exuiloader === 'undefined') {
			throw new TypeError('exuiloader.js must be included before this js file.');
		}
		
		var version = $.fn.jquery.split(' ')[0].split('.'),
			minMajor = 1,
			maxMajor = 4,
			ltMajor = 2,
			minMinor = 9,
			minPatch = 1;
		if (version[0] < ltMajor && version[1] < minMinor || version[0] === minMajor && version[1] === minMinor && version[2] < minPatch || version[0] >= maxMajor) {
            throw new Error('requires at least jQuery v1.9.1 but less than v4.0.0');
        }
	})(jQuery, exuiloader);
		
	function getAppPath() {
		var origin = window.document.location.origin,
			pathname = window.document.location.pathname,
			context = pathname.substring(0, pathname.substr(1).indexOf('/') + 1);
		
		return origin + context;
	}
	
	$.APP_PATH = getAppPath();
	
	/*function includeJS(path, force) {
		var url = path;
		if(!(/^http/i.test(path))){
			url = $.APP_PATH + path;
		}
		if(force) exuiloader.jsMap[url] = false;
		if(undefined === exuiloader.jsMap[url]) {
			exuiloader.jsMap[url] = false;
		}
		if(true === exuiloader.jsMap[url]){
			return;
		}
		jQuery.ajax({
			cache: true,
			async: false,
			url: url,
			dataType:'script',
			scriptCharset: "utf-8"
		});
		exuiloader.jsMap[url] = true;
	}*/
	
	function getPlugin(target) {
		if(!target || target.length==0) return '';
		
		var tbCls = ['slider','textbox','passwordbox','timespinner','datetimespinner','numberbox','numberspinner',
			'filebox','combo','combotreegrid','combotree','combogrid','combobox','datebox','datetimebox'];
		
		for(var i=tbCls.length-1; i>=0; i--){
			var type = tbCls[i];
			if(target.eq(0).hasClass(type+'-f')){
				return type;
			}else if(target.eq(0).hasClass('validatebox-text') && !target.eq(0).hasClass(type+'-f')) {
				return "validatebox";
			}
		}

		/*if(target.eq(0).hasClass('multiselect2side')) {
			return 'multiselect2side';
		}*/
		if(target.eq(0).hasClass('calendar')) {
			return 'calendar';
		}
		
		var tagName = target.eq(0).prop('tagName').toLowerCase();
		if('input' === tagName && target.eq(0).attr('type') &&
			-1 != $.inArray(target.eq(0).attr('type'), ['checkbox', 'radio'])) {
			tagName = target.eq(0).attr('type').toLowerCase();
		}
		return tagName;
	};
	
	var textboxMap = {'textbox':true,'passwordbox':true,'timespinner':true,'datetimespinner':true,'numberbox':true,'numberspinner':true,
		'combo':true,'combotreegrid':true,'combotree':true,'combogrid':true,'combobox':true,'datebox':true,'datetimebox':true};
	function getPluginVal(target) {
		var clsName = getPlugin(target);
		if(clsName) {
			// textbox及继承自textbox的组件
			if(textboxMap[clsName]) {
				if(target[clsName]('options').multiple) {
					return target.eq(0)[clsName]('getValues');
				} else {
					return target.eq(0)[clsName]('getValue');
				}
			}
			// filebox,slider
			var clses = ['filebox','slider'],
				idx = $.inArray(clsName, clses);
			if(-1 != idx) {
				return target.eq(0)[clses[idx]]('getValue');
			}
			// calendar
			clses = ['calendar'];
			idx = $.inArray(clsName, clses);
			if(-1 != idx) {
				return target.eq(0)[clses[idx]]('options').current;				
			}
			// validatebox,input,textarea
			clses = ['validatebox','input','textarea'];
			idx = $.inArray(clsName, clses);
			if(-1 != idx) {
				return target.eq(0).val();
			}
			// select,multiselect2side
			clses = ['select', 'multiselect2side'];
			idx = $.inArray(clsName, clses);
			if(-1 != idx) {
				var rtnVal = [];
				$.each(target.children(':selected'), function(i,n){
					rtnVal.push(jQuery(this).val());
				});
				return rtnVal;
			}
			// radio
			clses = ['radio'];
			idx = $.inArray(clsName, clses);
			if(-1 != idx) {
				var rtnVal = null;
				$.each(target.filter(':checked'), function(i,n){
					rtnVal=jQuery(this).val();
					return false;
				});
				return rtnVal;
			}
			//checkbox
			clses = ['checkbox'];
			idx = $.inArray(clsName, clses);
			if(-1 != idx) {
				var rtnVal = [];
				$.each(target.filter(':checked'), function(i,n){
					rtnVal.push(jQuery(this).val());
				});
				return rtnVal;
			}
		}else{
			return null;
		}
	};
	
	function setPluginVal(target, value) {
		var clsName = getPlugin(target);
		if(clsName) {
			// textbox及继承自textbox的组件
			if(textboxMap[clsName]) {
				if(target[clsName]('options').multiple) {
					target.eq(0)[clsName]('setValues', value);
				} else {
					target.eq(0)[clsName]('setValue', value);
				}
				return;
			}
			// filebox,slider
			var clses = ['filebox','slider'],
				idx = $.inArray(clsName, clses);
			if(-1 != idx) {
				if (target[clses[idx]]('options').range) {
					target.eq(0)[clses[idx]]('setValues', value);
				} else{
					target.eq(0)[clses[idx]]('setValue', value);
				}
				return;
			}
			// calendar
			clses = ['calendar'];
			idx = $.inArray(clsName, clses);
			if(-1 != idx) {
				target.eq(0)[clses[idx]]('options').current = value;
				return;
			}
			// validatebox,input,textarea
			clses = ['validatebox','input','textarea'];
			idx = $.inArray(clsName, clses);
			if(-1 != idx) {
				if (0 == idx) {
					target.eq(0).val(value).validatebox('validate');
				} else {
					target.eq(0).val(value);
				}
				return;
			}
			// multiselect2side,select
			clses = ['multiselect2side', 'select'];
			idx = $.inArray(clsName, clses);
			if(-1 != idx) {
				target.children(':selected').prop('selected', false);
				target.eq(0).val(value);
				return;
			}
			// radio
			clses = ['radio'];
			idx = $.inArray(clsName, clses);
			if(-1 != idx) {
				target.prop('checked', false);
				target.each(function() {
					if(jQuery(this).val() == value) {
						jQuery(this).prop('checked', true);
						return false;
					}
				});
				return;
			}
			// checkbox
			clses = ['checkbox'];
			idx = $.inArray(clsName, clses);
			if(-1 != idx) {
				target.prop('checked', false);
				target.each(function() {
					if ((typeof value === "string" && jQuery(this).val() == value) ||
						(value instanceof Array && 1 !== $.inArray(jQuery(this).val(), value))) {
						jQuery(this).prop('checked', true);
						return false;
					}
				});
				return;
			}
		}
	}
	
	$.config = {
		VERSION: '1.5.4.1',
		ALERT_TITLE: '消息',
		CONFIRM_TITLE: '请确认',
		PROMPT_TITLE: '请输入',
		STYLE: 'question',
		SHOW_TYPE: 'slide',
		TIME_OUT: 1500,
		PUB_DICT_URL: null,
		PRV_DICT_URL: null,
		init: function(options) {
			$.config = $.extend({}, $.config, options);
		}
	};
	exuiloader.locale = window.navigator.language || window.navigator.userLanguage || 'zh_cn';
	exuiloader.locale = exuiloader.locale.replace('-', '_').toLowerCase();
	//exuiloader.load('themes/icon.css');
	//includeJS('/easyui/plugins/jquery.parser.js');
	/*exuiloader.load('parser', null, true);
	jQuery.parser.parse();*/
	
	/*$.plugins = function(options) {
		
	}*/
	
	/*$.fn.plugin = function(component, options, param) {
		var $this = jQuery(this);
		if ('string' === typeof component) {
			if ('loaded' == exuiloader.queues[component]) {
				if (exuiloader.modules[component].init && options) {
					$this[component](options, param);
				}
			} else {
				exuiloader.load(component, function() {
					if (exuiloader.modules[component].init && options) {
						$this[component](options, param);
					}
				});
			}
		}
		return $this;
	}*/
	
	$.plugin = function(name) {
		if ('loaded' == exuiloader.queues[name]) {
			return;
		} else {
			if (name instanceof Array) {
				for (var i=0; i<name.length; i++) {
					exuiloader.load(name[i], null, true);
				}
			} else if ('string' == typeof name) {
				exuiloader.load(name, null, true);
			}
		}
	}
	
	$.xalert = function(options) {
		exuiloader.load('messager', function() {
			if (typeof options == "string") {
				jQuery.messager.alert($.config.ALERT_TITLE, options);
        	} else if (typeof options == "object") {
        		var defaults = {
					msg: null,
					style: null,
					title: $.config.ALERT_TITLE,
					ok: null,
					okFn: function() {},
					closable: true
				};
				var opts = $.extend({}, defaults, options);
		
				if (opts.ok) {
					jQuery.messager.defaults.ok = opts.ok;
				}
				jQuery.messager.alert(opts.title, opts.msg, opts.style, opts.okFn);
		
				if(!opts.closable) {
					jQuery("body > .messager-window:last .panel-tool > .panel-tool-close").remove();
				}
        	}
		});
	}
	
	$.xconfirm = function(options) {
		exuiloader.load('messager', function() {
			var defaults = {
				msg: null,
				style: $.config.STYLE,
				ok: null,
				okFn: null,
				cancel: null,
				cancelFn: null,
				title: $.config.CONFIRM_TITLE,
				closable: true
			};
			var opts = $.extend({}, defaults, options);
		
			if (opts.ok) {
				jQuery.messager.defaults.ok = opts.ok;
			}
			if (opts.cancel) {
				jQuery.messager.defaults.cancel = opts.cancel;
			}
			jQuery.messager.confirm(opts.title, opts.msg, function(data) {
				if (data) {
					opts.okFn ? opts.okFn(data) : null;
				} else {
					opts.cancelFn ? opts.cancelFn() : null;
				}
			});
		
			if(opts.style) {
				jQuery("body > .messager-window:last > .messager-body > .messager-icon")
					.removeClass("messager-question").addClass("messager-" + opts.style);
			}
			if(!opts.closable) {
				jQuery("body > .messager-window:last > .panel-heading > .panel-tool > .panel-tool-close").remove();
			}
		});
	}
	
	$.xprompt = function(options) {
		exuiloader.load('messager', function() {
			var defaults = {
				msg: null,
				style: $.config.STYLE,
				ok: null,
				okFn: null,
				cancel: null,
				cancelFn: null,
				title: $.config.PROMPT_TITLE,
				closable: true
			};
			var opts = $.extend({}, defaults, options);
		
			if (opts.ok) {
				jQuery.messager.defaults.ok = opts.ok;
			}
			if (opts.cancel) {
				jQuery.messager.defaults.cancel = opts.cancel;
			}
			jQuery.messager.prompt(opts.title, opts.msg, function(data) {
				if (data) {
					opts.okFn ? opts.okFn(data) : null;
				} else {
					opts.cancelFn ? opts.cancelFn() : null;
				}
			});
		
			if(opts.style) {
				jQuery("body > .messager-window:last > .messager-body > .messager-icon")
					.removeClass("messager-question").addClass("messager-" + opts.style);
			}
			if(!opts.closable) {
				jQuery("body > .messager-window:last > .panel-heading > .panel-tool > .panel-tool-close").remove();
			}
		});
	}
	
	$.xprogress = function(options) {
		exuiloader.load('messager', function() {
			jQuery.messager.progress(options);
		});
	}
	
	$.xmessager = function(options) {
		exuiloader.load('messager', function() {
			var defaults = {
				msg: null,
				title: null,
				timeout: $.config.TIME_OUT,
				showType: $.config.SHOW_TYPE,
				pos: 'bottom-right',
				style: null
			};
			var opts = $.extend({}, defaults, options);
			
			switch (opts.pos){
				case 'top-left':
					opts.style = {
						right:'',
						left:0,
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					};
					break;
				case 'top-center':
					opts.style = {
						right:'',
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					};
					break;
				case 'top-right':
					opts.style = {
						left:'',
						right:0,
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					};
					break;
				case 'center-left':
					opts.style = {
						left:0,
						right:'',
						bottom:''
					};
					break;
				case 'center':
					opts.style = {
						right:'',
						bottom:''
					};
					break;
				case 'center-right':
					opts.style = {
						left:'',
						right:0,
						bottom:''
					};
					break;
				case 'bottom-left':
					opts.style = {
						left:0,
						right:'',
						top:'',
						bottom:-document.body.scrollTop-document.documentElement.scrollTop
					};
					break;
				case 'bottom-center':
					opts.style = {
						right:'',
						top:'',
						bottom:-document.body.scrollTop-document.documentElement.scrollTop
					};
					break;
				case 'bottom-right':
					delete opts.style;
					break;
				default:
					break;
			}
		
			delete opts.pos;
			jQuery.messager.show(opts);
		});
	}
	
	$.showMessage = function(options) {
		exuiloader.load('messager', function() {
			var defaults = {
				msg: null,
				style: $.config.STYLE,
				title: $.config.ALERT_TITLE,
				timeout: $.config.TIME_OUT,
				autoClose: null,
				modal: false,
				ok: '',
				okFn: null,
				cancel: '',
				cancelFn: null,
				closable: true
			};
			var opts = $.extend({}, defaults, options);
		
			jQuery.messager.defaults.ok = $.config.ok;
			jQuery.messager.defaults.cancel = $.config.cancel;
			
			if (opts.cancel) {
				$.xconfirm({msg:opts.msg, style:opts.style, ok:opts.ok, okFn:opts.okFn, cancel:opts.cancelFn, title:opts.title});
			} else {
				$.xalert({msg:opts.msg, style:opts.style, title:opts.title, ok:opts.ok, okFn:opts.okFn});
			}
			var win = jQuery("body > .messager-window:last > .messager-body");
			win.window({"modal": opts.modal});     // 模式窗口, 
			win.window({"draggable" : false}); // 禁止拖动窗口
			win.window({"shadow" : false});    // 去掉阴影
			var msEffect = 200;                // 效果持续时间
			function destroyMsgWin() { win.window('destroy'); }
			function closeCustomWin(ms) {
				try {
					if(!win.window("window")) return;
					if(win) {
						win.window("window").fadeOut(ms);
						setTimeout(function(){ destroyMsgWin(); }, ms);
					}
				} catch (e) {}
			}
			if(null != opts.autoClose && !!opts.autoClose && parseInt(timeout)>0) {
				win.window({"onBeforeClose": function() {
					if(win.timer) {
						clearTimeout(win.timer);
					}
					closeCustomWin(msEffect);
					return false;
				}});
				win.timer=setTimeout(function(){ closeCustomWin(msEffect); }, _timeout);
				win.window('window').hover(function(){
						if(win.timer) {
							clearTimeout(win.timer);
						}
					},function() {
						win.timer=setTimeout(function(){ closeCustomWin(msEffect); }, _timeout);
					}
				);
			}
			if(!opts.closable) {
				jQuery("body > .messager-window:last > .panel-heading > .panel-tool > .panel-tool-close").remove();
			}
		});
	}
	
	/*$.autoCloseCenterMessage = function(options) {
		var defaults = {
			msg: null,
			style: null,
			title: null,
			timeout: $.config.TIME_OUT,
			closable: true
		};
		var opts = $.extend({}, defaults, options);
		
		$.showMessage(opts);
	}
	
	$.autoCloseRightBottomMessage = function(options) {
		exuiloader.load('messager', function() {
			var defaults = {
				msg: null,
				title: null,
				timeout: $.config.TIME_OUT,
				showType: $.config.SHOW_TYPE
			};
			var opts = $.extend({}, defaults, options);
		
			jQuery.messager.show(opts);
		});
	}*/
	
	/*$.showUncloseMessage = function(options) {
		var defaults = {
			msg: null,
			style: null,
			title: null,
			ok: null,
			okFn: null,
			cancel: null,
			cancelFn: null
		};
		var opts = $.extend({}, defaults, options);
		
		$.showMessage(opts);
	}*/
	
	$.fn.xdraggable = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['draggable']) {
			return $this.draggable(options, param);
		} else {
			/*exuiloader.load('draggable', function() {
				result = $this.draggable(options, param);
			});*/
			exuiloader.load('draggable', null, true);
			return $this.draggable(options, param);
		}
		/*if (result) {
			return result;
		} else {
			return $this;
		}*/
	}
	
	$.fn.xdroppable = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['droppable']) {
			return $this.droppable(options, param);
		} else {
			exuiloader.load('droppable', null, true);
			return $this.droppable(options, param);
		}
	}
	
	$.fn.xresizable = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['resizable']) {
			return $this.resizable(options, param);
		} else {
			exuiloader.load('resizable', null, true);
			return $this.resizable(options, param);
		}
	}
	
	$.fn.xpagination = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['pagination']) {
			return $this.pagination(options, param);
		} else {
			exuiloader.load('pagination', null, true);
			return $this.pagination(options, param);
		}
	}
	
	$.fn.xsearchbox = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['searchbox']) {
			return $this.searchbox(options, param);
		} else {
			exuiloader.load('searchbox', null, true);
			return $this.searchbox(options, param);
		}
	}
	
	$.fn.xprogressbar = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['progressbar']) {
			return $this.progressbar(options, param);
		} else {
			exuiloader.load('progressbar', null, true);
			return $this.progressbar(options, param);
		}
	}
	
	$.fn.xtooltip = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['tooltip']) {
			return $this.tooltip(options, param);
		} else {
			exuiloader.load('tooltip', null, true);
			return $this.tooltip(options, param);
		}
	}
	
	$.fn.xpanel = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['panel']) {
			return $this.panel(options, param);
		} else {
			exuiloader.load('panel', null, true);
			return $this.panel(options, param);
		}
	}
	
	$.fn.xtabs = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['tabs']) {
			return $this.tabs(options, param);
		} else {
			exuiloader.load('tabs', null, true);
			return $this.tabs(options, param);
		}
	}
	
	$.fn.xaccordion = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['accordion']) {
			return $this.accordion(options, param);
		} else {
			exuiloader.load('accordion', null, true);
			return $this.accordion(options, param);
		}
	}
	
	$.fn.xlayout = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['layout']) {
			return $this.layout(options, param);
		} else {
			exuiloader.load('layout', null, true);
			return $this.layout(options, param);
		}
	}
	
	$.fn.xmenu = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['menu']) {
			return $this.menu(options, param);
		} else {
			exuiloader.load('menu', null, true);
			return $this.menu(options, param);
		}
	}
	
	$.fn.xlinkbutton = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['linkbutton']) {
			return $this.linkbutton(options, param);
		} else {
			exuiloader.load('linkbutton', null, true);
			return $this.linkbutton(options, param);
		}
	}
	
	$.fn.xmenubutton = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['menubutton']) {
			return $this.menubutton(options, param);
		} else {
			exuiloader.load('menubutton', null, true);
			return $this.menubutton(options, param);
		}
	}
	
	$.fn.xsplitbutton = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['splitbutton']) {
			return $this.splitbutton(options, param);
		} else {
			exuiloader.load('splitbutton', null, true);
			return $this.splitbutton(options, param);
		}
	}
	
	$.fn.xswitchbutton = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['switchbutton']) {
			return $this.switchbutton(options, param);
		} else {
			exuiloader.load('switchbutton', null, true);
			return $this.switchbutton(options, param);
		}
	}
	
	$.fn.xform = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['form']) {
			return $this.form(options, param);
		} else {
			exuiloader.load('form', null, true);
			return $this.form(options, param);
		}
	}
	
	$.fn.xvalidatebox = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['validatebox']) {
			return $this.validatebox(options, param);
		} else {
			exuiloader.load('validatebox', null, true);
			return $this.validatebox(options, param);
		}
	}
	
	$.fn.xtextbox = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['textbox']) {
			return $this.textbox(options, param);
		} else {
			exuiloader.load('textbox', null, true);
			return $this.textbox(options, param);
		}
	}
	
	$.fn.xpasswordbox = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['passwordbox']) {
			return $this.passwordbox(options, param);
		} else {
			exuiloader.load('passwordbox', null, true);
			return $this.passwordbox(options, param);
		}
	}
	
	$.fn.xcombo = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['combo']) {
			return $this.combo(options, param);
		} else {
			exuiloader.load('combo', null, true);
			return $this.combo(options, param);
		}
	}
	
	$.fn.xcombobox = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['combobox']) {
			return $this.combobox(options, param);
		} else {
			exuiloader.load('combobox', null, true);
			return $this.combobox(options, param);
		}
	}
	
	$.fn.xcombotree = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['combotree']) {
			return $this.combotree(options, param);
		} else {
			exuiloader.load('combotree', null, true);
			return $this.combotree(options, param);
		}
	}
	
	$.fn.xcombogrid = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['combogrid']) {
			return $this.combogrid(options, param);
		} else {
			exuiloader.load('combogrid', null, true);
			return $this.combogrid(options, param);
		}
	}
	
	$.fn.xcombotreegrid = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['combotreegrid']) {
			return $this.combotreegrid(options, param);
		} else {
			exuiloader.load('combotreegrid', null, true);
			return $this.combotreegrid(options, param);
		}
	}
	
	$.fn.xnumberbox = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['numberbox']) {
			return $this.numberbox(options, param);
		} else {
			exuiloader.load('numberbox', null, true);
			return $this.numberbox(options, param);
		}
	}
	
	$.fn.xdatebox = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['datebox']) {
			return $this.datebox(options, param);
		} else {
			exuiloader.load('datebox', null, true);
			return $this.datebox(options, param);
		}
	}
	
	$.fn.xdatetimebox = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['datetimebox']) {
			return $this.datetimebox(options, param);
		} else {
			exuiloader.load('datetimebox', null, true);
			return $this.datetimebox(options, param);
		}
	}
	
	$.fn.xdatetimespinner = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['datetimespinner']) {
			return $this.datetimespinner(options, param);
		} else {
			exuiloader.load('datetimespinner', null, true);
			return $this.datetimespinner(options, param);
		}
	}
	
	$.fn.xcalendar = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['calendar']) {
			return $this.calendar(options, param);
		} else {
			exuiloader.load('calendar', null, true);
			return $this.calendar(options, param);
		}
	}
	
	$.fn.xspinner = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['spinner']) {
			return $this.spinner(options, param);
		} else {
			exuiloader.load('spinner', null, true);
			return $this.spinner(options, param);
		}
	}
	
	$.fn.xnumberspinner = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['numberspinner']) {
			return $this.numberspinner(options, param);
		} else {
			exuiloader.load('numberspinner', null, true);
			return $this.numberspinner(options, param);
		}
	}
	
	$.fn.xtimespinner = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['timespinner']) {
			return $this.timespinner(options, param);
		} else {
			exuiloader.load('timespinner', null, true);
			return $this.timespinner(options, param);
		}
	}
	
	$.fn.xslider = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['slider']) {
			return $this.slider(options, param);
		} else {
			exuiloader.load('slider', null, true);
			return $this.slider(options, param);
		}
	}
	
	$.fn.xfilebox = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['filebox']) {
			return $this.filebox(options, param);
		} else {
			exuiloader.load('filebox', null, true);
			return $this.filebox(options, param);
		}
	}
	
	$.fn.xwindow = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['window']) {
			return $this.window(options, param);
		} else {
			exuiloader.load('window', null, true);
			return $this.window(options, param);
		}
	}
	
	$.fn.xdialog = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['dialog']) {
			return $this.dialog(options, param);
		} else {
			exuiloader.load('dialog', null, true);
			return $this.dialog(options, param);
		}
	}
	
	$.fn.xdatagrid = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['datagrid']) {
			return $this.datagrid(options, param);
		} else {
			exuiloader.load('datagrid', null, true);
			return $this.datagrid(options, param);
		}
	}
	
	$.fn.xdatalist = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['datalist']) {
			return $this.datalist(options, param);
		} else {
			exuiloader.load('datalist', null, true);
			return $this.datalist(options, param);
		}
	}
	
	$.fn.xpropertygrid = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['propertygrid']) {
			return $this.propertygrid(options, param);
		} else {
			exuiloader.load('propertygrid', null, true);
			return $this.propertygrid(options, param);
		}
	}
	
	$.fn.xtree = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['tree']) {
			return $this.tree(options, param);
		} else {
			exuiloader.load('tree', null, true);
			return $this.tree(options, param);
		}
	}
	
	$.fn.xtreegrid = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['treegrid']) {
			return $this.treegrid(options, param);
		} else {
			exuiloader.load('treegrid', null, true);
			return $this.treegrid(options, param);
		}
	}
	
	$.fn.xportal = function(options, param) {
		var $this = jQuery(this), result;
		if ('loaded' == exuiloader.queues['portal']) {
			return $this.portal(options, param);
		} else {
			exuiloader.load('portal', null, true);
			return $this.portal(options, param);
		}
	}
	
	$.xnotify = function(msg, settings) {
		//includeJS('/easyui/extension/bootstrap-notify.min.js');
		exuiloader.load('notify', function() {
			var _settings = {
				//type:'success',
				placement:{
					align: 'center'
				},
				delay: 1500,
				mouse_over: 'pause',
				template: '<div data-notify="container" class="col-xs-6 col-sm-4 col-md-3 col-lg-2 alert alert-{0}" role="alert">' +
					'<button type="button" aria-hidden="true" class="close" data-notify="dismiss">×</button>' +
					'<span data-notify="icon"></span> ' +
					'<span data-notify="title">{1}</span> ' +
					'<span data-notify="message">{2}</span>' +
					'<div class="progress" data-notify="progressbar">' +
						'<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
					'</div>' +
					'<a href="{3}" target="{4}" data-notify="url"></a>' +
				'</div>' 
			};
			settings = jQuery.extend({}, _settings, settings);
			
			$.notify(msg, settings);
		});
		
	}
	
	$.objMap = new Object();
	$.fn.xecharts = function(options, params) {
		exuiloader.load('echarts', null, true);
		
		var $this = jQuery(this),
			chart = null,
			opts = {};
		options && $.extend(opts, options);
		
		var getFileExp = function(file) {
			return file.substring(file.lastIndexOf(".") + 1);
		},
		getFileName = function(file) {
			return file.substring(0, file.indexOf("."));
		},
		createChart = function(target) {
			var params = $.data(target, 'echarts').params;
			// 初始化echarts实例，并判断是否加载主题
			if (null == params.chart) {
				if (params.theme) {
					exuiloader.load('extension/echarts/themes/' + params.theme + '.js', null, true);
					params.chart = echarts.init(target.get(0), params.theme);
				} else {
					params.chart = echarts.init(target.get(0));
				}
				if (params.resize) {
					function adaption() {
						params.chart.resize();
					}
					window.onresize = function () {
						$.exui.throttle(adaption);
					};
				}
			}
			chart = params.chart;
			// 显示数据加载动画
			if (params.showLoading) {
				params.chart.showLoading();
			}
			// 判断是否为异步加载
			if (params.url) {
				if (typeof opts == "object" && !$.isEmptyObject(opts)) {params.chart.setOption(opts);}
				$.ajax({url:params.url, data:params.data, dataType:'json', success:function(data) {
						params.onLoadSuccess.call(params.chart, data);
						if (params.showLoading) {
							params.chart.hideLoading();
						}
					}
				});
			} else {
				params.chart.setOption(opts);
				if (params.showLoading) {
					params.chart.hideLoading();
				}
			}
		},
		methods = {
			update: function(jq, params) {
				return jq.each(function(index) {
					var target = $.objMap[$this][index],
						state = $.data(target, 'echarts');
					$.extend(state.params, params);
					createChart.call(this, target);
				});
			}
		};
			
		if (typeof options == 'string') {
			var method = methods[options];
			if (method) {
				return method($this, params);
			}
		}
			
		params = params || {};
		$this.each(function(index) {
			var target = jQuery(this),
				state = $.data(target, 'echarts');
			if (0 == index) {
				$.objMap[$this] = [target];
			} else {
				$.objMap[$this].push(target);
			}
			if (state) {
				$.extend(state.params, params);
			} else {
				state = $.data(target, 'echarts', {
					params: $.extend({}, {
						url: null,
						data: null,
						map: null,
						theme: null,
						showLoading: false,
						chart: null,
						resize: true,
						onLoadSuccess: function(data){}
					}, params)
				});
			}
				
			if (state.params.map && 'js'==getFileExp(state.params.map)) {
				// JavaScript方式引入地图
				exuiloader.load('extension/echarts/map/js/' + state.params.map, null, true)
				createChart.call(this, target);
			} else if (state.params.map && 'json'==getFileExp(state.params.map)) {
				// JSON方式引入地图
				$.get($.APP_PATH + '/easyui/extension/echarts/map/json/' + state.params.map, function(mapJson) {
					echarts.registerMap(getFileName(state.params.map), mapJson);
					createChart.call(this, target);
				});
			} else {
				createChart.call(this, target);
			}
			return state.params.chart;
		});
		
		return chart || $this;
	}
	
	$.fn.xserialize = function(options) {
		var skips = {
			names: null,
			hidden: null,
			disabled: null
		};
		var opts = $.extend({}, skips, options);
		
		var attrs = {},
			gettedNames = [],
			target = jQuery(this);
		if (!opts.names) {
			opts.names = [];
		}
		
		var ipts = jQuery('[textboxname],[name],[slidername]', target).not('[class="textbox-value"]');
		if (ipts.length) {
			var checkBtn = {}; // 存储radio和checkbox的键值对
			//ipts.each(function(){
			jQuery.each(ipts, function(){
				var $this = jQuery(this),
					clsName = getPlugin($this);
				// textbox及继承自textbox的组件
				if (textboxMap[clsName]) {
					var name = $this.attr('textboxname');
					if(opts.disabled && $this[clsName]('options').disabled) {
						return true;
					}
					if(opts.names && -1!==$.inArray(name, opts.names)) {
						return true;
					}
					if ($this[clsName]('options').multiple){
						var val = $this[clsName]('getValues');
						extendJSON(name, val);
					} else {
						var val = $this[clsName]('getValue');
						extendJSON(name, val);
					}
					return true;
				}
				// slider
				if ('slider' == clsName) {
					var name = $this.attr('slidername');
					if (opts.disabled && $this[clsName]('options').disabled) {
						return true;
					}
					if(opts.names && -1!==$.inArray(name, opts.names)) {
						return true;
					}
					if ($this[clsName]('options').range) {
						var val = $this[clsName]('getValues');
						extendJSON(name, val);
					} else{
						var val = $this[clsName]('getValue');
						extendJSON(name, val);
					}
					return true;
				}
				// multiselect2side
				/*if ('multiselect2side' == clsName) {
					var name = $this.attr('name');
					if(opts.disabled && jQuery(this).next('.ms2side__div').find(':disabled').length) {
						return true;
					}
					if(opts.names && -1!==$.inArray(name, opts.names)) {
						return true;
					}
					var val = $this['multiselect2side']('getValue');
					extendJSON(name, val);
					return true;
				}*/
				
				if(opts.disabled && $this.is(":disabled")) {
					return true;
				}
				if(opts.hidden && $this.is(":hidden")) {
					return true;
				}
				// validatebox,input,textarea,select
				var cTypes = ['validatebox','input','textarea','select'],
					idx = $.inArray(clsName, cTypes);
				if (-1 != idx) {
					var name = $this.attr('name');
					if (0 == idx) {
						if (opts.disabled && $this[clsName]('options').disabled) {
							return true;
						}
					} else {
						
					}
					if(opts.names && -1!==$.inArray(name, opts.names)) {
						return true;
					}
					var val = $this.val();
					extendJSON(name, val);
					return true;
				}
				// radio,checkbox
				if ('radio'==clsName || 'checkbox'==clsName) {
					var name = $this.attr('name');
					if(opts.names && -1!==jQuery.inArray(name, opts.names)) {
						return true;
					}
					switch (clsName){
					case 'radio':
						if ($this.prop('checked')) {
							checkBtn[name] = $this.val();
						}
						return true;
					case 'checkbox':
						if ($this.prop('checked')) {
							if (checkBtn[name]) {
								checkBtn[name] += ',' + $this.val();
							} else{
								checkBtn[name] = $this.val();
							}
						}
						return true;
					}
				}	
			});
			jQuery.extend(attrs, checkBtn);
		}
		
		function extendJSON(name, val) {
			if(!name) return;
			if(-1 !== $.inArray(name, gettedNames)) {
				// 只获取第一个name的值
				return;
			} else {
				gettedNames.push(name);
			}
			val = 'undefined'!==typeof(val)? val:'';
			val = $.isArray(val)? val.join(','):val;
			//var newObj = eval('({"'+name+'":'+jQuery.parseJSON(val)+'})');
			var newObj = new Object();
			newObj[name] = val;
			$.extend(attrs, newObj);
		}
		return attrs;
	}
	
	$.fn.xdeserialize = function(data) {
		if (!data) {
			return;
		}
		if (typeof data === 'string') {
			data = $.parseJSON(data);
		} else if (typeof data !== 'object') {
			return;
		}
		
		var target = jQuery(this),
			prefixs = [];
		loopJSON(data);
		
		function loopJSON(json) {
			$.each(json, function(i, d){
				if(null === d) {
					
				} else if('object' === $.type(d) || 'array' === $.type(d)) {
					prefixs.push(i);
					loopJSON(d);
					prefixs.pop();
				} else {
					prefixs.push(i);
					var prefix = prefixs.join('.').replace(/(\.(\d+))(?=.)?/i, '[$2]');
					setNameVal(prefix, d);
					prefixs.pop();
				}
			});
		}
		
		function setNameVal(name, val) {
			name = 'undefined'!==typeof(name)? ''+name:'';
			if('' === name) return;
			val = 'undefined'!==typeof(val)? val:'';
			
			var ipts = jQuery('[textboxname="'+name+'"],[slidername="'+name+'"],[name="'+name+'"]', target).not('[class="textbox-value"]'),
				clsName = getPlugin(ipts);
			if (clsName) {
				// textbox及继承自textbox的组件
				if (textboxMap[clsName]) {
					var opts = ipts[clsName]('options');
					if (opts && opts.multiple) {
						ipts[clsName]('setValues', val);
					} else {
						if(-1!=$.inArray(clsName, ['datetimebox','datebox']) && $.isNumeric(val)) {
							var valDate = new Date();
							valDate.setTime(val);
							val = $.fn.datebox.defaults.formatter(valDate);
						}
						ipts[clsName]('setValue', val);
					}
					return;
				}
				// slider
				if ('slider' == clsName) {
					var t = ipts.filter(".slider-f");
					if (t[clsName]('options').range) {
						t[clsName]('setValues', val);
					} else{
						t[clsName]('setValue', val);
					}
					return;
				}
				// validatebox,input,textarea
				var cTypes = ['validatebox','input','textarea'],
					idx = $.inArray(clsName, cTypes);
				if (-1 != idx) {
					if (0 == idx) {
						ipts.val(val).validatebox('validate');
					} else{
						ipts.val(val);
					}
					return;
				}
				// multiselect2side,select
				/*var cTypes = ['multiselect2side', 'select'],
					idx = $.inArray(clsName, cTypes);
				if (-1 != idx) {
					if (0 == idx) {
						ipts[clsName]('setValue',val);
					} else {
						ipts.val(val);
					}
					return;
				}*/
				// radio,checkbox
				if ('radio'==clsName || 'checkbox'==clsName) {
					ipts.prop('checked',false).each(function() {
						var $this = jQuery(this);
						if (val && -1!==String(val).indexOf($this.val()) || $this.val() == String(val)){
							$this.prop('checked', true);
						}
					});
					return;
				}
			}
		}
	}
	
	$.ajaxSetup({
		statusCode: {
			401: function(res) {
				$.xalert({msg:res.statusText, style:'error', title:res.status});
			},
			403: function(res) {
				//UNAUTHORIZED
				if ('UNAUTHORIZED' === res.statusText) {
					$.xalert({msg:'没有该操作相应权限，请检查后再试', style:'warning', title:'没有权限'});
				} else if ('Forbidden' === res.statusText) {
					var msg="访问被拒绝,无访问权限!";
					if(null!=res.responseText&&res.responseText.length>0){
						msg=res.responseText;
					}
					$.xalert({msg:msg, style:'warning', title:'没有权限'});
				} else{
					$.xalert({msg:res.statusText, style:'error', title:res.status});
				}
			},
			404: function(res) {
				$.xalert({msg:res.statusText, style:'error', title:res.status});
			},
			500: function(res) {
				$.xalert({msg:res.statusText, style:'error', title:res.status});
			},
			503: function(res) {
				$.xalert({msg:res.statusText, style:'error', title:res.status});
			},
			504: function(res) {
				$.xalert({msg:res.statusText, style:'error', title:res.status});
			},
			599: function(res) {
				$.xalert({msg:res.statusText, style:'error', title:res.status});
			},
			4003: function(res) {
				$.xalert({msg:res.statusText, style:'error', title:res.status});
			},
			5003: function(res) {
				$.xalert({msg:res.statusText, style:'error', title:res.status});
			},
			5004: function(res) {
				$.xalert({msg:res.statusText, style:'error', title:res.status});
			}
		}
	});
	
})));