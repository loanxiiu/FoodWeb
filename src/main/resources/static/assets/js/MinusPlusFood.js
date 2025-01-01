document.addEventListener("DOMContentLoaded", function () {
    // Lấy tham chiếu đến các phần tử
    const btnMinus = document.querySelector(".btn-minus");
    const btnPlus = document.querySelector(".btn-plus");
    const quantityInput = document.querySelector("#quantity");
    const quantityLabel = document.querySelector("label[for='quantity']");

    // Lấy giá trị số lượng còn trong kho từ nội dung của `<label>`
    let maxQuantity = parseInt(quantityLabel.textContent.replace(/\D/g, "")) || 0;

    // Gán sự kiện click cho nút giảm
    btnMinus.addEventListener("click", function () {
        let currentValue = parseInt(quantityInput.value) || 1; // Giá trị hiện tại
        if (currentValue > 1) { // Chỉ giảm nếu giá trị lớn hơn 1
            quantityInput.value = currentValue - 1;
            maxQuantity++; // Tăng số lượng trong kho
            quantityLabel.textContent = `${maxQuantity}`; // Cập nhật label
        }
    });

    // Gán sự kiện click cho nút tăng
    btnPlus.addEventListener("click", function () {
        let currentValue = parseInt(quantityInput.value) || 1; // Giá trị hiện tại
        if (maxQuantity > 0) { // Chỉ tăng nếu còn hàng
            quantityInput.value = currentValue + 1;
            maxQuantity--; // Giảm số lượng trong kho
            quantityLabel.textContent = `${maxQuantity}`; // Cập nhật label
        }
    });

    // Giới hạn nhập thủ công và cập nhật số lượng trong kho
    quantityInput.addEventListener("input", function () {
        let currentValue = parseInt(quantityInput.value);
        if (isNaN(currentValue) || currentValue < 1) {
            quantityInput.value = 1; // Đặt giá trị tối thiểu
        } else if (currentValue > maxQuantity + 1) {
            quantityInput.value = maxQuantity + 1; // Giới hạn không vượt quá số lượng kho
        }
        // Cập nhật số lượng còn trong kho dựa trên giá trị mới
        quantityLabel.textContent = `${maxQuantity - (parseInt(quantityInput.value) - 1)}`;
    });
});
