$(document).ready(function(){

    /* 1. Visualizing things on Hover - See next part for action on click */
    $('.stars li').on('mouseover', function(){
        var onStar = parseInt($(this).data('value'), 10); // The star currently mouse on

        // Now highlight all the stars that's not after the current hovered star
        $(this).parent().children('li.star').each(function(e){
            if (e < onStar) {
                $(this).addClass('hover');
            }
            else {
                $(this).removeClass('hover');
            }
        });

    }).on('mouseout', function(){
        $(this).parent().children('li.star').each(function(e){
            $(this).removeClass('hover');
        });
    });


    /* 2. Action to perform on click */
    $('.stars li').on('click', function(){
        var onStar = parseInt($(this).data('value'), 10); // The star currently selected
        var stars = $(this).parent().children('li.star');

        for (i = 0; i < stars.length; i++) {
            $(stars[i]).removeClass('selected');
        }

        for (i = 0; i < onStar; i++) {
            $(stars[i]).addClass('selected');
        }
    });

    $('.star').on('click', function() {
        var rating = $(this).data('value');
        var $ratingInput = $(this).closest('.rating-stars').find('.rating');
        $ratingInput.val(rating);
    });

    $('.feedback-rating-stars').each(function (){

        var ratingInput = $(this).find('#feedback-rating').val();
        var ratingValue = parseInt(ratingInput, 10);// The star currently selected
        console.log(ratingValue);
        var starsRated = $(this).children('.stars').children('li.star');

        for (i = 0; i < starsRated.length; i++) {
            $(starsRated[i]).removeClass('selected');
        }

        for (i = 0; i < ratingValue; i++) {
            $(starsRated[i]).addClass('selected');
        }
    });
});

