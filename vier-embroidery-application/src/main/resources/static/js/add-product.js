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
                toast: true,
                position: "top-end",
                iconHtml: `<img src="https://cdn-icons-png.flaticon.com/128/14090/14090371.png" alt="" width="50px" height="50px">`,
                title: "Добавено в количката!",
                showConfirmButton: false,
                timer: 1200,
                timerProgressBar: true,
                background: "#fff",
                customClass: {
                    popup: 'colored-toast'
                }
            });

        });
    });
});
