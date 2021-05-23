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
        $.ajax({
            url: "http://localhost:8080/algos/all",
            method: "get",
            dataType: "json",
            success: function (data, msg) {
                var text = "";
                text += "<option value='-1'>Select an Algorithm</option>";
                $.each(data, function (key, val) {
                    text += "<option value=" + val.id + ">" + val.friendlyName + "</option>";
                });
                $("#algos").html(text);
            },
            error: function (msg) {
                alert("an error occured");
                console.table(msg);
            }
        });

        $.ajax({
            url: "http://localhost:8080/types/all",
            method: "get",
            dataType: "json",
            success: function (data, msg) {
                var text = "";
                text += "<option value='-1'>Select a Type</option>";
                $.each(data, function (key, val) {
                    text += "<option value=" + val.id + ">" + val.name + "</option>";
                });
                $("#types").html(text);
            },
            error: function (msg) {
                alert("an error occured");
                console.table(msg);
            }
        });

        $("#types").change(function (e) {
            var text = "";
            text += "<option value='-1'>Select a File</option>";
            if (this.value > 0) {
                let data = JSON.stringify({
                    "type": this.value
                });
                $.ajax({
                    url: "http://localhost:8080/files/all-by-type",
                    method: "post",
                    dataType: "json",
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    data: data,
                    success: function (data, msg) {
                        $.each(data, function (key, val) {
                            text += "<option value=" + val.id + ">" + val.name + "</option>";
                        });
                        $("#model-one").html(text);
                        $("#model-two").html(text);
                    },
                    error: function (msg) {
                        alert("an error occured");
                        console.table(msg);
                    }
                });
            } else {
                $("#model-one").html(text);
                $("#model-two").html(text);
            }
        });

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
                url: "http://localhost:8080/similarity/check-two-models",
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
                    //buildChartJsAll();
                    $('.container').removeClass('invisible');
                },
                error: function (msg) {
                    alert("an error occured");
                    console.table(msg);
                }
            });
        });
    }

    if (path.startsWith("/database")) {
        $('.nav-link').filter(':contains("Datenbank")').addClass("active");
        $('#file').on('change', function () {
            const file = this.files[0];
            const reader = new FileReader();

            reader.onload = function (e) {
                $('#file-content').val(reader.result);
            }

            reader.readAsText(file);
        });

        $('#upload-file-form').submit(function (event) {
            event.preventDefault();
            let fileName = $('#file').val();
            let data = JSON.stringify({
                "type": $('#type option:selected').val(),
                "name": $("#bezeichnung").val(),
                "fileName": fileName.substring(fileName.lastIndexOf('\\') + 1),
                "file": $("#file-content").val()
            });

            $.ajax({
                url: "http://localhost:8080/files/upload",
                method: "post",
                dataType: "json",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                data: data,
                success: function (data, msg) {
                    window.location.reload();
                },
                error: function (msg) {
                    alert("an error occured");
                    console.table(msg);
                }
            });
        });

        $.ajax({
            url: "http://localhost:8080/types/all",
            method: "get",
            dataType: "json",
            success: function (data, msg) {
                var text = "";
                text += "<option value='-1'>Select a Type</option>";
                $.each(data, function (key, val) {
                    text += "<option value=" + val.id + ">" + val.name + "</option>";
                });
                $("#type").html(text);
            },
            error: function (msg) {
                alert("an error occured");
                console.table(msg);
            }
        });

        $(document).ready(function () {
            var table = $('#models').DataTable({
                "sAjaxSource": "/files/all",
                "sAjaxDataProp": "",
                "aoColumns": [
                    {"mData": "id"},
                    {"mData": "name"},
                    {"mData": "fileName"},
                    {"mData": "type.name"},
                    {"mData": "department.name"}
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

function buildChartJsAll() {
    var ctx = document.getElementById('chart2').getContext('2d');
    var myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Cosine Distance', 'Cosine Similarity', 'Fuzzy Score', 'Hamming Distance', 'Jaro-Winkler Distance', 'Jaro-Winkler Similarity', 'Levenshtein Distance', 'Longest Common Subsequence Distance'],
            datasets: [{
                label: 'Ähnlichkeitsscore',
                data: [97, 72, 87, 50, 92, 43, 66, 70],
                backgroundColor: 'rgba(50, 67, 251, 0.8)',
                borderColor: 'black',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        suggestedMax: 100,
                        suggestedMin: 100
                    }
                }]
            }
        }
    });
}