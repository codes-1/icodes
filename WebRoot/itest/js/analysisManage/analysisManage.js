window.analysisObj = {
		$analyOne:$("#contentarea")
}

$(function(){
	$.parser.parse();
	//切换项目
	initTree();
});

function initTree(){
	$.post(
			baseUrl + '/analysis/analysisAction!analysisMainList.action',
			null,
			function(data){
				data = JSON.parse(data);
				//构造树结构数据
				var tree = constrTree(data);
				$('#analysisManange').xtree({
					data: tree,
					animate:true,
					lines:true,
			    	onClick:function(node){
			    		if(node.attributes.url != ""){
			    			if( node.attributes.url=="bugModuleDistbuStat"){
			    				  var itemId = $("#analyitemId").val();
			    					var urlStr = baseUrl+"/caseManager/caseManagerAction!loadTree.action?dto.taskId="+itemId;
			    					$('#itemDetailTree').xtree({
			    					    url:urlStr,
			    					    method:'get',
			    						animate:true,
			    						lines:true,
			    						checkbox:true
			    						
			    					});
			    				$("#testCaseWin").xwindow('setTitle','测试需求分解树').xwindow('open');
			    			}else{
			    				var loadUrl = baseUrl + "/analysis/analysisAction!" + node.attributes.url + ".action";
								analysisObj.$analyOne.load(loadUrl);
			    			}
			    			
			    		}
					 }
				});
			},'text');
}

function constrTree(srcdata){
	if(srcdata!=null){
		var dataArr = srcdata.split(";");
		dataArr.pop();
		var treeNode = []; 
		
		for(var i=0; i<dataArr.length; i++){
	       var nodeArr = dataArr[i].split(",");
	       //目前数据库字段不正确，若后期更正，这可删除此段代码。代码开始
	       var nodeUrl ="";
	       if(nodeArr[3]!=null){
	    	  var temparr = nodeArr[3].split("=");
	    	  if(temparr.length>1){
	    		var nodeUrl = temparr[1].split("_")[0]; 
	    	  }
	       }
	       //代码结束
	       var node = {
	    		   id:nodeArr[1],
	    		   parentId:nodeArr[0],
	    		   name:nodeArr[2],
	    		   url:nodeUrl
	       }
		
	       treeNode.push(node);
		}
		
		return getTreeData(treeNode);
	}
	return
}

//构造树数据结构
function getTreeData(treeData){
	var treeNd = [];
	for(var j=0;j<treeData.length;j++){
		
		//获取父节点
		if(treeData[j].parentId === "0"){
			 var node = {
		    		   "id":treeData[j].id,
		    		   "parentId":treeData[j].parentId,
		    		   "text":treeData[j].name,
		    		   "attributes":{
		    			   "url":treeData[j].url
		    	        },
		    		   "children":[]
		       }
			 
			treeNd.push(node);
			treeData.splice(j,1);
			j--;
		}
	}
	
	//获取子节点
	getChildNode(treeNd);
	
	function getChildNode(parentsNode){
		if(parentsNode.length>0){
			for(var i=0; i<parentsNode.length; i++){
				for(var k=0; k<treeData.length; k++){
					if(parentsNode[i].id === treeData[k].parentId){
						 var chNode = {
					    		   "id":treeData[k].id,
					    		   "parentId":treeData[k].parentId,
					    		   "text":treeData[k].name,
					    		   "attributes":{
					    			   "url":treeData[k].url
					    	        },
					    		   "children":[]
					       }
						parentsNode[i].children.push(chNode);
						treeData.splice(k,1);
						k--;
					}
				}
				 getChildNode(parentsNode[i].children);
			}
		}
		
	}
	
	return treeNd;
}

//收缩树状图
document.getElementById("layoutLeftBtn").addEventListener('click',function(){
	
	document.getElementById("container").className = "tigger";
});

document.getElementById("layoutRigthBtn").addEventListener('click',function(){
	document.getElementById("container").className = "";

});


document.getElementById("viewReportBtn").addEventListener('click',function(){
	var moduleIds = "";
	var nodes = $('#itemDetailTree').tree('getChecked');
	var num = 0;
	if( nodes.length > 0){
		for(var i=0; i<nodes.length; i++){
			moduleIds += nodes[i].id + ",";
			if(!nodes[i].rootNode){
				//获取父节点id
				moduleIds = getParentNodeId(nodes[i].target,moduleIds);
			}
		}
		 num = moduleIds.length-1;
		 $("#moduleIds").val(moduleIds.substr(0,num));
		var loadUrl = baseUrl + "/analysis/analysisAction!bugModuleDistbuStat.action";
		analysisObj.$analyOne.load(loadUrl);
		$("#testCaseWin").xwindow('close');

	}else{
		$.xalert({title:'消息提示',msg:'请选择您要查看报表项！'});
	}
	
});

//获取父节点ID
function getParentNodeId(target,idStr){
	var nodePar = $('#itemDetailTree').xtree("getParent",target);
	if(idStr.indexOf(nodePar.id) == -1){
		idStr = nodePar.id + "," + idStr ;
	}
	if(!nodePar.rootNode){
		idStr = getParentNodeId(nodePar.target,idStr);
	}
	return idStr;
}


//@ sourceURL=analysisManange.js