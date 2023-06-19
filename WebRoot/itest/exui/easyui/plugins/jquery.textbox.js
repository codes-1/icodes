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
        $("body .textbox-f").each(function() {
            var state = $(this).data('textbox'),
                opts = state.options,
                tb = state.textbox,
                tbText = tb.find(".textbox-text");
            tb.css('width', opts.width);
            tbText.outerWidth(tb.width());
        });
    }
    $(function() {
        $(window).resize(function() {
            $.exui.throttle(adaption);
        });
    });
    var TEXTBOX_SERNO = 0;

    function init(target) {
        $(target).addClass("textbox-f").hide();
        var tb = $("<span class=\"textbox\">" + "<input class=\"textbox-text form-control\" autocomplete=\"off\">" + "<input type=\"hidden\" class=\"textbox-value\">" + "</span>").insertAfter(target);
        var namePro = $(target).attr("name");
        if (namePro) {
            tb.find("input.textbox-value").attr("name", namePro);
            $(target).removeAttr("name").attr("textboxName", namePro);
        }
        return tb;
    };

    function create(target) {
        var state = $.data(target, "textbox");
        var opts = state.options;
        var tb = state.textbox;
        var serno = "_exui_textbox_input" + (++TEXTBOX_SERNO);
        tb.addClass(opts.cls);
        tb.find(".textbox-text").remove();
        if (opts.multiline) {
            $("<textarea id=\"" + serno + "\" class=\"textbox-text form-control\" autocomplete=\"off\"></textarea>").prependTo(tb);
        } else {
            $("<input id=\"" + serno + "\" type=\"" + opts.type + "\" class=\"textbox-text form-control\" autocomplete=\"off\">").prependTo(tb);
        }
        $("#" + serno).attr("tabindex", $(target).attr("tabindex") || "").css("text-align", target.style.textAlign || "");
        tb.find(".textbox-addon").remove();
        var bb = opts.icons ? $.extend(true, [], opts.icons) : [];
        if (opts.iconCls) {
            bb.push({
                iconCls: opts.iconCls,
                disabled: true
            });
        }
        if (bb.length) {
            var bc = $("<span class=\"textbox-addon\"></span>").prependTo(tb);
            bc.addClass("textbox-addon-" + opts.iconAlign);
            for (var i = 0; i < bb.length; i++) {
                bc.append("<a href=\"javascript:;\" class=\"textbox-icon " + bb[i].iconCls + "\" icon-index=\"" + i + "\" tabindex=\"-1\"></a>");
            }
        }
        tb.find(".textbox-button").remove();
        if (opts.buttonText || opts.buttonIcon) {
            var tbBtn = $("<a href=\"javascript:;\" class=\"textbox-button\"></a>").prependTo(tb);
            tbBtn.addClass("textbox-button-" + opts.buttonAlign).linkbutton({
                btnCls: opts.btnCls,
                text: opts.buttonText,
                iconCls: opts.buttonIcon,
                onClick: function() {
                    var t = $(this).parent().prev();
                    t.textbox("options").onClickButton.call(t[0]);
                }
            });
        }
        if (opts.label) {
            if (typeof opts.label == "object") {
                state.label = $(opts.label);
                state.label.attr("for", serno);
            } else {
                $(state.label).remove();
                state.label = $("<label class=\"textbox-label\"></label>").html(opts.label);
                state.label.css("textAlign", opts.labelAlign).attr("for", serno);
                if (opts.labelPosition == "after") {
                    state.label.insertAfter(tb);
                } else {
                    state.label.insertBefore(target);
                }
                state.label.removeClass("textbox-label-left textbox-label-right textbox-label-top");
                state.label.addClass("textbox-label-" + opts.labelPosition);
            }
        } else {
            $(state.label).remove();
        }
        validate(target);
        isAble(target, opts.disabled);
        readonly(target, opts.readonly);
    };

    function destroy(target) {
        var state = $.data(target, "textbox");
        var tb = state.textbox;
        tb.find(".textbox-text").validatebox("destroy");
        tb.remove();
        $(state.label).remove();
        $(target).remove();
    };

    function resize(target, width) {
        var state = $.data(target, "textbox");
        var opts = state.options;
        var tb = state.textbox;
        var parent = tb.parent();
        if (width) {
            if (typeof width == "object") {
                $.extend(opts, width);
            } else {
                opts.width = width;
            }
        }
        if (isNaN(parseInt(opts.width))) {
            var c = $(target).clone();
            c.css("visibility", "hidden");
            c.insertAfter(target);
            opts.width = c.outerWidth();
            c.remove();
        }
        var isVisible = tb.is(":visible");
        if (!isVisible) {
            tb.appendTo("body");
        }
        var tbText = tb.find(".textbox-text");
        var btn = tb.find(".textbox-button");
        var tbAdn = tb.find(".textbox-addon");
        var tbIcon = tbAdn.find(".textbox-icon");
        if (opts.height == "auto") {
            tbText.css({
                margin: "",
                paddingTop: "",
                paddingBottom: "",
                height: "",
                lineHeight: ""
            });
        }
        tb._size(opts, parent);
        if (opts.label && opts.labelPosition) {
            if (opts.labelPosition == "top") {
                state.label._size({
                    width: opts.labelWidth == "auto" ? tb.outerWidth() : opts.labelWidth
                }, tb);
                /*if (opts.height != "auto") {
                    tb._size("height", tb.outerHeight() - state.label.outerHeight());
                }*/
            } else {
                state.label._size({
                    width: opts.labelWidth,
                    height: tb.outerHeight()
                }, tb);
                if (!opts.multiline) {
                    state.label.css("lineHeight", state.label.height() + "px");
                }
                tb._size("width", tb.outerWidth() - state.label.outerWidth());
            }
        }
        var paddingL = target.style.paddingLeft || 0, paddingR = target.style.paddingRight || 0;
		    paddingL = parseFloat(paddingL), paddingR = parseFloat(paddingR);
        if (opts.buttonAlign == "left" || opts.buttonAlign == "right") {
            btn.linkbutton("resize", {
                height: tb.height()
            });
			if (opts.buttonAlign == "left") {
				paddingL += getBtnSize("left");
        } else {
				paddingR += getBtnSize("right");
			}
		} else {
            btn.linkbutton("resize", {
                width: "100%"
            });
        }
        //var tbTextWidth = tb.width();// - tbIcon.length * opts.iconWidth - getBtnSize("left") - getBtnSize("right");
        var tbTextHeight = opts.height == "auto" ? tbText.height() : (tbText.outerHeight() - getBtnSize("top") - getBtnSize("bottom"));
        //var tbTextHeight = opts.height == "auto" ? tb.height() : tbText.outerHeight();//(tb.height() - getBtnSize("top") - getBtnSize("bottom"));
        //var tbTextHeight = opts.height == "auto" ? tbText.outerHeight() : tb.height();//(tb.height() - getBtnSize("top") - getBtnSize("bottom"));
        //tbAdn.css(opts.iconAlign, getBtnSize(opts.iconAlign) + "px");
        if (opts.iconAlign == "left" || opts.iconAlign == "right") {
			tbAdn.css(opts.iconAlign, getBtnSize(opts.iconAlign) + 1 + "px");
			if (opts.iconAlign == "left") {
				paddingL += tbAdn._outerWidth()
			} else{
				paddingR += tbAdn._outerWidth()
			}
		} else{
			tbAdn.css({"left":"1px","right":"1px"});
		}
        tbAdn.css("top", getBtnSize("top") + 1 + "px");
		//tbAdn.css("height", tb.height() - 2 + "px" );
		tbAdn.css("height", tbTextHeight - 2 + "px");
        tbIcon.css({
            width: opts.iconWidth + "px",
            height: tbTextHeight - 2 + "px"
        });
        tbText.css({
            paddingLeft: paddingL + 6,//(target.style.paddingLeft || ""),
            paddingRight: paddingR + 6,//(target.style.paddingRight || ""),
            //marginLeft: textAlign("left"),
            //marginRight: textAlign("right"),
            marginTop: getBtnSize("top"),
            marginBottom: getBtnSize("bottom")
        });
        if (opts.multiline) {
            tbText.css({
                paddingTop: (target.style.paddingTop || ""),
                paddingBottom: (target.style.paddingBottom || "")
            });
            tbTextHeight = opts.height == "auto" ? tb.height() : (tb.height() - getBtnSize("top") - getBtnSize("bottom"));
            tbText._outerHeight(tbTextHeight);
        } else {
            tbText.css({
                //paddingTop: 0,
                //paddingBottom: 0,
                height: tbTextHeight + "px",
                lineHeight: tbTextHeight + "px"
            });
        }
        //tbText._outerWidth(tbTextWidth);
        tbText._outerWidth(tb.width());
        opts.onResizing.call(target, opts.width, opts.height);
        if (!isVisible) {
            tb.insertAfter(target);
        }
        opts.onResize.call(target, opts.width, opts.height);

        function textAlign(position) {
            return (opts.iconAlign == position ? tbAdn._outerWidth() : 0) + getBtnSize(position);
        };

        function getBtnSize(position) {
            var w = 0;
            btn.filter(".textbox-button-" + position).each(function() {
                if (position == "left" || position == "right") {
                    w += $(this).outerWidth();
                } else {
                    w += $(this).outerHeight();
                }
            });
            return w;
        };
    };

    function validate(target) {
        var opts = $(target).textbox("options");
        var tb = $(target).textbox("textbox");
        tb.validatebox($.extend({}, opts, {
            deltaX: function(position) {
                return $(target).textbox("getTipX", position);
            },
            deltaY: function(position) {
                return $(target).textbox("getTipY", position);
            },
            onBeforeValidate: function() {
                opts.onBeforeValidate.call(target);
                var box = $(this);
                if (!box.is(":focus")) {
                    if (box.val() !== opts.value) {
                        opts.oldInputValue = box.val();
                        box.val(opts.value);
                    }
                }
            },
            onValidate: function(valid) {
                var box = $(this);
                if (opts.oldInputValue != undefined) {
                    box.val(opts.oldInputValue);
                    opts.oldInputValue = undefined;
                }
                var tb = box.parent();
                if (valid) {
                    tb.removeClass("textbox-invalid");
                } else {
                    tb.addClass("textbox-invalid");
                }
                opts.onValidate.call(target, valid);
            }
        }));
    };

    function render(target) {
        var state = $.data(target, "textbox");
        var opts = state.options;
        var tb = state.textbox;
        var tbText = tb.find(".textbox-text");
        tbText.attr("placeholder", opts.prompt);
        tbText.unbind(".textbox");
        $(state.label).unbind(".textbox");
        if (!opts.disabled && !opts.readonly) {
            if (state.label) {
                $(state.label).bind("click.textbox", function(e) {
                    if (!opts.hasFocusMe) {
                        tbText.focus();
                        $(target).textbox("setSelectionRange", {
                            start: 0,
                            end: tbText.val().length
                        });
                    }
                });
            }
            tbText.bind("blur.textbox", function(e) {
                if (!tb.hasClass("textbox-focused")) {
                    return;
                }
                opts.value = $(this).val();
                if (opts.value == "") {
                    $(this).val(opts.prompt).addClass("textbox-prompt");
                } else {
                    $(this).removeClass("textbox-prompt");
                }
                tb.removeClass("textbox-focused");
            }).bind("focus.textbox", function(e) {
                opts.hasFocusMe = true;
                if (tb.hasClass("textbox-focused")) {
                    return;
                }
                if ($(this).val() != opts.value) {
                    $(this).val(opts.value);
                }
                $(this).removeClass("textbox-prompt");
                tb.addClass("textbox-focused");
            });
            for (var event in opts.inputEvents) {
                tbText.bind(event + ".textbox", {
                    target: target
                }, opts.inputEvents[event]);
            }
        }
        var tbAdn = tb.find(".textbox-addon");
        tbAdn.unbind().bind("click", {
            target: target
        }, function(e) {
            var tbIcon = $(e.target).closest("a.textbox-icon:not(.textbox-icon-disabled)");
            if (tbIcon.length) {
                var iconIndex = parseInt(tbIcon.attr("icon-index"));
                var icon = opts.icons[iconIndex];
                if (icon && icon.handler) {
                    icon.handler.call(tbIcon[0], e);
                }
                opts.onClickIcon.call(target, iconIndex);
            }
        });
        tbAdn.find(".textbox-icon").each(function(index) {
            var icon = opts.icons[index];
            var $this = $(this);
            if (!icon || icon.disabled || opts.disabled || opts.readonly) {
                $this.addClass("textbox-icon-disabled");
            } else {
                $this.removeClass("textbox-icon-disabled");
            }
        });
        var btn = tb.find(".textbox-button");
        btn.linkbutton((opts.disabled || opts.readonly) ? "disable" : "enable");
        tb.unbind(".textbox").bind("_resize.textbox", function(e, width) {
            if ($(this).hasClass("exui-fluid") || width) {
                resize(target);
            }
            return false;
        });
    };

    function isAble(target, mode) {
        var state = $.data(target, "textbox");
        var opts = state.options;
        var tb = state.textbox;
        var tbText = tb.find(".textbox-text");
        var ss = $(target).add(tb.find(".textbox-value"));
        opts.disabled = mode;
        if (opts.disabled) {
            tbText.blur();
            tbText.validatebox("disable");
            tb.addClass("textbox-disabled");
            ss.attr("disabled", "disabled");
            $(state.label).addClass("textbox-label-disabled");
        } else {
            tbText.validatebox("enable");
            tb.removeClass("textbox-disabled");
            ss.removeAttr("disabled");
            $(state.label).removeClass("textbox-label-disabled");
        }
    };

    function readonly(target, mode) {
        var state = $.data(target, "textbox");
        var opts = state.options;
        var tb = state.textbox;
        var tbText = tb.find(".textbox-text");
        opts.readonly = mode == undefined ? true : mode;
        if (opts.readonly) {
            tbText.triggerHandler("blur.textbox");
        }
        tbText.validatebox("readonly", opts.readonly);
        tb.removeClass("textbox-readonly").addClass(opts.readonly ? "textbox-readonly" : "");
    };
    $.fn.textbox = function(options, param) {
        if (typeof options == "string") {
            var method = $.fn.textbox.methods[options];
            if (method) {
                return method(this, param);
            } else {
                return this.each(function() {
                    var textbox = $(this).textbox("textbox");
                    textbox.validatebox(options, param);
                });
            }
        }
        options = options || {};
        return this.each(function() {
            var state = $.data(this, "textbox");
            if (state) {
                $.extend(state.options, options);
                if (options.value != undefined) {
                    state.options.originalValue = options.value;
                }
            } else {
                state = $.data(this, "textbox", {
                    options: $.extend({}, $.fn.textbox.defaults, $.fn.textbox.parseOptions(this), options),
                    textbox: init(this)
                });
                state.options.originalValue = state.options.value;
            }
            create(this);
            render(this);
            if (state.options.doSize) {
                resize(this);
            }
            var initValue = state.options.value;
            state.options.value = "";
            $(this).textbox("initValue", initValue);
        });
    };
    $.fn.textbox.methods = {
        options: function(jq) {
            return $.data(jq[0], "textbox").options;
        },
        cloneFrom: function(jq, originalTarget) {
            return jq.each(function() {
                var t = $(this);
                if (t.data("textbox")) {
                    return;
                }
                if (!$(originalTarget).data("textbox")) {
                    $(originalTarget).textbox();
                }
                var opts = $.extend(true, {}, $(originalTarget).textbox("options"));
                var namePro = t.attr("name") || "";
                t.addClass("textbox-f").hide();
                t.removeAttr("name").attr("textboxName", namePro);
                var cloneTarget = $(originalTarget).next().clone().insertAfter(t);
                var serno = "_exui_textbox_input" + (++TEXTBOX_SERNO);
                cloneTarget.find(".textbox-value").attr("name", namePro);
                cloneTarget.find(".textbox-text").attr("id", serno);
                var cloneLabel = $($(originalTarget).textbox("label")).clone();
                if (cloneLabel.length) {
                    cloneLabel.attr("for", serno);
                    if (opts.labelPosition == "after") {
                        cloneLabel.insertAfter(t.next());
                    } else {
                        cloneLabel.insertBefore(t);
                    }
                }
                $.data(this, "textbox", {
                    options: opts,
                    textbox: cloneTarget,
                    label: (cloneLabel.length ? cloneLabel : undefined)
                });
                var btn = $(originalTarget).textbox("button");
                if (btn.length) {
                    t.textbox("button").linkbutton($.extend(true, {}, btn.linkbutton("options")));
                }
                render(this);
                validate(this);
            });
        },
        textbox: function(jq) {
            return $.data(jq[0], "textbox").textbox.find(".textbox-text");
        },
        button: function(jq) {
            return $.data(jq[0], "textbox").textbox.find(".textbox-button");
        },
        label: function(jq) {
            return $.data(jq[0], "textbox").label;
        },
        destroy: function(jq) {
            return jq.each(function() {
                destroy(this);
            });
        },
        resize: function(jq, width) {
            return jq.each(function() {
                resize(this, width);
            });
        },
        disable: function(jq) {
            return jq.each(function() {
                isAble(this, true);
                render(this);
            });
        },
        enable: function(jq) {
            return jq.each(function() {
                isAble(this, false);
                render(this);
            });
        },
        readonly: function(jq, mode) {
            return jq.each(function() {
                readonly(this, mode);
                render(this);
            });
        },
        isValid: function(jq) {
            return jq.textbox("textbox").validatebox("isValid");
        },
        clear: function(jq) {
            return jq.each(function() {
                $(this).textbox("setValue", "");
            });
        },
        setText: function(jq, text) {
            return jq.each(function() {
                var opts = $(this).textbox("options");
                var tb = $(this).textbox("textbox");
                text = text == undefined ? "" : String(text);
                if ($(this).textbox("getText") != text) {
                    tb.val(text);
                }
                opts.value = text;
                if (!tb.is(":focus")) {
                    if (text) {
                        tb.removeClass("textbox-prompt");
                    } else {
                        tb.val(opts.prompt).addClass("textbox-prompt");
                    }
                }
                $(this).textbox("validate");
            });
        },
        initValue: function(jq, value) {
            return jq.each(function() {
                var state = $.data(this, "textbox");
                $(this).textbox("setText", value);
                state.textbox.find(".textbox-value").val(value);
                $(this).val(value);
            });
        },
        setValue: function(jq, value) {
            return jq.each(function() {
                var opts = $.data(this, "textbox").options;
                var oldValue = $(this).textbox("getValue");
                $(this).textbox("initValue", value);
                if (oldValue != value) {
                    opts.onChange.call(this, value, oldValue);
                    $(this).closest("form").trigger("_change", [this]);
                }
            });
        },
        getText: function(jq) {
            var tb = jq.textbox("textbox");
            if (tb.is(":focus")) {
                return tb.val();
            } else {
                return jq.textbox("options").value;
            }
        },
        getValue: function(jq) {
            return jq.data("textbox").textbox.find(".textbox-value").val();
        },
        reset: function(jq) {
            return jq.each(function() {
                var opts = $(this).textbox("options");
                $(this).textbox("textbox").val(opts.originalValue);
                $(this).textbox("setValue", opts.originalValue);
            });
        },
        getIcon: function(jq, index) {
            return jq.data("textbox").textbox.find(".textbox-icon:eq(" + index + ")");
        },
        getTipX: function(jq, position) {
            var state = jq.data("textbox");
            var opts = state.options;
            var tb = state.textbox;
            var tbText = tb.find(".textbox-text");
            var position = position || opts.tipPosition;
            var p1 = tb.offset();
            var p2 = tbText.offset();
            var w1 = tb.outerWidth();
            var w2 = tbText.outerWidth();
            if (position == "right") {
                return w1 - w2 - p2.left + p1.left;
            } else {
                if (position == "left") {
                    return p1.left - p2.left;
                } else {
                    return (w1 - w2 - p2.left + p1.left) / 2 - (p2.left - p1.left) / 2;
                }
            }
        },
        getTipY: function(jq, position) {
            var state = jq.data("textbox");
            var opts = state.options;
            var tb = state.textbox;
            var tbText = tb.find(".textbox-text");
            var position = position || opts.tipPosition;
            var p1 = tb.offset();
            var p2 = tbText.offset();
            var h1 = tb.outerHeight();
            var h2 = tbText.outerHeight();
            if (position == "left" || position == "right") {
                return (h1 - h2 - p2.top + p1.top) / 2 - (p2.top - p1.top) / 2;
            } else {
                if (position == "bottom") {
                    return (h1 - h2 - p2.top + p1.top);
                } else {
                    return (p1.top - p2.top);
                }
            }
        },
        getSelectionStart: function(jq) {
            return jq.textbox("getSelectionRange").start;
        },
        getSelectionRange: function(jq) {
            var tb = jq.textbox("textbox")[0];
            var start = 0;
            var end = 0;
            if (typeof tb.selectionStart == "number") {
                start = tb.selectionStart;
                end = tb.selectionEnd;
            } else {
                if (tb.createTextRange) {
                    var s = document.selection.createRange();
                    var textRange = tb.createTextRange();
                    textRange.setEndPoint("EndToStart", s);
                    start = textRange.text.length;
                    end = start + s.text.length;
                }
            }
            return {
                start: start,
                end: end
            };
        },
        setSelectionRange: function(jq, range) {
            return jq.each(function() {
                var tb = $(this).textbox("textbox")[0];
                var start = range.start;
                var end = range.end;
                if (tb.setSelectionRange) {
                    tb.setSelectionRange(start, end);
                } else {
                    if (tb.createTextRange) {
                        var textRange = tb.createTextRange();
                        textRange.collapse();
                        textRange.moveEnd("character", end);
                        textRange.moveStart("character", start);
                        textRange.select();
                    }
                }
            });
        }
    };
    $.fn.textbox.parseOptions = function(target) {
        var t = $(target);
        return $.extend({}, $.fn.validatebox.parseOptions(target), $.parser.parseOptions(target, ["prompt", "iconCls", "iconAlign", "buttonText", "buttonIcon", "buttonAlign", "btnCls", "label", "labelPosition", "labelAlign", {
            multiline: "boolean",
            iconWidth: "number",
            labelWidth: "number"
        }]), {
            value: (t.val() || undefined),
            type: (t.attr("type") ? t.attr("type") : undefined)
        });
    };
    $.fn.textbox.defaults = $.extend({}, $.fn.validatebox.defaults, {
        doSize: true,
        width: "auto",
        height: 34, //"auto",
        cls: null,
        prompt: "",
        value: "",
        type: "text",
        multiline: false,
        icons: [],
        iconCls: null,
        iconAlign: "right",
        iconWidth: "auto", //18,
        buttonText: "",
        buttonIcon: null,
        buttonAlign: "right",
        label: null,
        labelWidth: "auto",
        labelPosition: "before",
        labelAlign: "left",
        inputEvents: {
            blur: function(e) {
                var t = $(e.data.target);
                var opts = t.textbox("options");
                if (t.textbox("getValue") != opts.value) {
                    t.textbox("setValue", opts.value);
                }
            },
            keydown: function(e) {
                if (e.keyCode == 13) {
                    var t = $(e.data.target);
                    t.textbox("setValue", t.textbox("getText"));
                }
            }
        },
        btnCls: 'primary',
        onChange: function(newValue, oldValue) {},
        onResizing: function(width, height) {},
        onResize: function(width, height) {},
        onClickButton: function() {},
        onClickIcon: function(index) {}
    });
})(jQuery);