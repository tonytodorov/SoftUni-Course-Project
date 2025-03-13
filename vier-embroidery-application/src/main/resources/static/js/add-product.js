document.addEventListener("DOMContentLoaded", function () {
    const buttons = document.querySelectorAll(".add-to-cart");

    buttons.forEach(button => {
        button.addEventListener("click", function () {
            const product = this.parentElement;
            const name = product.getAttribute("data-name");
            const price = parseFloat(product.getAttribute("data-price"));
            const image = product.querySelector("img").src;

            let cart = JSON.parse(localStorage.getItem("cart")) || [];

            let existingProduct = cart.find(item => item.name === name && item.image === image);

            if (existingProduct) {
                existingProduct.quantity += 1;
            } else {
                cart.push({ name, price, image, quantity: 1 });
            }

            localStorage.setItem("cart", JSON.stringify(cart));
            Swal.fire({
                title: "Добавено в количката",
                text: "Вашият артикул е добавен успешно!",
                icon: "success",
                showConfirmButton: false,
                timer: 2000
            });
        });
    });
});
