<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Custom Login Page</title>
    <link href="${url.resourcesPath}/css/styles.css" rel="stylesheet"/>
</head>
<body>
<div class="login-container">
    <img src="${url.resourcesPath}/img/logo.png" alt="Logo" class="logo"/>
    <form action="${url.loginAction}" method="post">
        <input type="text" id="username" name="username" placeholder="Username"/>
        <input type="password" id="password" name="password" placeholder="Password"/>
        <button type="submit">Login</button>
    </form>
</div>
<script src="${url.resourcesPath}/js/scripts.js"></script>
</body>
</html>