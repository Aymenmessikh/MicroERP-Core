const Web3 = require("web3").default;

const web3 = new Web3("http://localhost:7545"); // Ganache

const contractJson = require('../blockchain-auth/build/contracts/Authentication.json');
const contractABI = contractJson.abi;
const contractAddress = "0x5b622fc17750cF182131fA6d478709f71Ba8A510";

const contract = new web3.eth.Contract(contractABI, contractAddress);

// Check if username exists
async function usernameExists(username) {
    try {
        return await contract.methods.usernameExists(username).call();
    } catch (err) {
        console.error("Error checking username:", err);
        throw new Error("Failed to check username");
    }
}

// Register user with username and address
async function registerUser(username, address) {
    try {
        // First check if username already exists
        const exists = await usernameExists(username);
        if (exists) {
            throw new Error("Username already exists");
        }

        // Get accounts from the connected node
        const accounts = await web3.eth.getAccounts();

        // Register the user (IMPORTANT: We'll register the ADDRESS parameter as the user)
        // This ensures the address passed in is the one that gets registered
        const result = await contract.methods.registerUser(address, username).send({
            from: accounts[0], // Use the first account as the transaction sender (admin)
            gas: 3000000
        });

        console.log("User registered successfully:", {
            address: address,
            username: username,
            transaction: result.transactionHash
        });

        return result;
    } catch (err) {
        console.error("Registration error:", err);
        throw new Error(err.message || "Failed to register user");
    }
}

// Verify signature
async function verifySignature(message, signature, address) {
    try {
        // Recover the address from the signature
        const recoveredAddress = web3.eth.accounts.recover(message, signature);

        // Compare the recovered address with the claimed address
        if (recoveredAddress.toLowerCase() !== address.toLowerCase()) {
            throw new Error("Signature verification failed");
        }

        return true;
    } catch (err) {
        console.error("Signature verification error:", err);
        throw new Error("Invalid signature");
    }
}

// Login user with address and signature
async function loginUser(address, signature, message) {
    try {
        console.log("Attempting to login with address:", address);

        // First verify the signature
        await verifySignature(message, signature, address);

        // Then check if the user exists
        const userExists = await contract.methods.userExists(address).call();
        if (!userExists) {
            throw new Error("User does not exist");
        }

        const username = await contract.methods.loginUser(address).call();
        console.log("Login successful for:", address, "Username:", username);

        return username;
    } catch (err) {
        console.error("Login error:", err);
        throw new Error(err.message || "User not found or login failed");
    }
}

module.exports = {
    registerUser,
    loginUser,
    usernameExists,
    verifySignature
};