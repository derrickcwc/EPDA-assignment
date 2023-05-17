$(document).ready(function(){
    $('#filter').keyup(function (){
        var searchInput = $('#filter').val().toUpperCase();
        const productCards = document.querySelectorAll('.productName');
        productCards.forEach(card => {
            const productName = card.querySelector('.card-title').textContent.toUpperCase();
            if (productName.includes(searchInput)) {
                card.style.display = 'block';
            } else {
                card.style.display = 'none';
            }
        });
    })
});