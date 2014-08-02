<#import "macros/common.ftl" as common/>

<@common.page>

    <div class="well well-sm" style="font-size: 11px;">
        <strong>{{Monitoring statistics for last}} ${currentTimeInterval.synonym!?html}.</strong>
        <ul class="menu">
            <#list timeIntervals as timeInterval>
                <#if timeInterval == currentTimeInterval>
                    <#assign clazz="active">
                <#else>
                    <#assign clazz="">
                </#if>
                <li>
                    <a href="<@link name="DashboardPage"/>?timeInterval=${timeInterval!?string}" class="${clazz!}">
                    ${timeInterval.synonym!?html}
                    </a>
                </li>
            </#list>
        </ul>
    </div>


<table class="dashboard table table-bordered table-striped">
    <thead class="thead-colored">
    <tr>
        <th>{{Site id}}</th>
        <th>{{Site name}}</th>
        <th>{{Rules for site}}</th>
        <th>{{Max rule checks}}</th>
        <th>
            <div style="width:100%;"><center>{{Rule checks}}</center></div>
            <div style="width:100%;">
                <table style="width:100%;">
                    <tbody>
                    <tr style="width:100%;">
                        <td style="width:25%;border-style:none;">
                            <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}">
                                <center><font color="white">{{Total}}</font><center>
                            </a>
                        </td>
                        <td style="width:25%;border-style:none;">
                            <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&status=SUCCEEDED">
                                <center><font color="white">{{Succeeded}}</font></center>
                            </a>
                        </td>
                        <td style="width:25%;border-style:none;">
                            <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&status=PENDING">
                                <center><font color="white">{{Pending}}</font></center>
                            </a>
                        </td>
                        <td style="width:25%;border-style:none;">
                            <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&status=FAILED">
                                <center><font color="white">{{Failed}}</font></center>
                            </a>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </th>
        <th>
            <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&withAlertsOnly=true">
                <font color="white">{{Alert triggers}}</font>
            </a>
        </th>
    </tr>
    </thead>
    <tbody>
        <#if sites?? && (sites?size > 0)>
            <#list sites as site>
            <tr>
                <td style="text-align:left;">
                    <a href="<@link name="SiteEditPage" id="${site.id}"/>">
                    ${site.id}
                    </a>
                </td>
                <td style="text-align:left;"><a href="${site.url?html}" target="_blank">${site.name?html}</a></td>
                <td>${site.ruleCount}</td>
                <td>${site.maxTotalRuleCheckCount}</td>
                <td>
                    <div style="width:100%;">
                        <table style="width:100%;">
                            <tbody>
                            <tr style="width:100%;">
                                <td style="width:25%;border-style:none;">
                                    <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&limit=${site.totalRuleCheckCount}&siteId=${site.id?number}">
                                    <center>${site.totalRuleCheckCount}</center>
                                    </a>
                                </td>
                                <td style="width:25%;border-style:none;">
                                    <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&limit=${site.succeededRuleCheckCount}&siteId=${site.id?number}&status=SUCCEEDED">
                                        <center>${site.succeededRuleCheckCount}</center>
                                    </a>
                                </td>
                                <td style="width:25%;border-style:none;">
                                    <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&limit=${site.pendingRuleCheckCount}&siteId=${site.id?number}&status=PENDING">
                                    <center>${site.pendingRuleCheckCount}</center>
                                    </a>
                                </td>
                                <td style="width:25%;border-style:none;">
                                    <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&limit=${site.failedRuleCheckCount}&siteId=${site.id?number}&status=FAILED">
                                        <center>${site.failedRuleCheckCount}</center>
                                    </a>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </td>
                <td>
                    <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&limit=${site.alertTriggerCount}&siteId=${site.id?number}&withAlertsOnly=true">
                    ${site.alertTriggerCount}
                    </a>
                </td>
            </tr>
            </#list>
        <#else>
        <tr>
            <td colspan="6">
                {{No sites currently monitored}}
            </td>
        </tr>
        </#if>
    </tbody>
</table>
</@common.page>
