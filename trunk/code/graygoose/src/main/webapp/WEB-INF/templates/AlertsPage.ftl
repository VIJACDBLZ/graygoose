<#-- @ftlvariable name="alerts" type="java.util.Collection<com.codeforces.graygoose.model.Alert>" -->
<#import "macros/common.ftl" as common>

<@common.page>
<@common.secondaryMenu>
<a href="<@link name="AlertAddPage"/>"><i class="fa fa-plus-circle"></i></a>
<a href="<@link name="AlertAddPage"/>">{{Add Alert}}</a>
</@common.secondaryMenu>

<table class="alerts table table-bordered table-striped">
    <thead class="thead-colored">
    <tr>
        <th>{{Id}}</th>
        <th>{{Name}}</th>
        <th>{{Type}}</th>
        <th>{{E-mail}}&nbsp;/&nbsp;{{Phone}}</th>
        <th>{{Password}}</th>
        <th>{{Max alerts per hour}}</th>
        <th>{{Actions}}</th>
    </tr>
    </thead>
    <tbody>
        <#if alerts?? && (alerts?size > 0)>
            <#list alerts as alert>
            <tr alertId="${alert.id}">
                <td style="text-align:right;">${alert.id}</td>
                <td style="text-align:left;">${alert.name?html}</td>
                <td style="text-align:left;">${alert.type?html}</td>
                <td style="text-align:left;"><#if alert.email?? && (alert.email?length > 0)>${alert.email?html}<#elseif alert.smsServicePhone?? && (alert.smsServicePhone?length > 0)>${alert.smsServicePhone?html}<#else>&nbsp;</#if></td>
                <td><#if (alert.password)?? && (alert.password?length > 0)>{{*present*}}<#else>&nbsp;</#if></td>
                <td>${alert.maxAlertCountPerHour?int}</td>
                <td>
                    <a href="<@link name="AlertEditPage" id="${alert.id}"/>">{{Edit}}</a>
                    <a href="#" class="delete-alert-link" alertName="${alert.name}" alertId="${alert.id}">{{Delete}}</a>
                    <a href="#" class="test-alert-link" alertId="${alert.id}">{{Test}}</a>
                </td>
            </tr>
            </#list>
        <#else>
        <tr>
            <td colspan="7">{{No alerts}}</td>
        </tr>
        </#if>
    </tbody>
</table>

<script type="text/javascript">
    $(function() {
        $("a.delete-alert-link").click(function() {
            var id = $(this).attr("alertId");
            var name = $(this).attr("alertName");
            if (confirm("{{Are you sure you want to delete alert}}" + " " + name + "?")) {
                $.post("<@link name="AlertsDataPage"/>", {action: "deleteAlert", alertId: id}, function(json) {
                    if (json["success"] == "true") {
                        <#--document.location = "<@link name="AlertsPage"/>";-->
                        $("table.alerts tr[alertId='" + id +"']").remove();
                        var rows = $("table.alerts tr");
                        if (rows.length == 1) {
                            $("table.alerts tbody").append("<tr><td colspan=\"7\">{{No alerts}}</td></tr>");
                        }
                    } else {
                        alert(json["error"]);
                    }
                }, "json");
            }
        });

        $("a.test-alert-link").click(function() {
            var id = $(this).attr("alertId");
            $.post("<@link name="AlertsDataPage"/>", {action: "testAlert", alertId: id}, function(json) {
                if (json["success"] == "true") {
                    alert("{{All alert routines were processed without any error}}");
                } else {
                    alert(json["error"]);
                }
            }, "json");
        });
    });
</script>

</@common.page>
