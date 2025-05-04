// API endpoints
const API_BASE_URL = '/api';
const ENDPOINTS = {
    USERS: `${API_BASE_URL}/users`,
    MOVIES: `${API_BASE_URL}/movies`,
    RENTALS: `${API_BASE_URL}/rentals`,
    ACTIVE_RENTALS: `${API_BASE_URL}/rentals/active`,
    PRICING_CATEGORIES: `${API_BASE_URL}/pricing-categories`,
    LATE_FEES: `${API_BASE_URL}/late-fees`
};

// Page Management
document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', (e) => {
        e.preventDefault();
        const page = e.target.closest('.nav-item').dataset.page;
        showPage(page);
        loadPageData(page);
    });
});

function showPage(pageId) {
    // Hide all pages
    document.querySelectorAll('.page').forEach(page => {
        page.classList.add('hidden');
        page.classList.remove('active');
    });
    // Show selected page
    const selectedPage = document.getElementById(pageId);
    if (selectedPage) {
        selectedPage.classList.remove('hidden');
        selectedPage.classList.add('active');
    }
    // Update navigation
    document.querySelectorAll('.nav-item').forEach(item => {
        item.classList.remove('active');
        if (item.dataset.page === pageId) {
            item.classList.add('active');
        }
    });
}

// Modal Management
function showModal(title, content) {
    document.getElementById('modalTitle').textContent = title;
    document.getElementById('modalContent').innerHTML = content;
    document.getElementById('modal').classList.remove('hidden');
}

function closeModal() {
    document.getElementById('modal').classList.add('hidden');
}

// Form Utilities
function createFormGroup(label, input) {
    return `
        <div class="form-group">
            <label class="form-label">${label}</label>
            ${input}
        </div>
    `;
}

function createInput(type, name, placeholder = '', value = '') {
    return `<input type="${type}" name="${name}" class="form-input" placeholder="${placeholder}" value="${value}">`;
}

function createSelect(name, options, selectedValue = '') {
    const optionsHtml = options.map(option => 
        `<option value="${option.value}" ${option.value === selectedValue ? 'selected' : ''}>${option.label}</option>`
    ).join('');
    return `<select name="${name}" class="form-input">${optionsHtml}</select>`;
}

// API Utilities
async function fetchAPI(endpoint, options = {}) {
    try {
        const response = await fetch(endpoint, {
            headers: {
                'Content-Type': 'application/json',
            },
            ...options
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        showError(`API Error: ${error.message}`);
        throw error;
    }
}

// User Management
function openAddUserModal() {
    const content = `
        <form id="addUserForm" onsubmit="addUser(event)">
            ${createFormGroup('Name', createInput('text', 'name', 'Enter name'))}
            ${createFormGroup('Email', createInput('email', 'email', 'Enter email'))}
            ${createFormGroup('Phone', createInput('tel', 'phone', 'Enter phone'))}
            <button type="submit" class="btn btn-primary w-full">Add User</button>
        </form>
    `;
    showModal('Add New User', content);
}

async function addUser(event) {
    event.preventDefault();
    const form = event.target;
    const userData = {
        name: form.name.value,
        email: form.email.value,
        phone: form.phone.value
    };
    try {
        await fetchAPI(ENDPOINTS.USERS, {
            method: 'POST',
            body: JSON.stringify(userData)
        });
        closeModal();
        loadUsers();
        showSuccess('User added successfully');
    } catch (error) {
        showError('Failed to add user');
    }
}

// Movie Management
function openAddMovieModal() {
    const content = `
        <form id="addMovieForm" onsubmit="addMovie(event)">
            ${createFormGroup('Title', createInput('text', 'title', 'Enter title'))}
            ${createFormGroup('Genre', createInput('text', 'genre', 'Enter genre'))}
            ${createFormGroup('Available Copies', createInput('number', 'copies', 'Enter number of copies'))}
            ${createFormGroup('Price Category', createSelect('priceCategory', [
                {value: '1', label: 'New Release'},
                {value: '2', label: 'Regular'},
                {value: '3', label: 'Classic'}
            ]))}
            <button type="submit" class="btn btn-primary w-full">Add Movie</button>
        </form>
    `;
    showModal('Add New Movie', content);
}

// Rental Management
function openAddRentalModal() {
    Promise.all([
        fetchAPI(ENDPOINTS.USERS),
        fetchAPI(ENDPOINTS.MOVIES)
    ]).then(([users, movies]) => {
        const content = `
            <form id="addRentalForm" onsubmit="addRental(event)">
                ${createFormGroup('User', createSelect('userId', 
                    users.map(user => ({value: user.id, label: user.name}))
                ))}
                ${createFormGroup('Movie', createSelect('movieId',
                    movies.filter(movie => movie.availableCopies > 0)
                        .map(movie => ({value: movie.id, label: `${movie.title} ($${movie.currentPrice})`}))
                ))}
                <button type="submit" class="btn btn-primary w-full">Create Rental</button>
            </form>
        `;
        showModal('New Rental', content);
    }).catch(error => {
        showError('Failed to load rental form data');
    });
}

// Notification Utilities
function showSuccess(message) {
    showNotification(message, 'success');
}

function showError(message) {
    showNotification(message, 'error');
}

function showNotification(message, type) {
    const alert = document.createElement('div');
    alert.className = `alert alert-${type} fixed top-4 right-4 z-50`;
    alert.innerHTML = message;
    document.body.appendChild(alert);
    setTimeout(() => alert.remove(), 5000);
}

// Initialize dashboard on page load
document.addEventListener('DOMContentLoaded', () => {
    loadDashboard();
});

// Format utilities
function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString();
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

// Loading states
function showLoading() {
    const spinner = document.createElement('div');
    spinner.className = 'spinner spinner-primary fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 z-50';
    document.body.appendChild(spinner);
    return () => spinner.remove();
}

// Error handling
window.onerror = function(msg, url, lineNo, columnNo, error) {
    showError('An error occurred: ' + msg);
    return false;
};

window.addEventListener('unhandledrejection', function(event) {
    showError('Promise error: ' + event.reason);
});
