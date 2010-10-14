<#import "macros/common.ftl" as common/>

<div class="vertical-form">
    <table>
        <tr>
            <td colspan="2" style="text-align:center;padding-bottom:1em;">
                <#if edit>
                    <h4>{{Edit Site Form}}</h4>
                <#else>
                    <h4>{{Add Site Form}}</h4>
                </#if>
            </td>
        </tr>
        <form action="" method="post">
            <input type="hidden" name="action" value="<#if edit>edit<#else>add</#if>">
            <#if id??>
                <input type="hidden" name="id" value="${id!}">
            </#if>
            
            <tr>
                <td class="field-name">
                    {{Name}}:
                </td>
                <td>
                    <input class="textbox" name="name" value="${name!?html}">
                </td>
            </tr>
            <@common.subscript error="${error__name!?html}"/>

            <tr>
                <td class="field-name">
                    {{Url}}:
                </td>
                <td>
                    <input class="textbox" name="url" value="${url!?html}">
                </td>
            </tr>
            <@common.subscript error="${error__url!?html}"/>

            <tr>
                <td class="field-name">
                    {{Rescan period}}:
                </td>
                <td>
                    <select name="rescanPeriod">
                        <#assign values=[30, 60, 120, 300]/>
                        <#list values as value>
                            <#if rescanPeriod?? && value?string==rescanPeriod>
                                <option selected="selected" value="${value}">${value}</option>
                            <#else>
                                <option value="${value}">${value}</option>
                            </#if>
                        </#list>
                    </select>
                    {{seconds}}
                </td>
            </tr>
            <@common.subscript error="${error__rescanPeriod!?html}"/>

            <tr>
                <td colspan="2" class="buttons">
                    <#if edit>
                        <input class="button" type="submit" value="{{Save}}">
                    <#else>
                        <input class="button" type="submit" value="{{Add}}">
                    </#if>
                </td>
            </tr>
        </form>
    </table>
</div>

<script type="text/javascript">
    $(function() {
        $("input[name='name']").focus();        
    });
</script>