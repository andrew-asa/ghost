// 加载jquery
var script=document.createElement("script");
script.type="text/javascript";
script.src="https://code.jquery.com/jquery-3.6.0.js";
document.getElementsByTagName('head')[0].appendChild(script);
ghost={
    image2Base64: function (img) {
        var canvas = document.createElement("canvas");
        canvas.width = img.width;
        canvas.height = img.height;
        var ctx = canvas.getContext("2d");
        ctx.drawImage(img, 0, 0, img.width, img.height);
        var dataURL = canvas.toDataURL("image/png");
        return dataURL;
    },
    imageNode2Base64: function (selector,callBack) {
        var imgurl = $(selector).attr("src");
        console.log(imgurl);
        var img = new Image();
        img.src=imgurl;
        img.onload = function () {
            callBack(image2Base64(img));
        }
    }
};

function image2Base64(img) {
    var canvas = document.createElement("canvas");
    canvas.width = img.width;
    canvas.height = img.height;
    var ctx = canvas.getContext("2d");
    ctx.drawImage(img, 0, 0, img.width, img.height);
    var dataURL = canvas.toDataURL("image/png");
    return dataURL;
}




