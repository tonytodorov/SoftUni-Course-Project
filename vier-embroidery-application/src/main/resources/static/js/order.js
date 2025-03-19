function populateOrderSummary() {
    const orderSummaryItems = document.getElementById('order-summary-items');
    let totalAmount = 0;

    const cartItems = JSON.parse(localStorage.getItem("cart")) || [];

    if (window.location.pathname === "/order" && cartItems.length === 0) {
        window.location.href = "/";
    }

    cartItems.forEach(item => {
        const row = document.createElement('tr');
        const total = item.quantity * item.price;
        totalAmount += total;

        row.innerHTML = `
            <td><img src="${item.image}" alt="${item.name}" class="order-img" style="width: 50px; height: 50px;"></td>
            <td>${item.name}</td>
            <td>${item.quantity}</td>
            <td>${item.size}</td>
            <td>${item.price.toFixed(2)} лв.</td>
        `;
        orderSummaryItems.appendChild(row);
    });

    document.getElementById('order-total').textContent = totalAmount.toFixed(2) + ' лв.';
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
            paymentMethod: "CASH_ON_DELIVERY",
            cartItems: JSON.parse(localStorage.getItem("cart") || "[]").map(item => ({
                productId: item.id,
                quantity: item.quantity,
                size: item.size
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
                    Swal.fire({
                        toast: true,
                        position: "top-end",
                        iconHtml: `<img src="https://cdn-icons-png.flaticon.com/128/6815/6815043.png" alt="" width="50px" height="50px">`,
                        title: "Успешно направена поръчка!",
                        showConfirmButton: false,
                        timer: 1500,
                        timerProgressBar: true,
                        background: "#fff",
                        customClass: {
                            popup: 'colored-toast'
                        }
                    });
                    setTimeout(() => {
                        window.location.href = '/';
                        localStorage.removeItem("cart");
                    }, 1600);
                }
            })
            .catch(error => {
                if (data) {
                    Swal.fire({
                        toast: true,
                        position: "top-end",
                        iconHtml: `<img src="https://cdn-icons-png.flaticon.com/128/10308/10308693.png" alt="" width="50px" height="50px">`,
                        title: "Не успяхте да направите поръчка!",
                        showConfirmButton: false,
                        timer: 1200,
                        timerProgressBar: true,
                        background: "#fff",
                        customClass: {
                            popup: 'colored-toast'
                        }
                    });
                    window.location.href = '/';
                    localStorage.clear()
                }
                console.error(error);
                window.location.href = '/order';
            });
    });
});

