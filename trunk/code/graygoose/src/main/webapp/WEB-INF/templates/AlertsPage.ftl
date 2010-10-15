<#import "macros/common.ftl" as common/>

<@common.page>
    <div style="text-align:right;padding-right:1em;">
        <a href="<@link name="AlertAddPage"/>">{{Add Alert}}</a>
    </div>
    <table class="grid">
        <thead>
        <tr>
            <th>
                {{Id}}
            </th>
            <th>
                {{Name}}
            </th>
            <th>
                {{Type}}
            </th>
            <th>
                {{E-mail}}
            </th>
            <th>
                {{Password}}
            </th>
            <th>
                {{Max alerts per hour}}
            </th>
            <th>
                {{Actions}}
            </th>
        </tr>
        </thead>
        <tbody>
        <#list alerts as alert>
            <tr>
                <td>${alert.id}</td>
                <td>${alert.name?html}</td>
                <td>${alert.type?html}</td>
                <td>${alert.email?html}</td>
                <td>${alert.password?html}</td>
                <td>${alert.maxAlertCountPerHour?int}</td>
                <td>
                    <a href="<@link name="AlertEditPage" id="${alert.id}"/>">{{Edit}}</a>
                    <a href="#" class="delete-alert-link" alertName="${alert.name}" alertId="${alert.id}">{{Delete}}</a>
                </td>
            </tr>
        </#list>
        <#if alerts?size==0>
            <tr>
                <td colspan="7">
                    {{No alerts}}
                </td>
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
                        document.location = "<@link name="AlertsPage"/>";
                    } else {
                        alert(json["error"]);
                    }
                }, "json");
            }
        });
    });
</script>
</@common.page>