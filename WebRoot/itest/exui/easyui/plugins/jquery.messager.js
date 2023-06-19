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
    function adaption() {
        // center window
        $("body div.messager-body").window('center');
    }

    function winResize(mode) {
        if (mode) {
            $(window).unbind('resize.messager').bind('resize.messager', function(e) {
                $.exui.throttle(adaption);
            });
        } else {
            $(window).unbind('resize.messager');
        }
    }

    function keyEvent() {
        $(document).unbind(".messager").bind("keydown.messager", function(e) {
            if (e.keyCode == 27) {
                $("body").children("div.messager-window").children("div.messager-body").each(function() {
                    $(this).dialog("close");
                });
            } else {
                if (e.keyCode == 9) {
                    var win = $("body").children("div.messager-window");
                    if (!win.length) {
                        return;
                    }
                    var components = win.find(".messager-input,.messager-button .l-btn");
                    for (var i = 0; i < components.length; i++) {
                        if ($(components[i]).is(":focus")) {
                            $(components[i >= components.length - 1 ? 0 : i + 1]).focus();
                            return false;
                        }
                    }
                } else {
                    if (e.keyCode == 13) {
                        var inputFields = $(e.target).closest("input.messager-input");
                        if (inputFields.length) {
                            var mbody = inputFields.closest(".messager-body");
                            btnClickEvent(mbody, inputFields.val());
                        }
                    }
                }
            }
        });
    };

    function unbindEvent() {
        $(document).unbind(".messager");
    };

    function showMessage(options) {
        var mopts = $.extend({}, $.messager.defaults, {
            modal: false,
            shadow: false,
            draggable: false,
            resizable: false,
            closed: true,
            style: {
                left: "",
                top: "",
                right: 0,
                zIndex: $.fn.window.defaults.zIndex++,
                bottom: -document.body.scrollTop - document.documentElement.scrollTop
            },
            title: "",
            width: 300,
            height: "auto",
            minHeight: 0,
            showType: "slide",
            showSpeed: 600,
            content: options.msg,
            timeout: 4000
        }, options);
        var win = $("<div class=\"messager-body\"></div>").appendTo("body");
        win.dialog($.extend({}, mopts, {
            noheader: (mopts.title ? false : true),
            openAnimation: (mopts.showType),
            closeAnimation: (mopts.showType == "show" ? "hide" : mopts.showType),
            openDuration: mopts.showSpeed,
            closeDuration: mopts.showSpeed,
            onOpen: function() {
                win.dialog("dialog").hover(function() {
                    if (mopts.timer) {
                        clearTimeout(mopts.timer);
                    }
                },
                function() {
                    hoverOut();
                });
                hoverOut();

                function hoverOut() {
                    if (mopts.timeout > 0) {
                        mopts.timer = setTimeout(function() {
                            if (win.length && win.data("dialog")) {
                                win.dialog("close");
                            }
                        }, mopts.timeout);
                    }
                };
                if (options.onOpen) {
                    options.onOpen.call(this);
                } else {
                    mopts.onOpen.call(this);
                }
            },
            onClose: function() {
                if (mopts.timer) {
                    clearTimeout(mopts.timer);
                }
                if (options.onClose) {
                    options.onClose.call(this);
                } else {
                    mopts.onClose.call(this);
                }
                win.dialog("destroy");
            }
        }));
        win.dialog("dialog").css(mopts.style);
        win.dialog("open");
        if (!$.isNumeric(mopts.style.left) && !$.isNumeric(mopts.style.right) && !$.isNumeric(mopts.style.top) && !$.isNumeric(mopts.style.bottom)) {
            winResize(true);
        } else {
            winResize(false);
        }
        return win;
    };

    function createDialog(options) {
        keyEvent();
        var mbody = $("<div class=\"messager-body\"></div>").appendTo("body");
        mbody.dialog($.extend({}, options, {
            noheader: (options.title ? false : true),
            onClose: function() {
                unbindEvent();
                if (options.onClose) {
                    options.onClose.call(this);
                }
                mbody.dialog("destroy");
            }
        }));
        var win = mbody.dialog("dialog").addClass("messager-window");
        win.find(".dialog-button").addClass("messager-button").find("a:first").focus();
        return mbody;
    };

    function btnClickEvent(dlg, mode) {
    	dlg.dialog("options").fn(mode);
        dlg.dialog("close");
    };
    $.messager = {
        show: function(options) {
            return showMessage(options);
        },
        alert: function(title, msg, icon, fn) {
            var _title = typeof title == "object" ? title : {
                title: title,
                msg: msg,
                icon: icon,
                fn: fn
            };
            var cls = _title.icon ? "messager-icon messager-" + _title.icon : "";
            _title = $.extend({}, $.messager.defaults, {
                //content: "<div class=\"" + cls + "\"></div>" + "<div>" + _title.msg + "</div>" + "<div style=\"clear:both;\"/>"
                content: '<div class="' + cls + '"></div><div>' + _title.msg + '</div><div style="clear:both;"></div>'
            }, _title);
            if (!_title.buttons) {
                _title.buttons = [{
                    text: _title.ok,
                    onClick: function() {
                        btnClickEvent(dlg);
                    }
                }];
            }
            switch (_title.icon) {
                case 'error':
                    _title.cls = 'panel-danger'; //_title.cls = 'panel-danger card-danger-light';
                    break;
                case 'info':
                    _title.cls = 'panel-info'; //_title.cls = 'panel-info card-info-light';
                    break;
                case 'question':
                    _title.cls = 'panel-success'; //_title.cls = 'panel-success card-success-light';
                    break;
                case 'warning':
                    _title.cls = 'panel-warning'; //_title.cls = 'panel-warning card-warning-light';
                    break;
                case 'dark':
                    _title.cls = 'panel-dark';
                    break;
                default:
                    break;
            }
            var dlg = createDialog(_title);
            winResize(true);
            return dlg;
        },
        confirm: function(title, msg, fn) {
            var _title = typeof title == "object" ? title : {
                title: title,
                msg: msg,
                fn: fn
            };
            _title = $.extend({}, $.messager.defaults, {
                //content: "<div class=\"messager-icon messager-question\"></div>" + "<div>" + _title.msg + "</div>" + "<div style=\"clear:both;\"/>"
                content: '<div class="messager-icon messager-question"></div><div>' + _title.msg + '</div><div style="clear:both;"></div>'
            }, _title);
            if (!_title.buttons) {
                _title.buttons = [{
                    text: _title.ok,
                    onClick: function() {
                        btnClickEvent(dlg, true);
                    }
                }, {
                    text: _title.cancel,
                    onClick: function() {
                        btnClickEvent(dlg, false);
                    }
                }];
            }
            var dlg = createDialog(_title);
            winResize(true);
            return dlg;
        },
        prompt: function(title, msg, fn) {
            var _title = typeof title == "object" ? title : {
                title: title,
                msg: msg,
                fn: fn
            };
            _title = $.extend({}, $.messager.defaults, {
                //content: "<div class=\"messager-icon messager-question\"></div>" + "<div>" + _title.msg + "</div>" + "<br/>" + "<div style=\"clear:both;\"/>" + "<div><input class=\"messager-input\" type=\"text\"/></div>"
                content: '<div class="messager-icon messager-question"></div><div>' + _title.msg + '</div><br/><div style="clear:both;"></div><div><input class="messager-input form-control" type="text"/></div>'
            }, _title);
            if (!_title.buttons) {
                _title.buttons = [{
                    text: _title.ok,
                    onClick: function() {
                        btnClickEvent(dlg, dlg.find(".messager-input").val());
                    }
                }, {
                    text: _title.cancel,
                    onClick: function() {
                        btnClickEvent(dlg);
                    }
                }];
            }
            var dlg = createDialog(_title);
            dlg.find("input.messager-input").focus();
            winResize(true);
            return dlg;
        },
        progress: function(options) {
            var methods = {
                bar: function() {
                    return $("body>div.messager-window").find("div.messager-p-bar");
                },
                close: function() {
                    var dlg = $("body>div.messager-window>div.messager-body:has(div.messager-progress)");
                    if (dlg.length) {
                        dlg.dialog("close");
                    }
                }
            };
            if (typeof options == "string") {
                var method = methods[options];
                return method();
            }
            options = options || {};
            var progress = $.extend({}, {
                title: "",
                minHeight: 0,
                content: undefined,
                msg: "",
                text: undefined,
                interval: 300
            }, options);
            var dlg = createDialog($.extend({}, $.messager.defaults, {
                //content: "<div class=\"messager-progress\"><div class=\"messager-p-msg\">" + progress.msg + "</div><div class=\"messager-p-bar\"></div></div>",
                content: '<div class="messager-progress"><div class="messager-p-msg">' + progress.msg + '</div><div class="messager-p-bar"></div></div>',
                closable: false,
                doSize: false
            }, progress, {
                onClose: function() {
                    if (this.timer) {
                        clearInterval(this.timer);
                    }
                    if (options.onClose) {
                        options.onClose.call(this);
                    } else {
                        $.messager.defaults.onClose.call(this);
                    }
                }
            }));
            var bar = dlg.find("div.messager-p-bar");
            bar.progressbar({
                text: progress.text
            });
            dlg.dialog("resize");
            if (progress.interval) {
                dlg[0].timer = setInterval(function() {
                    var v = bar.progressbar("getValue");
                    v += 10;
                    if (v > 100) {
                        v = 0;
                    }
                    bar.progressbar("setValue", v);
                }, progress.interval);
            }
            winResize(true);
            return dlg;
        }
    };
    $.messager.defaults = $.extend({}, $.fn.dialog.defaults, {
        ok: "Ok",
        cancel: "Cancel",
        width: 300,
        height: "auto",
        minHeight: 150,
        modal: true,
        collapsible: false,
        minimizable: false,
        maximizable: false,
        resizable: false,
        fn: function() {}
    });
})(jQuery);
