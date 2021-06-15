$(document).ready(function (e) {
    let path = window.location.pathname;

    if (path.endsWith("/") || path.startsWith("/database")) {
        $('.nav-link').filter(':contains("Datenbank")').addClass("active");

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
                $("#deleteAufgabe").html(text);
            }
        });

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


        $("#deleteAufgabe").on("change", function (e) {
            $.ajax({
                url: "/files/delete-all-by-aufgabe/" + this.value,
                method: "GET",
                dataType: "json",
                success: function (data, msg) {
                    return window.location.reload();
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

    if (path.startsWith("/check-more")) {
        $('.nav-link').filter(':contains("Ähnlichkeitsprüfung")').addClass("active");

        function ajaxAufgabe(){
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
        }

        ajaxAufgabe();

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
                error: function(data, msg) {
                    alert("Es ist ein Fehler aufgetreten, bitte erneut Operation ausführen." +
                        "\nSollte dies nicht möglich sein, muss die Aufgabe manuell neu hochgeladen werden.");
                    ajaxAufgabe();
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
                    "ajax": "/similarity/all-by-aufgabe/" + select,
                    "sAjaxDataProp": "",
                    "createdRow": function( row, data, dataIndex ) {
                        if (data["score"] >= $('#changeThreshold').val()) {
                            $(row).addClass('highlight');
                        }
                    },
                    "aaSorting": [[ 3, "desc" ]],
                    "columns": [
                        {"data": "id"},
                        {"data": "file1.name"},
                        {"data": "file2.name"},
                        {"data": "score"},
                        {render: function(data, type, row){
                                return '<button data-file1="' + row["file1"].name +'" data-file2="' + row["file2"].name +'" data-id="' + row["id"] +'" class="btn btn-light showDifference">Zeige</button>';
                            }
                        }
                    ]
                });
            }
        }

        $(document).on('click', '.showDifference', function(){
            $('#differenceModal').modal('show');
            let file1 = this.dataset.file1;
            let file2 = this.dataset.file2;
            $.ajax({
                url: "/similarity/show-difference/" + this.dataset.id,
                method: "GET",
                dataType: "json",
                success: function (data, msg) {
                    let html = "<canvas id='chart1' width='7' height='1'/>";
                    html += showDiff(file1, file2, data.fileDiff);
                    $(".modal-body").html(html);
                    buildChartJs(data.similarity)
                },
                error: function (msg) {
                    alert(msg["responseText"]);
                    console.table(msg);
                }
            });
        });
    }

    if (path.startsWith("/manual")) {
        $('.nav-link').filter(':contains("Anleitung")').addClass("active");
    }
});

function showDiff(fOne, fTwo, diff) {
    let html = "<table class='table'><thead><tr><th>" + fOne + "</th><th>" + fTwo + "</th></tr></thead><tbody>";
    for(let i = 0; i < diff.fileOne.length; i++) {
        let a = diff.fileOne[i];
        let b = diff.fileTwo[i];
        html += "<tr>";
        if(a.toString().includes("+++") || b.toString().includes("+++")) {
            html += "<td>" + a.replaceAll("+++", "") + "</td><td>" + b.replaceAll("+++", "") + "</td>";
        } else {
            html += "<td class='bg-danger text-white'>" + a.replaceAll("+++", "") + "</td><td class='bg-danger text-white'>" + b.replaceAll("+++", "") + "</td>";
        }
        html += "</tr>"
    }
    html += "</tbody></table>";
    return html;
}

function buildChartJs(value) {
    var ctx = document.getElementById('chart1').getContext('2d');
    let color = 'rgba(0, 175, 0, 0.8)';
    if (value >= $('#changeThreshold').val()) {
        color = 'rgba(175, 0, 0, 0.8)';
    }
    var myChart = new Chart(ctx, {
        type: 'horizontalBar',
        data: {
            datasets: [{
                label: 'Ähnlichkeitsscore',
                data: [value],
                backgroundColor: color,
                borderColor: 'black',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                xAxes: [{
                    ticks: {
                        beginAtZero: true,
                        min: 0,
                        max: 100
                    }
                }]
            }
        }
    });
}
