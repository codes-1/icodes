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
        var t = $(target);
        t.addClass("tree");
        return t;
    };
    
    function bindEvents(target) {
        var opts = $.data(target, "tree").options;
        
        $(target).unbind().bind("mouseover", function(e) {
            var tt = $(e.target);
            var treeNode = tt.closest("div.tree-node");
            
            if (!treeNode.length) {
                return;
            }
            
            treeNode.addClass("tree-node-hover");
            if (tt.hasClass("tree-hit")) {
                if (tt.hasClass("tree-expanded")) {
                    tt.addClass("tree-expanded-hover");
                } else {
                    tt.addClass("tree-collapsed-hover");
                }
            }
            e.stopPropagation();
        }).bind("mouseout", function(e) {
            var tt = $(e.target);
            var treeNode = tt.closest("div.tree-node");
            if (!treeNode.length) {
                return;
            }
            
            treeNode.removeClass("tree-node-hover");
            if (tt.hasClass("tree-hit")) {
                if (tt.hasClass("tree-expanded")) {
                    tt.removeClass("tree-expanded-hover");
                } else {
                    tt.removeClass("tree-collapsed-hover");
                }
            }
            e.stopPropagation();
        }).bind("click", function(e) {
            var tt = $(e.target);
            var treeNode = tt.closest("div.tree-node");
            if (!treeNode.length) {
                return;
            }
            
            if (tt.hasClass("tree-hit")) {
                toggle(target, treeNode[0]);
                return false;
            } else {
                if (tt.hasClass("tree-checkbox")) {
                    checkNode(target, treeNode[0]);
                    return false;
                } else {
                    selectNode(target, treeNode[0]);
                    opts.onClick.call(target, getNode(target, treeNode[0]));
                }
            }
            e.stopPropagation();
        }).bind("dblclick", function(e) {
            var treeNode = $(e.target).closest("div.tree-node");
            if (!treeNode.length) {
                return;
            }
            selectNode(target, treeNode[0]);
            opts.onDblClick.call(target, getNode(target, treeNode[0]));
            e.stopPropagation();
        }).bind("contextmenu", function(e) {
            var treeNode = $(e.target).closest("div.tree-node");
            if (!treeNode.length) {
                return;
            }
            opts.onContextMenu.call(target, e, getNode(target, treeNode[0]));
            e.stopPropagation();
        });
    };
    
    function disableDnd(target) {
        var opts = $.data(target, "tree").options;
        opts.dnd = false;
        var treeNode = $(target).find("div.tree-node");
        treeNode.draggable("disable");
        treeNode.css("cursor", "pointer");
    };
    
    function enableDnd(target) {
        var state = $.data(target, "tree");
        var opts = state.options;
        var tree = state.tree;
        
        state.disabledNodes = [];
        opts.dnd = true;
        
        tree.find("div.tree-node").draggable({
            disabled: false,
            revert: true,
            cursor: "pointer",
            proxy: function(source) {
                var p = $("<div class=\"tree-node-proxy\"></div>").appendTo("body");
                p.html("<span class=\"tree-dnd-icon tree-dnd-no\">&nbsp;</span>" + $(source).find(".tree-title").html());
                p.hide();
                return p;
            },
            deltaX: 15,
            deltaY: 15,
            onBeforeDrag: function(e) {
                if (opts.onBeforeDrag.call(target, getNode(target, this)) == false) {
                    return false;
                }
                if ($(e.target).hasClass("tree-hit") || $(e.target).hasClass("tree-checkbox")) {
                    return false;
                }
                if (e.which != 1) {
                    return false;
                }
                
                var indent = $(this).find("span.tree-indent");
                if (indent.length) {
                    e.data.offsetWidth -= indent.length * indent.width();
                }
            },
            onStartDrag: function(e) {
                $(this).next("ul").find("div.tree-node").each(function() {
                    $(this).droppable("disable");
                    state.disabledNodes.push(this);
                });
                $(this).draggable("proxy").css({
                    left: -10000,
                    top: -10000
                });
                opts.onStartDrag.call(target, getNode(target, this));
                
                var treeNode = getNode(target, this);
                if (treeNode.id == undefined) {
                    treeNode.id = "exui_tree_node_id_temp";
                    updateNode(target, treeNode);
                }
                state.draggingNodeId = treeNode.id;
            },
            onDrag: function(e) {
                var x1 = e.pageX,
                y1 = e.pageY,
                x2 = e.data.startX,
                y2 = e.data.startY;
                var d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
                if (d > 3) {
                    $(this).draggable("proxy").show();
                }
                this.pageY = e.pageY;
            },
            onStopDrag: function() {
                for (var i = 0; i < state.disabledNodes.length; i++) {
                    $(state.disabledNodes[i]).droppable("enable");
                }
                state.disabledNodes = [];
                
                var treeNode = findNode(target, state.draggingNodeId);
                if (treeNode && treeNode.id == "exui_tree_node_id_temp") {
                    treeNode.id = "";
                    updateNode(target, treeNode);
                }
                opts.onStopDrag.call(target, treeNode);
            }
        }).droppable({
            accept: "div.tree-node",
            onDragEnter: function(e, source) {
                if (opts.onDragEnter.call(target, this, getTree(source)) == false) {
                    setDragCls(source, false);
                    $(this).removeClass("tree-node-append tree-node-top tree-node-bottom");
                    $(this).droppable("disable");
                    state.disabledNodes.push(this);
                }
            },
            onDragOver: function(e, source) {
                if ($(this).droppable("options").disabled) {
                    return;
                }
                
                var y = source.pageY;
                var top = $(this).offset().top;
                var outerHeight = top + $(this).outerHeight();
                setDragCls(source, true);
                $(this).removeClass("tree-node-append tree-node-top tree-node-bottom");
                
                if (y > top + (outerHeight - top) / 2) {
                    if (outerHeight - y < 5) {
                        $(this).addClass("tree-node-bottom");
                    } else {
                        $(this).addClass("tree-node-append");
                    }
                } else {
                    if (y - top < 5) {
                        $(this).addClass("tree-node-top");
                    } else {
                        $(this).addClass("tree-node-append");
                    }
                }
                if (opts.onDragOver.call(target, this, getTree(source)) == false) {
                    setDragCls(source, false);
                    $(this).removeClass("tree-node-append tree-node-top tree-node-bottom");
                    $(this).droppable("disable");
                    state.disabledNodes.push(this);
                }
            },
            onDragLeave: function(e, source) {
                setDragCls(source, false);
                $(this).removeClass("tree-node-append tree-node-top tree-node-bottom");
                opts.onDragLeave.call(target, this, getTree(source));
            },
            onDrop: function(e, source) {
                var node = this;
                var dropObj, point;
                
                if ($(this).hasClass("tree-node-append")) {
                    dropObj = dropSource;
                    point = "append";
                } else {
                    dropObj = dropTarget;
                    point = $(this).hasClass("tree-node-top") ? "top": "bottom";
                }
                if (opts.onBeforeDrop.call(target, node, getTree(source), point) == false) {
                    $(this).removeClass("tree-node-append tree-node-top tree-node-bottom");
                    return;
                }
                
                dropObj(source, node, point);
                $(this).removeClass("tree-node-append tree-node-top tree-node-bottom");
            }
        });
        
        function getTree(source, pop) {
            return $(source).closest("ul.tree").tree(pop ? "pop": "getData", source);
        };
        function setDragCls(source, dnd) {
            var dndIcon = $(source).draggable("proxy").find("span.tree-dnd-icon");
            dndIcon.removeClass("tree-dnd-yes tree-dnd-no").addClass(dnd ? "tree-dnd-yes": "tree-dnd-no");
        };
        function dropSource(source, node) {
            if (getNode(target, node).state == "closed") {
                expand(target, node, function() {
                    startDrop();
                });
            } else {
                startDrop();
            }
            function startDrop() {
                var point = getTree(source, true);
                $(target).tree("append", {
                    parent: node,
                    data: [point]
                });
                opts.onDrop.call(target, node, point, "append");
            };
        };
        function dropTarget(source, node, point) {
            var param = {};
            if (point == "top") {
                param.before = node;
            } else {
                param.after = node;
            }
            
            var data = getTree(source, true);
            param.data = data;
            $(target).tree("insert", param);
            opts.onDrop.call(target, node, data, point);
        };
    };
    
    function checkNode(target, node, checked, cascaded) {
        var state = $.data(target, "tree");
        var opts = state.options;
        if (!opts.checkbox) {
            return;
        }
        
        var treeNode = getNode(target, node);
        if (!treeNode.checkState) {
            return;
        }
        
        var ck = $(node).find(".tree-checkbox");
        if (checked == undefined) {
            if (ck.hasClass("tree-checkbox1")) {
                checked = false;
            } else {
                if (ck.hasClass("tree-checkbox0")) {
                    checked = true;
                } else {
                    if (treeNode._checked == undefined) {
                        treeNode._checked = $(node).find(".tree-checkbox").hasClass("tree-checkbox1");
                    }
                    checked = !treeNode._checked;
                }
            }
        }
        treeNode._checked = checked;
        
        if (checked) {
            if (ck.hasClass("tree-checkbox1")) {
                return;
            }
        } else {
            if (ck.hasClass("tree-checkbox0")) {
                return;
            }
        }
        if (!cascaded) {
            if (opts.onBeforeCheck.call(target, treeNode, checked) == false) {
                return;
            }
        }
        
        if (opts.cascadeCheck) {
            setCascadeChk(target, treeNode, checked);
            setChecked(target, treeNode);
        } else {
            setSingleChk(target, treeNode, checked ? "1": "0");
        }
        if (!cascaded) {
            opts.onCheck.call(target, treeNode, checked);
        }
    };
    
    function setCascadeChk(target, node, checked) {
        var opts = $.data(target, "tree").options;
        var status = checked ? 1 : 0;
        setSingleChk(target, node, status);
        if (opts.deepCheck) {
            $.exui.forEach(node.children || [], true, function(n) {
                setSingleChk(target, n, status);
            });
        } else {
            var parentNode = [];
            if (node.children && node.children.length) {
                parentNode.push(node);
            }
            $.exui.forEach(node.children || [], true, function(n) {
                if (!n.hidden) {
                    setSingleChk(target, n, status);
                    if (n.children && n.children.length) {
                        parentNode.push(n);
                    }
                }
            });
            for (var i = parentNode.length - 1; i >= 0; i--) {
                var parent = parentNode[i];
                setSingleChk(target, parent, _46(parent));
            }
        }
    };
    
    function setSingleChk(target, node, checked) {
        var opts = $.data(target, "tree").options;
        if (!node.checkState || checked == undefined) {
            return;
        }
        if (node.hidden && !opts.deepCheck) {
            return;
        }
        
        var ck = $("#" + node.domId).find(".tree-checkbox");
        node.checkState = ["unchecked", "checked", "indeterminate"][checked];
        node.checked = (node.checkState == "checked");
        ck.removeClass("tree-checkbox0 tree-checkbox1 tree-checkbox2");
        ck.addClass("tree-checkbox" + checked);
    };
    
    function setChecked(target, node) {
        var pd = getParent(target, $("#" + node.domId)[0]);
        if (pd) {
            setSingleChk(target, pd, _46(pd));
            setChecked(target, pd);
        }
    };
    
    function _46(row) {
        var c0 = 0;
        var c1 = 0;
        var len = 0;
        
        $.exui.forEach(row.children || [], false, function(r) {
            if (r.checkState) {
                len++;
                if (r.checkState == "checked") {
                    c1++;
                } else {
                    if (r.checkState == "unchecked") {
                        c0++;
                    }
                }
            }
        });
        if (len == 0) {
            return undefined;
        }
        var _4e = 0;
        if (c0 == len) {
            _4e = 0;
        } else {
            if (c1 == len) {
                _4e = 1;
            } else {
                _4e = 2;
            }
        }
        return _4e;
    };
    
    function _4f(target, _51) {
        var opts = $.data(target, "tree").options;
        if (!opts.checkbox) {
            return;
        }
        var _53 = $(_51);
        var ck = _53.find(".tree-checkbox");
        var treeNode = getNode(target, _51);
        if (opts.view.hasCheckbox(target, treeNode)) {
            if (!ck.length) {
                treeNode.checkState = treeNode.checkState || "unchecked";
                $("<span class=\"tree-checkbox\"></span>").insertBefore(_53.find(".tree-title"));
            }
            if (treeNode.checkState == "checked") {
                checkNode(target, _51, true, true);
            } else {
                if (treeNode.checkState == "unchecked") {
                    checkNode(target, _51, false, true);
                } else {
                    var _55 = _46(treeNode);
                    if (_55 === 0) {
                        checkNode(target, _51, false, true);
                    } else {
                        if (_55 === 1) {
                            checkNode(target, _51, true, true);
                        }
                    }
                }
            }
        } else {
            ck.remove();
            treeNode.checkState = undefined;
            treeNode.checked = undefined;
            setChecked(target, treeNode);
        }
    };
    
    function loadData(target, ul, data, loaded, successed) {
        var state = $.data(target, "tree");
        var opts = state.options;
        var roots = $(ul).prevAll("div.tree-node:first");
        
        data = opts.loadFilter.call(target, data, roots[0]);
        var _5e = matchNode(target, "domId", roots.attr("id"));
        if (!loaded) {
            _5e ? _5e.children = data: state.data = data;
            $(ul).empty();
        } else {
            if (_5e) {
                _5e.children ? _5e.children = _5e.children.concat(data) : _5e.children = data;
            } else {
                state.data = state.data.concat(data);
            }
        }
        
        opts.view.render.call(opts.view, target, ul, data);
        if (opts.dnd) {
            enableDnd(target);
        }
        if (_5e) {
            updateNode(target, _5e);
        }
        
        for (var i = 0; i < state.tmpIds.length; i++) {
            checkNode(target, $("#" + state.tmpIds[i])[0], true, true);
        }
        state.tmpIds = [];
        setTimeout(function() {
            _61(target, target);
        }, 0);
        if (!successed) {
            opts.onLoadSuccess.call(target, _5e, data);
        }
    };
    
    function _61(target, ul, _63) {
        var opts = $.data(target, "tree").options;
        
        if (opts.lines) {
            $(target).addClass("tree-lines");
        } else {
            $(target).removeClass("tree-lines");
            return;
        }
        
        if (!_63) {
            _63 = true;
            $(target).find("span.tree-indent").removeClass("tree-line tree-join tree-joinbottom");
            $(target).find("div.tree-node").removeClass("tree-node-last tree-root-first tree-root-one");
            
            var roots = $(target).tree("getRoots");
            if (roots.length > 1) {
                $(roots[0].target).addClass("tree-root-first");
            } else {
                if (roots.length == 1) {
                    $(roots[0].target).addClass("tree-root-one");
                }
            }
        }
        
        $(ul).children("li").each(function() {
            var childs = $(this).children("div.tree-node");
            var ul = childs.next("ul");
            if (ul.length) {
                if ($(this).next().length) {
                    _67(childs);
                }
                _61(target, ul, _63);
            } else {
                _68(childs);
            }
        });
        
        var _69 = $(ul).children("li:last").children("div.tree-node").addClass("tree-node-last");
        _69.children("span.tree-join").removeClass("tree-join").addClass("tree-joinbottom");
        
        function _68(_6a, _6b) {
            var _6c = _6a.find("span.tree-icon");
            _6c.prev("span.tree-indent").addClass("tree-join");
        };
        function _67(_6d) {
            var _6e = _6d.find("span.tree-indent, span.tree-hit").length;
            _6d.next().find("div.tree-node").each(function() {
                $(this).children("span:eq(" + (_6e - 1) + ")").addClass("tree-line");
            });
        };
    };
    
    function reload(target, ul, param, callback) {
        var opts = $.data(target, "tree").options;
        param = $.extend({}, opts.queryParams, param || {});
        var treeNode = null;
        
        if (target != ul) {
            var _75 = $(ul).prev();
            treeNode = getNode(target, _75[0]);
        }
        if (opts.onBeforeLoad.call(target, treeNode, param) == false) {
            return;
        }
        
        var folder = $(ul).prev().children("span.tree-folder");
        folder.addClass("tree-loading");
        
        var loaded = opts.loader.call(target, param, function(data) {
            folder.removeClass("tree-loading");
            opts.onBeforeRender.call(target, data);
            loadData(target, ul, data);
            if (callback) {
                callback();
            }
        }, function() {
            folder.removeClass("tree-loading");
            opts.onLoadError.apply(target, arguments);
            if (callback) {
                callback();
            }
        });
        if (loaded == false) {
            folder.removeClass("tree-loading");
        }
    };
    
    function expand(target, node, callback) {
        var opts = $.data(target, "tree").options;
        var hit = $(node).children("span.tree-hit");
        
        if (hit.length == 0) {
            return;
        }
        if (hit.hasClass("tree-expanded")) {
            return;
        }
        
        var treeNode = getNode(target, node);
        if (opts.onBeforeExpand.call(target, treeNode) == false) {
            return;
        }
        hit.removeClass("tree-collapsed tree-collapsed-hover").addClass("tree-expanded");
        hit.next().addClass("tree-folder-open");
        
        var ul = $(node).next();
        if (ul.length) {
            if (opts.animate) {
                ul.slideDown("normal", function() {
                    treeNode.state = "open";
                    opts.onExpand.call(target, treeNode);
                    if (callback) {
                        callback();
                    }
                });
            } else {
                ul.css("display", "block");
                treeNode.state = "open";
                opts.onExpand.call(target, treeNode);
                if (callback) {
                    callback();
                }
            }
        } else {
            var _7f = $("<ul style=\"display:none\"></ul>").insertAfter(node);
            reload(target, _7f[0], {id: treeNode.id}, function() {
                if (_7f.is(":empty")) {
                    _7f.remove();
                }
                
                if (opts.animate) {
                    _7f.slideDown("normal", function() {
                        treeNode.state = "open";
                        opts.onExpand.call(target, treeNode);
                        if (callback) {
                            callback();
                        }
                    });
                } else {
                    _7f.css("display", "block");
                    treeNode.state = "open";
                    opts.onExpand.call(target, treeNode);
                    if (callback) {
                        callback();
                    }
                }
            });
        }
    };
    
    function collapse(target, node) {
        var opts = $.data(target, "tree").options;
        var hit = $(node).children("span.tree-hit");
        
        if (hit.length == 0) {
            return;
        }
        if (hit.hasClass("tree-collapsed")) {
            return;
        }
        
        var treeNode = getNode(target, node);
        if (opts.onBeforeCollapse.call(target, treeNode) == false) {
            return;
        }
        hit.removeClass("tree-expanded tree-expanded-hover").addClass("tree-collapsed");
        hit.next().removeClass("tree-folder-open");
        
        var ul = $(node).next();
        if (opts.animate) {
            ul.slideUp("normal", function() {
                treeNode.state = "closed";
                opts.onCollapse.call(target, treeNode);
            });
        } else {
            ul.css("display", "none");
            treeNode.state = "closed";
            opts.onCollapse.call(target, treeNode);
        }
    };
    
    function toggle(target, node) {
        var hit = $(node).children("span.tree-hit");
        if (hit.length == 0) {
            return;
        }
        if (hit.hasClass("tree-expanded")) {
            collapse(target, node);
        } else {
            expand(target, node);
        }
    };
    
    function expandAll(target, node) {
        var childs = getChildren(target, node);
        if (node) {
            childs.unshift(getNode(target, node));
        }
        for (var i = 0; i < childs.length; i++) {
            expand(target, childs[i].target);
        }
    };
    
    function expandTo(target, node) {
        var nodes = [];
        var p = getParent(target, node);
        
        while (p) {
            nodes.unshift(p);
            p = getParent(target, p.target);
        }
        for (var i = 0; i < nodes.length; i++) {
            expand(target, nodes[i].target);
        }
    };
    
    function scrollTo(target, node) {
        var c = $(target).parent();
        while (c[0].tagName != "BODY" && c.css("overflow-y") != "auto") {
            c = c.parent();
        }
        
        var n = $(node);
        var ntop = n.offset().top;
        if (c[0].tagName != "BODY") {
            var ctop = c.offset().top;
            if (ntop < ctop) {
                c.scrollTop(c.scrollTop() + ntop - ctop);
            } else {
                if (ntop + n.outerHeight() > ctop + c.outerHeight() - 18) {
                    c.scrollTop(c.scrollTop() + ntop + n.outerHeight() - ctop - c.outerHeight() + 18);
                }
            }
        } else {
            c.scrollTop(ntop);
        }
    };
    
    function collapseAll(target, node) {
        var childs = getChildren(target, node);
        if (node) {
            childs.unshift(getNode(target, node));
        }
        for (var i = 0; i < childs.length; i++) {
            collapse(target, childs[i].target);
        }
    };
    
    function appendNode(target, param) {
        var parent = $(param.parent);
        var data = param.data;
        
        if (!data) {
            return;
        }
        data = $.isArray(data) ? data: [data];
        if (!data.length) {
            return;
        }
        
        var ul;
        if (parent.length == 0) {
            ul = $(target);
        } else {
            if (isLeaf(target, parent[0])) {
                var treeIcon = parent.find("span.tree-icon");
                treeIcon.removeClass("tree-file").addClass("tree-folder tree-folder-open");
                var hit = $("<span class=\"tree-hit tree-expanded\"></span>").insertBefore(treeIcon);
                if (hit.prev().length) {
                    hit.prev().remove();
                }
            }
            ul = parent.next();
            if (!ul.length) {
                ul = $("<ul></ul>").insertAfter(parent);
            }
        }
        loadData(target, ul[0], data, true, true);
    };
    
    function insertNode(target, param) {
        var ref = param.before || param.after;
        var parent = getParent(target, ref);
        var data = param.data;
        if (!data) {
            return;
        }
        
        data = $.isArray(data) ? data: [data];
        if (!data.length) {
            return;
        }
        appendNode(target, {
            parent: (parent ? parent.target: null),
            data: data
        });
        
        var roots = parent ? parent.children: $(target).tree("getRoots");
        for (var i = 0; i < roots.length; i++) {
            if (roots[i].domId == $(ref).attr("id")) {
                for (var j = data.length - 1; j >= 0; j--) {
                    roots.splice((param.before ? i: (i + 1)), 0, data[j]);
                }
                roots.splice(roots.length - data.length, data.length);
                break;
            }
        }
        
        var li = $();
        for (var i = 0; i < data.length; i++) {
            li = li.add($("#" + data[i].domId).parent());
        }
        if (param.before) {
            li.insertBefore($(ref).parent());
        } else {
            li.insertAfter($(ref).parent());
        }
    };
    
    function removeNode(target, node) {
        var parent = del(node);
        $(node).parent().remove();
        
        if (parent) {
            if (!parent.children || !parent.children.length) {
                var pt = $(parent.target);
                pt.find(".tree-icon").removeClass("tree-folder").addClass("tree-file");
                pt.find(".tree-hit").remove();
                $("<span class=\"tree-indent\"></span>").prependTo(pt);
                pt.next().remove();
            }
            updateNode(target, parent);
        }
        
        _61(target, target);
        function del(n) {
            var id = $(n).attr("id");
            var p = getParent(target, n);
            var cc = p ? p.children: $.data(target, "tree").data;
            for (var i = 0; i < cc.length; i++) {
                if (cc[i].domId == id) {
                    cc.splice(i, 1);
                    break;
                }
            }
            return p;
        };
    };
    
    function updateNode(target, param) {
        var opts = $.data(target, "tree").options;
        var n = $(param.target);
        var treeNode = getNode(target, param.target);
        
        if (treeNode.iconCls) {
            n.find(".tree-icon").removeClass(treeNode.iconCls);
        }
        
        $.extend(treeNode, param);
        n.find(".tree-title").html(opts.formatter.call(target, treeNode));
        if (treeNode.iconCls) {
            n.find(".tree-icon").addClass(treeNode.iconCls);
        }
        _4f(target, param.target);
    };
    
    function getRoot(target, node) {
        if (node) {
            var p = getParent(target, node);
            while (p) {
                node = p.target;
                p = getParent(target, node);
            }
            return getNode(target, node);
        } else {
            var roots = getRoots(target);
            return roots.length ? roots[0] : null;
        }
    };
    
    function getRoots(target) {
        var data = $.data(target, "tree").data;
        for (var i = 0; i < data.length; i++) {
            _ba(data[i]);
        }
        return data;
    };
    
    function getChildren(target, node) {
        var childs = [];
        var n = getNode(target, node);
        var data = n ? (n.children || []) : $.data(target, "tree").data;
        
        $.exui.forEach(data, true, function(_bf) {
            childs.push(_ba(_bf));
        });
        return childs;
    };
    
    function getParent(target, node) {
        var p = $(node).closest("ul").prevAll("div.tree-node:first");
        return getNode(target, p[0]);
    };
    
    function getChecked(target, state) {
        state = state || "checked";
        if (!$.isArray(state)) {
            state = [state];
        }
        
        var checkboxs = [];
        $.exui.forEach($.data(target, "tree").data, true, function(n) {
            if (n.checkState && $.exui.indexOfArray(state, n.checkState) != -1) {
                checkboxs.push(_ba(n));
            }
        });
        
        return checkboxs;
    };
    
    function getSelected(target) {
        var selectedNode = $(target).find("div.tree-node-selected");
        return selectedNode.length ? getNode(target, selectedNode[0]) : null;
    };
    
    function getData(target, node) {
        var treeNode = getNode(target, node);
        if (treeNode && treeNode.children) {
            $.exui.forEach(treeNode.children, true, function(_cd) {
                _ba(_cd);
            });
        }
        return treeNode;
    };
    
    function getNode(target, node) {
        return matchNode(target, "domId", $(node).attr("id"));
    };
    
    function findNode(target, id) {
        return matchNode(target, "id", id);
    };
    
    function matchNode(target, domId, id) {
        var data = $.data(target, "tree").data;
        var _d6 = null;
        
        $.exui.forEach(data, true, function(node) {
            if (node[domId] == id) {
                _d6 = _ba(node);
                return false;
            }
        });
        return _d6;
    };
    
    function _ba(node) {
        node.target = $("#" + node.domId)[0];
        return node;
    };
    
    function selectNode(target, node) {
        var opts = $.data(target, "tree").options;
        var treeNode = getNode(target, node);
        if (opts.onBeforeSelect.call(target, treeNode) == false) {
            return;
        }
        $(target).find("div.tree-node-selected").removeClass("tree-node-selected");
        $(node).addClass("tree-node-selected");
        opts.onSelect.call(target, treeNode);
    };
    
    function isLeaf(target, node) {
        return $(node).children("span.tree-hit").length == 0;
    };
    
    function beginEdit(target, node) {
        var opts = $.data(target, "tree").options;
        var treeNode = getNode(target, node);
        if (opts.onBeforeEdit.call(target, treeNode) == false) {
            return;
        }
        
        $(node).css("position", "relative");
        var nt = $(node).find(".tree-title");
        var outerWidth = nt.outerWidth();
        nt.empty();
        
        var editor = $("<input class=\"tree-editor form-control\">").appendTo(nt);
        editor.val(treeNode.text).focus();
        editor.width(outerWidth + 20).height(20);
        //editor.css('height', '20px');
        //editor._outerHeight(20);
        
        editor.bind("click", function(e) {
            return false;
        }).bind("mousedown", function(e) {
            e.stopPropagation();
        }).bind("mousemove", function(e) {
            e.stopPropagation();
        }).bind("keydown", function(e) {
            if (e.keyCode == 13) {
                endEdit(target, node);
                return false;
            } else {
                if (e.keyCode == 27) {
                    cancelEdit(target, node);
                    return false;
                }
            }
        }).bind("blur", function(e) {
            e.stopPropagation();
            endEdit(target, node);
        });
    };
    
    function endEdit(target, node) {
        var opts = $.data(target, "tree").options;
        $(node).css("position", "");
        
        var editor = $(node).find("input.tree-editor");
        var val = editor.val();
        editor.remove();
        
        var treeNode = getNode(target, node);
        treeNode.text = val;
        updateNode(target, treeNode);
        opts.onAfterEdit.call(target, treeNode);
    };
    
    function cancelEdit(target, node) {
        var opts = $.data(target, "tree").options;
        $(node).css("position", "");
        $(node).find("input.tree-editor").remove();
        
        var node = getNode(target, node);
        updateNode(target, node);
        opts.onCancelEdit.call(target, node);
    };
    
    function doFilter(target, q) {
        var state = $.data(target, "tree");
        var opts = state.options;
        var ids = {};
        
        $.exui.forEach(state.data, true, function(node) {
            if (opts.filter.call(target, q, node)) {
                $("#" + node.domId).removeClass("tree-node-hidden");
                ids[node.domId] = 1;
                node.hidden = false;
            } else {
                $("#" + node.domId).addClass("tree-node-hidden");
                node.hidden = true;
            }
        });
        
        for (var id in ids) {
            _f7(id);
        }
        
        function _f7(id) {
            var p = $(target).tree("getParent", $("#" + id)[0]);
            while (p) {
                $(p.target).removeClass("tree-node-hidden");
                p.hidden = false;
                p = $(target).tree("getParent", p.target);
            }
        };
    };
    
    $.fn.tree = function(options, param) {
        if (typeof options == "string") {
            return $.fn.tree.methods[options](this, param);
        }
        
        var options = options || {};
        return this.each(function() {
            var state = $.data(this, "tree");
            var opts;
            if (state) {
                opts = $.extend(state.options, options);
                state.options = opts;
            } else {
                opts = $.extend({}, $.fn.tree.defaults, $.fn.tree.parseOptions(this), options);
                $.data(this, "tree", {
                    options: opts,
                    tree: init(this),
                    data: [],
                    tmpIds: []
                });
                
                var data = $.fn.tree.parseData(this);
                if (data.length) {
                    loadData(this, this, data);
                }
            }
            
            bindEvents(this);
            if (opts.data) {
                loadData(this, this, $.extend(true, [], opts.data));
            }
            reload(this, this);
        });
    };
    
    $.fn.tree.methods = {
        options: function(jq) {
            return $.data(jq[0], "tree").options;
        },
        loadData: function(jq, data) {
            return jq.each(function() {
                loadData(this, this, data);
            });
        },
        getNode: function(jq, target) {
            return getNode(jq[0], target);
        },
        getData: function(jq, target) {
            return getData(jq[0], target);
        },
        reload: function(jq, target) {
            return jq.each(function() {
                if (target) {
                    var node = $(target);
                    var hit = node.children("span.tree-hit");
                    hit.removeClass("tree-expanded tree-expanded-hover").addClass("tree-collapsed");
                    node.next().remove();
                    expand(this, target);
                } else {
                    $(this).empty();
                    reload(this, this);
                }
            });
        },
        getRoot: function(jq, target) {
            return getRoot(jq[0], target);
        },
        getRoots: function(jq) {
            return getRoots(jq[0]);
        },
        getParent: function(jq, target) {
            return getParent(jq[0], target);
        },
        getChildren: function(jq, target) {
            return getChildren(jq[0], target);
        },
        getChecked: function(jq, state) {
            return getChecked(jq[0], state);
        },
        getSelected: function(jq) {
            return getSelected(jq[0]);
        },
        isLeaf: function(jq, target) {
            return isLeaf(jq[0], target);
        },
        find: function(jq, id) {
            return findNode(jq[0], id);
        },
        select: function(jq, target) {
            return jq.each(function() {
                selectNode(this, target);
            });
        },
        check: function(jq, target) {
            return jq.each(function() {
                checkNode(this, target, true);
            });
        },
        uncheck: function(jq, target) {
            return jq.each(function() {
                checkNode(this, target, false);
            });
        },
        collapse: function(jq, target) {
            return jq.each(function() {
                collapse(this, target);
            });
        },
        expand: function(jq, target) {
            return jq.each(function() {
                expand(this, target);
            });
        },
        collapseAll: function(jq, target) {
            return jq.each(function() {
                collapseAll(this, target);
            });
        },
        expandAll: function(jq, target) {
            return jq.each(function() {
                expandAll(this, target);
            });
        },
        expandTo: function(jq, target) {
            return jq.each(function() {
                expandTo(this, target);
            });
        },
        scrollTo: function(jq, target) {
            return jq.each(function() {
                scrollTo(this, target);
            });
        },
        toggle: function(jq, target) {
            return jq.each(function() {
                toggle(this, target);
            });
        },
        append: function(jq, param) {
            return jq.each(function() {
                appendNode(this, param);
            });
        },
        insert: function(jq, param) {
            return jq.each(function() {
                insertNode(this, param);
            });
        },
        remove: function(jq, target) {
            return jq.each(function() {
                removeNode(this, target);
            });
        },
        pop: function(jq, target) {
            var node = jq.tree("getData", target);
            jq.tree("remove", target);
            return node;
        },
        update: function(jq, param) {
            return jq.each(function() {
                updateNode(this, $.extend({}, param, {
                    checkState: param.checked ? "checked": (param.checked === false ? "unchecked": undefined)
                }));
            });
        },
        enableDnd: function(jq) {
            return jq.each(function() {
                enableDnd(this);
            });
        },
        disableDnd: function(jq) {
            return jq.each(function() {
                disableDnd(this);
            });
        },
        beginEdit: function(jq, target) {
            return jq.each(function() {
                beginEdit(this, target);
            });
        },
        endEdit: function(jq, target) {
            return jq.each(function() {
                endEdit(this, target);
            });
        },
        cancelEdit: function(jq, target) {
            return jq.each(function() {
                cancelEdit(this, target);
            });
        },
        doFilter: function(jq, text) {
            return jq.each(function() {
                doFilter(this, text);
            });
        }
    };
    
    $.fn.tree.parseOptions = function(target) {
        var t = $(target);
        
        return $.extend({}, $.parser.parseOptions(target, ["url", "method", {
            checkbox: "boolean",
            cascadeCheck: "boolean",
            onlyLeafCheck: "boolean"
        },
        {
            animate: "boolean",
            lines: "boolean",
            dnd: "boolean"
        }]));
    };
    
    $.fn.tree.parseData = function(target) {
        var data = [];
        parseChilds(data, $(target));
        return data;
        
        function parseChilds(aa, tree) {
            tree.children("li").each(function() {
                var node = $(this);
                var item = $.extend({}, $.parser.parseOptions(this, ["id", "iconCls", "state"]), {
                    checked: (node.attr("checked") ? true: undefined)
                });
                item.text = node.children("span").html();
                if (!item.text) {
                    item.text = node.html();
                }
                
                var childs = node.children("ul");
                if (childs.length) {
                    item.children = [];
                    parseChilds(item.children, childs);
                }
                aa.push(item);
            });
        };
    };
    
    var TREENODE_SERNO = 1;
    var view = {
        render: function(target, ul, data) {
            var state = $.data(target, "tree");
            var opts = state.options;
            var treeNode = $(ul).prev(".tree-node");
            var _122 = treeNode.length ? $(target).tree("getNode", treeNode[0]) : null;
            var level = treeNode.find("span.tree-indent, span.tree-hit").length;
            var cc = createTreeNode.call(this, level, data);
            $(ul).append(cc.join(""));
            
            function createTreeNode(level, data) {
                var cc = [];
                for (var i = 0; i < data.length; i++) {
                    var item = data[i];
                    if (item.state != "open" && item.state != "closed") {
                        item.state = "open";
                    }
                    item.domId = "_exui_tree_" + TREENODE_SERNO++;
                    cc.push("<li>");
                    cc.push("<div id=\"" + item.domId + "\" class=\"tree-node\">");
                    for (var j = 0; j < level; j++) {
                        cc.push("<span class=\"tree-indent\"></span>");
                    }
                    
                    if (item.state == "closed") {
                        cc.push("<span class=\"tree-hit tree-collapsed\"></span>");
                        cc.push("<span class=\"tree-icon tree-folder " + (item.iconCls ? item.iconCls: "") + "\"></span>");
                    } else {
                        if (item.children && item.children.length) {
                            cc.push("<span class=\"tree-hit tree-expanded\"></span>");
                            cc.push("<span class=\"tree-icon tree-folder tree-folder-open " + (item.iconCls ? item.iconCls: "") + "\"></span>");
                        } else {
                            cc.push("<span class=\"tree-indent\"></span>");
                            cc.push("<span class=\"tree-icon tree-file " + (item.iconCls ? item.iconCls: "") + "\"></span>");
                        }
                    }
                    if (this.hasCheckbox(target, item)) {
                        var flag = 0;
                        if (_122 && _122.checkState == "checked" && opts.cascadeCheck) {
                            flag = 1;
                            item.checked = true;
                        } else {
                            if (item.checked) {
                                $.exui.addArrayItem(state.tmpIds, item.domId);
                            }
                        }
                        item.checkState = flag ? "checked": "unchecked";
                        cc.push("<span class=\"tree-checkbox tree-checkbox" + flag + "\"></span>");
                    } else {
                        item.checkState = undefined;
                        item.checked = undefined;
                    }
                    cc.push("<span class=\"tree-title\">" + opts.formatter.call(target, item) + "</span>");
                    cc.push("</div>");
                    if (item.children && item.children.length) {
                        var tmp = createTreeNode.call(this, level + 1, item.children);
                        cc.push("<ul style=\"display:" + (item.state == "closed" ? "none": "block") + "\">");
                        cc = cc.concat(tmp);
                        cc.push("</ul>");
                    }
                    cc.push("</li>");
                }
                return cc;
            };
        },
        hasCheckbox: function(target, item) {
            var state = $.data(target, "tree");
            var opts = state.options;
            if (opts.checkbox) {
                if ($.isFunction(opts.checkbox)) {
                    if (opts.checkbox.call(target, item)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    if (opts.onlyLeafCheck) {
                        if (item.state == "open" && !(item.children && item.children.length)) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
            return false;
        }
    };
    
    $.fn.tree.defaults = {
        url: null,
        method: "post",
        animate: false,
        checkbox: false,
        cascadeCheck: true,
        onlyLeafCheck: false,
        lines: false,
        dnd: false,
        data: null,
        queryParams: {},
        formatter: function(node) {
            return node.text;
        },
        filter: function(q, node) {
            var qq = [];
            $.map($.isArray(q) ? q: [q], function(q) {
                q = $.trim(q);
                if (q) {
                    qq.push(q);
                }
            });
            for (var i = 0; i < qq.length; i++) {
                var _129 = node.text.toLowerCase().indexOf(qq[i].toLowerCase());
                if (_129 >= 0) {
                    return true;
                }
            }
            return ! qq.length;
        },
        loader: function(param, success, error) {
            var opts = $(this).tree("options");
            if (!opts.url) {
                return false;
            }
            $.ajax({
                type: opts.method,
                url: opts.url,
                data: param,
                dataType: "json",
                success: function(data) {
                    success(data);
                },
                error: function() {
                    error.apply(this, arguments);
                }
            });
        },
        loadFilter: function(data, parent) {
            return data;
        },
        view: view,
        onBeforeLoad: function(node, param) {},
        onBeforeRender: function(data) {},
        onLoadSuccess: function(node, data) {},
        onLoadError: function() {},
        onClick: function(node) {},
        onDblClick: function(node) {},
        onBeforeExpand: function(node) {},
        onExpand: function(node) {},
        onBeforeCollapse: function(node) {},
        onCollapse: function(node) {},
        onBeforeCheck: function(node, checked) {},
        onCheck: function(node, checked) {},
        onBeforeSelect: function(node) {},
        onSelect: function(node) {},
        onContextMenu: function(e, node) {},
        onBeforeDrag: function(node) {},
        onStartDrag: function(node) {},
        onStopDrag: function(node) {},
        onDragEnter: function(target, source) {},
        onDragOver: function(target, source) {},
        onDragLeave: function(target, source) {},
        onBeforeDrop: function(target, source, point) {},
        onDrop: function(target, source, point) {},
        onBeforeEdit: function(node) {},
        onAfterEdit: function(node) {},
        onCancelEdit: function(node) {}
    };
})(jQuery);