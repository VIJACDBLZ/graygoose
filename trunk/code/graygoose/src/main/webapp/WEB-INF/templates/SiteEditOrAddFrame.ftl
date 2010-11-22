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
                    <#assign values=[60, 120, 300, 600]/>
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

<#if edit>
<div>
    <div class="rulesHeaderContainer" align="center">
        <h4>Alert Rules</h4>
    </div>
    <div style="text-align:right;padding-right:1em;">
        <a href="#" class="add-rule-link" rel="rule-add-edit-dialog">{{Add Rule}}</a>
    </div>
    <table class="grid">
        <thead>
        <tr>
            <th>{{Id}}</th>
            <th>{{Type}}</th>
            <th>{{Settings}}</th>
            <th>{{Creation time}}</th>
            <th>{{Attached alerts}}</th>
            <th>{{Actions}}</th>
        </tr>
        </thead>
        <tbody>
            <#if rules?? && (rules?size > 0)>
                <#list rules as rule>
                <tr id="rule${rule.id}">
                    <td style="text-align:right;">${rule.id}</td>
                    <td>${rule.ruleType?html}</td>
                    <td>
                        <#list rule.ruleType.propertyNames as propertyName>
                            <div style="text-align:left;"><label>${propertyName}
                                : </label>${(rule.data[propertyName])!?html}</div>
                        </#list>
                    </td>
                    <td>${rule.creationTime?datetime}</td>
                    <td>
                        <p>
                            <a href="#" class="attach-alert-link" rel="alert-attach-dialog" ruleId="${rule.id}">{{Attach
                                alert}}</a>
                        </p>
                        <#list alertsByRuleId[rule.id?string] as alert>
                            <p id="alert${alert.id}rule${rule.id}relation">
                            ${alert.name} (id=${alert.id})
                                on ${failCountByAlertIdAndRuleIdConcatenation[alert.id?string + "#" + rule.id?string]}
                                fail(s)
                                <a href="#" class="detach-alert-link" alertId="${alert.id}" ruleId="${rule.id}">
                                    <img src="${home}/images/detach_alert.gif">
                                </a>
                            </p>
                        </#list>
                    </td>
                    <td>
                        <a href="#" class="edit-rule-link" rel="rule-add-edit-dialog" ruleId="${rule.id}">{{Edit}}</a>
                        <a href="#" class="delete-rule-link" ruleId="${rule.id}">{{Delete}}</a>
                        <a href="#" class="test-rule-link" rel="rule-test-dialog" ruleId="${rule.id}">{{Test}}</a>
                    </td>
                </tr>
                </#list>
                <#else>
                <tr>
                    <td colspan="6">
                        {{No rules for this site}}
                    </td>
                </tr>
            </#if>
        </tbody>
    </table>
</div>

<div class="rule-add-edit-dialog" style="visibility:hidden;">
    <div class="vertical-form">
        <form action="" method="post">
            <input type="hidden" name="ruleId">
            <table>
                <tr>
                    <td class="field-name">{{Type}}:</td>
                    <td>
                        <select name="ruleType">
                            <option value="">{{&lt;select value&gt;}}</option>
                            <option value="RESPONSE_CODE_RULE_TYPE">{{Check response code}}</option>
                            <option value="SUBSTRING_RULE_TYPE">{{Check by substring}}</option>
                            <option value="REGEX_RULE_TYPE">{{Check by regex}}</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td class="error under" errorVar="error__ruleType"></td>
                </tr>

                <tr class="RESPONSE_CODE_RULE_TYPE typeRow">
                    <td class="field-name">{{Expected codes}}:</td>
                    <td><input name="expectedCodes" value="${expectedCodes!?html}"></td>
                </tr>
                <tr class="RESPONSE_CODE_RULE_TYPE typeRow">
                    <td>&nbsp;</td>
                    <td class="error under" errorVar="error__expectedCodes"></td>
                </tr>

                <tr class="SUBSTRING_RULE_TYPE typeRow">
                    <td class="field-name">{{Expected substring}}:</td>
                    <td><input name="expectedSubstring" value="${expectedSubstring!?html}"></td>
                </tr>
                <tr class="SUBSTRING_RULE_TYPE typeRow">
                    <td>&nbsp;</td>
                    <td class="error under" errorVar="error__expectedSubstring"></td>
                </tr>

                <tr class="SUBSTRING_RULE_TYPE typeRow">
                    <td class="field-name">{{Minimal count}}:</td>
                    <td><input name="expectedSubstringMinimalCount" value="${expectedSubstringMinimalCount!?html}"></td>
                </tr>
                <tr class="SUBSTRING_RULE_TYPE typeRow">
                    <td>&nbsp;</td>
                    <td class="error under" errorVar="error__expectedSubstringMinimalCount"></td>
                </tr>

                <tr class="SUBSTRING_RULE_TYPE typeRow">
                    <td class="field-name">{{Maximal count}}:</td>
                    <td><input name="expectedSubstringMaximalCount" value="${expectedSubstringMaximalCount!?html}"></td>
                </tr>
                <tr class="SUBSTRING_RULE_TYPE typeRow">
                    <td>&nbsp;</td>
                    <td class="error under" errorVar="error__expectedSubstringMaximalCount"></td>
                </tr>

                <tr class="REGEX_RULE_TYPE typeRow">
                    <td class="field-name">{{Regex}}:</td>
                    <td><input name="expectedRegex" value="${expectedRegex!?html}"></td>
                </tr>
                <tr class="REGEX_RULE_TYPE typeRow">
                    <td>&nbsp;</td>
                    <td class="error under" errorVar="error__expectedRegex"></td>
                </tr>

                <tr>
                    <td colspan="2" class="buttons">
                        <input type="button" value="{{Save}}" style="padding: 0.25em 1.5em;">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>

