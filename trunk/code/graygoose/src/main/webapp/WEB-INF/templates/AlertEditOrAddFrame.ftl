<#import "macros/common.ftl" as common/>

<div class="vertical-form">
    <table>
        <tr>
            <td colspan="2" style="text-align:center;padding-bottom:1em;">
                <#if edit>
                <h4>{{Edit Alert Form}}</h4>
                <#else>
                <h4>{{Add Alert Form}}</h4>
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
                    {{Notification type}}:
                </td>
                <td>
                    <select name="type">
                        <#assign typeValues = ["{{&lt;select value&gt;}}", "{{E-mail}}", "{{Google calendar event}}"]/>
                        <#list typeValues as typeValue>
                        <#if type?? && typeValue == type>
                        <option selected value="${typeValue}">${typeValue}</option>
                        <#else>
                        <option value="${typeValue}">${typeValue}</option>
                        </#if>
                        </#list>
                    </select>
                </td>
            </tr>
            <@common.subscript error="${error__type!?html}"/>

            <tr>
                <td class="field-name">
                    {{E-mail}}:
                </td>
                <td>
                    <input class="textbox" name="email" value="${email!?html}">
                </td>
            </tr>
            <@common.subscript error="${error__email!?html}"/>

            <tr>
                <td class="field-name">
                    {{Password}}:
                </td>
                <td>
                    <input class="textbox" type="password" name="password" value="${password!?html}">
                </td>
            </tr>
            <@common.subscript error="${error__password!?html}"/>

            <tr>
                <td class="field-name">
                    {{Password confirmation}}:
                </td>
                <td>
                    <input class="textbox" type="password" name="passwordConfirmation"
                           value="${passwordConfirmation!?html}">
                </td>
            </tr>
            <@common.subscript error="${error__passwordConfirmation!?html}"/>

            <tr>
                <td class="field-name">
                    {{Max alert count per hour}}:
                </td>
                <td>
                    <select name="maxAlertCountPerHour">
                        <#assign alertCountValues = [0, 1, 2, 3, 5, 10]/>
                        <#list alertCountValues as alertCountValue>
                        <#if maxAlertCountPerHour?? && alertCountValue?string==maxAlertCountPerHour>
                        <option selected="selected" value="${alertCountValue}">${alertCountValue}</option>
                        <#else>
                        <option value="${alertCountValue}">${alertCountValue}</option>
                        </#if>
                        </#list>
                    </select>
                </td>
            </tr>
            <@common.subscript error="${error__maxAlertCountPerHour!?html}"/>

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