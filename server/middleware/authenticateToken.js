const jwt = require('jsonwebtoken');

function authenticateToken(req, res, next) {
    // Get the token from the request headers
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];

    // Check if the token exists
    if (!token) {
        return res.status(401).json({ error: 'Access token not found' });
    }

    try {
        // Verify the token using the secret key
        const secretKey = process.env.JWT_SECRET_KEY; // Use environment variable for secret key
        if (!secretKey) {
            throw new Error('JWT secret key not found');
        }

        // Verify the token and attach decoded payload to the request object
        req.user = jwt.verify(token, secretKey);

        // Proceed to the next middleware or route handler
        next();
    } catch (err) {
        // Token verification failed
        return res.status(403).json({ error: 'Invalid token' });
    }
}

module.exports = authenticateToken;
