<#import "macros/common.ftl" as common/>

<@common.page>
<div style="text-align:right;padding-right:1em;">
    <a href="<@link name="SiteAddPage"/>">{{Add Site}}</a>
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
            {{URL}}
        </th>
        <th>
            {{Rescan period, sec.}}
        </th>
        <th>
            {{Creation time}}
        </th>
        <th>
            {{Actions}}
        </th>
    </tr>
    </thead>
    <tbody>
        <#list sites as site>
        <tr>
            <td>${site.id}</td>
            <td>${site.name?html}</td>
            <td>${site.url?html}</td>
            <td>${site.rescanPeriodSeconds?int}</td>
            <td>${site.creationTime?datetime}</td>
            <td>
                <a href="<@link name="SiteEditPage" id="${site.id}"/>">{{Edit}}</a>
                <a href="#" class="delete-site-link" siteName="${site.name}" siteId="${site.id}">{{Delete}}</a>
            </td>
        </tr>
        </#list>
        <#if sites?size==0>
        <tr>
            <td colspan="6">
                {{No sites}}
            </td>
        </tr>
        </#if>
    </tbody>
</table>
<script type="text/javascript">
    $(function() {
        $("a.delete-site-link").click(function() {
            var id = $(this).attr("siteId");
            var name = $(this).attr("siteName");
            if (confirm("{{Are you sure you want to delete site}}" + " " + name + "?")) {
                $.post("<@link name="SitesDataPage"/>", {action: "deleteSite", siteId: id}, function(json) {
                    if (json["success"] == "true") {
                        document.location = "<@link name="SitesPage"/>";
                    } else {
                        alert(json["error"]);
                    }
                }, "json");
            }
        });
    });
</script>
</@common.page>