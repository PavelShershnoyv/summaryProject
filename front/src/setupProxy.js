const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/ums',
    createProxyMiddleware({
      target: 'http://localhost:8084',
      changeOrigin: true,
    })
  );
  app.use(
    '/mss',
    createProxyMiddleware({
      target: 'http://localhost:8082',
      changeOrigin: true,
    })
  );
};
