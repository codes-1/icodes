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
        var state = $.data(target, "pagination");
        var opts = state.options;
        var bb = state.bb = {};
        var pag = $(target).addClass("pagination").html("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tr></tr></table>");
        var tr = pag.find("tr");
        var layout = $.extend([], opts.layout);
        if (!opts.showPageList) {
            removeSign(layout, "list");
        }
        if (!opts.showPageInfo) {
        		removeSign(layout, "info");
        }
        if (!opts.showRefresh) {
            removeSign(layout, "refresh");
        }
        if (layout[0] == "sep") {
            layout.shift();
        }
        if (layout[layout.length - 1] == "sep") {
            layout.pop();
        }
        for (var length = 0; length < layout.length; length++) {
            var lay = layout[length];
            if (lay == "list") {
            	var pageListDefault = opts.pageList[0],
            		pos = $.inArray(opts.pageSize, opts.pageList),
            		pagList = $('<ul class="dropdown-menu" role="menu"></ul>');
            	if (pos != -1) {
            		pageListDefault = opts.pageList[pos];
            	}/* else {
            		pageListDefault = '&nbsp;&nbsp;';
            	}*/
            	for (var i = 0; i < opts.pageList.length; i++) {
                    $('<li><a href="javascript:void(0)">'+opts.pageList[i]+'</a></li>').appendTo(pagList);
                }
            	var list = $('<div class="btn-group ' + opts.dropPos + '">'
            		+ '<button type="button" class="btn btn-default btn-secondary dropdown-toggle btn-xs" data-toggle="dropdown">'
            		+ '<span>' + pageListDefault + '</span> <span class="caret"></span></button></div>').append(pagList);
            	$('<td></td>').append(list).appendTo(tr);
                
            	pagList.bind("click", function(e) {
            		var t = $(e.target),
            			pageSizeSpan = pagList.prev().find('span:first-child'),
                		selectedPageList = pageSizeSpan.text();
                	if (t.is('a') && selectedPageList != t.text()) {
                		pageSizeSpan.text(t.text());
                		opts.pageSize = parseInt(t.text());
                		opts.onChangePageSize.call(target, opts.pageSize);
                		select(target, opts.pageNumber);
                	}
                });
            	/*var pagList = $('<ul class="dropdown-menu" role="menu"></ul>').css('min-width','2em');
            	for (var i = 0; i < opts.pageList.length; i++) {
                    $('<li><a href="javascript:void(0)">'+opts.pageList[i]+'</a></li>').appendTo(pagList);
                }
            	
            	$('<td class="btn-group btn-group-xs ' + opts.dropPos + '"><button type="button" class="btn btn-default btn-secondary">' + opts.pageList[0] 
            		+ '</button><button type="button" class="btn btn-default btn-secondary dropdown-toggle" data-toggle="dropdown">'
            		+ '<span class="caret"></span></button></td>').css('padding','3px 8px').append(pagList).appendTo(tr);
                
            	pagList.find("a").bind("click", function() {
                	var selectedPageList = pagList.siblings(':first').text();
                	if (selectedPageList != $(this).text()) {
                		pagList.siblings(':first').text($(this).text());
                		opts.pageSize = parseInt($(this).text());
                    	opts.onChangePageSize.call(target, opts.pageSize);
                    	select(target, opts.pageNumber);
                	}
                });*/
            } else {
                if (lay == "sep") {
                    $("<td><div class=\"pagination-btn-separator\"></div></td>").appendTo(tr);
                } else {
                    if (lay == "first") {
                        bb.first = initButton("first");
                    } else {
                        if (lay == "prev") {
                            bb.prev = initButton("prev");
                        } else {
                            if (lay == "next") {
                                bb.next = initButton("next");
                            } else {
                                if (lay == "last") {
                                    bb.last = initButton("last");
                                } else {
                                    if (lay == "manual") {
                                        $("<span style=\"padding-left:6px;\"></span>").html(opts.beforePageText).appendTo(tr).wrap("<td></td>");
                                        bb.num = $("<input class=\"pagination-num form-control\" type=\"text\" value=\"1\" size=\"2\">").appendTo(tr).wrap("<td></td>");
                                        bb.num.unbind(".pagination").bind("keydown.pagination",
                                        function(e) {
                                            if (e.keyCode == 13) {
                                                var num = parseInt($(this).val()) || 1;
                                                select(target, num);
                                                return false;
                                            }
                                        });
                                        bb.after = $("<span style=\"padding-right:6px;\"></span>").appendTo(tr).wrap("<td></td>");
                                    } else {
                                        if (lay == "refresh") {
                                            bb.refresh = initButton("refresh");
                                        } else {
                                            if (lay == "links") {
                                                $("<td class=\"pagination-links\"></td>").appendTo(tr);
                                            } else {
                                            		if (lay == "info") {
                                            				if (length == layout.length-1) {
                                            						$("<div class=\"pagination-info\"></div>").appendTo(pag);
                                            				} else {
                                            						$("<td><div class=\"pagination-info\"></div></td>").appendTo(tr);
                                            				}
                                            		}
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (opts.buttons) {
            $("<td><div class=\"pagination-btn-separator\"></div></td>").appendTo(tr);
            if ($.isArray(opts.buttons)) {
                for (var i = 0; i < opts.buttons.length; i++) {
                    var button = opts.buttons[i];
                    if (button == "-") {
                        $("<td><div class=\"pagination-btn-separator\"></div></td>").appendTo(tr);
                    } else {
                        var td = $("<td></td>").appendTo(tr);
                        var href1 = $("<a href=\"javascript:void(0)\"></a>").appendTo(td);
                        href1[0].onclick = eval(button.handler ||
                        function() {});
                        href1.linkbutton($.extend({}, button, {
                            plain: true
                        }));
                    }
                }
            } else {
                var td = $("<td></td>").appendTo(tr);
                $(opts.buttons).appendTo(td).show();
            }
        }
        $("<div style=\"clear:both;\"></div>").appendTo(pag);
        function initButton(value) {
            var nav = opts.nav[value];
            var href = $("<a href=\"javascript:void(0)\"></a>").appendTo(tr);
            href.wrap("<td></td>");
            href.linkbutton({
                iconCls: nav.iconCls,
                plain: true
            }).unbind(".pagination").bind("click.pagination", function() {
                nav.handler.call(target);
            });
            return href;
        };
        function removeSign(layout, value) {
            var len = $.inArray(value, layout);
            if (len >= 0) {
                layout.splice(len, 1);
            }
            return layout;
        };
    };
    
    function select(target, page) {
        var opts = $.data(target, "pagination").options;
        refresh(target, {
            pageNumber: page
        });
        opts.onSelectPage.call(target, opts.pageNumber, opts.pageSize);
    };
    
    function refresh(target, options) {
        var state = $.data(target, "pagination");
        var opts = state.options;
        var bb = state.bb;
        $.extend(opts, options || {});
        var pagList = $(target).find("select.pagination-page-list");
        if (pagList.length) {
            pagList.val(opts.pageSize + "");
            opts.pageSize = parseInt(pagList.val());
        }
        var num = Math.ceil(opts.total / opts.pageSize) || 1;
        if (opts.pageNumber < 1) {
            opts.pageNumber = 1;
        }
        if (opts.pageNumber > num) {
            opts.pageNumber = num;
        }
        if (opts.total == 0) {
            opts.pageNumber = 0;
            num = 0;
        }
        if (bb.num) {
            bb.num.val(opts.pageNumber);
        }
        if (bb.after) {
            bb.after.html(opts.afterPageText.replace(/{pages}/, num));
        }
        var pagLinks = $(target).find("td.pagination-links");
        if (pagLinks.length) {
            pagLinks.addClass('btn-group').empty();
            var number = opts.pageNumber - Math.floor(opts.links / 2);
            if (number < 1) {
                number = 1;
            }
            var number1 = number + opts.links - 1;
            if (number1 > num) {
                number1 = num;
            }
            number = number1 - opts.links + 1;
            if (number < 1) {
                number = 1;
            }
            for (var i = number; i <= number1; i++) {
                var href = $("<a class=\"pagination-link\" href=\"javascript:void(0)\"></a>").appendTo(pagLinks);
                href.linkbutton({
                    //plain: false,
                    text: i
                });
                if (i == opts.pageNumber) {
                    href.linkbutton("select");
                    
                    if (href.hasClass('active')) {
                    	href.addClass('btn-pager').removeClass("btn-default btn-secondary active");
                    }
                } else {
                    href.unbind(".pagination").bind("click.pagination", {
                        pageNumber: i
                    },
                    function(e) {
                        select(target, e.data.pageNumber);
                    });
                }
            }
        }
        var message = opts.displayMsg;
        message = message.replace(/{from}/, opts.total == 0 ? 0 : opts.pageSize * (opts.pageNumber - 1) + 1);
        message = message.replace(/{to}/, Math.min(opts.pageSize * (opts.pageNumber), opts.total));
        message = message.replace(/{total}/, opts.total);
        $(target).find("div.pagination-info").html(message);
        if (bb.first) {
            bb.first.linkbutton({
                disabled: ((!opts.total) || opts.pageNumber == 1)
            });
        }
        if (bb.prev) {
            bb.prev.linkbutton({
                disabled: ((!opts.total) || opts.pageNumber == 1)
            });
        }
        if (bb.next) {
            bb.next.linkbutton({
                disabled: (opts.pageNumber == num)
            });
        }
        if (bb.last) {
            bb.last.linkbutton({
                disabled: (opts.pageNumber == num)
            });
        }
        loading(target, opts.loading);
    };
    
    function loading(target, flg) {
        var state = $.data(target, "pagination");
        var opts = state.options;
        opts.loading = flg;
        if (opts.showRefresh && state.bb.refresh) {
            state.bb.refresh.linkbutton({
                iconCls: (opts.loading ? "pagination-loading": "pagination-load")
            });
        }
    };
    
    $.fn.pagination = function(options, param) {
        if (typeof options == "string") {
            return $.fn.pagination.methods[options](this, param);
        }
        
        options = options || {};
        return this.each(function() {
            var opts;
            var state = $.data(this, "pagination");
            if (state) {
                opts = $.extend(state.options, options);
            } else {
                opts = $.extend({}, $.fn.pagination.defaults, $.fn.pagination.parseOptions(this), options);
                $.data(this, "pagination", {
                    options: opts
                });
            }
            
            init(this);
            refresh(this);
        });
    };
    
    $.fn.pagination.methods = {
        options: function(jq) {
            return $.data(jq[0], "pagination").options;
        },
        loading: function(jq) {
            return jq.each(function() {
                loading(this, true);
            });
        },
        loaded: function(jq) {
            return jq.each(function() {
                loading(this, false);
            });
        },
        refresh: function(jq, options) {
            return jq.each(function() {
                refresh(this, options);
            });
        },
        select: function(jq, page) {
            return jq.each(function() {
                select(this, page);
            });
        }
    };
    
    $.fn.pagination.parseOptions = function(target) {
        var tar = $(target);
        return $.extend({},$.parser.parseOptions(target, ['dropPos',{
            total: "number",
            pageSize: "number",
            pageNumber: "number",
            links: "number"
        },{
            loading: "boolean",
            showPageList: "boolean",
            showPageInfo:"boolean",
            showRefresh: "boolean"
        }]), {
            pageList: (tar.attr("pageList") ? eval(tar.attr("pageList")) : undefined)
        });
    };
    
    $.fn.pagination.defaults = {
        total: 1,
        pageSize: 10,
        pageNumber: 1,
        pageList: [10, 20, 30, 50],
        loading: false,
        buttons: null,
        dropPos: 'dropup',  //dropup,dropdown
        showPageList: true,
        showPageInfo:true,
        showRefresh: true,
        links: 10,
        layout: ["list", "sep", "first", "prev", "sep", "manual", "sep", "next", "last", "sep", "refresh"],
        onSelectPage: function(pageNumber, pageSize) {},
        onBeforeRefresh: function(pageNumber, pageSize) {},
        onRefresh: function(pageNumber, pageSize) {},
        onChangePageSize: function(pageSize) {},
        beforePageText: "Page",
        afterPageText: "of {pages}",
        displayMsg: "Displaying {from} to {to} of {total} items",
        nav: {
            first: {
                iconCls: "pagination-first",
                handler: function() {
                    var opts = $(this).pagination("options");
                    if (opts.pageNumber > 1) {
                        $(this).pagination("select", 1);
                    }
                }
            },
            prev: {
                iconCls: "pagination-prev",
                handler: function() {
                    var opts = $(this).pagination("options");
                    if (opts.pageNumber > 1) {
                        $(this).pagination("select", opts.pageNumber - 1);
                    }
                }
            },
            next: {
                iconCls: "pagination-next",
                handler: function() {
                    var opts = $(this).pagination("options");
                    var num = Math.ceil(opts.total / opts.pageSize);
                    if (opts.pageNumber < num) {
                        $(this).pagination("select", opts.pageNumber + 1);
                    }
                }
            },
            last: {
                iconCls: "pagination-last",
                handler: function() {
                    var opts = $(this).pagination("options");
                    var num = Math.ceil(opts.total / opts.pageSize);
                    if (opts.pageNumber < num) {
                        $(this).pagination("select", num);
                    }
                }
            },
            refresh: {
                iconCls: "pagination-refresh",
                handler: function() {
                    var opts = $(this).pagination("options");
                    if (opts.onBeforeRefresh.call(this, opts.pageNumber, opts.pageSize) != false) {
                        $(this).pagination("select", opts.pageNumber);
                        opts.onRefresh.call(this, opts.pageNumber, opts.pageSize);
                    }
                }
            }
        }
    };
})(jQuery);//# sourceURL=pagination.js