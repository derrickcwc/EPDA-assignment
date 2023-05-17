function showErrorModal(message) {
    $('#errorMsg').html(message); // set the error message
    const myModal = new bootstrap.Modal(document.getElementById('myModal'), focus)
    const modalToggle = document.getElementById('myModal')
    myModal.show(modalToggle);
}
$(document).ready(function() {
    showErrorModal("${errorMsg}");
});
