<#import "macros/common.ftl" as common/>

<@common.page>
<table class="grid">
    <caption>
        <strong>Monitoring statistics for last ${currentTimeInterval.synonym!?html}.</strong>
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
        <th>{{Site URL}}</th>
        <th>{{Rules for site}}</th>
        <th>{{Max rule checks}}</th>
        <th>
            <div style="width:100%;">{{Rule checks}}</div>
            <div style="width:100%;">
                <table style="width:100%;">
                    <tbody>
                    <tr style="width:100%;">
                        <td style="width:25%;border-style:none;">{{Total}}</td>
                        <td style="width:25%;border-style:none;">{{Succeeded}}</td>
                        <td style="width:25%;border-style:none;">{{Pending}}</td>
                        <td style="width:25%;border-style:none;">{{Failed}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </th>
        <th>{{Alert triggers}}</th>
    </tr>
    </thead>
    <tbody>
    <#if sites?? && (sites?size > 0)>
    <#list sites as site>
    <#assign siteInfo=siteInfoBySiteId[site.id?string]>
    <tr>
        <td>${site.id}</td>
        <td style="text-align:left;">${site.name?html}</td>
        <td style="text-align:left;">${site.url?html}</td>
        <td>${siteInfo.ruleCount}</td>
        <td>${siteInfo.maxTotalRuleCheckCount}</td>
        <td>
            <div style="width:100%;">
                <table style="width:100%;">
                    <tbody>
                    <tr style="width:100%;">
                        <td style="width:25%;border-style:none;">${siteInfo.totalRuleCheckCount}</td>
                        <td style="width:25%;border-style:none;">${siteInfo.succeededRuleCheckCount}</td>
                        <td style="width:25%;border-style:none;">${siteInfo.pendingRuleCheckCount}</td>
                        <td style="width:25%;border-style:none;">${siteInfo.failedRuleCheckCount}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </td>
        <td>${siteInfo.alertTriggerCount}</td>
    </tr>
    </#list>
    <#else>
    <tr>
        <td colspan="7">
            {{No sites currently monitored}}
        </td>
    </tr>
    </#if>
    </tbody>
</table>
        </@common.page>