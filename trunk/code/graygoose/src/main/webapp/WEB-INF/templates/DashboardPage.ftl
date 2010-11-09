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
        <th>{{Site checks}}</th>
        <th>{{Rule checks: &lt;total&gt; (&lt;succeded&gt;, &lt;pending&gt;, &lt;failed&gt;)}}</th>
        <th>{{Alert triggers}}</th>
    </tr>
    </thead>
    <tbody>
    <#if sites?? && (sites?size > 0)>
    <#list sites as site>
    <tr>
        <td>${site.id}</td>
        <td>${site.name?html}</td>
        <td>${site.url?html}</td>
        <#assign siteInfo=siteInfoBySiteId[site.id?string]>
        <td>${siteInfo.ruleCount}</td>
        <td>${siteInfo.siteCheckCount}</td>
        <td>${siteInfo.totalRuleCheckCount}
            (${siteInfo.succeededRuleCheckCount}, ${siteInfo.pendingRuleCheckCount}, ${siteInfo.failedRuleCheckCount})
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