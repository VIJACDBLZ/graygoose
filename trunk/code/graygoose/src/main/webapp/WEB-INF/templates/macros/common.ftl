<#setting url_escaping_charset='UTF-8'>
<#macro page>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN">
<html>
<head>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="-1">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <title>Graygoose::${pageTitle}</title>
    <link href=${home}/favicon.ico" rel="icon" type="image/x-icon" />
    <link rel="stylesheet" href="${home}/css/clear.css" type="text/css"/>
    <link rel="stylesheet" href="${home}/css/style.css" type="text/css"/>
    <#list css as file>
        <link rel="stylesheet" href="${home}/${file}"/>
    </#list>
    <script type="text/javascript" src="${home}/js/jquery-1.3.2.min.js"></script>
    <#list js as file>
        <script type="text/javascript" src="${home}/${file}"></script>
    </#list>
</head>
<body>
<@frame name="messageBoxFrame"/>
<div id="header">
    <@frame name="topMenuFrame"/>
    <a href="${home}" title="{{Webmail home page}}">
        <img src="${home}/images/logo.png" width="95" height="56"/>
    </a>
</div>
<div id="content">
    <@frame name="mainMenuFrame"/>
    <div style="padding: 1em 0;">
    <#nested>
    </div>
</div>
<div id="footer">
    <@caption params=["http://code.google.com/p/graygoose"]>
        Easy configurable system to rescan your web applications periodically and send notifications in case of failure.
        <br/>
        <a href="{0}">Graygoose</a>
    </@caption>
</div>
</body>
</html>
</#macro>

<#macro errorLabel text = "">
<#if text?? && (text?length > 0)>
    <div class="error">${text!?html}</div>
</#if>
</#macro>

<#macro subscript error = "" hint = "" clazz = "under">
<#if (error?? && (error?length > 0)) || (hint?? && (hint?length > 0))>
<tr>
    <td>&nbsp;</td>
    <td>
        <div class="${clazz}">
            <#if error?? && (error?length > 0)>
                <@errorLabel text=error/>
            <#else>
                ${hint}
            </#if>
        </div>
    </td>
</tr>
</#if>
</#macro>

<#macro colorBox clazz style="">
<div class="color-box ${clazz}" style="${style!}">
    <div class="t t1">&nbsp;</div>
    <div class="t t2">&nbsp;</div>
    <div class="t t3">&nbsp;</div>
    <div class="t t4">&nbsp;</div>
    <div class="text">
        <#nested>
    </div>
</div>
</#macro>
