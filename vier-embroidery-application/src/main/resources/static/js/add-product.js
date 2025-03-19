document.addEventListener("DOMContentLoaded", function () {
    const buttons = document.querySelectorAll(".add-to-cart");

    buttons.forEach(button => {
        button.addEventListener("click", handleAddToCart);
    });
});

function handleAddToCart(event) {
    const product = event.currentTarget.closest(".product-container");
    if (!product) return;

    const productData = extractProductData(product);
    updateCart(productData);
    showSuccessToast();
}

function extractProductData(product) {
    return {
        id: product.getAttribute("data-id"),
        name: product.getAttribute("data-name"),
        price: parseFloat(product.getAttribute("data-price")),
        image: product.querySelector("img").src,
        size: product.querySelector(".size-dropdown").value,
        quantity: 1
    };
}

function updateCart(productData) {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];

    let existingProduct = cart.find(item =>
        item.name === productData.name &&
        item.image === productData.image &&
        item.size === productData.size
    );

    if (existingProduct) {
        existingProduct.quantity += 1;
    } else {
        cart.push(productData);
    }

    localStorage.setItem("cart", JSON.stringify(cart));
}

function showSuccessToast() {
    Swal.fire({
        toast: true,
        position: "top-end",
        iconHtml: `<img src="https://cdn-icons-png.flaticon.com/128/14090/14090371.png" alt="" width="50px" height="50px">`,
        title: "Добавено в количката!",
        showConfirmButton: false,
        timer: 1200,
        timerProgressBar: true,
        background: "#fff",
        customClass: { popup: 'colored-toast' }
    });
}
