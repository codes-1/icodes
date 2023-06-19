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
    function init(target) {
        var state = $.data(target, "datetimebox");
        var opts = state.options;
        $(target).datebox($.extend({},
        opts, {
            onShowPanel: function() {
                var value = $(this).datetimebox("getValue");
                setValue(this, value, true);
                opts.onShowPanel.call(this);
            },
            formatter: $.fn.datebox.defaults.formatter,
            parser: $.fn.datebox.defaults.parser
        }));
        $(target).removeClass("datebox-f").addClass("datetimebox-f");
        $(target).datebox("calendar").calendar({
            onSelect: function(date) {
                opts.onSelect.call(this.target, date);
            }
        });
        if (!state.spinner) {
            var panel = $(target).datebox("panel");
            var p = $("<div style=\"padding:2px\"><input></div>").insertAfter(panel.children("div.datebox-calendar-inner"));
            state.spinner = p.children("input");
        }
        state.spinner.timespinner({
            width: opts.spinnerWidth,
            showSeconds: opts.showSeconds,
            separator: opts.timeSeparator
        });
        $(target).datetimebox("initValue", opts.value);
    };
    function initCurrentTime(target) {
        var cal = $(target).datetimebox("calendar");
        var spinner = $(target).datetimebox("spinner");
        var current = cal.calendar("options").current;
        return new Date(current.getFullYear(), current.getMonth(), current.getDate(), spinner.timespinner("getHours"), spinner.timespinner("getMinutes"), spinner.timespinner("getSeconds"));
    };
    function query(target, q) {
        setValue(target, q, true);
    };
    function enter(target) {
        var opts = $.data(target, "datetimebox").options;
        var date = initCurrentTime(target);
        setValue(target, opts.formatter.call(target, date));
        $(target).combo("hidePanel");
    };
    function setValue(target, value, flg) {
        var opts = $.data(target, "datetimebox").options;
        $(target).combo("setValue", value);
        if (!flg) {
            if (value) {
                var parserTime = opts.parser.call(target, value);
                $(target).combo("setText", opts.formatter.call(target, parserTime));
                $(target).combo("setValue", opts.formatter.call(target, parserTime));
            } else {
                $(target).combo("setText", value);
            }
        }
        var parserTime = opts.parser.call(target, value);
        $(target).datetimebox("calendar").calendar("moveTo", parserTime);
        $(target).datetimebox("spinner").timespinner("setValue", getTrueTimeFormat(parserTime));
        function getTrueTimeFormat(parserTime) {
            function getTimeFormat(num) {
                return (num < 10 ? "0": "") + num;
            };
            var detailedTime = [getTimeFormat(parserTime.getHours()), getTimeFormat(parserTime.getMinutes())];
            if (opts.showSeconds) {
                detailedTime.push(getTimeFormat(parserTime.getSeconds()));
            }
            return detailedTime.join($(target).datetimebox("spinner").timespinner("options").separator);
        };
    };
    $.fn.datetimebox = function(options, param) {
        if (typeof options == "string") {
            var opts = $.fn.datetimebox.methods[options];
            if (opts) {
                return opts(this, param);
            } else {
                return this.datebox(options, param);
            }
        }
        options = options || {};
        return this.each(function() {
            var state = $.data(this, "datetimebox");
            if (state) {
                $.extend(state.options, options);
            } else {
                $.data(this, "datetimebox", {
                    options: $.extend({},
                    $.fn.datetimebox.defaults, $.fn.datetimebox.parseOptions(this), options)
                });
            }
            init(this);
        });
    };
    $.fn.datetimebox.methods = {
        options: function(jq) {
            var opts = jq.datebox("options");
            return $.extend($.data(jq[0], "datetimebox").options, {
                originalValue: opts.originalValue,
                disabled: opts.disabled,
                readonly: opts.readonly
            });
        },
        cloneFrom: function(jq, from) {
            return jq.each(function() {
                $(this).datebox("cloneFrom", from);
                $.data(this, "datetimebox", {
                    options: $.extend(true, {},
                    $(from).datetimebox("options")),
                    spinner: $(from).datetimebox("spinner")
                });
                $(this).removeClass("datebox-f").addClass("datetimebox-f");
            });
        },
        spinner: function(jq) {
            return $.data(jq[0], "datetimebox").spinner;
        },
        initValue: function(jq, val) {
            return jq.each(function() {
                var opts = $(this).datetimebox("options");
                var value = opts.value;
                if (value) {
                    value = opts.formatter.call(this, opts.parser.call(this, value));
                }
                $(this).combo("initValue", value).combo("setText", value);
            });
        },
        setValue: function(jq, value) {
            return jq.each(function() {
                setValue(this, value);
            });
        },
        reset: function(jq) {
            return jq.each(function() {
                var opts = $(this).datetimebox("options");
                $(this).datetimebox("setValue", opts.originalValue);
            });
        }
    };
    $.fn.datetimebox.parseOptions = function(target) {
        var tar = $(target);
        return $.extend({},
        $.fn.datebox.parseOptions(target), $.parser.parseOptions(target, ["timeSeparator", "spinnerWidth", {
            showSeconds: "boolean"
        }]));
    };
    $.fn.datetimebox.defaults = $.extend({},
    $.fn.datebox.defaults, {
        spinnerWidth: "100%",
        showSeconds: true,
        timeSeparator: ":",
        panelEvents:{
        	mousedown:function(e){}
				},
        keyHandler: {
            up: function(e) {},
            down: function(e) {},
            left: function(e) {},
            right: function(e) {},
            enter: function(e) {
                enter(this);
            },
            query: function(q, e) {
                query(this, q);
            }
        },
        buttons: [{
            text: function(target) {
                return $(target).datetimebox("options").currentText;
            },
            handler: function(target) {
                var opts = $(target).datetimebox("options");
                setValue(target, opts.formatter.call(target, new Date()));
                $(target).datetimebox("hidePanel");
            }
        },
        {
            text: function(target) {
                return $(target).datetimebox("options").okText;
            },
            handler: function(target) {
                enter(target);
            }
        },
        {
            text: function(target) {
                return $(target).datetimebox("options").closeText;
            },
            handler: function(target) {
                $(target).datetimebox("hidePanel");
            }
        }],
        formatter: function(date) {
            var h = date.getHours();
            var M = date.getMinutes();
            var s = date.getSeconds();
            function getTimeFormat(value) {
                return (value < 10 ? "0": "") + value;
            };
            var separator = $(this).datetimebox("spinner").timespinner("options").separator;
            var detailedTime = $.fn.datebox.defaults.formatter(date) + " " + getTimeFormat(h) + separator + getTimeFormat(M);
            if ($(this).datetimebox("options").showSeconds) {
                detailedTime += separator + getTimeFormat(s);
            }
            return detailedTime;
        },
        parser: function(s) {
            if ($.trim(s) == "") {
                return new Date();
            }
            var time = s.split(" ");
            var parserTime = $.fn.datebox.defaults.parser(time[0]);
            if (time.length < 2) {
                return parserTime;
            }
            var separator = $(this).datetimebox("spinner").timespinner("options").separator;
            var tt = time[1].split(separator);
            var hour = parseInt(tt[0], 10) || 0;
            var minute = parseInt(tt[1], 10) || 0;
            var second = parseInt(tt[2], 10) || 0;
            return new Date(parserTime.getFullYear(), parserTime.getMonth(), parserTime.getDate(), hour, minute, second);
        }
    });
})(jQuery);