<div class="rule-test-dialog" style="visibility:hidden;">
    <div class="vertical-form" style="width:40em;'">
        <form action="" method="post">
            <table style="width:100%;">
                <tr>
                    <td style="text-align:left; width:10em;">{{Response code}}:</td>
                    <td><input name="responseCode" value="" style="width:100%;"></td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align:left;">{{Response text}}:</td>
                </tr>
                <tr height="100em">
                    <td colspan="2" height="100%">
                        <textarea name="responseText" wrap="off" style="overflow:scroll;width:100%;" rows="10">
                        </textarea>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align:center;">
                        <input name="test" type="button" value="{{Test}}" style="padding: 0.25em 1.5em;">
                        <input name="fetch" type="button" value="{{Fetch}}"
                               style="padding: 0.25em 1.5em;margin-left:2em;">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>

<div class="alert-attach-dialog" style="visibility:hidden;">
    <div class="vertical-form" style="width:30em;">
        <form action="" method="post">
            <table>
                <tr>
                    <td class="field-name" style="width:15em;">{{Alert}}:</td>
                    <td>
                        <select name="alertId">
                            <option value="">{{&lt;select value&gt;}}</option>
                            <#list alerts as alert>
                                <option value="${alert.id}">${alert.name} (id=${alert.id})</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td class="error under" errorVar="error__alertId"></td>
                </tr>
                <tr>
                    <td class="field-name">{{Max consecutive fail count}}:</td>
                    <td><input name="maxConsecutiveFailCount"></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td class="error under" errorVar="error__maxConsecutiveFailCount"></td>
                </tr>
                <tr>
                    <td colspan="2" class="buttons">
                        <input type="button" value="{{Attach}}" style="padding: 0.25em 1.5em;">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>

