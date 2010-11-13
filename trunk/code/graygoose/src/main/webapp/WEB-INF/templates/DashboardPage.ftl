<#import "macros/common.ftl" as common/>

<@common.page>
<table class="grid">
    <caption>
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
    </caption>
    <thead>
    <tr>
        <th>{{Site id}}</th>
        <th>{{Site name}}</th>
        <th>{{Rules for site}}</th>
        <th>{{Max rule checks}}</th>
        <th>
            <div style="width:100%;">{{Rule checks}}</div>
            <div style="width:100%;">
                <table style="width:100%;">
                    <tbody>
                    <tr style="width:100%;">
                        <td style="width:25%;border-style:none;">
                            <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}">
                                {{Total}}
                            </a>
                        </td>
                        <td style="width:25%;border-style:none;">
                            <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&status=SUCCEEDED">
                                {{Succeeded}}
                            </a>
                        </td>
                        <td style="width:25%;border-style:none;">
                            <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&status=PENDING">
                                {{Pending}}
                            </a>
                        </td>
                        <td style="width:25%;border-style:none;">
                            <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&status=FAILED">
                                {{Failed}}
                            </a>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </th>
        <th>
            <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&withAlertsOnly=true">
                {{Alert triggers}}
            </a>
        </th>
    </tr>
    </thead>
    <tbody>
    <#if sites?? && (sites?size > 0)>
    <#list sites as site>
    <tr>
        <td style="text-align:right;">${site.id}</td>
        <td style="text-align:left;"><a href="${site.url?html}">${site.name?html}</a></td>
        <td>${site.ruleCount}</td>
        <td>${site.maxTotalRuleCheckCount}</td>
        <td>
            <div style="width:100%;">
                <table style="width:100%;">
                    <tbody>
                    <tr style="width:100%;">
                        <td style="width:25%;border-style:none;">
                            <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&limit=${site.totalRuleCheckCount}&siteId=${site.id?number}">
                                ${site.totalRuleCheckCount}
                            </a>
                        </td>
                        <td style="width:25%;border-style:none;">
                            <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&limit=${site.succeededRuleCheckCount}&siteId=${site.id?number}&status=SUCCEEDED">
                                ${site.succeededRuleCheckCount}
                            </a>
                        </td>
                        <td style="width:25%;border-style:none;">
                            <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&limit=${site.pendingRuleCheckCount}&siteId=${site.id?number}&status=PENDING">
                                ${site.pendingRuleCheckCount}
                            </a>
                        </td>
                        <td style="width:25%;border-style:none;">
                            <a href="<@link name="LogsPage"/>?timeInterval=${currentTimeInterval!?string}&limit=${site.failedRuleCheckCount}&siteId=${site.id?number}&status=FAILED">
                                ${site.failedRuleCheckCount}
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