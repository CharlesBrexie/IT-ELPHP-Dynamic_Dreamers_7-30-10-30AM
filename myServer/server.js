const express = require('express');
const sqlite3 = require('sqlite3').verbose();
const bodyParser = require('body-parser');
const app = express();
const PORT = 3000;

// Middleware
app.use(bodyParser.json());

// Connect to SQLite database
const db = new sqlite3.Database('./database.db', (err) => {
    if (err) {
        console.error('Could not connect to database', err);
    } else {
        console.log('Connected to SQLite database');
    }
});

// Example endpoint to fetch data from the database
app.get('/data', (req, res) => {
    db.all('SELECT * FROM my_table', [], (err, rows) => {
        if (err) {
            res.status(400).json({ error: err.message });
            return;
        }
        res.json({ data: rows });
    });
});

// Login endpoint
app.post('/login', (req, res) => {
    const { email, password } = req.body; // Get the email and password from the request body

    if (!email || !password) {
        return res.status(400).json({ message: 'Email and password are required' });
    }

    // Query to find user with matching email and password
    db.get('SELECT * FROM users WHERE email = ? AND password = ?', [email, password], (err, row) => {
        if (err) {
            res.status(500).json({ error: 'Database error' });
            return;
        }
        if (row) {
            // User found, successful login
            res.json({ success: true, message: 'Login successful' });
        } else {
            // User not found or incorrect credentials
            res.status(401).json({ success: false, message: 'Invalid credentials' });
        }
    });
});

// Start the server
app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});
