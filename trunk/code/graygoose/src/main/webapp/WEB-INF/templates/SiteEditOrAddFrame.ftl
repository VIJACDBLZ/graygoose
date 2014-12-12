<#-- @ftlvariable name="id" type="java.lang.Long" -->
<#-- @ftlvariable name="edit" type="java.lang.Boolean" -->
<#import "macros/common.ftl" as common>

<div class="row">
    <div class="col-md-4 col-md-offset-4">
        <form role="form" action="" method="post">
            <input type="hidden" name="action" value="<#if edit>edit<#else>add</#if>">
            <#if id??>
                <input type="hidden" name="id" value="${id!}">
            </#if>

            <div class="form-group">
                <label for="name">{{Name}}:</label>
                <input type="text" class="form-control name" id="name" name="name" placeholder="{{Enter name}}" value="${name!?html}">
            </div>
            <@common.errorField for="name" value="${error__name!}"/>

            <div class="form-group">
                <label for="url">{{Url}}:</label>
                <input type="text" class="form-control url" id="url" name="url" placeholder="{{Enter url}}" value="${url!?html}">
            </div>
            <@common.errorField for="url" value="${error__url!}"/>

            <div class="form-group">
                <label for="pauseFromMinute">{{Pause from (hh:mm)}}:</label>
                <input type="text" class="form-control" id="pauseFromMinute" name="pauseFromMinute" placeholder="{{hh:mm}}" value="${pauseFromMinute!?html}">
            </div>
            <@common.errorField for="pauseFromMinute" value="${error__pauseFromMinute!}"/>

            <div class="form-group">
                <label for="pauseToMinute">{{Pause to (hh:mm)}}:</label>
                <input type="text" class="form-control" id="pauseToMinute" name="pauseToMinute" placeholder="{{hh:mm}}" value="${pauseToMinute!?html}">
            </div>
            <@common.errorField for="pauseToMinute" value="${error__pauseToMinute!}"/>

            <div class="form-group">
                <label for="rescanPeriod">{{Rescan period, s}}:</label>

                <select class="form-control" id="rescanPeriod" name="rescanPeriod">
                <#assign values=[60, 120, 300, 600]/>
                <#list values as value>
                    <#if rescanPeriod?? && value?string==rescanPeriod>
                        <option selected="selected" value="${value}">${value}</option>
                    <#else>
                        <option value="${value}">${value}</option>
                    </#if>
                </#list>
                </select>
            </div>
            <@common.errorField for="rescanPeriod" value="${error__rescanPeriod!}"/>

            <#if edit>
                <button type="submit" class="btn btn-default">{{Edit}}</button>
            <#else>
                <button type="submit" class="btn btn-default">{{Add}}</button>
            </#if>

            <br><br>
        </form>
    </div>
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
    <div class="pull-right">
        <a href="#" class="rule-add-edit-dialog"><i class="fa fa-plus-circle"></i> {{Add Rule}}</a>
    </div>
    <table class="rules table table-bordered table-striped">
        <thead class="thead-colored">
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
                            <div style="text-align:left;"><label>${propertyName}:</label> ${(rule.data[propertyName])!?html}</div>
                        </#list>
                    </td>
                    <td>${rule.creationTime?datetime}</td>
                    <td>
                        <p>
                            <a href="#" class="alert-attach-dialog attach-alert-link" rel="alert-attach-dialog" ruleId="${rule.id}">{{Attach
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
                        <#--<a href="#" class="edit-rule-link" rel="rule-add-edit-dialog" ruleId="${rule.id}">{{Edit}}</a>-->
                        <a href="#" class="rule-add-edit-dialog edit-rule-link" ruleId="${rule.id}">{{Edit}}</a>
                        <a href="#" class="delete-rule-link" ruleId="${rule.id}">{{Delete}}</a>
                        <a href="#" class="rule-test-dialog test-rule-link" rel="rule-test-dialog" ruleId="${rule.id}">{{Test}}</a>
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

<div class="smartmodal" id="rule-add-edit-dialog">
    <div class="col-md-5">
        <form role="form" action="" method="post">
            <input type="hidden" name="ruleId">
            <h1>Add Rule</h1>

            <table width="400" align="left">
                <tr>
                    <div class="form-group">
                        <label for="ruleType">{{Type}}:</label>

                        <select name="ruleType" id="ruleType">
                            <option value="">&lt;{{select value}}&gt;</option>
                            <option value="RESPONSE_CODE_RULE_TYPE">{{Check response code}}</option>
                            <option value="SUBSTRING_RULE_TYPE">{{Check by substring}}</option>
                            <option value="REGEX_MATCH_RULE_TYPE">{{Check by regex (matches)}}</option>
                            <option value="REGEX_NOT_MATCH_RULE_TYPE">{{Check by regex (not matches)}}</option>
                            <option value="REGEX_FIND_RULE_TYPE">{{Check by regex (occurrence count)}}</option>
                        </select>
                    </div>

                    <@common.errorField for="ruleType" value="${error__ruleType!}"/>
                </tr>

                <tr class="RESPONSE_CODE_RULE_TYPE ruleOption" style="display:none;">
                    <td><div class="form-group">
                        <label for="codes">{{Expected codes}}:</label>
                        <input type="text" class="form-control" id="codes" name="codes" placeholder="{{Expected codes}}" value="${codes!?html}">
                    </div></td>
                    <td class="error under" errorVar="error__codes"></td>
                </tr>

                <tr class="SUBSTRING_RULE_TYPE ruleOption" style="display:none;">
                    <td><div class="form-group">
                        <label for="codes">{{Expected substring}}:</label>
                        <input type="text" class="form-control" id="substring" name="substring" placeholder="{{Expected substring}}" value="${substring!?html}">
                    </div></td>
                    <td class="error under" errorVar="error__substring"></td>
                </tr>

                <tr class="SUBSTRING_RULE_TYPE ruleOption" style="display:none;">
                    <td><div class="form-group">
                        <label for="substringMinCount">{{Minimal count}}:</label>
                        <input type="text" class="form-control" id="substringMinCount" name="substringMinCount" placeholder="{{Minimal count}}" value="${substringMinCount!?html}">
                    </div></td>
                    <td class="error under" errorVar="error__substringMinCount"></td>
                </tr>

                <tr class="SUBSTRING_RULE_TYPE ruleOption" style="display:none;">
                    <td><div class="form-group">
                        <label for="substringMaxCount">{{Maximal count}}:</label>
                        <input type="text" class="form-control" id="substringMaxCount" name="substringMaxCount" placeholder="{{Maximal count}}" value="${substringMaxCount!?html}">
                    </div></td>
                    <td class="error under" errorVar="error__substringMaxCount"></td>
                </tr>

                <tr class="REGEX_MATCH_RULE_TYPE ruleOption" style="display:none;">
                    <td><div class="form-group">
                        <label for="matchPattern">{{Regex}}:</label>
                        <input type="text" class="form-control" id="matchPattern" name="matchPattern" placeholder="{{Regex}}" value="${matchPattern!?html}">
                    </div></td>
                    <td class="error under" errorVar="error__matchPattern"></td>
                </tr>

                <tr class="REGEX_NOT_MATCH_RULE_TYPE ruleOption" style="display:none;">
                    <td><div class="form-group">
                        <label for="notMatchPattern">{{Regex}}:</label>
                        <input type="text" class="form-control" id="notMatchPattern" name="notMatchPattern" placeholder="{{Regex}}" value="${notMatchPattern!?html}">
                    </div></td>
                    <td class="error under" errorVar="error__notMatchPattern"></td>
                </tr>

                <tr class="REGEX_FIND_RULE_TYPE ruleOption" style="display:none;">
                    <td><div class="form-group">
                        <label for="findPattern">{{Regex}}:</label>
                        <input type="text" class="form-control" id="findPattern" name="findPattern" placeholder="{{Regex}}" value="${findPattern!?html}">
                    </div></td>
                    <td class="error under" errorVar="error__findPattern"></td>
                </tr>

                <tr class="REGEX_FIND_RULE_TYPE ruleOption" style="display:none;">
                    <td><div class="form-group">
                        <label for="patternMinCount">{{Minimal count}}:</label>
                        <input type="text" class="form-control" id="patternMinCount" name="patternMinCount" placeholder="{{Minimal count}}" value="${patternMinCount!?html}">
                    </div></td>
                    <td class="error under" errorVar="error__patternMinCount"></td>
                </tr>

                <tr class="REGEX_FIND_RULE_TYPE ruleOption" style="display:none;">
                    <td><div class="form-group">
                        <label for="patternMaxCount">{{Maximal count}}:</label>
                        <input type="text" class="form-control" id="patternMaxCount" name="patternMaxCount" placeholder="{{Maximal count}}" value="${patternMaxCount!?html}">
                    </div></td>
                    <td class="error under" errorVar="error__patternMaxCount"></td>
                </tr>

                <tr class="buttonForAll ruleOption" style="display:none;">
                    <td colspan="2" class="buttons">
                        <input type="button" class="btn btn-default" value="{{Save}}" style="padding: 0.25em 1.5em;">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>

<div class="smartmodal" id="rule-test-dialog">
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

<div class="smartmodal" id="alert-attach-dialog">
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
    $(function() {
        $.smartModal();
    });

    $('#ruleType').change(function() {
        $('.ruleOption').hide();
        $('.' + $(this).val()).show();
        $('.buttonForAll').show();
    });

    $('.buttonForAll').click(function() {
        var ruleType = $('#ruleType').val();
        var siteId = $("input[name='id']").val();
        var postParameters = {ruleType: ruleType, siteId: siteId};

        var ruleId = $("input[name='ruleId']").val();

        if (ruleId) {
            postParameters["ruleId"] = ruleId;
            postParameters["action"] = "edit";
        } else {
            postParameters["action"] = "add";
        }

        $("tr." + ruleType + " input").each(function() {
            postParameters[$(this).attr("name")] = $(this).val();
        });

        $.post("<@link name="RulesDataPage"/>", postParameters, function(json) {
            if (json["success"] == "true") {
                document.location = "";
            } else {
                $("td.error").each(function() {
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

    $(".edit-rule-link").click(function() {
        var a = $(this);
        var ruleId = a.attr("ruleId");

        $.post("<@link name="RulesDataPage"/>", {action: "findById", ruleId: ruleId}, function(json) {
            var ruleType = json["ruleType"];

            $("#ruleType > option").each(function() {
                if ($(this).val() == ruleType) {
                    $(this).attr("selected", "selected");
                }
            });

            $("input[name='ruleId']").val(ruleId);

            $(".ruleOption").hide();
            $("." + ruleType).show();
            $('.buttonForAll').show();

            $("tr." + ruleType + " input").each(function() {
                $(this).val(json[$(this).attr("name")]);
            });
        }, "json");
    });

    $(".delete-rule-link").click(function() {
        var ruleId = $(this).attr("ruleId");
        if (confirm("{{Are you sure you want to delete rule #}}" + ruleId + "?")) {
            $.post("<@link name="RulesDataPage"/>", {action: "deleteRule", ruleId: ruleId}, function(json) {
                if (json["success"] == "true") {
                    $("#rule" + ruleId).remove();

                    var rows = $("table.rules tr");
                    if (rows.length == 1) {
                        $("table.rules tbody").append("<tr><td colspan=\"7\">{{No rules}}</td></tr>");
                    }
                } else {
                    alert(json["error"]);
                }
            }, "json");
        }
    });

    $(".test-rule-link").click(function() {
        var a = $(this);
        var ruleId = a.attr("ruleId");
        var url = $("input.url[name='url']").val();
        var siteName = $("input.name[name='name']").val();

        $("input[name='test']").click(function() {
            $.post("<@link name="RulesDataPage"/>", {
                action: "checkRule", ruleId: ruleId, url: url, siteName: siteName,
                responseCode: $("input[name='responseCode']").val(),
                responseText: $("textarea[name='responseText']").val()
            }, function(json) {
                if (json["success"] == "true") {
                    alert("{{Test passed successfully.}}");
                } else {
                    alert(json["error"]);
                }
            }, "json");
        });

        $("input[name='fetch']").click(function() {
            $.post("<@link name="RulesDataPage"/>", {action: "fetch", url: url}, function(json) {
                if (json["success"] == "true") {
                    $("input[name='responseCode']").val(json["responseCode"]);
                    $("textarea[name='responseText']").val(json["responseText"]);
                } else {
                    alert(json["error"]);
                }
            }, "json");
        });
    });

    $(".attach-alert-link").click(function() {
        var a = $(this);
        var ruleId = a.attr("ruleId");

        $("input[type='button']").click(function() {
            $.post("<@link name="RuleAlertRelationsDataPage"/>", {
                action: "attachAlert", ruleId: ruleId,
                alertId: $("select[name='alertId']").val(),
                maxConsecutiveFailCount: $("input[name='maxConsecutiveFailCount']").val()
            }, function(json) {
                if (json["success"] == "true") {
                    document.location = "";
                } else {
                    $("td.error").each(function() {
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
    });

    $(".detach-alert-link").click(function() {
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
</script>

</#if>
