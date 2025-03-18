document.addEventListener("DOMContentLoaded", function () {
    const buttons = document.querySelectorAll(".add-to-cart");

    buttons.forEach(button => {
        button.addEventListener("click", function () {
            const product = this.parentElement;
            const id = product.getAttribute("data-id");
            const name = product.getAttribute("data-name");
            const price = parseFloat(product.getAttribute("data-price"));
            const image = product.querySelector("img").src;
            const size = product.querySelector(".size-dropdown").value;

            let cart = JSON.parse(localStorage.getItem("cart")) || [];

            let existingProduct = cart.find(item => item.name === name && item.image === image && item.size === size);

            if (existingProduct) {
                existingProduct.quantity += 1;
            } else {
                cart.push({ id, name, price, image, quantity: 1, size });
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
