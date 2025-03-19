window.addEventListener("resize", () => {
    document.body.style.paddingRight = "0px";
});

document.addEventListener("DOMContentLoaded", function () {
    const wishlistContainer = document.getElementById("wishlist-items");
    const emptyWishlistMessage = document.getElementById("empty-wishlist");

    function loadWishlist() {
        let wishlist = JSON.parse(localStorage.getItem("wishlist")) || [];

        if (wishlistContainer) {
            wishlistContainer.innerHTML = "";

            if (wishlist.length === 0) {
                emptyWishlistMessage.style.display = "block";
                return;
            }

            emptyWishlistMessage.style.display = "none";

            wishlist.forEach(item => {
                const wishlistItem = document.createElement("div");
                wishlistItem.classList.add("wishlist-item");

                wishlistItem.innerHTML = `
                    <img src="${item.image}" alt="Product Image">
                    <div class="wishlist-info">
                        <h2>${item.name}</h2>
                        <p>${item.description}</p>
                        <button class="add-to-cart-btn" data-id="${item.id}" data-name="${item.name}" data-price="${item.price}" data-image="${item.image}">Добави в количката</button>
                        <button class="remove-btn" data-id="${item.id}">Премахни</button>
                    </div>
                `;

                wishlistContainer.appendChild(wishlistItem);
            });

            document.querySelectorAll(".remove-btn").forEach(button => {
                button.addEventListener("click", function () {
                    removeFromWishlist(this.getAttribute("data-id"));
                    loadWishlist();
                    updateWishlistIcons();
                    showToast("Премахнато от любими!", "https://cdn-icons-png.flaticon.com/128/458/458594.png");
                });
            });

            document.querySelectorAll(".add-to-cart-btn").forEach(button => {
                button.addEventListener("click", function () {
                    openSizeSelectionModal(this);
                });
            });
        }
    }

    function addToWishlist(product) {
        let wishlist = JSON.parse(localStorage.getItem("wishlist")) || [];

        if (!wishlist.some(item => item.id === product.id)) {
            wishlist.push(product);
            showToast("Добавено в любими!", "https://cdn-icons-png.flaticon.com/128/7245/7245139.png");
        }

        localStorage.setItem("wishlist", JSON.stringify(wishlist));
        updateWishlistIcons();
    }

    function addToCartFromWishlist(id, name, price, image, size) {
        let cart = JSON.parse(localStorage.getItem("cart")) || [];

        price = parseFloat(price);

        let existingProduct = cart.find(item => item.name === name && item.image === image && item.size === size);

        if (existingProduct) {
            existingProduct.quantity += 1;
        } else {
            cart.push({ id, name, price, image, quantity: 1, size });
        }

        localStorage.setItem("cart", JSON.stringify(cart));

        showToast("Добавено в количката!", "https://cdn-icons-png.flaticon.com/128/14090/14090371.png");
    }


    function removeFromWishlist(productId) {
        let wishlist = JSON.parse(localStorage.getItem("wishlist")) || [];
        wishlist = wishlist.filter(item => item.id !== productId);
        localStorage.setItem("wishlist", JSON.stringify(wishlist));
    }

    function updateWishlistIcons() {
        let wishlist = JSON.parse(localStorage.getItem("wishlist")) || [];

        document.querySelectorAll(".wishlist-icon").forEach(icon => {
            const productId = icon.getAttribute("data-id");
            if (wishlist.some(item => item.id === productId)) {
                icon.classList.add("active");
                icon.innerHTML = "❤️";
            } else {
                icon.classList.remove("active");
                icon.innerHTML = "🤍";
            }
        });
    }

    function showToast(message, iconUrl) {
        Swal.fire({
            toast: true,
            position: "top-end",
            iconHtml: `<img src="${iconUrl}" alt="" width="50px" height="50px">`,
            title: message,
            showConfirmButton: false,
            timer: 1200,
            timerProgressBar: true,
            background: "#fff",
            customClass: {
                popup: 'colored-toast'
            },
            didOpen: () => {
                document.body.style.paddingRight = "0px";
            },
            willClose: () => {
                document.body.style.paddingRight = "0px";
            }
        });
    }

    function openSizeSelectionModal(button) {
        const productId = button.getAttribute("data-id");
        const productName = button.getAttribute("data-name");
        const productPrice = button.getAttribute("data-price");
        const productImage = button.getAttribute("data-image");

        Swal.fire({
            title: "Изберете размер",
            html: `
                <img src="${productImage}" alt="Product Image" width="120px" style="margin-bottom:10px;">
                <h3>${productName}</h3>
                <h3>${productPrice} лв.</h3>
                <select id="size-dropdown" class="size-dropdown">
                    <option value="S">S</option>
                    <option value="M">M</option>
                    <option value="L">L</option>
                    <option value="XL">XL</option>
                </select>
            `,
            showConfirmButton: true,
            confirmButtonText: "Добави",
            showCancelButton: true,
            cancelButtonText: "Назад",
            preConfirm: () => {
                const size = document.getElementById("size-dropdown").value;
                addToCartFromWishlist(productId, productName, productPrice, productImage, size);
                return true;
            },
            customClass: {
                confirmButton: 'custom-confirm-button',
                cancelButton: 'custom-cancel-button'
            },
            didOpen: () => {
                document.body.style.paddingRight = "0px";
            },
            willClose: () => {
                document.body.style.paddingRight = "0px";
            }
        });
    }

    document.body.addEventListener("click", function (event) {
        if (event.target.classList.contains("wishlist-icon")) {
            const icon = event.target;
            const productContainer = icon.closest(".product-container");

            const product = {
                id: productContainer.getAttribute("data-id"),
                name: productContainer.querySelector("h2").textContent,
                description: productContainer.querySelector("p").textContent,
                price: productContainer.getAttribute("data-price"),
                image: productContainer.querySelector("img").src
            };

            if (icon.classList.contains("active")) {
                removeFromWishlist(product.id);
                showToast("Премахнато от любими!", "https://cdn-icons-png.flaticon.com/128/458/458594.png");
            } else {
                addToWishlist(product);
            }

            updateWishlistIcons();
            loadWishlist();
        }
    });

    updateWishlistIcons();
    loadWishlist();
});
