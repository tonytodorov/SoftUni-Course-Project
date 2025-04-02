document.addEventListener("DOMContentLoaded", () => {

    const cartItems = JSON.parse(localStorage.getItem("cart")) || [];
    const orderSummaryItems = document.getElementById("order-summary-items");

    if (!orderSummaryItems) {
        return;
    }

    let totalAmount = 0;

    cartItems.forEach((item, index) => {
        orderSummaryItems.innerHTML += `
            <tr>
                <td><img src="${item.image}" alt="${item.name}" class="order-img" style="width: 50px; height: 50px;"></td>
                <td>${item.name}</td>
                <td>${item.quantity}</td>
                <td>${item.size}</td>
                <td>${item.price.toFixed(2)} лв.</td>
            </tr>
        `;
        totalAmount += item.quantity * item.price;

        const cartItemsContainer = document.getElementById("cart-items-container");

        const cartItemIdField = document.createElement('input');
        cartItemIdField.type = 'hidden';
        cartItemIdField.name = `cartItems[${index}].id`;
        cartItemIdField.value = item.id.toString();
        cartItemsContainer.appendChild(cartItemIdField);

        const quantityField = document.createElement('input');
        quantityField.type = 'hidden';
        quantityField.name = `cartItems[${index}].quantity`;
        quantityField.value = item.quantity;
        cartItemsContainer.appendChild(quantityField);

        const sizeField = document.createElement('input');
        sizeField.type = 'hidden';
        sizeField.name = `cartItems[${index}].size`;
        sizeField.value = item.size;
        cartItemsContainer.appendChild(sizeField);
    });

    document.getElementById("order-total").textContent = `${totalAmount.toFixed(2)} лв.`;
});

document.addEventListener("DOMContentLoaded", () => {
    if (window.location.pathname === "/order/order-success") {
        localStorage.removeItem("cart");
    }
});
