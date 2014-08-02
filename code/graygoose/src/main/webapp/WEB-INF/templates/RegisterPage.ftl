<#import "macros/common.ftl" as common>

<@common.basePage>
<div class="container">
    <div class="row margin-top-large">
        <div class="col-md-4 col-md-offset-4">
            <h3>{{Welcome to the Graygoose!}}</h3>
            <hr>
            <form role="form" action="" method="post">
                <input type="hidden" name="action" value="register">

                <div class="form-group">
                    <label for="email">{{Email}}:</label>
                    <input type="email" class="form-control" id="email" name="email" placeholder="{{Enter email}}" value="${email!}">
                </div>
                <@common.errorField for="email" value="${error__email!}"/>

                <div class="form-group">
                    <label for="name">{{Name}}:</label>
                    <input type="text" class="form-control" id="name" name="name" placeholder="{{Name}}" value="${name!}">
                </div>
                <@common.errorField for="name" value="${error__name!}"/>

                <div class="form-group">
                    <label for="password">{{Password}}:</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="{{Password}}" value="${password!}">
                </div>
                <@common.errorField for="password" value="${error__password!}"/>

                <div class="form-group">
                    <label for="password2">{{Confirm password}}:</label>
                    <input type="password" class="form-control" id="password2" name="password2" placeholder="{{Confirm password}}" value="${password2!}">
                </div>
                <@common.errorField for="password2" value="${error__password2!}"/>

                <button type="submit" class="btn btn-default">{{Register}}</button>
            </form>
        </div>
    </div>
</div>
</@common.basePage>



