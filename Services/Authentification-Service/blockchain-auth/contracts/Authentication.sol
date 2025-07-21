pragma solidity ^0.8.0;

contract Authentication {
    struct User { // Structure to store user information
        string username;
        bool exists;
    }

    mapping(address => User) private users; // Mapping from address to User
    mapping(string => bool) private usernames; // Mapping from username to check if username exists

    event UserRegistered(address indexed userAddress, string username);
    event UserLoggedIn(address indexed userAddress, string username);

    function registerUser(address _userAddress, string memory _username) public returns (bool) {
        require(bytes(_username).length > 0, "Username cannot be empty"); // Check if username is not empty
        require(!usernames[_username], "Username already exists"); // Check if username already exists
        require(!users[_userAddress].exists, "Address already registered"); // Check if address already has a user

        users[_userAddress] = User(_username, true); // Register the user
        usernames[_username] = true;
        emit UserRegistered(_userAddress, _username); // Emit event
        return true;
    }

    function userExists(address _userAddress) public view returns (bool) { // Function to check if user exists
        return users[_userAddress].exists;
    }

    function loginUser(address _userAddress) public view returns (string memory) {
        require(users[_userAddress].exists, "User does not exist"); // Check if user exists
        return users[_userAddress].username;
    }

    function usernameExists(string memory _username) public view returns (bool) {
        return usernames[_username];
    }
}