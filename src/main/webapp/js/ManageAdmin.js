function showErrorModal(message) {
    $('#errorMsg').html(message); // set the error message
    const myModal = new bootstrap.Modal(document.getElementById('myModal'), focus)
    const modalToggle = document.getElementById('myModal')
    myModal.show(modalToggle);

    modalToggle.addEventListener('hidden.bs.modal', function (event) {
        $('.modal-backdrop').hide();
        location.reload();
    })
}
$(document).ready(function() {
    (function () {
        'use strict'

        // Fetch all the forms we want to apply custom Bootstrap validation styles to
        var forms = document.querySelectorAll('.needs-validation')

        // Loop over them and prevent submission
        Array.prototype.slice.call(forms)
            .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }

                    form.classList.add('was-validated')
                }, false)
            })
    })()

    //For error Message
    showErrorModal("${errorMsg}");

});

