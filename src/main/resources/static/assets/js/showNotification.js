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