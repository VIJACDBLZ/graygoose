<#import "macros/common.ftl" as common/>

<div class="row">
    <div class="col-md-4 col-md-offset-4">
        <form role="form" action="" method="post">
            <input type="hidden" name="action" value="<#if edit>edit<#else>add</#if>">
        <#if id??>
            <input type="hidden" name="id" value="${id!}">
        </#if>

            <div class="form-group">
                <label for="name">{{Name}}:</label>
                <input type="text" class="form-control" id="name" name="name" placeholder="{{Enter name}}" value="${name!?html}">
            </div>
        <@common.errorField for="name" value="${error__name!}"/>

            <div class="form-group">
                <label for="type">{{Notification type}}:</label>

                <select class="form-control" id="type" name="type">
                <#assign typeValues = ["{{&lt;select value&gt;}}", "{{E-mail}}", "{{Google calendar event}}"]/>
                <#list typeValues as typeValue>
                    <#if type?? && typeValue == type>
                        <option selected value="${typeValue}">${typeValue}</option>
                    <#else>
                        <option value="${typeValue}">${typeValue}</option>
                    </#if>
                </#list>
                </select>
            </div>
        <@common.errorField for="type" value="${error__type!}"/>

            <div class="form-group">
                <label for="email">{{Email}}:</label>
                <input type="email" class="form-control" id="email" name="email" placeholder="{{Enter email}}" value="${email!}">
            </div>
        <@common.errorField for="email" value="${error__email!}"/>

            <div class="form-group">
                <label for="password">{{Password}}:</label>
                <input type="password" class="form-control" id="password" name="password" placeholder="{{Password}}" value="${password!}">
            </div>
        <@common.errorField for="password" value="${error__password!}"/>

            <div class="form-group">
                <label for="passwordConfirmation">{{Password confirmation}}:</label>
                <input type="password" class="form-control" id="passwordConfirmation" name="passwordConfirmation" placeholder="{{Password confirmation}}" value="${password2!}">
            </div>
        <@common.errorField for="passwordConfirmation" value="${error__passwordConfirmation!}"/>

            <div class="form-group">
                <label for="maxAlertCountPerHour">{{Max alert count per hour}}:</label>

                <select class="form-control" id="maxAlertCountPerHour" name="maxAlertCountPerHour">
                <#assign alertCountValues = [0, 1, 2, 3, 5, 10]/>
                <#list alertCountValues as alertCountValue>
                    <#if maxAlertCountPerHour?? && alertCountValue?string==maxAlertCountPerHour>
                        <option selected="selected" value="${alertCountValue}">${alertCountValue}</option>
                    <#else>
                        <option value="${alertCountValue}">${alertCountValue}</option>
                    </#if>
                </#list>
                </select>
            </div>
        <@common.errorField for="maxAlertCountPerHour" value="${error__maxAlertCountPerHour!}"/>

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
