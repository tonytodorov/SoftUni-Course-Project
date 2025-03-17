function populateOrderSummary() {
    const orderSummaryItems = document.getElementById('order-summary-items');
    let totalAmount = 0;

    const cartItems = JSON.parse(localStorage.getItem("cart")) || [];

    cartItems.forEach(item => {
        const row = document.createElement('tr');
        const total = item.quantity * item.price;
        totalAmount += total;

        row.innerHTML = `
            <td><img src="${item.image}" alt="${item.name}" class="order-img" style="width: 50px; height: 50px;"></td>
            <td>${item.name}</td>
            <td>${item.quantity}</td>
            <td>${item.price.toFixed(2)} лв.</td>
        `;
        orderSummaryItems.appendChild(row);
    });

    document.getElementById('order-total').textContent = totalAmount.toFixed(2) + ' лв.';
}

function validateForm(event) {
    const form = document.getElementById('order-form');
    const name = form.name.value.trim();
    const surname = form.surname.value.trim();
    const email = form.email.value.trim();
    const phone = form.phone.value.trim();
    const city = form.city.value.trim();
    const address = form.address.value.trim();

    if (!name || !surname || !email || !phone || !city || !address) {
        alert("All fields must be filled out.");
        event.preventDefault();
        return false;
    }

    return true;
}

document.addEventListener('DOMContentLoaded', () => {
    populateOrderSummary();

    const form = document.getElementById('order-form');
    form.addEventListener('submit', function (event) {
        event.preventDefault();

        const orderData = {
            firstName: form.name.value.trim(),
            lastName: form.surname.value.trim(),
            email: form.email.value.trim(),
            phoneNumber: form.phone.value.trim(),
            city: form.city.value.trim(),
            address: form.address.value.trim(),
            paymentMethod: "UPON_DELIVERY",
            cartItems: JSON.parse(localStorage.getItem("cart") || "[]").map(item => ({
                productId: item.id,
                quantity: item.quantity
            }))
        };

        fetch('/order', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderData),
            credentials: "include"
        })
            .then(response => {
                if (response.ok) {
                    return response.text();
                } else {
                    throw new Error('Failed to place order. HTTP Status: ' + response.status);
                }
            })
            .then(data => {
                if (data) {
                    alert('Order placed successfully!');
                    window.location.href = '/';
                    localStorage.clear()
                }
            })
            .catch(error => {
                alert('An error occurred while placing the order.');
                console.error(error);
                window.location.href = '/order';
            });
    });
});

