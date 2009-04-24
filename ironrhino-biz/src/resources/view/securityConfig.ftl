<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title>Security Config</title>
<script>
Initialization.init = function(){
	ApplicationContextConsole.get('filterInvocationInterceptorObjectDefinitionSource.definitionAsText',function(text){$('#fsource').val(text)});
}
</script>
</head>

<body>
<div>FilterInvocationInterceptorObjectDefinitionSource<br />
<textarea id="fsource"
	name="filterInvocationInterceptorObjectDefinitionSource" rows="15"
	cols="100"></textarea> <br />
<input type="button" id="saveFilterInvocationButton" value="save"
	onclick="ApplicationContextConsole.set('filterInvocationInterceptorObjectDefinitionSource.definitionAsText',$('#fsource').val(),function(){alert('success')})" />
<input type="button" id="refreshFilterInvocationButton" value="refresh"
	onclick="ApplicationContextConsole.call('filterInvocationInterceptorObjectDefinitionSource.refresh()',null,function(){alert('success');})" /></div>
</body>
</html>
