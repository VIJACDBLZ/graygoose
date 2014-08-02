<#import "macros/common.ftl" as common>

<@common.basePage>
<div class="container">
    <div class="row margin-top-large">
        <div class="col-md-4 col-md-offset-4">
            <h3>{{Welcome to the Graygoose!}}</h3>
            <hr>
            <form role="form" action="" method="post">
                <input type="hidden" name="action" value="enter">

                <div class="form-group">
                    <label for="email">{{Email}}:</label>
                    <input type="email" class="form-control" id="email" name="email" placeholder="{{Enter email}}" value="${email!}">
                </div>
                <@common.errorField for="email" value="${error__email!}"/>

                <div class="form-group">
                    <label for="password">{{Password}}:</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password" value="${password!}">
                </div>
                <@common.errorField for="password" value="${error__password!}"/>

                <button type="submit" class="btn btn-default">{{Enter}}</button>
            </form>
        </div>
    </div>
</div>
</@common.basePage>
