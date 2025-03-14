const cartItems = [
    { name: 'Product 1', quantity: 2, price: 19.99 },
    { name: 'Product 2', quantity: 1, price: 49.99 },
    { name: 'Product 3', quantity: 3, price: 9.99 }
];

function populateOrderSummary() {
    const orderSummaryItems = document.getElementById('order-summary-items');
    let totalAmount = 0;

    cartItems.forEach(item => {
        const row = document.createElement('tr');
        const total = item.quantity * item.price;
        totalAmount += total;

        row.innerHTML = `
            <td>${item.name}</td>
            <td>${item.quantity}</td>
            <td>$${item.price.toFixed(2)}</td>
            <td>$${total.toFixed(2)}</td>
        `;
        orderSummaryItems.appendChild(row);
    });

    document.getElementById('order-total').textContent = totalAmount.toFixed(2);
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
    form.addEventListener('submit', validateForm);
});
