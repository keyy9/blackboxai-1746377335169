// Load initial data
async function loadData() {
    try {
        const response = await fetch('/data/movies.json');
        const data = await response.json();
        localStorage.setItem('movieRentalData', JSON.stringify(data));
        updateAll();
    } catch (error) {
        showNotification('Error loading data', 'error');
        console.error('Error:', error);
    }
}

// Data management
function getData() {
    const data = localStorage.getItem('movieRentalData');
    return data ? JSON.parse(data) : { movies: [], users: [], rentals: [] };
}

function saveData(data) {
    localStorage.setItem('movieRentalData', JSON.stringify(data));
}

// UI Updates
function updateDashboard() {
    const data = getData();

    // Update stats
    document.getElementById('totalMovies').textContent = data.movies.length;
    document.getElementById('activeUsers').textContent = data.users.length;
    document.getElementById('activeRentals').textContent =
        data.rentals.filter(r => r.status === 'active').length;
    document.getElementById('overdueRentals').textContent =
        data.rentals.filter(r => isOverdue(r)).length;

    // Update recent rentals
    const rentalsTable = document.querySelector('#recentRentalsTable tbody');
    rentalsTable.innerHTML = data.rentals.map(rental => {
        const movie = data.movies.find(m => m.id === rental.movieId);
        const user = data.users.find(u => u.id === rental.userId);
        const status = isOverdue(rental) ? 'overdue' : rental.status;
        return `
            <tr>
                <td class="table-cell">${movie.title}</td>
                <td class="table-cell">${user.name}</td>
                <td class="table-cell">${formatDate(rental.date)}</td>
                <td class="table-cell">
                    <span class="status-badge status-${status}">
                        ${status.charAt(0).toUpperCase() + status.slice(1)}
                    </span>
                </td>
            </tr>
        `;
    }).join('');
}

function updateMovies() {
    const data = getData();
    const movieGrid = document.querySelector('#movieGrid');
    movieGrid.innerHTML = data.movies.map(movie => `
        <div class="movie-card">
            <img src="${movie.image}" alt="${movie.title}" class="movie-image">
            <div class="content">
                <h3 class="text-xl font-semibold mb-2">${movie.title}</h3>
                <p class="text-gray-600 mb-4">${movie.genre}</p>
                <div class="flex justify-between items-center">
                    <span class="${movie.available ? 'text-green-600' : 'text-red-600'}">
                        ${movie.available ? 'Available' : 'Rented'}
                    </span>
                    <button onclick="rentMovie(${movie.id})"
                            class="btn btn-primary"
                            ${!movie.available ? 'disabled' : ''}>
                        Rent ($${movie.price})
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

function updateUsers() {
    const data = getData();
    const usersTable = document.querySelector('#usersTable tbody');
    usersTable.innerHTML = data.users.map(user => `
        <tr>
            <td class="table-cell">${user.name}</td>
            <td class="table-cell">${user.email}</td>
            <td class="table-cell">${user.activeRentals}</td>
            <td class="table-cell">
                <button onclick="editUser(${user.id})" class="text-blue-600 hover:text-blue-800 mr-2">
                    <i class="fas fa-edit"></i>
                </button>
                <button onclick="deleteUser(${user.id})" class="text-red-600 hover:text-red-800">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

// Actions
function rentMovie(movieId) {
    const data = getData();
    const movie = data.movies.find(m => m.id === movieId);

    if (movie && movie.available) {
        movie.available = false;
        const rental = {
            id: data.rentals.length + 1,
            movieId: movieId,
            userId: 1, // Default to first user for demo
            date: new Date().toISOString().split('T')[0],
            dueDate: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
            status: 'active'
        };

        data.rentals.push(rental);
        const user = data.users.find(u => u.id === 1);
        user.activeRentals++;

        saveData(data);
        updateAll();
        showNotification('Movie rented successfully!');
    }
}

function returnMovie(rentalId) {
    const data = getData();
    const rental = data.rentals.find(r => r.id === rentalId);

    if (rental && rental.status === 'active') {
        rental.status = 'returned';
        rental.returnDate = new Date().toISOString().split('T')[0];

        const movie = data.movies.find(m => m.id === rental.movieId);
        movie.available = true;

        const user = data.users.find(u => u.id === rental.userId);
        user.activeRentals--;

        saveData(data);
        updateAll();
        showNotification('Movie returned successfully!');
    }
}

// Utility functions
function formatDate(dateString) {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
}

function isOverdue(rental) {
    return rental.status === 'active' && new Date(rental.dueDate) < new Date();
}

function showNotification(message, type = 'success') {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    document.body.appendChild(notification);
    setTimeout(() => notification.remove(), 3000);
}

function updateAll() {
    updateDashboard();
    updateMovies();
    updateUsers();
}

// Navigation
document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', (e) => {
        e.preventDefault();
        const pageId = e.currentTarget.dataset.page;

        // Update navigation
        document.querySelectorAll('.nav-item').forEach(nav => {
            nav.classList.remove('active');
        });
        e.currentTarget.classList.add('active');

        // Show selected page
        document.querySelectorAll('.page').forEach(page => {
            page.classList.add('hidden');
        });
        document.getElementById(pageId).classList.remove('hidden');
    });
});

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    loadData();
});
