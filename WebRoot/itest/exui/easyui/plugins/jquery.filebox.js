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
    var FILEBOX_SERNO = 0;
    
    function create(target) {
        var state = $.data(target, "filebox");
        var opts = state.options;
        
        opts.fileboxId = "filebox_file_id_" + (++FILEBOX_SERNO);
        $(target).addClass("filebox-f").textbox(opts);
        $(target).textbox("textbox").attr("readonly", "readonly");
        state.filebox = $(target).next().addClass("filebox");
        
        var fileInput = init(target);
        var btn = $(target).filebox("button");
        if (btn.length) {
            $("<label class=\"filebox-label\" for=\"" + opts.fileboxId + "\"></label>").appendTo(btn);
            if (btn.linkbutton("options").disabled) {
                fileInput.attr("disabled", "disabled");
            } else {
                fileInput.removeAttr("disabled");
            }
        }
    };
    
    function init(target) {
        var state = $.data(target, "filebox");
        var opts = state.options;
        
        state.filebox.find(".textbox-value").remove();
        opts.oldValue = "";
        
        var fileInput = $("<input type=\"file\" class=\"textbox-value\">").appendTo(state.filebox);
        fileInput.attr("id", opts.fileboxId).attr("name", $(target).attr("textboxName") || "");
        fileInput.attr("accept", opts.accept);
        fileInput.attr("capture", opts.capture);
        if (opts.multiple) {
            fileInput.attr("multiple", "multiple");
        }
        fileInput.change(function() {
            var fileVal = this.value;
            if (this.files) {
                fileVal = $.map(this.files, function(_e) {
                    return _e.name;
                }).join(opts.separator);
            }
            $(target).filebox("setText", fileVal);
            opts.onChange.call(target, fileVal, opts.oldValue);
            opts.oldValue = fileVal;
        });
        return fileInput;
    };
    
    $.fn.filebox = function(options, param) {
        if (typeof options == "string") {
            var method = $.fn.filebox.methods[options];
            if (method) {
                return method(this, param);
            } else {
                return this.textbox(options, param);
            }
        }
        
        options = options || {};
        return this.each(function() {
            var state = $.data(this, "filebox");
            if (state) {
                $.extend(state.options, options);
            } else {
                $.data(this, "filebox", {
                    options: $.extend({}, $.fn.filebox.defaults, $.fn.filebox.parseOptions(this), options)
                });
            }
            
            create(this);
        });
    };
    
    $.fn.filebox.methods = {
        options: function(jq) {
            var opts = jq.textbox("options");
            return $.extend($.data(jq[0], "filebox").options, {
                width: opts.width,
                value: opts.value,
                originalValue: opts.originalValue,
                disabled: opts.disabled,
                readonly: opts.readonly
            });
        },
        clear: function(jq) {
            return jq.each(function() {
                $(this).textbox("clear");
                init(this);
            });
        },
        reset: function(jq) {
            return jq.each(function() {
                $(this).filebox("clear");
            });
        },
        setValue: function(jq) {
        	return jq;
        },
        setValues:function(jq){
        	return jq;
        },
        files:function(jq){
        	return jq.next().find(".textbox-value")[0].files;
        }
    };
    
    $.fn.filebox.parseOptions = function(target) {
        var t = $(target);
        return $.extend({}, $.fn.textbox.parseOptions(target), $.parser.parseOptions(target, ["accept", "capture", "separator"]), {
            multiple: (t.attr("multiple") ? true: undefined)
        });
    };
    
    $.fn.filebox.defaults = $.extend({}, $.fn.textbox.defaults, {
        buttonIcon: null,
        buttonText: "选择文件",
        buttonAlign: "right",
        inputEvents: {},
        accept: "",
        capture:"",
        separator: ",",
        multiple: false
    });
})(jQuery);