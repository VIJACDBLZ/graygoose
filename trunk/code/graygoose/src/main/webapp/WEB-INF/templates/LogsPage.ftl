<#import "macros/common.ftl" as common/>

<@common.page>
<table class="grid">
    <thead>
    <tr>
        <th>{{Site id}}</th>
        <th>{{Site name}}</th>
        <th>{{Site URL}}</th>
        <th>{{Rule id}}</th>
        <th>{{Rule type}}</th>
        <th>{{Rule settings}}</th>
        <th>{{Status}}</th>
        <th>{{Description}}</th>
        <th>{{Check time}}</th>
        <th>{{Triggered alert}}</th>
    </tr>
    </thead>
    <tbody>
    <#if events?? && (events?size > 0)>
    <#list events as event>
    <tr>
        <td style="text-align:right;">${event.site.id}</td>
        <td style="text-align:left;">${event.site.name?html}</td>
        <td style="text-align:left;"><a href="${event.site.url?html}">${event.site.url?html}</a></td>
        <td style="text-align:right;">${event.rule.id}</td>
        <td>${event.rule.ruleType?html}</td>
        <td>
            <#list event.rule.ruleType.propertyNames as propertyName>
            <div style="text-align:left;"><label>${propertyName}: </label>${(event.rule.data[propertyName])!?html}</div>
            </#list>
        </td>
        <td>${event.status}</td>
        <td style="text-align:left;">${event.description}</td>
        <td>${event.checkTime}</td>
        <td><#if event.alert??>${event.alert.name}</#if></td>
    </tr>
    </#list>
    <#else>
    <tr>
        <td colspan="10">
            {{No events for now}}
        </td>
    </tr>
    </#if>
    </tbody>
</table>
        </@common.page>