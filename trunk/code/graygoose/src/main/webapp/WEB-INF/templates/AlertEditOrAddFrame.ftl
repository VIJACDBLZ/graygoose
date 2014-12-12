<#-- @ftlvariable name="edit" type="java.lang.Boolean" -->
<#-- @ftlvariable name="id" type="java.lang.String" -->
<#-- @ftlvariable name="name" type="java.lang.String" -->
<#-- @ftlvariable name="type" type="java.lang.String" -->
<#-- @ftlvariable name="email" type="java.lang.String" -->
<#-- @ftlvariable name="password" type="java.lang.String" -->
<#-- @ftlvariable name="passwordConfirmation" type="java.lang.String" -->
<#-- @ftlvariable name="smsServiceUrl" type="java.lang.String" -->
<#-- @ftlvariable name="smsServicePhoneParameterName" type="java.lang.String" -->
<#-- @ftlvariable name="smsServicePhone" type="java.lang.String" -->
<#-- @ftlvariable name="smsServiceMessageParameterName" type="java.lang.String" -->
<#-- @ftlvariable name="maxAlertCountPerHour" type="java.lang.Integer" -->
<#-- @ftlvariable name="error__name" type="java.lang.String" -->
<#-- @ftlvariable name="error__type" type="java.lang.String" -->
<#-- @ftlvariable name="error__email" type="java.lang.String" -->
<#-- @ftlvariable name="error__password" type="java.lang.String" -->
<#-- @ftlvariable name="error__passwordConfirmation" type="java.lang.String" -->
<#-- @ftlvariable name="error__smsServiceUrl" type="java.lang.String" -->
<#-- @ftlvariable name="error__smsServicePhoneParameterName" type="java.lang.String" -->
<#-- @ftlvariable name="error__smsServicePhone" type="java.lang.String" -->
<#-- @ftlvariable name="error__smsServiceMessageParameterName" type="java.lang.String" -->
<#-- @ftlvariable name="error__maxAlertCountPerHour" type="java.lang.String" -->
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
                <#assign typeValues = ["{{&lt;select value&gt;}}", "{{E-mail}}", "{{Google calendar event}}", "{{SMS POST-request}}"]/>
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

            <div class="form-group optionalField emailAlertTypeField calendarAlertTypeField">
                <label for="email">{{Email}}:</label>
                <input type="email" class="form-control" id="email" name="email" placeholder="{{Enter email}}" value="${email!}">
            </div>
        <@common.errorField for="email" value="${error__email!}"/>

            <div class="form-group optionalField calendarAlertTypeField">
                <label for="password">{{Password}}:</label>
                <input type="password" class="form-control" id="password" name="password" placeholder="{{Password}}" value="${password!}">
            </div>
        <@common.errorField for="password" value="${error__password!}"/>

            <div class="form-group optionalField calendarAlertTypeField">
                <label for="passwordConfirmation">{{Password confirmation}}:</label>
                <input type="password" class="form-control" id="passwordConfirmation" name="passwordConfirmation" placeholder="{{Password confirmation}}" value="${passwordConfirmation!}">
            </div>
        <@common.errorField for="passwordConfirmation" value="${error__passwordConfirmation!}"/>

            <div class="form-group optionalField smsAlertTypeField">
                <label for="smsServiceUrl">{{SMS service URL}}:</label>
                <input type="text" class="form-control" id="smsServiceUrl" name="smsServiceUrl" placeholder="{{Enter SMS service URL}}" value="${smsServiceUrl!}">
            </div>
        <@common.errorField for="smsServiceUrl" value="${error__smsServiceUrl!}"/>

            <div class="form-group optionalField smsAlertTypeField">
                <label for="smsServicePhoneParameterName">{{Phone parameter name}}:</label>
                <input type="text" class="form-control" id="smsServicePhoneParameterName" name="smsServicePhoneParameterName" placeholder="{{Enter phone parameter name}}" value="${smsServicePhoneParameterName!}">
            </div>
        <@common.errorField for="smsServicePhoneParameterName" value="${error__smsServicePhoneParameterName!}"/>

            <div class="form-group optionalField smsAlertTypeField">
                <label for="smsServicePhone">{{Phone}}:</label>
                <input type="text" class="form-control" id="smsServicePhone" name="smsServicePhone" placeholder="{{Enter phone}}" value="${smsServicePhone!}">
            </div>
        <@common.errorField for="smsServicePhone" value="${error__smsServicePhone!}"/>

            <div class="form-group optionalField smsAlertTypeField">
                <label for="smsServiceMessageParameterName">{{Message parameter name}}:</label>
                <input type="text" class="form-control" id="smsServiceMessageParameterName" name="smsServiceMessageParameterName" placeholder="{{Enter message parameter name}}" value="${smsServiceMessageParameterName!}">
            </div>
        <@common.errorField for="smsServiceMessageParameterName" value="${error__smsServiceMessageParameterName!}"/>

            <div class="form-group">
                <label for="maxAlertCountPerHour">{{Max alert count per hour}}:</label>

                <select class="form-control" id="maxAlertCountPerHour" name="maxAlertCountPerHour">
                <#list [0, 1, 2, 3, 5, 10, 30] as alertCountValue>
                    <#if maxAlertCountPerHour?? && alertCountValue == maxAlertCountPerHour>
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
    function updateOptionalFieldVisibility() {
        $(".optionalField").hide();

        var newType = $("#type").val();

        if (newType === "E-mail") {
            $(".optionalField.emailAlertTypeField").show();
        } else if (newType === "Google calendar event") {
            $(".optionalField.calendarAlertTypeField").show();
        } else if (newType === "SMS POST-request") {
            $(".optionalField.smsAlertTypeField").show();
        }
    }

    $(function () {
        $("input[name='name']").focus();

        updateOptionalFieldVisibility();

        //noinspection JSUnresolvedFunction
        $("#type").change(function () {
            updateOptionalFieldVisibility();
        });
    });
</script>
