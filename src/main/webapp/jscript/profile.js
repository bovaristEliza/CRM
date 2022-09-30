var p = document.getElementById("profile");
function show() {
    p.classList.toggle('hideP'); // toggle the hideP class
}

document.getElementById('button').addEventListener('click', show);


$("#toggle-customerView").click(function() {
    $("#edit").hide().attr("formnovalidate");
    $("#customerView").show();
});

$("#toggle-edit").click(function() {
    $("#customerView").hide().attr("formnovalidate");
    $("#edit").show();
});

$("document").ready(function() {
    $("#edit").hide();
});