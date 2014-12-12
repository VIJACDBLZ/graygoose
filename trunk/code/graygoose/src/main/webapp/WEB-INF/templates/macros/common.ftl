<#-- @ftlvariable name="root" type="java.lang.String" -->
<#macro basePage>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="${root}/favicon.ico">

    <title>${pageTitle?html}</title>

    <!-- Bootstrap core CSS -->
    <link href="${root}/css/bootstrap.css" rel="stylesheet">
    <link href="${root}/css/graygoose.css" rel="stylesheet">
    <link href="${root}/css/jumbotron-narrow.css" rel="stylesheet">
    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">

    <link rel="stylesheet" href="${root}/css/style.css" type="text/css"/>
    <link rel='stylesheet' href='${root}/css/jquery.smartModal.css'>

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="${root}/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="${root}/js/ie-emulation-modes-warning.js"></script>

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="${root}/js/ie10-viewport-bug-workaround.js"></script>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <#--<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.js"></script>-->
    <script type="text/javascript" src="${root}/js/jquery-1.5.2.min.js"></script>
    <script src="${root}/js/bootstrap.js"></script>
    <script type="text/javascript" src="${root}/js/jquery.smartModal.js"></script>
    <script type="text/javascript" src="${root}/js/jquery.autocomplete.pack.js"></script>
    <script type="text/javascript" src="${root}/js/jquery.noty.packaged.js"></script>
</head>
<body>
    <#nested/>
</body>
</html>
</#macro>

<#macro title text>
<h1>${text}</h1>
</#macro>

<#macro page>
    <@basePage>
    <#if noty??>
    <script>
        $(function() {
            noty({text: '${noty.text}', type: '${noty.type}', layout: 'bottomRight'});
        });
    </script>
    </#if>

    <div class="container">
        <div class="header">
            <ul class="nav nav-pills pull-right">
                <#if (pageTitle == "Dashboard")>
                    <li class="active"><a href="<@link name="DashboardPage"/>"><i class="fa fa-home"></i> Dashboard</a></li>
                <#else>
                    <li><a href="<@link name="DashboardPage"/>"><i class="fa fa-home"></i> Dashboard</a></li>
                </#if>

                <#if (pageTitle == "Sites")>
                    <li class="active"><a href="<@link name="SitesPage"/>"><i class="fa fa-file-o"></i> Sites</a></li>
                <#else>
                    <li><a href="<@link name="SitesPage"/>"><i class="fa fa-file-o"></i> Sites</a></li>
                </#if>

                <#if (pageTitle == "Alerts")>
                    <li class="active"><a href="<@link name="AlertsPage"/>"><i class="fa fa-bell"></i> Alerts</a></li>
                <#else>
                    <li><a href="<@link name="AlertsPage"/>"><i class="fa fa-bell"></i> Alerts</a></li>
                </#if>

                <#if (pageTitle == "Logs")>
                    <li class="active"><a href="<@link name="LogsPage"/>"><i class="fa fa-list"></i> Logs</a></li>
                <#else>
                    <li><a href="<@link name="LogsPage"/>"><i class="fa fa-list"></i> Logs</a></li>
                </#if>

                <#if (pageTitle == "Logout")>
                    <li class="active"><a href="<@link name="LogoutPage"/>"><i class="fa fa-sign-out"></i> Logout</a></li>
                <#else>
                    <li><a href="<@link name="LogoutPage"/>"><i class="fa fa-sign-out"></i> Logout</a></li>
                </#if>
            </ul>
            <h3 class="text-muted"><a href="<@link name="DashboardPage"/>"><img src="${root}/images/logo.png"/></a></h3>
        </div>

        <@title text="${pageTitle?html}"/>
        <#nested/>

        <div class="footer">
            <p>&copy; Graygoose by Codeforces</p>
        </div>

    </div> <!-- /container -->
    </@basePage>
</#macro>

<#macro secondaryMenu>
<div class="pull-right secondary-menu">
    <#nested/>
</div>
</#macro>

<#macro errorField for value>
    <#if value?? && value?length!=0>
    <div>
        <span class="errorField">${value}</span>
    </div>
    </#if>
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
