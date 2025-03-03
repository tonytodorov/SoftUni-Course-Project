// checkout.js

// Simulate fetching cart items (you can replace this with actual logic)
const cartItems = [
    { name: 'Product 1', quantity: 2, price: 19.99 },
    { name: 'Product 2', quantity: 1, price: 49.99 },
    { name: 'Product 3', quantity: 3, price: 9.99 }
];

// Populate order summary
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

    // Update the total amount
    document.getElementById('order-total').textContent = totalAmount.toFixed(2);
}

// Form validation
function validateForm(event) {
    const form = document.getElementById('checkout-form');
    const name = form.name.value.trim();
    const email = form.email.value.trim();
    const address = form.address.value.trim();
    const city = form.city.value.trim();
    const zipcode = form.zipcode.value.trim();
    const cardNumber = form['card-number'].value.trim();
    const expiryDate = form['expiry-date'].value.trim();
    const cvv = form['cvv'].value.trim();

    // Simple validation
    if (!name || !email || !address || !city || !zipcode || !cardNumber || !expiryDate || !cvv) {
        alert("All fields must be filled out.");
        event.preventDefault(); // Prevent form submission
        return false;
    }

    // You can add more specific validation here (e.g., regex for email, card number, etc.)

    return true;
}

// Initialize page
document.addEventListener('DOMContentLoaded', () => {
    // Populate the order summary when the page is loaded
    populateOrderSummary();

    // Attach form validation to the checkout form
    const form = document.getElementById('checkout-form');
    form.addEventListener('submit', validateForm);
});
