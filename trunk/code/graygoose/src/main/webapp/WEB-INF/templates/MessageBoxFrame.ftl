<#import "macros/common.ftl" as common>

<#if text?? && (text?length>0)>
<div class="message-box-wrapper">
<@common.colorBox clazz="brown-box message-box">${text?html}</@common.colorBox>
</div>
<script type="text/javascript">
    $(function() {
        setTimeout(function() {
            $(".message-box").fadeOut();
        }, 5000);
    });
</script>
</#if>