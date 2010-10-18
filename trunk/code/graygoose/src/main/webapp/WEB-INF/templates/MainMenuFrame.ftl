<#import "macros/common.ftl" as common>

<@common.colorBox clazz="blue-box">
<span>${currentPage}</span>

<ul class="menu">
    <#list links as item>
        <#if item.active>
            <#assign clazz="active">
            <#else>
                <#assign clazz="">
        </#if>

        <li><a href="<@link name=item.webPageClassName/>" class="${clazz!}">${item.text}</a></li>
    </#list>
</ul>
</@common.colorBox>
