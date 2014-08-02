package com.codeforces.graygoose.web.page.data;

import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.misc.Noty;
import com.codeforces.graygoose.model.Site;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.link.Link;

@Link("data/sites")
public class SitesDataPage extends DataPage {
    @Parameter
    private Long siteId;

    private Site site;

    @Inject
    private SiteDao siteDao;

    @Override
    public void initializeAction() {
        super.initializeAction();

        if (siteId != null) {
            site = siteDao.find(siteId);
        }
    }

    @Action("deleteSite")
    public void onDeleteSite() {
        try {
            if (site != null) {
                siteDao.markDeleted(site);
                put("success", true);

                addMessage($("Site has been deleted"), Noty.Type.SUCCESS);
            } else {
                put("error", $("No such site"));
            }
        } catch (Exception e) {
            put("error", e.getMessage());
        }

        printTemplateMapAsStringsUsingJson("success", "error");
    }
}
