$(document).ready(function (e) {
    let path = window.location.pathname;

    if (path.endsWith("/")) {
        $('.nav-link').filter(':contains("Über")').addClass("active");
    }

    if (path.startsWith("/check-more")) {
        $('.nav-link').filter(':contains("Ähnlichkeitsprüfung")').addClass("active");

        $.ajax({
            url: "/aufgabe/all",
            method: "GET",
            dataType: "json",
            success: function (data, msg) {
                var text = "";
                text += "<option value='-1'>Select one</option>"
                $.each(data, function(key, value) {
                    text += "<option value='" + value.id + "'>" + value.name + "</option>"
                })
                $("#aufgabe").html(text);
                $("#showAufgabe").html(text);
            }
        });

        $("#aufgabe").on("change", function (e) {
            $('#checkModels').DataTable().clear().destroy();
            let aufgabe = this.value;
            let data = {
                "aufgabe": aufgabe
            }
            $.ajax({
                url: "/files/all-by-aufgabe",
                method: "POST",
                dataType: "json",
                data: data,
                async: true,
                beforeSend: function() {
                    $(".spanner").addClass("show");
                    $(".overlay").addClass("show");
                    $('#changeThreshold').prop("disabled", true);
                    $('#aufgabe').prop("disabled", true);
                    $('#showAufgabe').prop("disabled", true);
                },
                complete: function(){
                    $(".spanner").removeClass("show");
                    $(".overlay").removeClass("show");
                    $('#changeThreshold').prop("disabled", false);
                    $('#aufgabe').prop("disabled", false);
                    $('#showAufgabe').prop("disabled", false);
                },
                success: function (data, msg) {
                    recalcDatatable(aufgabe);
                    $('#showAufgabe option[value="' + aufgabe + '"]').prop('selected', true)
                    $('#aufgabe option[value="-1"]').prop('selected', true)
                },
            });
        });

        $("#showAufgabe").on("change", function (e) {
            recalcDatatable(this.value)
        });

        $('#changeThreshold').on("change", function(e){
            recalcDatatable($("#showAufgabe").val())
        })

        function recalcDatatable(select){
            $('#checkModels').DataTable().clear().destroy();
            if (select !== -1) {
                $('#checkModels').DataTable({
                    "sAjaxSource": "/similarity/all-by-aufgabe/" + select,
                    "sAjaxDataProp": "",
                    "createdRow": function( row, data, dataIndex ) {
                        if (data["score"] >= $('#changeThreshold').val()) {
                            $(row).addClass('highlight');
                        }
                    },
                    "aaSorting": [[ 3, "desc" ]],
                    "aoColumns": [
                        {"mData": "id"},
                        {"mData": "file1.name"},
                        {"mData": "file2.name"},
                        {"mData": "score"},
                        {"mData": function() {
                            return "<a id='showDifference'>Zeige</a>"
                        }},
                    ]
                });
            }
        }
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
        if(a.toString().includes("+++") || a.toString().includes("---")) {
            html += "<td class='" + (a.toString().includes("+++") ? "bg-success" : "bg-danger text-white") + "'>" + a + "</td>";
            //html += "<td class='" + (a.toString().includes("+++") ? "text-success" : "text-danger") + "'>" + a + "</td>";
        } else {
            html += "<td>" + a + "</td>";
        }
        if(b.toString().includes("+++") || b.toString().includes("---")) {
            html += "<td class='" + (b.toString().includes("+++") ? "bg-success" : "bg-danger text-white") + "'>" + b + "</td>";
            //html += "<td class='" + (b.toString().includes("+++") ? "text-success" : "text-danger") + "'>" + b + "</td>";
        } else {
            html += "<td>" + b + "</td>";
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
