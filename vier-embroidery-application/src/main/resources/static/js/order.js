document.addEventListener('DOMContentLoaded', () => {
    const cartItems = JSON.parse(localStorage.getItem("cart")) || [];

    if (window.location.pathname === "/order" && !cartItems.length) {
        return (window.location.href = "/");
    }

    const orderSummaryItems = document.getElementById('order-summary-items');
    const totalAmount = cartItems.reduce((sum, item) => {
        orderSummaryItems.innerHTML += `
            <tr>
                <td><img src="${item.image}" alt="${item.name}" class="order-img" style="width: 50px; height: 50px;"></td>
                <td>${item.name}</td>
                <td>${item.quantity}</td>
                <td>${item.size}</td>
                <td>${item.price.toFixed(2)} лв.</td>
            </tr>
        `;
        return sum + item.quantity * item.price;
    }, 0);

    document.getElementById('order-total').textContent = `${totalAmount.toFixed(2)} лв.`;

    document.getElementById('order-form').addEventListener('submit', async (event) => {
        event.preventDefault();
        const form = event.target;

        const orderData = {
            firstName: form.name.value.trim(),
            lastName: form.surname.value.trim(),
            email: form.email.value.trim(),
            phoneNumber: form.phone.value.trim(),
            city: form.city.value.trim(),
            address: form.address.value.trim(),
            paymentMethod: "CASH_ON_DELIVERY",
            cartItems: cartItems.map(({id, quantity, size}) => ({id, quantity, size}))
        };

        try {
            const response = await fetch('/order', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(orderData),
                credentials: "include"
            });

            const responseData = await response.json();

            if (!response.ok) {
                if (response.status === 400 && typeof responseData === 'object') {
                    let errorMessages = Object.values(responseData).join("<br>");

                    Swal.fire({
                        icon: "error",
                        title: "Грешка при въвеждане",
                        html: errorMessages,
                        background: "#fff",
                        customClass: {
                            popup: 'colored-toast'
                        }
                    });
                    return;
                }

                throw new Error(`Failed to place order. HTTP Status: ${response.status}`);
            }

            Swal.fire({
                toast: true,
                position: "top-end",
                iconHtml: `<img src="https://cdn-icons-png.flaticon.com/128/6815/6815043.png" width="50px" height="50px">`,
                title: "Успешно направена поръчка!",
                showConfirmButton: false,
                timer: 1500,
                timerProgressBar: true,
                background: "#fff",
                customClass: {popup: 'colored-toast'}
            });

            setTimeout(() => {
                localStorage.removeItem("cart");
                window.location.href = '/';
            }, 1600);
        } catch (error) {
            Swal.fire({
                toast: true,
                position: "top-end",
                iconHtml: `<img src="https://cdn-icons-png.flaticon.com/128/10308/10308693.png" width="50px" height="50px">`,
                title: "Не успяхте да направите поръчка!",
                showConfirmButton: false,
                timer: 1200,
                timerProgressBar: true,
                background: "#fff",
                customClass: {popup: 'colored-toast'}
            });

            localStorage.clear();
            window.location.href = '/order';
        }
    });
});
