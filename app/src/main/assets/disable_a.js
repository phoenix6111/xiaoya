$(function(){
    $("a").each(function () {
            var textValue = $(this).html();
            $(this).css("cursor", "default");
            $(this).attr('href', '#');     //修改<a>的 href属性值为 #  这样状态栏不会显示链接地址
            $(this).click(function (event) {
                event.preventDefault();   // 如果<a>定义了 target="_blank“ 需要这句来阻止打开新页面
            });
    });
});