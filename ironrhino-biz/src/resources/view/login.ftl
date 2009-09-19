<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#escape x as x?html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>Login</title>
<meta name="decorator" content="simple" />
<style>
form fieldset div.fieldset {
	margin-top: 20px;
	margin-bottom: 20px;
	margin-left: auto;
	margin-right: auto;
	width: 300px;
	text-align: center;
}

form fieldset {
	background-color: #CAE1FF;
}

form fieldset div.label {
	float: left;
	width: 100px;
}
</style>
<@authorize ifNotGranted="ROLE_BUILTIN_USER">
	<script>
	Initialization.initForm = function() {
		$('#login_form')[0].onsuccess = redirect;
		$('#targetUrl').val(self.location.href);
	}

	function redirect() {
		<#if targetUrl??>
			top.location.href = '<#if !targetUrl?string?contains('://')>${base}</#if>${targetUrl}';
		<#else>
			top.location.href = '${base}';
		</#if>
	}
</script>
</@authorize>
</head>

<body>
<div style="margin-top: 50px;"><img
	src="${base}/images/login.jpg"
	style="display: block; margin-left: auto; margin-right: auto" />
<h1 id="title"
	style="width: 550px; margin-left: auto; margin-right: auto; text-align: center; color: #4A708B;">IronRhino</h1>
</div>
<@authorize ifAnyGranted="ROLE_BUILTIN_USER">
	<p style="text-align: center;">您已经以${authentication('name')}身份登录,换个用户名请先<a href="${base}/logout">注销</a>,<a
		href="${base}">进入</a></p>
</@authorize>
<@authorize ifNotGranted="ROLE_BUILTIN_USER">
	<div
		style="margin-left: auto; margin-right: auto; width: 550px; font-size: 140%;">
	<@s.form id="login_form" action="check" method="post" cssClass="ajax">
		<@s.hidden id="targetUrl" name="targetUrl" />
		<@s.textfield label="%{getText('username')}" name="username"
			cssClass="required" labelposition="left" />
		<@s.password label="%{getText('password')}" name="password"
			cssClass="required" labelposition="left" />
		<div class="fieldset"><@s.submit value="登录" /></div>
	</@s.form></div>
</@authorize>
<p
	style="width: 550px; margin-top: 10px; margin-left: auto; margin-right: auto; text-align: center; color: #4A708B;">Copyright
&copy; IronRhino</p>


</body>
</html></#escape>
