$(document).ready(function (e) {
    let path = window.location.pathname;

    if (path.endsWith("/")) {
        $('.nav-link').filter(':contains("Über")').addClass("active");
    }

    if (path.startsWith("/check-more")) {
        $('.nav-link').filter(':contains("Ähnlichkeitsprüfung")').addClass("active");
    }

    if (path.startsWith("/manual")) {
        $('.nav-link').filter(':contains("Anleitung")').addClass("active");
    }

    if (path.startsWith("/check-two")) {
        $('.nav-link').filter(':contains("Ähnlichkeitsprüfung")').addClass("active");

        $('#check-two-models-form').submit(function (event) {
            event.preventDefault();
            let fOne = $('#model-one option:selected').html();
            let fTwo = $('#model-two option:selected').html();
            let data = JSON.stringify({
                "type": $('#types option:selected').val(),
                "algo": $('#algos option:selected').val(),
                "fileOne": $('#model-one option:selected').val(),
                "fileTwo": $('#model-two option:selected').val()
            });
            $.ajax({
                url: "/similarity/check-two-models",
                method: "post",
                dataType: "json",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                data: data,
                success: function (data, msg) {
                    buildChartJs(data.similarity);
                    showDiff(fOne, fTwo, data.fileDiff);
                    $('.container').removeClass('invisible');
                },
                error: function (msg) {
                    alert(msg["responseText"]);
                    console.table(msg);
                }
            });
        });
    }

    if (path.startsWith("/database")) {
        $('.nav-link').filter(':contains("Datenbank")').addClass("active");

        $('#upload-zip-file-form').submit(function (event) {
            event.preventDefault();
            let form = document.getElementById('upload-zip-file-form');
            let data = new FormData(form);

            $.ajax({
                url: "/files/upload-zip",
                method: "post",
                data: data,
                contentType: false,
                processData: false,
                success: function (data, msg) {
                    window.location.reload();
                },
                error: function (msg) {
                    alert(msg["responseText"]);
                    console.table(msg);
                }
            });
        });

        $(document).ready(function () {
            var table = $('#models').DataTable({
                "sAjaxSource": "/files/all",
                "sAjaxDataProp": "",
                "aoColumns": [
                    {"mData": "id"},
                    {"mData": "name"},
                    {"mData": "aufgabe.name"}
                ]
            })
        });
    }
});

function showDiff(fOne, fTwo, diff) {
    let html = "<thead><tr><th>" + fOne + "</th><th>" + fTwo + "</th></tr></thead><tbody>";
    for(let i = 0; i < diff.fileOne.length; i++) {
        let a = diff.fileOne[i];
        let b = diff.fileTwo[i];
        html += "<tr>";

        if (a.toString() === b.toString()) {
            html += "<td class='" + ("bg-warning text-white") + "'>" + a + "</td>";
            html += "<td class='" + ("bg-warning text-white") + "'>" + b + "</td>";
        }
        else {
            html += "<td>" + a + "</td>";
        }
        html += "</tr>"
    }
    html += "</tbody>";
    $("#result-diff").html(html);
}

function buildChartJs(value) {
    var ctx = document.getElementById('chart1').getContext('2d');
    var myChart = new Chart(ctx, {
        type: 'horizontalBar',
        data: {

            datasets: [{
                label: 'Ähnlichkeitsscore',
                data: [value],
                backgroundColor: 'rgba(50, 67, 251, 0.8)',
                borderColor: 'black',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                xAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    });
    $("#resultText").html("Der Algorithmus <b>" + $('#algos option:selected').html() + "</b> hat einen Score von <b>" + value + "</b> Punkten!");
}
