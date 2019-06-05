<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String msg = (String)request.getAttribute("msg");
String loginName = (String)request.getAttribute("loginName");
String passwd = (String)request.getAttribute("passwd");
String userAuth = (String)request.getAttribute("userAuth");
msg = null == msg ? "" : msg;
loginName = null == loginName ? "" : loginName;
passwd = null == passwd ? "" : passwd;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>智慧计划审查系统</title>
<link href="<%=basePath %>css/css.css" rel="stylesheet" type="text/css" />
</head>

<body id="bodyBj">
<div class="login-box">
  <div class="login-con">
	  <form class="form-signin" action="login.sp" method="post">
	    <input type="hidden" name="act" value="login" >
	    <div class="login-logo"><img width="134px" height="114px" src="<%=basePath %>img/logo(1).png" ></div>
	    <div class="login-tit">智慧计划审查系统</div>
	    <div class="login-input"><input type="text" name="loginName" class="input" placeholder="账号" value="<%=loginName %>"/></div>
	    <div class="login-input"><input type="password" name="passwd" class="input" placeholder="密码" value="<%=passwd %>"/></div>	    
	    
	    <div><font color="red"><%=msg %></font></div>
	    <div style="height: 5px">
		</div>		
	    <div class="login-btn"><input type="submit" class="input" value="登录" /></div>
	  </form>
  </div>
</div>
<script>
	var heightVal = window.innerHeight;
	document.getElementById('bodyBj').style.height = heightVal + 'px';

</script>
</body>
</html>
