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
        var state = $.data(target, "searchbox");
        var opts = state.options;
        var icons = $.extend(true, [], opts.icons);
        
        icons.push({
            iconCls: "searchbox-button",
            handler: function(e) {
                var tar = $(e.data.target);
                var options = tar.searchbox("options");
                options.searcher.call(e.data.target, tar.searchbox("getValue"), tar.searchbox("getName"));
            }
        });
        initMenu();
        
        var item = getMenuItem();
        $(target).addClass("searchbox-f").textbox($.extend({}, opts, {
            icons: icons,
            buttonText: (item ? item.text: "")
        }));
        $(target).attr("searchboxName", $(target).attr("textboxName"));
        state.searchbox = $(target).next();
        state.searchbox.addClass("searchbox");
        setSearchbox(item);
        
        function initMenu() {
            if (opts.menu) {
                state.menu = $(opts.menu).menu();
                var options = state.menu.menu("options");
                var click = options.onClick;
                options.onClick = function(itm) {
                    setSearchbox(itm);
                    click.call(this, itm);
                };
            } else {
                if (state.menu) {
                    state.menu.menu("destroy");
                }
                state.menu = null;
            }
        };
        function getMenuItem() {
            if (state.menu) {
                var children = state.menu.children("div.menu-item:first");
                state.menu.children("div.menu-item").each(function() {
                    var ops = $.extend({},
                    $.parser.parseOptions(this), {
                        selected: ($(this).attr("selected") ? true: undefined)
                    });
                    if (ops.selected) {
                        children = $(this);
                        return false;
                    }
                });
                return state.menu.menu("getItem", children[0]);
            } else {
                return null;
            }
        };
        function setSearchbox(item) {
            if (!item) {
                return;
            }
            $(target).textbox("button").menubutton({
                text: item.text,
                iconCls: (item.iconCls || null),
                menu: state.menu,
                menuAlign: opts.buttonAlign,
                plain: false
            });
            state.searchbox.find("input.textbox-value").attr("name", item.name || item.text);
            $(target).searchbox("resize");
        };
    };
    
    $.fn.searchbox = function(options, param) {
        if (typeof options == "string") {
            var opts = $.fn.searchbox.methods[options];
            if (opts) {
                return opts(this, param);
            } else {
                return this.textbox(options, param);
            }
        }
        options = options || {};
        return this.each(function() {
            var state = $.data(this, "searchbox");
            if (state) {
                $.extend(state.options, options);
            } else {
                $.data(this, "searchbox", {
                    options: $.extend({},
                    $.fn.searchbox.defaults, $.fn.searchbox.parseOptions(this), options)
                });
            }
            init(this);
        });
    };
    
    $.fn.searchbox.methods = {
        options: function(jq) {
            var opts = jq.textbox("options");
            return $.extend($.data(jq[0], "searchbox").options, {
                width: opts.width,
                value: opts.value,
                originalValue: opts.originalValue,
                disabled: opts.disabled,
                readonly: opts.readonly
            });
        },
        menu: function(jq) {
            return $.data(jq[0], "searchbox").menu;
        },
        getName: function(jq) {
            return $.data(jq[0], "searchbox").searchbox.find("input.textbox-value").attr("name");
        },
        selectName: function(jq, name) {
            return jq.each(function() {
                var menu = $.data(this, "searchbox").menu;
                if (menu) {
                    menu.children("div.menu-item").each(function() {
                        var item = menu.menu("getItem", this);
                        if (item.name == name) {
                            $(this).trigger("click");
                            return false;
                        }
                    });
                }
            });
        },
        destroy: function(jq) {
            return jq.each(function() {
                var menu = $(this).searchbox("menu");
                if (menu) {
                    menu.menu("destroy");
                }
                $(this).textbox("destroy");
            });
        }
    };
    
    $.fn.searchbox.parseOptions = function(target) {
        var tar = $(target);
        return $.extend({}, $.fn.textbox.parseOptions(target), $.parser.parseOptions(target, ["menu"]), {
            searcher: (tar.attr("searcher") ? eval(tar.attr("searcher")) : undefined)
        });
    };
    
    $.fn.searchbox.defaults = $.extend({}, $.fn.textbox.defaults, {
        inputEvents: $.extend({}, $.fn.textbox.defaults.inputEvents, {
            keydown: function(e) {
                if (e.keyCode == 13) {
                    e.preventDefault();
                    var tar = $(e.data.target);
                    var opts = tar.searchbox("options");
                    tar.searchbox("setValue", $(this).val());
                    opts.searcher.call(e.data.target, tar.searchbox("getValue"), tar.searchbox("getName"));
                    return false;
                }
            }
        }),
        buttonAlign: "left",
        menu: null,
        searcher: function(value, name) {}
    });
})(jQuery);