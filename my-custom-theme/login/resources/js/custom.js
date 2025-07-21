document.addEventListener('DOMContentLoaded', function() {
    // Password visibility toggle
    const togglePassword = document.getElementById('toggle-password');
    const passwordInput = document.getElementById('password');

    if (togglePassword && passwordInput) {
        togglePassword.addEventListener('click', function() {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);

            // Toggle icon class (you'll need to add appropriate icons in your CSS)
            this.querySelector('.eye-icon').classList.toggle('show');
        });
    }

    // Add form validation if needed
    const loginForm = document.getElementById('kc-form-login');
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;

            // Example of simple client-side validation
            if (!username || !password) {
                // Handle empty fields if needed
                // Note: Keycloak has server-side validation, so this is optional
            }
        });
    }

    // You can add more interactive features here
});