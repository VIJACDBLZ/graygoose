<#import "macros/common.ftl" as common>

<div class="menu">
    <div>
        <#if user??>
            ${user.email} |
        </#if>
        <#list links as link>
            <a href="${link.address}">${link.text}</a>
            <#if link_has_next>|</#if>
        </#list>
    </div>
    <div class="langs">
        <a href="?lang=en">Eng</a> |
        <a href="?lang=ru">Rus</a>
    </div>
</div>