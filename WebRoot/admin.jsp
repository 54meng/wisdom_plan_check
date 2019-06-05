<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%response.setContentType("text/html;charset=UTF-8"); %>
<%
response.sendRedirect("admin/template/admin/index.jsp");
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="img/favicon.ico">

<title>旅游后台管理系统</title>

<!-- Bootstrap core CSS -->
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="bootstrap/css/dashboard.css" rel="stylesheet">

<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
<script src="bootstrap/js/ie-emulation-modes-warning.js"></script>

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="bootstrap/js/html5shiv.min.js"></script>
      <script src="bootstrap/js/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">切换导航</span> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">旅游后台管理系统</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<!-- <li><a href="#" >控制台</a></li>
            <li><a href="#">设置</a></li>
            <li><a href="#">个人中心</a></li>
            <li><a href="#">帮助</a></li> -->
				</ul>
				<!--<form class="navbar-form navbar-right">
            <input type="text" class="form-control" placeholder="Search...">
          </form>
           -->
			</div>
		</div>
	</nav>

	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-3 col-md-2 sidebar">
				<ul class="nav nav-sidebar">
					<li class="active"><a href="panel/userMgrPanel.jsp">用户管理<span
							class="sr-only">(当前)</span></a></li>
					<li><a href="#">景点管理</a></li>
					<li><a href="#">美食管理</a></li>
					<li><a href="#">购物管理</a></li>
				</ul>
			</div>
			<div id="main"
				class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h1 id="main_title" class="page-header">用户管理</h1>
				<p>
					<button type="button" class="btn btn-primary" data-toggle="modal"
						data-target="#addEditUser">新建</button>
					<button type="button" class="btn btn-primary" onclick="$().alert()">删除</button>
					<button type="button" class="btn btn-primary">刷新</button>
				</p>
				<div class="table-responsive">
					<table class="table table-striped table-condensed table-hover">
						<thead>
							<tr>
								<th style="width:10px"><input type="checkbox"
									onclick="bs4j.checkTable(this);" /></th>
								<th>Header</th>
								<th>Header</th>
								<th>Header</th>
								<th>Header</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td><input type="checkbox" /></td>
								<td>Lorem</td>
								<td>ipsum</td>
								<td>dolor</td>
								<td>sit</td>
								<td><input class="btn btn-primary btn-xs" type="button"
									value="编辑"></td>
							</tr>
							<tr>
								<td><input type="checkbox" /></td>
								<td>amet</td>
								<td>consectetur</td>
								<td>adipiscing</td>
								<td>elit</td>
								<td><input class="btn btn-primary btn-xs" type="button"
									value="编辑"></td>
							</tr>
							<tr>
								<td><input type="checkbox" /></td>
								<td>Integer</td>
								<td>nec</td>
								<td>odio</td>
								<td>Praesent</td>
								<td><input class="btn btn-primary btn-xs" type="button"
									value="编辑"></td>
							</tr>
							<tr>
								<td><input type="checkbox" /></td>
								<td>libero</td>
								<td>Sed</td>
								<td>cursus</td>
								<td>ante</td>
								<td><input class="btn btn-primary btn-xs" type="button"
									value="编辑"></td>
							</tr>
							<tr>
								<td><input type="checkbox" /></td>
								<td>dapibus</td>
								<td>diam</td>
								<td>Sed</td>
								<td>nisi</td>
								<td><input class="btn btn-primary btn-xs" type="button"
									value="编辑"></td>
							</tr>
							<tr>
								<td><input type="checkbox" /></td>
								<td>Nulla</td>
								<td>quis</td>
								<td>sem</td>
								<td>at</td>
								<td><input class="btn btn-primary btn-xs" type="button"
									value="编辑"></td>
							</tr>
							<tr>
								<td><input type="checkbox" /></td>
								<td>nibh</td>
								<td>elementum</td>
								<td>imperdiet</td>
								<td>Duis</td>
								<td><input class="btn btn-primary btn-xs" type="button"
									value="编辑"></td>
							</tr>
							<tr>
								<td><input type="checkbox" /></td>
								<td>sagittis</td>
								<td>ipsum</td>
								<td>Praesent</td>
								<td>mauris</td>
								<td><input class="btn btn-primary btn-xs" type="button"
									value="编辑"></td>
							</tr>
							<tr>
								<td><input type="checkbox" /></td>
								<td>Fusce</td>
								<td>nec</td>
								<td>tellus</td>
								<td>sed</td>
								<td><input class="btn btn-primary btn-xs" type="button"
									value="编辑"></td>
							</tr>
							<tr>
								<td><input type="checkbox" /></td>
								<td>augue</td>
								<td>semper</td>
								<td>porta</td>
								<td>Mauris</td>
								<td><input class="btn btn-primary btn-xs" type="button"
									value="编辑"></td>
							</tr>
						</tbody>
					</table>
				</div>
				<nav>
					<ul class="pagination">
						<li class="disabled"><span> <span aria-hidden="true">&laquo;</span>
						</span></li>
						<li class="active"><span>1 <span class="sr-only">(current)</span></span>
						</li>
						<li class=""><span>2 <span class="sr-only">(current)</span></span>
						</li>
						<li class=""><span>3 <span class="sr-only">(current)</span></span>
						</li>
						<li class=""><span>4 <span class="sr-only">(current)</span></span>
						</li>
						<li class=""><span>5 <span class="sr-only">(current)</span></span>
						</li>
						<li class=""><span> <span aria-hidden="true">&raquo;</span>
						</span></li>
					</ul>
				</nav>
				<!-- Modal -->
				<div class="modal fade" id="addEditUser" tabindex="-1" role="dialog"
					aria-labelledby="myModalLabel">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4 class="modal-title" id="myModalLabel">用户编辑</h4>
							</div>
							<div class="modal-body">
								<form class="form-horizontal">
									<div class="form-group">
										<label for="userName" class="col-sm-2 control-label">用户名</label>
										<div class="col-sm-10">
											<input type="text" class="form-control" id="userName"
												placeholder="用户名">
										</div>
									</div>
									<div class="form-group">
										<label for="loginUser" class="col-sm-2 control-label">登录名</label>
										<div class="col-sm-10">
											<input type="text" class="form-control" id="loginUser"
												placeholder="登录名">
										</div>
									</div>
									<div class="form-group">
										<label for="password" class="col-sm-2 control-label">登录密码</label>
										<div class="col-sm-10">
											<input type="password" class="form-control"
												id="password" placeholder="登录密码">
										</div>
									</div>
									<div class="form-group">
										<label for="userType" class="col-sm-2 control-label">用户状态</label>
										<div class="col-sm-10 radio">
											<label><input name="userType"  type="radio" value="1">启用</label>
											<label><input name="userType"  type="radio" value="0">禁用</label>
										</div>
									</div>
								</form>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">关闭</button>
								<button type="button" class="btn btn-primary" onclick="alert(1);">保存</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Bootstrap core JavaScript
    ================================================== -->
		<!-- Placed at the end of the document so the pages load faster -->
		<script src="//cdn.bootcss.com/jquery/2.1.4/jquery.min.js"></script>
		<script src="bootstrap/js/bootstrap.min.js"></script>
		<!-- Just to make our placeholder images work. Don't actually copy the next line! -->
		<script src="//cdn.bootcss.com/holder/2.8.0/holder.min.js"></script>
		<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
		<script src="bootstrap/js/ie10-viewport-bug-workaround.js"></script>
		<script src="bs4j/bs4j-core.js"></script>
</body>
</html>
