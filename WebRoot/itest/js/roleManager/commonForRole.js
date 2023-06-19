/**
 * 将字符串数据转化成树结构的父子节点数据
 */

//构造树结构数据
function constructTree(data){
    var dataArr = data.split(';');
    var treeData = [];
    for(var i =0 ; i<dataArr.length; i++){
         var temp = dataArr[i].split(',');
         var node = {
         	parentId:temp[0],
         	id:temp[1],
         	text:temp[2]
         }
         treeData.push(node);
    }

    return toTreeNode(treeData);
}

//构造树的节点
function toTreeNode(treeData){
	var treeNd = [];
    for(var i=0;i<treeData.length;i++){
    	
    	//获取父节点
    	if(treeData[i].parentId === "-1"){
          var node = {
          	parentId:treeData[i].parentId,
          	id:treeData[i].id,
          	text:treeData[i].text,
          	children:[]
          }

         treeNd.push(node);
         treeData.splice(i,1);
         i--;
    	}
    }
     //构造树的子节点
      toTreeChildren(treeNd);

    //构造树的子节点
    function toTreeChildren(treeNode){
    	if(treeData.length !== 0){
    	for(var j=0;j<treeNode.length;j++){
    		for(var k=0;k<treeData.length;k++){
               if(treeNode[j].id === treeData[k].parentId){
                   var obj = {
                   	   parentId:treeData[k].parentId,
                   	   id:treeData[k].id,
                   	   text:treeData[k].text,
                   	   children:[]
                   }

                   treeNode[j].children.push(obj);
                   treeData.splice(k,1);
                   k--;
               }
    		}
            toTreeChildren( treeNode[j].children);
    	}

    	}
    }
    return treeNd;
}


/*获取页面url参数*/
//dlgName:对话框id;name:传参名称
function getQueryParam(dlgName,name){
	var obj = $('#'+dlgName).dialog("options");
	var queryParams = obj["queryParams"];
	
	return queryParams[name];
}

/*关闭对话框*/
//dlgId:dialog对话框的id
function closeDlg(dlgId){
	  $('#' + dlgId).dialog('destroy');
}

