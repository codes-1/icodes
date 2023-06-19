/**
 * EasyUI for jQuery 1.5.4.1
 * 
 * Copyright (c) 2009-2018 www.jeasyui.com. All rights reserved.
 *
 * Licensed under the freeware license: http://www.jeasyui.com/license_freeware.php
 * To use it on other terms please contact us: info@jeasyui.com
 *
 */
(function($) {
	/**
	 * build the dialog
	 */
    function buildDialog(target) {
        var opts = $.data(target, "dialog").options;
        opts.inited = false;
        
        $(target).window($.extend({}, opts, {
            onResize: function(w, h) {
                if (opts.inited) {
                    resize(this);
                    opts.onResize.call(this, w, h);
                }
            }
        }));
        
        var win = $(target).window("window");
        if (opts.toolbar) {
            if ($.isArray(opts.toolbar)) {
                $(target).siblings("div.dialog-toolbar").remove();
                var toolbar = $('<div class="dialog-toolbar"><table cellspacing="0" cellpadding="0"><tr></tr></table></div>').appendTo(win);
                var tr = toolbar.find("tr");
                
                for (var i = 0; i < opts.toolbar.length; i++) {
                    var p = opts.toolbar[i];
                    
                    if (p == "-") {
                        $('<td><div class="dialog-tool-separator"></div></td>').appendTo(tr);
                    } else {
                        var td = $("<td></td>").appendTo(tr);
                        var tool = $('<a href="javascript:void(0)"></a>').appendTo(td);
                        
                        tool[0].onclick = eval(p.handler || function() {});
                        tool.linkbutton($.extend({}, p, {plain: true}));
                    }
                }
            } else {
                $(opts.toolbar).addClass("dialog-toolbar").appendTo(win);
                $(opts.toolbar).show();
            }
        } else {
            $(target).siblings("div.dialog-toolbar").remove();
        }
        
        if (opts.buttons) {
            if ($.isArray(opts.buttons)) {
                $(target).siblings("div.dialog-button").remove();
                
                var buttons = $('<div class="dialog-button"></div>').appendTo(win);
                for (var i = 0; i < opts.buttons.length; i++) {
                    var p = opts.buttons[i];
                    var button = $('<a href="javascript:void(0)"></a>').appendTo(buttons);
                    if (p.handler) {
                        button[0].onclick = p.handler;
                    }
                    button.linkbutton(p);
                }
            } else {
                $(opts.buttons).addClass("dialog-button").appendTo(win);
                $(opts.buttons).show();
            }
        } else {
            $(target).siblings("div.dialog-button").remove();
        }
        opts.inited = true;
        
        var closed = opts.closed;
        win.show();
        $(target).window("resize", {});
        if (closed) {
            win.hide();
        }
    };
    
    function resize(target, width) {
        var t = $(target);
        var opts = t.dialog("options");
        var noheader = opts.noheader;
        var tb = t.siblings(".dialog-toolbar");
        var bb = t.siblings(".dialog-button");
        
        tb.insertBefore(target).css({
            borderTopWidth: (noheader ? 1 : 0),
            top: (noheader ? tb.length: 0)
        });
        bb.insertAfter(target);
        
        tb.add(bb)._outerWidth(t._outerWidth()).find(".exui-fluid:visible").each(function() {
            $(this).triggerHandler("_resize");
        });
        
        var height = tb._outerHeight() + bb._outerHeight();
        if (!isNaN(parseInt(opts.height))) {
            t._outerHeight(t._outerHeight() - height);
        } else {
            var minHeight = t._size("min-height");
            if (minHeight) {
                t._size("min-height", minHeight - height);
            }
            var maxHeight = t._size("max-height");
            if (maxHeight) {
                t._size("max-height", maxHeight - height);
            }
        }
        
        var shadow = $.data(target, "window").shadow;
        if (shadow) {
            var cc = t.panel("panel");
            shadow.css({
                width: cc._outerWidth(),
                height: cc._outerHeight()
            });
        }
    };
    
    $.fn.dialog = function(options, param) {
        if (typeof options == "string") {
            var method = $.fn.dialog.methods[options];
            if (method) {
                return method(this, param);
            } else {
                return this.window(options, param);
            }
        }
        
        options = options || {};
        return this.each(function() {
            var state = $.data(this, "dialog");
            if (state) {
                $.extend(state.options, options);
            } else {
                $.data(this, "dialog", {
                    options: $.extend({}, $.fn.dialog.defaults, $.fn.dialog.parseOptions(this), options)
                });
            }
            
            buildDialog(this);
        });
    };
    
    $.fn.dialog.methods = {
        options: function(jq) {
            var opts = $.data(jq[0], "dialog").options;
            var popts = jq.panel("options");
            
            $.extend(opts, {
                width: popts.width,
                height: popts.height,
                left: popts.left,
                top: popts.top,
                closed: popts.closed,
                collapsed: popts.collapsed,
                minimized: popts.minimized,
                maximized: popts.maximized
            });
            return opts;
        },
        dialog: function(jq) {
            return jq.window("window");
        }
    };
    
    $.fn.dialog.parseOptions = function(target) {
        var t = $(target);
        return $.extend({}, $.fn.window.parseOptions(target), $.parser.parseOptions(target, ["toolbar", "buttons"]), {
            toolbar: (t.children(".dialog-toolbar").length ? t.children(".dialog-toolbar").removeClass("dialog-toolbar") : undefined),
            buttons: (t.children(".dialog-button").length ? t.children(".dialog-button").removeClass("dialog-button") : undefined)
        });
    };
    
    $.fn.dialog.defaults = $.extend({}, $.fn.window.defaults, {
        title: "New Dialog",
        collapsible: false,
        minimizable: false,
        maximizable: false,
        resizable: false,
        toolbar: null,
        buttons: null
    });
})(jQuery);