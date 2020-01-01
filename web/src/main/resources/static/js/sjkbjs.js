function getcontact(contact_id) {
    window.location.href = '/backstage/user/edit/' + contact_id;
}

function doMulti(dom) {
    $(".expclass").each(function (index, element) {
        var p = $(element).prop('selected');
        var m = $(element).data("multi");
        if ($(element).prop('selected'))
            $("#multiplier").val(m);
    });
}

function doMath(e, d) {
    var tar = e.target.id;
    var n = parseFloat($("#net").val());
    var t = parseFloat($("#tax").val());
    var m = parseFloat($("#multiplier").val());
    var s = parseFloat($("#shipping").val());
    var invoice = parseFloat($("#invoice").val());
    var multi = parseFloat($("#multiplier").val());
    if (m === 0.0) {
        m = 1.0;
    }
    switch (tar) {
        case "net":
        case "tax":
        case "shipping":
            invoice = parseFloat((n / m) + t + s).toFixed(2);
            break;
        case "invoice":
            multi = parseFloat(n / (invoice - t - s)).toFixed(2);
    }
    var paid = parseFloat(n + t + s).toFixed(2);
    var i = $("#invoice").val();
    $("#paid").val(paid);
    $("#invoice").val(invoice);
    $("#multiplier").val(multi);
}