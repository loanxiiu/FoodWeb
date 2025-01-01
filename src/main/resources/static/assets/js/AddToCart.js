$(document).ready(function () {
    $(".add-to-cart-btn").click(function () {
        var foodId = $(this).data("id");
        var foodName = $(this).data("name");
        var quantity = document.getElementById('quantity') != null ? document.getElementById('quantity').value :1// Get the quantity from the hidden input

        if (isNaN(quantity) || quantity <= 0) {
            quantity = 1; // Set quantity to 1 if invalid
        }

        $.ajax({
            url: '/food_web/cartItem',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                foodId: foodId,
                quantity: quantity
            }),
            success: function (response) {
                showNotification(foodName + " đã được thêm vào giỏ hàng");
                // Update cart count if needed
                // $('#cart-count').text(response.quantity);
            },
            error: function (error) {
                showNotification("Không thể thêm sản phẩm vào giỏ hàng", "error");
                // Handle the error
                // console.error("Error:", error.responseText);
            }
        });
    });

    function showNotification(message, type = 'success') {
        // Create notification element
        var notification = $('<div>', {
            class: 'notification ' + type,
            text: message
        }).appendTo('body');

        // Style the notification
        notification.css({
            position: 'fixed',
            top: '20px',
            right: '20px',
            backgroundColor: type === 'success' ? 'green' : 'red',
            color: 'white',
            padding: '10px',
            borderRadius: '5px',
            zIndex: '1000'
        });

        // Remove notification after 3 seconds
        setTimeout(function () {
            notification.fadeOut(300, function () {
                $(this).remove();
            });
        }, 3000);
    }
});
