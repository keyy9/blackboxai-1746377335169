// Test script to verify all components are loaded and working
document.addEventListener('DOMContentLoaded', async () => {
    console.log('Running system tests...');
    
    // Test 1: Check if all required files are loaded
    const tests = [
        { name: 'Styles loaded', test: () => document.querySelector('link[href="css/styles.css"]') !== null },
        { name: 'App.js loaded', test: () => typeof loadData === 'function' },
        { name: 'Help.js loaded', test: () => typeof HelpSystem === 'function' },
        { name: 'Movies data accessible', test: async () => {
            try {
                const response = await fetch('data/movies.json');
                const data = await response.json();
                return data.movies && Array.isArray(data.movies);
            } catch (e) {
                return false;
            }
        }}
    ];

    // Run tests
    console.log('Test Results:');
    for (const test of tests) {
        try {
            const result = await test.test();
            console.log(`${test.name}: ${result ? '✅ PASS' : '❌ FAIL'}`);
        } catch (e) {
            console.log(`${test.name}: ❌ FAIL (Error: ${e.message})`);
        }
    }

    // Test UI components
    console.log('\nUI Components:');
    const uiTests = [
        { name: 'Navigation', selector: '.nav-item' },
        { name: 'Dashboard', selector: '#dashboard' },
        { name: 'Movies Grid', selector: '#movieGrid' },
        { name: 'Help Button', selector: '#helpButton' }
    ];

    for (const test of uiTests) {
        const element = document.querySelector(test.selector);
        console.log(`${test.name}: ${element ? '✅ Present' : '❌ Missing'}`);
    }
});
