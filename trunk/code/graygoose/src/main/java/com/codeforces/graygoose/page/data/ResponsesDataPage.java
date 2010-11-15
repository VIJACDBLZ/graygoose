package com.codeforces.graygoose.page.data;

import com.codeforces.graygoose.dao.ResponseDao;
import com.codeforces.graygoose.model.Response;
import com.codeforces.graygoose.validation.LongValidator;
import com.google.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.annotation.Validate;
import org.nocturne.link.Link;
import org.nocturne.validation.RequiredValidator;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Link("data/responses")
public class ResponsesDataPage extends DataPage {
    @Parameter
    private Long responseId;

    @Inject
    private ResponseDao responseDao;

    @Validate("downloadResponse")
    public boolean validateDownloadResponse() {
        addValidator("responseId", new RequiredValidator());
        addValidator("responseId", new LongValidator(1, Long.MAX_VALUE));

        return runValidation();
    }

    @Action("downloadResponse")
    public void onDownloadResponse() {
        final Response response = responseDao.find(responseId);
        final HttpServletResponse httpServletResponse = getResponse();

        if (response != null) {
            try {
                IOUtils.write(response.getText().getValue(), httpServletResponse.getWriter());
            } catch (IOException e) {
                // No operations.
            }
        }
    }

    @Override
    public void action() {
        // No actions.
    }
}