<script type="text/javascript">
    function updateFormByType() {
        var type = $("#sm_content select").val();
        $("#sm_content .typeRow").hide();

        if (type) {
            $("#sm_content ." + type).show();
        }
    }

    function updateFormByRule(ruleJson) {
        var ruleType = ruleJson["ruleType"];

        $("#sm_content select[name='ruleType'] > option").each(function() {
            if ($(this).val() == ruleType) {
                $(this).attr("selected", "selected");
            }
        });

        updateFormByType();

        $("#sm_content tr." + ruleType + " input").each(function() {
            $(this).val(ruleJson[$(this).attr("name")]);
        });
    }

    function saveRule() {
        var ruleType = $("#sm_content select[name='ruleType']").val();
        var siteId = $("input[name='id']").val();
        var postParameters = {ruleType: ruleType, siteId: siteId};

        var ruleId = $("#sm_content input[name='ruleId']").val();

        if (ruleId) {
            postParameters["ruleId"] = ruleId;
            postParameters["action"] = "edit";
        } else {
            postParameters["action"] = "add";
        }

        $("#sm_content tr." + ruleType + " input").each(function() {
            postParameters[$(this).attr("name")] = $(this).val();
        });

        $.post("<@link name="RulesDataPage"/>", postParameters, function(json) {
            if (json["success"] == "true") {
                document.location = "";
            } else {
                $("#sm_content td.error").each(function() {
                    var errorContainer = $(this);
                    errorContainer.text("");
                    errorContainer.text(json[$(this).attr("errorVar")]);
                });

                var error = json["error"];
                if (error) {
                    alert(error);
                }
            }
        }, "json");
    }

    $(function() {
        $("a.add-rule-link").each(function() {
            var a = $(this);

            a.smart_modal({show: function() {
                updateFormByType();

                $("#sm_content select[name='ruleType']").change(function() {
                    updateFormByType();
                });

                $("#sm_content input[type='button']").click(function() {
                    saveRule();
                });
            }});
        });

        $("a.edit-rule-link").each(function() {
            var a = $(this);
            var ruleId = a.attr("ruleId");

            a.smart_modal({show: function() {
                $("#sm_content input[name='ruleId']").val(ruleId);
                updateFormByType();

                $("#sm_content select[name='ruleType']").change(function() {
                    updateFormByType();
                });

                $("#sm_content input[type='button']").click(function() {
                    saveRule();
                });

                $.post("<@link name="RulesDataPage"/>", {action: "findById", ruleId: ruleId}, function(json) {
                    updateFormByRule(json);
                }, "json");
            }});
        });

        $("a.delete-rule-link").click(function() {
            var ruleId = $(this).attr("ruleId");
            if (confirm("{{Are you sure you want to delete rule #}}" + ruleId + "?")) {
                $.post("<@link name="RulesDataPage"/>", {action: "deleteRule", ruleId: ruleId}, function(json) {
                    if (json["success"] == "true") {
                        $("#rule" + ruleId).remove();
                    } else {
                        alert(json["error"]);
                    }
                }, "json");
            }
        });

        $("a.test-rule-link").each(function() {
            var a = $(this);
            var ruleId = a.attr("ruleId");
            var url = $("input.textbox[name='url']").val();

            a.smart_modal({show: function() {
                $("#sm_content input[name='test']").click(function() {
                    $.post("<@link name="RulesDataPage"/>", {
                        action: "checkRule", ruleId: ruleId, url: url,
                        responseCode: $("#sm_content input[name='responseCode']").val(),
                        responseText: $("#sm_content textarea[name='responseText']").val()
                    }, function(json) {
                        if (json["success"] == "true") {
                            alert("{{Test passed successfully.}}");
                        } else {
                            alert(json["error"]);
                        }
                    }, "json");
                });

                $("#sm_content input[name='fetch']").click(function() {
                    $.post("<@link name="RulesDataPage"/>", {action: "fetch", url: url}, function(json) {
                        if (json["success"] == "true") {
                            $("#sm_content input[name='responseCode']").val(json["responseCode"]);
                            $("#sm_content textarea[name='responseText']").val(json["responseText"]);
                        } else {
                            alert(json["error"]);
                        }
                    }, "json");
                });
            }});
        });

        $("a.attach-alert-link").each(function() {
            var a = $(this);
            var ruleId = a.attr("ruleId");

            a.smart_modal({show: function() {
                $("#sm_content input[type='button']").click(function() {
                    $.post("<@link name="RuleAlertRelationsDataPage"/>", {
                        action: "attachAlert", ruleId: ruleId,
                        alertId: $("#sm_content select[name='alertId']").val(),
                        maxConsecutiveFailCount: $("#sm_content input[name='maxConsecutiveFailCount']").val()
                    }, function(json) {
                        if (json["success"] == "true") {
                            document.location = "";
                        } else {
                            $("#sm_content td.error").each(function() {
                                var errorContainer = $(this);
                                errorContainer.text("");
                                errorContainer.text(json[$(this).attr("errorVar")]);
                            });

                            var error = json["error"];
                            if (error) {
                                alert(error);
                            }
                        }
                    }, "json");
                });
            }});
        });

        $("a.detach-alert-link").click(function() {
            var ruleId = $(this).attr("ruleId");
            var alertId = $(this).attr("alertId");

            if (confirm("{{Are you sure you want to detach alert #}}" + alertId + "?")) {
                $.post("<@link name="RuleAlertRelationsDataPage"/>", {
                    action: "detachAlert", ruleId: ruleId, alertId: alertId
                }, function(json) {
                    if (json["success"] == "true") {
                        $("#alert" + alertId + "rule" + ruleId + "relation").remove();
                    } else {
                        alert(json["error"]);
                    }
                }, "json");
            }
        });
    });
</script>
</#if>