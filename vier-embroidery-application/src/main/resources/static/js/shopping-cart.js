document.addEventListener("DOMContentLoaded", function () {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    const cartItemsContainer = document.getElementById("cart-items");
    const cartTotal = document.getElementById("cart-total");

    function updateCart() {
        cartItemsContainer.innerHTML = "";
        let total = 0;
        cart.forEach((item, index) => {
            let row = document.createElement("tr");
            row.innerHTML = `
                    <td><img src="${item.image}" alt="${item.name}" class="cart-img"></td>
                    <td>${item.name}</td>
                    <td>$${item.price}</td>
                    <td><input type="number" value="1" min="1" class="quantity" data-index="${index}"></td>
                    <td class="item-total">$${item.price}</td>
                    <td><button class="remove-item" data-index="${index}">X</button></td>
                `;
            cartItemsContainer.appendChild(row);
            total += parseFloat(item.price);
        });
        cartTotal.textContent = total.toFixed(2);
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
                let price = parseFloat(cart[index].price);
                let itemTotal = price * quantity;
                this.parentElement.nextElementSibling.textContent = `$${itemTotal.toFixed(2)}`;
                updateCartTotal();
            });
        });
    }

    function updateCartTotal() {
        let total = 0;
        document.querySelectorAll(".item-total").forEach(cell => {
            total += parseFloat(cell.textContent.replace("$", ""));
        });
        cartTotal.textContent = total.toFixed(2);
    }

    updateCart();
});
