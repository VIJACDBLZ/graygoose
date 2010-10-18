package com.codeforces.graygoose.frame;

import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.model.Site;
import com.codeforces.graygoose.page.web.WebPage;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.annotation.Validate;
import org.nocturne.validation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class SiteEditOrAddFrame extends ApplicationFrame {
    @Parameter
    private Long id;

    @Parameter(stripMode = Parameter.StripMode.SAFE)
    private String name;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String url;

    @Parameter
    private int rescanPeriod;

    private Site site;
    private Class<? extends WebPage> redirectPageClass;

    @Inject
    private SiteDao siteDao;

    public void setup(long id, Class<? extends WebPage> redirectPageClass) {
        this.id = id;
        this.redirectPageClass = redirectPageClass;
    }

    public void setup(Class<? extends WebPage> redirectPageClass) {
        this.redirectPageClass = redirectPageClass;
    }

    @Override
    public void initializeAction() {
        super.initializeAction();

        if (id != null) {
            site = siteDao.find(id);
            put("name", site.getName());
            put("url", site.getUrl());
            put("rescanPeriod", "" + site.getRescanPeriodSeconds());
            put("edit", true);
            put("id", site.getId());
        } else {
            put("edit", false);
        }
    }

    @Override
    public void action() {
        // No operations.
    }

    private boolean validateEditOrAdd() {
        addValidator("name", new RequiredValidator());
        addValidator("name", new LengthValidator(1, 100));

        addValidator("url", new RequiredValidator());
        addValidator("url", new LengthValidator(1, 200));
        addValidator("url", new Validator() {
            @Override
            public void run(String value) throws ValidationException {
                try {
                    String protocol = new URL(value).getProtocol();
                    if (!"http".equalsIgnoreCase(protocol)) {
                        throw new ValidationException($("Only http protocol is supported"));
                    }
                } catch (MalformedURLException e) {
                    throw new ValidationException($("Enter valid URL"));
                }
            }
        });

        addValidator("rescanPeriod", new RequiredValidator());
        addValidator("rescanPeriod", new OptionValidator("30", "60", "120", "300"));

        return runValidation();
    }

    @Validate("add")
    public boolean validateAdd() {
        return validateEditOrAdd();
    }

    @Validate("edit")
    public boolean validateEdit() {
        return validateEditOrAdd();
    }

    @Action("edit")
    public void onEdit() {
        if (site != null) {
            site.setName(name);
            site.setRescanPeriodSeconds(rescanPeriod);
            site.setUrl(url);

            setMessage($("Site has been updated"));
        } else {
            setMessage($("Can't find site to update"));
        }

        abortWithRedirect(redirectPageClass);
    }

    @Action("add")
    public void onAdd() {
        Site site = new Site(name, url, rescanPeriod, new Date());
        siteDao.insert(site);
        setMessage($("Site has been added"));
        abortWithRedirect(redirectPageClass);
    }
}
