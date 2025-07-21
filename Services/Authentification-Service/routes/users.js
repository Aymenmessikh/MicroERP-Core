const express = require('express');
const router = express.Router();
const jwt = require('jsonwebtoken');
const blockchainService = require('../Services/blockchainService');

// Secret key for JWT (store in environment variables in production)
const JWT_SECRET = process.env.JWT_SECRET || '6fbe9c2f04e6fbc82a52d92a9834a17cb09f376abb914edb1ef9c1adf3043e51d82cd8572c91fa8cbd7a93ea6d6fc38b4e7c77d1883dfe0a833cf9c32f2bcfbf';
const JWT_EXPIRATION = '1h'; // Token expiration time

/* GET users listing. */
router.get('/', function(req, res, next) {
    res.send('respond with a resource');
});

// POST /users/register
router.post('/register', async (req, res) => {
    const { address, username } = req.body;

    if (!address || !username) {
        return res.status(400).json({ error: "Address and username are required" });
    }

    try {
        const tx = await blockchainService.registerUser(username, address);
        res.json({
            success: true,
            message: "User registered successfully",
            transactionHash: tx.transactionHash,
            address: address,
            username: username
        });
    } catch (err) {
        console.error("Registration route error:", err);
        res.status(500).json({ error: err.message });
    }
});

// POST /users/login
router.post('/login', async (req, res) => {
    const { address, signature, message } = req.body;

    if (!address || !signature || !message) {
        return res.status(400).json({ error: "Address, signature, and message are required" });
    }

    try {
        console.log("Login attempt with address:", address);
        const username = await blockchainService.loginUser(address, signature, message);

        // Generate JWT token
        const token = jwt.sign(
            {
                username: username,
                address: address,
            },
            JWT_SECRET,
            { expiresIn: JWT_EXPIRATION }
        );

        res.json({
            success: true,
            username,
            address,
            message: "Login successful",
            token
        });
    } catch (err) {
        console.error("Login route error:", err.message);
        res.status(401).json({ error: err.message });
    }
});

// New route to validate token (optional, mainly for testing)
router.post('/validate-token', (req, res) => {
    const authHeader = req.headers.authorization;

    if (!authHeader || !authHeader.startsWith('Bearer ')) {
        return res.status(401).json({ error: "No token provided" });
    }

    const token = authHeader.split(' ')[1];

    try {
        const decoded = jwt.verify(token, JWT_SECRET);
        res.json({
            valid: true,
            user: decoded
        });
    } catch (err) {
        res.status(401).json({
            valid: false,
            error: "Invalid token"
        });
    }
});

const fs = require("fs");
const path = require("path");

// Serve public key
router.get('/.well-known/jwks.json', (req, res) => {
    res.json({
        keys: [
            {
                kty: "oct", // ou "RSA" si tu veux basculer vers RSA
                alg: "HS256",
                k: Buffer.from(JWT_SECRET).toString('base64url') // Encode properly
            }
        ]
    });
});

module.exports = router;