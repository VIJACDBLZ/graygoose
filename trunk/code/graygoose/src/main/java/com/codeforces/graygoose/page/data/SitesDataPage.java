package com.codeforces.graygoose.page.data;

import org.nocturne.link.Link;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import com.codeforces.graygoose.model.Site;
import com.codeforces.graygoose.dao.SiteDao;
import com.google.inject.Inject;

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
                siteDao.delete(site);
                put("success", true);
                
                setMessage($("Site has been deleted"));
            } else {
                put("error", $("No such site"));
            }
        } catch (Exception e) {
            put("error", e.getMessage());
        }

        printTemplateMapAsStringsUsingJson("success", "error");
    }

    @Override
    public void action() {
        // No operations.
    }
}
