document.addEventListener("DOMContentLoaded", function () {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    const cartItemsContainer = document.getElementById("cart-items");
    const cartTotal = document.getElementById("cart-total");
    const orderButton = document.getElementById("order-button");

    function updateCart() {
        cartItemsContainer.innerHTML = "";
        let total = 0;
        cart.forEach((item, index) => {
            let row = document.createElement("tr");
            let itemTotal = item.price * item.quantity;
            row.innerHTML = `
                <td><img src="${item.image}" alt="${item.name}" class="cart-img"></td>
                <td>${item.name}</td>
                <td>
                    <input type="number" value="${item.quantity}" min="1" class="quantity" data-index="${index}" style="width: 50px; height: 20px; font-size: 16px; text-align: center;">
                </td>
                <td>${item.size}</td>
                <td class="item-total">${itemTotal.toFixed(2)} лв.</td>
                <td>
                    <button class="remove-item" data-index="${index}" style="color: red; font-size: 18px; border: none; background: none; cursor: pointer;">❌</button>
                </td>
            `;

            cartItemsContainer.appendChild(row);
            total += itemTotal;
        });
        cartTotal.textContent = `Общо: ${total.toFixed(2)} лв.`;

        orderButton.disabled = cart.length === 0;

        addEventListeners();
    }

    function addEventListeners() {
        document.querySelectorAll(".remove-item").forEach(button => {
            button.addEventListener("click", function () {
                cart.splice(this.getAttribute("data-index"), 1);
                localStorage.setItem("cart", JSON.stringify(cart));
                updateCart();
            });
        });

        document.querySelectorAll(".quantity").forEach(input => {
            input.addEventListener("input", function () {
                let index = this.getAttribute("data-index");
                let quantity = parseInt(this.value);

                if (isNaN(quantity) || quantity < 1) {
                    this.value = 1;
                    quantity = 1;
                }

                cart[index].quantity = quantity;
                localStorage.setItem("cart", JSON.stringify(cart));
                updateCart();
            });
        });
    }

    updateCart();
});
