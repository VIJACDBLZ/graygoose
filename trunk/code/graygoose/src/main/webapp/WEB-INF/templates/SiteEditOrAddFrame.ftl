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

<div>
    <div class="rulesHeaderContainer" align="center">
        <h4>Alert Rules</h4>
    </div>
    <table class="grid">
        <thead>
        <tr>
            <th>{{Id}}</th>
            <th>{{Type}}</th>
            <th>{{Settings}}</th>
            <th>{{Creation time}}</th>
            <th>{{Actions}}</th>
        </tr>
        </thead>
        <tbody>
        <#if rules??>
            <#list rules as rule>
            <tr id="rule${rule.id}">
                <td>${rule.id}</td>
                <td>${rule.ruleType?html}</td>
                <td>
                    <#list rule.data?keys as key>
                        <div><label>${key}: </label>${rule.data[key]}</div>
                    </#list>
                </td>
                <td>${rule.creationTime?datetime}</td>
                <td>
                    <a href="#" class="edit-rule-link" rel="rule-add-edit-dialog" ruleId="${rule.id}">{{Edit}}</a>
                    <a href="#" class="delete-rule-link" ruleId="${rule.id}">{{Delete}}</a>
                </td>
            </tr>
            </#list>
            <#else>
            <tr>
                <td colspan="5">
                    {{No rules for this site}}
                </td>
            </tr>
        </#if>
        </tbody>
    </table>
</div>

<script type="text/javascript">
    $(function() {
        $("input[name='name']").focus();
    });
</script>

<script type="text/javascript">
    $(function() {
        $("a.edit-rule-link").smart_modal({show: function() {
            var ruleId = $(this).attr("ruleId");
            $("#sm_content input[name='id']").attr("value", ruleId);    //???
        }});
    });
</script>

<script type="text/javascript">
    $(function() {
        $("a.delete-rule-link").click(function() {
            var ruleId = $(this).attr("ruleId");
            if (confirm("{{Are you sure you want to delete rule No}}" + " " + ruleId + "?")) {
                $.post("<@link name="RulesDataPage"/>", {action: "deleteRule", ruleId: ruleId}, function(json) {
                    if (json["success"] == "true") {
                        $("#rule" + ruleId).remove();
                    } else {
                        alert(json["error"]);
                    }
                }, "json");
            }
        });
    });
</script>

<div class="rule-add-edit-dialog" style="visibility:hidden;">
    <form action="" method="post">
        <label>
            {{Id}}:
            <input name="id" type="text"/>
        </label>
    </form>
</div>