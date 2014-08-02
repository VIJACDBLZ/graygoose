<#import "macros/common.ftl" as common>

<@common.page>
<@common.secondaryMenu>
    <a href="<@link name="SiteAddPage"/>"><i class="fa fa-plus-circle"></i></a>
    <a href="<@link name="SiteAddPage"/>">{{Add Site}}</a>
</@common.secondaryMenu>

<table class="sites table table-bordered table-striped">
    <thead class="thead-colored">
    <tr>
        <th>{{Id}}</th>
        <th>{{Name}}</th>
        <th>{{URL}}</th>
        <th>{{Rescan period, sec.}}</th>
        <th>{{Period without scan}}</th>
        <th>{{Creation time}}</th>
        <th>{{Actions}}</th>
    </tr>
    </thead>
    <tbody>
        <#if sites?? && (sites?size > 0)>
            <#list sites as site>
            <tr siteId="${site.id}">
                <td style="text-align:right;">${site.id}</td>
                <td style="text-align:left;">${site.name?html}</td>
                <td style="text-align:left;"><a href="${site.url?html}" target="_blank">${site.url?html}</a></td>
                <td>${site.rescanPeriodSeconds?int}</td>
                <td>
                    <#if !(site.pauseFromMinute??)>
                        None
                    <#else >
                    ${dateFormatter.longToStringHHMM(site.pauseFromMinute!"")} <b>-</b>
                    ${dateFormatter.longToStringHHMM(site.pauseToMinute!"")}
                    </#if>
                </td>
                <td>${site.creationTime?datetime}</td>
                <td>
                    <a href="<@link name="SiteEditPage" id="${site.id}"/>">{{Edit}}</a>
                    <a href="#" class="delete-site-link" siteName="${site.name}" siteId="${site.id}">{{Delete}}</a>
                </td>
            </tr>
            </#list>
        <#else>
        <tr>
            <td colspan="7">
                {{No sites}}
            </td>
        </tr>
        </#if>
    </tbody>
</table>

<script type="text/javascript">
    $(function() {
        $("table.sites a.delete-site-link").click(function() {
            var id = $(this).attr("siteId");
            var name = $(this).attr("siteName");
            if (confirm("{{Are you sure you want to delete site}}" + " " + name + "?")) {
                $.post("<@link name="SitesDataPage"/>", {action: "deleteSite", siteId: id}, function(json) {
                    if (json["success"] == "true") {
                        $("table.sites tr[siteId='" + id +"']").remove();
                        var rows = $("table.sites tr");
                        if (rows.length == 1) {
                            $("table.sites tbody").append("<tr><td colspan=\"7\">{{No sites}}</td></tr>");
                        }
                    } else {
                        alert(json["error"]);
                    }
                }, "json");
            }
        });
    });
</script>

</@common.page>
