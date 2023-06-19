<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/itest/plug-in/chosen/chosen.css" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/itest/css/itestManual/itestManual.css" />
<style>
.btnC {
	width:100%;
	text-align:center;
	padding:0px 0px;
} 

ul {
    margin-bottom: 50px;
} 
.margin-r{
    margin-right:0px
}
</style>
<div class="manualPage">
	<div class="contentIt">
		<div class="leftCons">
			<div class="leftP">
				<p class="title">项目测试</p>
			</div>
			<div class="proFlow row" style="display: block;">
				<div class="first col-lg-2 margin-r" >
					<button type="button" class="btn btn-primary btnC"
						style="height: 3em; background: #0C84FF; border: none; border-radius: 5px;">01.新建测试项目</button>
					<div class="testPro">
					<div style="text-align:center">
						<img alt="新建测试项目"
							src="<%=request.getContextPath()%>/itest/images/homeImage/project.png"></div>
						<div class="proUl">
							<ul>
								<li>预先维护人员,权限</li>
								<li>预先维护基础数据</li>
								<li>维护项目信息</li>
								

							</ul>
						</div>
					</div>
				</div>

				<div class="first col-lg-2 margin-r">
					<button type="button" class="btn btn-primary btnC"
						style="height: 3em; background: #FF8308; border: none; border-radius: 5px;">02.设置测试流程</button>
					<div class="testPro">
					<div style="text-align:center">
						<img alt="设置测试流程"
							src="<%=request.getContextPath()%>/itest/images/homeImage/process.png"></div>
						<div class="proUl">
							<ul>
								<li>设置启用的流程</li>
								<li>设置流程节点人员</li>
								<li>维护项目版本信息</li>
							</ul>
						</div>
					</div>
				</div>

				<div class="first col-lg-2 margin-r">
					<button type="button" class="btn btn-primary btnC"
						style="height: 3em; background: #00B4E5; border: none; border-radius: 5px;">03.提交测试需求</button>
					<div class="testPro">
					<div style="text-align:center">
						<img alt="提交测试需求"
							src="<%=request.getContextPath()%>/itest/images/homeImage/tree.png"></div>
						<div class="proUl">
							<ul>
								<li>维护所测功能模块</li>
								<li>设置各模块人员</li>
								<li>提交测试模块</li>
							</ul>
						</div>
					</div>
				</div>

				<div class="first col-lg-2 margin-r">
					<button type="button" class="btn btn-primary btnC"
						style="height: 3em; background: #0FC66B; border: none; border-radius: 5px;">04.编写测试用例</button>
					<div class="testPro">
					<div style="text-align:center">
						<img alt="编写测试用例"
							src="<%=request.getContextPath()%>/itest/images/homeImage/text.png"></div>
						<div class="proUl">
							<ul>
								<li>编写测试用例</li>
								<li>构建测试用例包</li>
								<li>执行测试用例</li>
							</ul>
						</div>
					</div>
				</div>

				<div class="first col-lg-2 margin-r">
					<button type="button" class="btn btn-primary btnC"
						style="height: 3em; background: #F34767; border: none; border-radius: 5px;">05.编写缺陷报告</button>
					<div class="testPro">
					<div style="text-align:center">
						<img alt="编写缺陷报告"
							src="<%=request.getContextPath()%>/itest/images/homeImage/insect.png">
							</div>
						<div class="proUl">
							<ul>
								<li>编写缺陷报告</li>
								<li>按流程流转缺陷</li>
								<li>处理名下缺陷</li>
							</ul>
						</div>
					</div>
				</div>


			</div>
		</div>

		<div class="rightCon">
			<div class="rightP">
				<p class="titles">项目管理</p>
			</div>

			<div class="proFlows row" style="display:block">
				<div class="second ">
					<button type="button" class="btn btn-primary btnC"
						style="height: 3em; background: #953FE3; border: none; border-radius: 5px;">项目任务及迭代</button>
					<div class="testPro col-md-12">
					<div style="text-align:center">
						<img alt="项目任务及迭代"
							src="<%=request.getContextPath()%>/itest/images/homeImage/iteration.png"></div>
						<div class="proUl">
							<ul>
								<li>新建项目任务</li>
								<li>构建项目迭代</li>
								<li>执行分配的任务</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>