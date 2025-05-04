// API endpoints
const API_BASE_URL = '/api';
const ENDPOINTS = {
    USERS: `${API_BASE_URL}/users`,
    MOVIES: `${API_BASE_URL}/movies`,
    RENTALS: `${API_BASE_URL}/rentals`,
    ACTIVE_RENTALS: `${API_BASE_URL}/active-rentals`
};

// Navigation
document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', (e) => {
        e.preventDefault();
        const page = e.target.closest('.nav-item').dataset.page;
        showPage(page);
        loadPageData(page);
    });
});

// Page loading functions
function loadPageData(page) {
    switch(page) {
        case 'dashboard':
            loadDashboard();
            break;
        case 'users':
            loadUsers();
            break;
        case 'movies':
            loadMovies();
            break;
        case 'rentals':
            loadRentals();
            break;
        case 'pricing':
            loadPricing();
            break;
        case 'reports':
            loadReports();
            break;
    }
}

// Dashboard
async function loadDashboard() {
    try {
        const [users, movies, activeRentals] = await Promise.all([
            fetch(ENDPOINTS.USERS).then(res => res.json()),
            fetch(ENDPOINTS.MOVIES).then(res => res.json()),
            fetch(ENDPOINTS.ACTIVE_RENTALS).then(res => res.json())
        ]);

        // Update stats
        document.querySelector('#totalMovies').textContent = movies.length;
        document.querySelector('#activeUsers').textContent = users.length;
        document.querySelector('#activeRentals').textContent = activeRentals.length;
        document.querySelector('#overdueRentals').textContent = 
            activeRentals.filter(rental => new Date(rental.dueDate) < new Date()).length;

        // Update recent rentals table
        const recentRentalsTable = document.querySelector('#recentRentalsTable tbody');
        recentRentalsTable.innerHTML = activeRentals
            .slice(0, 5)
            .map(rental => `
                <tr>
                    <td class="px-6 py-4 whitespace-nowrap">${rental.movieTitle}</td>
                    <td class="px-6 py-4 whitespace-nowrap">${rental.userName}</td>
                    <td class="px-6 py-4 whitespace-nowrap">${formatDate(rental.rentalDate)}</td>
                    <td class="px-6 py-4 whitespace-nowrap">${formatDate(rental.dueDate)}</td>
                    <td class="px-6 py-4 whitespace-nowrap">
                        <span class="px-2 py-1 text-sm ${getRentalStatusClass(rental)} rounded-full">
                            ${getRentalStatus(rental)}
                        </span>
                    </td>
                </tr>
            `).join('');
    } catch (error) {
        showError('Error loading dashboard data');
        console.error(error);
    }
}

// Users
async function loadUsers() {
    try {
        const users = await fetch(ENDPOINTS.USERS).then(res => res.json());
        const usersTable = document.querySelector('#usersTable tbody');
        usersTable.innerHTML = users.map(user => `
            <tr>
                <td class="px-6 py-4 whitespace-nowrap">${user.name}</td>
                <td class="px-6 py-4 whitespace-nowrap">${user.email}</td>
                <td class="px-6 py-4 whitespace-nowrap">${user.phone}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                    <button onclick="editUser(${user.id})" class="text-blue-600 hover:text-blue-800 mr-2">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button onclick="deleteUser(${user.id})" class="text-red-600 hover:text-red-800">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        showError('Error loading users');
        console.error(error);
    }
}

// Movies
async function loadMovies() {
    try {
        const movies = await fetch(ENDPOINTS.MOVIES).then(res => res.json());
        const moviesTable = document.querySelector('#moviesTable tbody');
        moviesTable.innerHTML = movies.map(movie => `
            <tr>
                <td class="px-6 py-4 whitespace-nowrap">${movie.title}</td>
                <td class="px-6 py-4 whitespace-nowrap">${movie.genre}</td>
                <td class="px-6 py-4 whitespace-nowrap">${movie.availableCopies}</td>
                <td class="px-6 py-4 whitespace-nowrap">$${movie.currentPrice}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                    <button onclick="editMovie(${movie.id})" class="text-blue-600 hover:text-blue-800 mr-2">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button onclick="deleteMovie(${movie.id})" class="text-red-600 hover:text-red-800">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        showError('Error loading movies');
        console.error(error);
    }
}

// Rentals
async function loadRentals() {
    try {
        const rentals = await fetch(ENDPOINTS.RENTALS).then(res => res.json());
        const rentalsTable = document.querySelector('#rentalsTable tbody');
        rentalsTable.innerHTML = rentals.map(rental => `
            <tr>
                <td class="px-6 py-4 whitespace-nowrap">${rental.movieTitle}</td>
                <td class="px-6 py-4 whitespace-nowrap">${rental.userName}</td>
                <td class="px-6 py-4 whitespace-nowrap">${formatDate(rental.rentalDate)}</td>
                <td class="px-6 py-4 whitespace-nowrap">${formatDate(rental.dueDate)}</td>
                <td class="px-6 py-4 whitespace-nowrap">$${rental.totalPrice}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                    ${!rental.returnDate ? `
                        <button onclick="returnMovie(${rental.id})" class="text-green-600 hover:text-green-800">
                            <i class="fas fa-undo"></i> Return
                        </button>
                    ` : `Returned ${formatDate(rental.returnDate)}`}
                </td>
            </tr>
        `).join('');
    } catch (error) {
        showError('Error loading rentals');
        console.error(error);
    }
}

// Utility functions
function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString();
}

function getRentalStatus(rental) {
    if (rental.returnDate) return 'Returned';
    return new Date(rental.dueDate) < new Date() ? 'Overdue' : 'Active';
}

function getRentalStatusClass(rental) {
    if (rental.returnDate) return 'text-gray-600 bg-gray-100';
    return new Date(rental.dueDate) < new Date() 
        ? 'text-red-600 bg-red-100' 
        : 'text-green-600 bg-green-100';
}

function showError(message) {
    const alert = document.createElement('div');
    alert.className = 'fixed top-4 right-4 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded';
    alert.role = 'alert';
    alert.innerHTML = `
        <strong class="font-bold">Error!</strong>
        <span class="block sm:inline">${message}</span>
    `;
    document.body.appendChild(alert);
    setTimeout(() => alert.remove(), 5000);
}

// Initialize dashboard on page load
document.addEventListener('DOMContentLoaded', () => {
    loadDashboard();
});
