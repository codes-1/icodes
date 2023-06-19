/**
 * EasyUI for jQuery 1.5.4.1
 * 
 * Copyright (c) 2009-2018 www.jeasyui.com. All rights reserved.
 *
 * Licensed under the freeware license: http://www.jeasyui.com/license_freeware.php
 * To use it on other terms please contact us: info@jeasyui.com
 *
 */
/**
 * easyloader - EasyUI for jQuery
 * 
 */
(function() {
	var modules = {
		draggable: {
			js: 'jquery.draggable.js',
			init: true
		},
		droppable: {
			js: 'jquery.droppable.js',
			init: true
		},
		resizable: {
			js: 'jquery.resizable.js',
			init: true
		},
		linkbutton: {
			js: 'jquery.linkbutton.js',
			css: 'linkbutton.css',
			init: true
		},
		progressbar: {
			js: 'jquery.progressbar.js',
			css: 'progressbar.css',
			init: true
		},
		tooltip: {
			js: 'jquery.tooltip.js',
			css: 'tooltip.css',
			init: true
		},
		pagination: {
			js: 'jquery.pagination.js',
			css: 'pagination.css',
			dependencies: ['linkbutton'],
			init: true
		},
		datagrid: {
			js: 'jquery.datagrid.js',
			css: 'datagrid.css',
			dependencies: ['panel', 'resizable', 'linkbutton', 'pagination'],
			init: true
		},
		treegrid: {
			js: 'jquery.treegrid.js',
			css: 'tree.css',
			dependencies: ['datagrid'],
			init: true
		},
		propertygrid: {
			js: 'jquery.propertygrid.js',
			css: 'propertygrid.css',
			dependencies: ['datagrid'],
			init: true
		},
		datalist: {
			js: 'jquery.datalist.js',
			css: 'datalist.css',
			dependencies: ['datagrid'],
			init: true
		},
		panel: {
			js: 'jquery.panel.js',
			css: 'panel.css',
			init: true
		},
		window: {
			js: 'jquery.window.js',
			css: 'window.css',
			dependencies: ['resizable', 'draggable', 'panel'],
			init: true
		},
		dialog: {
			js: 'jquery.dialog.js',
			css: 'dialog.css',
			dependencies: ['linkbutton', 'window'],
			init: true
		},
		messager: {
			js: 'jquery.messager.js',
			css: 'messager.css',
			dependencies: ['linkbutton', 'dialog', 'progressbar'],
			init: false
		},
		layout: {
			js: 'jquery.layout.js',
			css: 'layout.css',
			dependencies: ['resizable', 'panel'],
			init: true
		},
		form: {
			js: 'jquery.form.js',
			init: true
		},
		menu: {
			js: 'jquery.menu.js',
			css: 'menu.css',
			init: true
		},
		tabs: {
			js: 'jquery.tabs.js',
			css: 'tabs.css',
			dependencies: ['panel', 'linkbutton'],
			init: true
		},
		menubutton: {
			js: 'jquery.menubutton.js',
			css: 'menubutton.css',
			dependencies: ['linkbutton', 'menu'],
			init: true
		},
		splitbutton: {
			js: 'jquery.splitbutton.js',
			css: 'splitbutton.css',
			dependencies: ['menubutton'],
			init: true
		},
		switchbutton: {
			js: 'jquery.switchbutton.js',
			css: 'switchbutton.css',
			init: true
		},
		accordion: {
			js: 'jquery.accordion.js',
			css: 'accordion.css',
			dependencies: ['panel'],
			init: true
		},
		calendar: {
			js: 'jquery.calendar.js',
			css: 'calendar.css',
			init: true
		},
		textbox: {
			js: 'jquery.textbox.js',
			css:'textbox.css',
			dependencies: ['validatebox', 'linkbutton'],
			init: true
		},
		passwordbox: {
			js: 'jquery.passwordbox.js',
			css: 'passwordbox.css',
			dependencies: ['textbox'],
			init: true
		},
		filebox: {
			js: 'jquery.filebox.js',
			css: 'filebox.css',
			dependencies: ['textbox'],
			init: true
		},
		combo: {
			js: 'jquery.combo.js',
			css: 'combo.css',
			dependencies: ['panel', 'textbox'],
			init: true
		},
		combobox: {
			js: 'jquery.combobox.js',
			css: 'combobox.css',
			dependencies: ['combo'],
			init: true
		},
		combotree: {
			js: 'jquery.combotree.js',
			dependencies: ['combo', 'tree'],
			init: true
		},
		combogrid: {
			js: 'jquery.combogrid.js',
			dependencies: ['combo', 'datagrid'],
			init: true
		},
		combotreegrid: {
			js: 'jquery.combotreegrid.js',
			dependencies: ['combo', 'treegrid'],
			init: true
		},
		validatebox: {
			js: 'jquery.validatebox.js',
			css: 'validatebox.css',
			dependencies: ['tooltip'],
			init: true
		},
		numberbox: {
			js: 'jquery.numberbox.js',
			dependencies: ['textbox'],
			init: true
		},
		searchbox: {
			js: 'jquery.searchbox.js',
			css: 'searchbox.css',
			dependencies: ['menubutton', 'textbox'],
			init: true
		},
		spinner: {
			js: 'jquery.spinner.js',
			css: 'spinner.css',
			dependencies: ['textbox'],
			init: true
		},
		numberspinner: {
			js: 'jquery.numberspinner.js',
			dependencies: ['spinner', 'numberbox'],
			init: true
		},
		timespinner: {
			js: 'jquery.timespinner.js',
			dependencies: ['spinner'],
			init: true
		},
		datetimespinner: {
			js: 'jquery.datetimespinner.js',
			dependencies: ['datebox', 'timespinner'],
			init: true
		},
		tree: {
			js: 'jquery.tree.js',
			css: 'tree.css',
			dependencies: ['draggable', 'droppable'],
			init: true
		},
		datebox: {
			js: 'jquery.datebox.js',
			css: 'datebox.css',
			dependencies: ['calendar', 'combo'],
			init: true
		},
		datetimebox: {
			js: 'jquery.datetimebox.js',
			dependencies: ['datebox', 'timespinner'],
			init: true
		},
		slider: {
			js: 'jquery.slider.js',
			css: 'slider.css',
			dependencies: ['draggable'],
			init: true
		},
		parser: {
			js: 'jquery.parser.js'
		},
		mobile: {
			js: 'jquery.mobile.js'
		},
		validate_extend: {
			js: 'extension/jquery.validate_extend.js',
			init: false
		},
		portal: {
			js: 'extension/jquery.portal.js',
			dependencies: ['panel', 'draggable'],
			init: true
		},
		detailview: {
			js: 'extension/datagridview/datagrid-detailview.js',
			dependencies: ['datagrid'],
			init: false
		},
		groupview: {
			js: 'extension/datagridview/datagrid-groupview.js',
			dependencies: ['datagrid'],
			init: false
		},
		bufferview: {
			js: 'extension/datagridview/datagrid-bufferview.js',
			dependencies: ['datagrid'],
			init: false
		},
		scrollview: {
			js: 'extension/datagridview/datagrid-scrollview.js',
			dependencies: ['datagrid'],
			init: false
		},
		edatagrid: {
			js: 'extension/jquery.edatagrid.js',
			dependencies: ['datagrid'],
			init: true
		},
		cellediting: {
			js: 'extension/datagrid-cellediting.js',
			dependencies: ['datagrid'],
			init: false
		},
		columnsext: {
			js: 'extension/columns-ext.js',
			dependencies: ['treegrid'],
			init: false
		},
		datagridfilter: {
			js: 'extension/datagrid-filter.js',
			dependencies: ['datagrid'],
			init: false
		},
		datagriddnd: {
			js: 'extension/datagrid-dnd.js',
			dependencies: ['datagrid'],
			init: false
		},
		etree: {
			js: 'extension/jquery.etree.js',
			dependencies: ['tree'],
			init: true
		},
		treegridnd: {
			js: 'extension/treegrid-dnd.js',
			dependencies: ['treegrid'],
			init: false
		},
		pivotgrid: {
			js: 'extension/jquery.pivotgrid.js',
			dependencies: ['treegrid'],
			init: true
		},
		ribbon: {
			js: 'extension/ribbon/jquery.ribbon.js',
			css: 'extension/ribbon/ribbon.css',
			dependencies: ['tabs', 'ribbonicon'],
			init: true
		},
		ribbonicon: {
			css: 'extension/ribbon/ribbon-icon.css',
		},
		texteditor: {
			js: 'extension/texteditor/jquery.texteditor.js',
			css: 'extension/texteditor/texteditor.css',
			dependencies: ['dialog'],
			init: true
		},
		color: {
			js: 'extension/jquery.color.js',
			dependencies: ['combo'],
			init: true
		},
		echarts: {
			js: 'extension/echarts/echarts.js'
		},
		notify: {
			js: 'extension/bootstrap-notify.min.js'
		},
		printarea: {
			js: 'extension/printarea/jquery.PrintArea.js',
			css: 'extension/printarea/PrintArea.css'
		}
	};

	var locales = {
		'zh_cn': 'exui-lang_zh_CN.js'
	};

	//var queues = {};

	function loadJs(url, callback, sync) {
		var async = sync ? false : true;
		jQuery.ajax({
			cache: true,
			async: async,
			url: url,
			dataType:'script',
			scriptCharset: "utf-8",
			success: function() {
				if(callback) {
					callback();
				}
			}
		});
			
		/*if (sync) {
			if('undefined' === typeof exuiloader.jsMap[url]) {
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
				scriptCharset: "utf-8",
				success: function() {
					if(callback) {
						callback();
					}
				}
			});
			exuiloader.jsMap[url] = true;
		} else {
			var done = false;
			var script = document.createElement('script');
			script.type = 'text/javascript';
			script.language = 'javascript';
			script.src = url;
			script.onload = script.onreadystatechange = function() {
				if(!done && (!script.readyState || script.readyState == 'loaded' || script.readyState == 'complete')) {
					done = true;
					script.onload = script.onreadystatechange = null;
					if(callback) {
						callback.call(script);
					}
				}
			}
			document.getElementsByTagName("head")[0].appendChild(script);
		}*/
	}

	function runJs(url, callback) {
		loadJs(url, function() {
			//document.getElementsByTagName("head")[0].removeChild(this);
			if(callback) {
				callback();
			}
		});
	}

	function loadCss(url, callback) {
		var link = document.createElement('link');
		link.rel = 'stylesheet';
		link.type = 'text/css';
		link.media = 'screen';
		link.href = url;
		document.getElementsByTagName('head')[0].appendChild(link);
		if(callback) {
			callback.call(link);
		}
	}

	function loadSingle(name, callback, sync) {
		exuiloader.queues[name] = 'loading';

		var module = modules[name];
		var jsStatus = 'loading';
		var cssStatus = (exuiloader.css && module['css']) ? 'loading' : 'loaded';

		if(exuiloader.css && module['css']) {
			if(/^http/i.test(module['css'])) {
				var url = module['css'];
			} else {
				var url = exuiloader.base + 'themes/' + exuiloader.theme + '/' + module['css'];
			}
			loadCss(url, function() {
				cssStatus = 'loaded';
				if(jsStatus == 'loaded' && cssStatus == 'loaded') {
					finish();
				}
			});
		}

		if(/^http/i.test(module['js'])) {
			var url = module['js'];
		} else {
			if (module['js'].indexOf('extension') >= 0){
				var url = exuiloader.base + module['js'];
			} else {
				var url = exuiloader.base + 'plugins/' + module['js'];
			}
		}
		
		loadJs(url, function() {
			jsStatus = 'loaded';
			if(jsStatus == 'loaded' && cssStatus == 'loaded') {
				finish();
			}
		}, sync);
		/*if (sync) {
			loadJs(url);
		} else {
			loadJs(url, function() {
				jsStatus = 'loaded';
				if(jsStatus == 'loaded' && cssStatus == 'loaded') {
					finish();
				}
			});
		}*/

		function finish() {
			exuiloader.queues[name] = 'loaded';
			exuiloader.onProgress(name);
			if(callback) {
				callback();
			}
		}
	}

	function loadModule(name, callback, sync) {
		var mm = [];
		var doLoad = false;

		if(typeof name == 'string') {
			add(name);
		} else {
			for(var i = 0; i < name.length; i++) {
				add(name[i]);
			}
		}

		function add(name) {
			if(!modules[name]) return;
			var d = modules[name]['dependencies'];
			if(d) {
				for(var i = 0; i < d.length; i++) {
					add(d[i]);
				}
			}
			mm.push(name);
		}

		function finish() {
			if(callback) {
				callback();
			}
			exuiloader.onLoad(name);
		}

		var time = 0;

		function loadMm() {
			if(mm.length) {
				var m = mm[0]; // the first module
				if(!exuiloader.queues[m]) {
					doLoad = true;
					loadSingle(m, function() {
						mm.shift();
						loadMm();
					}, sync);
				} else if(exuiloader.queues[m] == 'loaded') {
					mm.shift();
					loadMm();
				} else {
					arguments.callee;
					/*if(time < exuiloader.timeout) {
						time += 10;
						setTimeout(arguments.callee, 10);
					}*/
				}
			} else {
				if(exuiloader.locale && doLoad == true && locales[exuiloader.locale]) {
					var url = exuiloader.base + 'locale/' + locales[exuiloader.locale];
					runJs(url, function() {
						finish();
					});
				} else {
					finish();
				}
			}
		}

		loadMm();
	}

	exuiloader = {
		modules: modules,
		locales: locales,
		queues: {},
		jsMap: {},
		
		base: '.',
		theme: 'material',
		css: true,
		locale: null,
		timeout: 2000,
		
		load: function(name, callback, sync) {
			if (/\.css$/i.test(name)){
				if (/^http/i.test(name)){
					loadCss(name, callback);
				} else {
					loadCss(exuiloader.base + name, callback);
				}
			} else if (/\.js$/i.test(name)){
				if (/^http/i.test(name)){
					loadJs(name, callback);
				} else {
					loadJs(exuiloader.base + name, callback);
				}
			} else {
				loadModule(name, callback, sync);
			}
		},
		onProgress: function(name){},
		onLoad: function(name){}
	};

	/*exuiloader.load = function(name, callback) {
		if(_timerLoading) clearTimeout(_timerLoading);

		var mm = [];
		var _valiext = false;
		if('string' === typeof(name)) {
			add(name);
			if(name === 'validatebox') _valiext = true;
		} else {
			for(var i = 0; i < name.length; i++) {
				add(name[i]);
				if(name[i] === 'validatebox') _valiext = true;
			}
		}

		function add(_f, _fc) {
			if(!modules[_f]) return;
			var d0 = modules[_f]["dependencies"];
			if(d0 && d0.length) {
				for(var i = 0; i < d0.length; i++) {
					add(d0[i]);
					if(d0[i] === 'validatebox') _valiext = true;
				}
			}
			mm.push(modules[_f]);
		}

		function includeF(mm) {
			if(mm && mm.css) {
				includeCSS('/js/exui/themes/' + exuiloader.theme + '/' + mm.css);
			}
			if(mm && mm.js) {
				includeJS('/js/exui/plugins/' + mm.js);
			}
		}
		if(mm && mm.length) {
			for(var i = 0; i < mm.length; i++) {
				includeF(mm[i]);
			}
		}
		if('undefined' != typeof(exuiloader.locales[exuiloader.locale]) && exuiloader.locales[exuiloader.locale]) {
			//includeJS('/js/exui/locale/' + exuiloader.locales[exuiloader.locale], 1);
			if('undefined' === typeof(langUsed)) {
				includeJS('/js/exui/locale/' + exuiloader.locales[exuiloader.locale], 1);
			}
			langUsed.call(jQuery);
		}
		if(_valiext) {
			includeF(modules['validate_extend']);
		}
		mm = null;
		if(callback) callback();
		_timerLoading = setTimeout(_showBodyElement, 120);
	};*/
	
	var scripts = document.getElementsByTagName('script');
	for(var i=0; i<scripts.length; i++){
		var src = scripts[i].src;
		if (!src) continue;
		var m = src.match(/exuiloader\.js(\W|$)/i);
		if (m){
			exuiloader.base = src.substring(0, m.index);
		}
	}

	window.using = exuiloader.load;

	if(window.jQuery) {
		jQuery(function() {
			exuiloader.load('themes/icon.css');
			/*exuiloader.load('parser', function() {
				jQuery.parser.parse();
			});*/
			exuiloader.load('parser', null, true);
			jQuery.parser.parse();
		});
	}
})();