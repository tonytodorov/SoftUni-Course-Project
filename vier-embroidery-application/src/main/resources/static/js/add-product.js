document.addEventListener("DOMContentLoaded", function () {
    const buttons = document.querySelectorAll(".add-to-cart");

    buttons.forEach(button => {
        button.addEventListener("click", function () {
            const product = this.parentElement;
            const name = product.getAttribute("data-name");
            const price = product.getAttribute("data-price");
            const image = product.querySelector("img").src;

            let cart = JSON.parse(localStorage.getItem("cart")) || [];
            cart.push({name, price, image});
            localStorage.setItem("cart", JSON.stringify(cart));
            alert("Added to cart!");
        });
    });
});
