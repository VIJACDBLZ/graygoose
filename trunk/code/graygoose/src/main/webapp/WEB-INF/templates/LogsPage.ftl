<#import "macros/common.ftl" as common/>

<@common.page>
<table class="grid">
    <caption>
        <strong>
            {{Rule check events for last}} ${currentTimeInterval.synonym!?html}.
            <#if events??>{{Records}}: ${events?size?int}.</#if>
            {{Limit}}: ${limit?int}.
            <#if siteId??>{{Site id}}: ${siteId?number}.</#if>
            <#if status??>{{Status}}: ${status?html}.</#if>
            <#if withAlertsOnly??>{{Triggers alert}}: ${withAlertsOnly?string}.</#if>
        </strong>
        <ul class="menu">
            <#list timeIntervals as timeInterval>
            <#if timeInterval == currentTimeInterval>
            <#assign clazz="active">
            <#else>
            <#assign clazz="">
            </#if>
            <li>
                <a href="<@link name="LogsPage"/>?timeInterval=${timeInterval!?string}&limit=${limit?int}<#if siteId??>&siteId=${siteId?number}</#if><#if status??>&status=${status?html}</#if><#if withAlertsOnly??>&withAlertsOnly=${withAlertsOnly?string}</#if>"
                   class="${clazz!}">
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
        <th>{{Rule id}}</th>
        <th>{{Rule type}}</th>
        <th>{{Rule settings}}</th>
        <th>{{Status}}</th>
        <th>{{Description}}</th>
        <th>{{Check time}}</th>
        <th>{{Triggered alerts}}</th>
    </tr>
    </thead>
    <tbody>
    <#if events?? && (events?size > 0)>
    <#list events as event>
    <tr>
        <td style="text-align:right;">${event.site.id}</td>
        <td style="text-align:left;"><a href="${event.site.url!?html}">${event.site.name!?html}</a></td>
        <td style="text-align:right;">${event.rule.id}</td>
        <td>${event.rule.ruleType!?html}</td>
        <td>
            <#list event.rule.ruleType.propertyNames as propertyName>
            <div style="text-align:left;"><label>${propertyName!?html}: </label>${(event.rule.data[propertyName])!?html}
            </div>
            </#list>
        </td>
        <td>${event.status!?html}</td>
        <td style="text-align:left;">${event.description!?html}</td>
        <td><#if event.checkTime??>${event.checkTime!?datetime}</#if></td>
        <td>
            <#if event.alerts?? && (event.alerts?size > 0)>
            <#list event.alerts as alert>
            <div style="text-align:left;">${alert.name!?html}</div>
            </#list>
            </#if>
        </td>
    </tr>
    </#list>
    <#else>
    <tr>
        <td colspan="9">
            {{No events}}
        </td>
    </tr>
    </#if>
    </tbody>
</table>
        </@common.page>