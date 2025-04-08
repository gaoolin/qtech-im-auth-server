// 双 Token 版 request.js
// 功能：
//
// 自动在请求头中携带 Access Token
// 自动处理 401 错误，使用 Refresh Token 续签 Access Token
// 多请求并发情况下自动队列，防止重复刷新
// 刷新失败时自动跳转登录
// 这里直接使用本地的 axios（不需要 import/export）

const baseURL = '/auth';
const instance = axios.create({
    baseURL,
    timeout: 10000,
});

let isRefreshing = false;
let requestQueue = [];

// 重定向到登录
const redirectToLogin = () => {
    console.error('Token expired or invalid, redirecting to login...');
    window.location.href = '/auth/login';
};

// 刷新 Token 方法（如果需要刷新，可以保留逻辑）
const refreshAccessToken = () => {
    return instance.post('/refresh')  // 服务端从 session 刷新
        .then((res) => {
            const {refreshToken} = res.data;
            window.accessToken = refreshToken;  // 更新全局变量
            return refreshToken;
        })
        .catch((err) => {
            requestQueue = [];
            redirectToLogin();
            return Promise.reject(err);
        });
};

// 请求拦截器
instance.interceptors.request.use(
    (config) => {
        const token = window.accessToken;
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// 响应拦截器
instance.interceptors.response.use(
    (response) => response.data,
    (error) => {
        const {config, response} = error;
        if (response) {
            switch (response.status) {
                case 401:
                    if (!isRefreshing) {
                        isRefreshing = true;
                        return refreshAccessToken()
                            .then((newToken) => {
                                requestQueue.forEach((cb) => cb(newToken));
                                requestQueue = [];
                                isRefreshing = false;

                                config._retry = true;
                                config.headers['Authorization'] = `Bearer ${newToken}`;
                                return instance(config);
                            })
                            .catch((err) => {
                                isRefreshing = false;
                                return Promise.reject(err);
                            });
                    }

                    return new Promise((resolve) => {
                        requestQueue.push((newToken) => {
                            config._retry = true;
                            config.headers['Authorization'] = `Bearer ${newToken}`;
                            resolve(instance(config));
                        });
                    });
                    break;
                case 403:
                    showToast('Access denied', 'danger');
                    return Promise.reject(error);
                default:
                    return Promise.reject(error);
            }
        }
        return Promise.reject(error);
    }
);

// 封装统一请求方法
const request = {
    get: (url, params) => instance.get(url, {params}),
    post: (url, data) => instance.post(url, data),
    put: (url, data) => instance.put(url, data),
    delete: (url, params) => instance.delete(url, {params}),
};

// 挂到全局 window 下，方便页面直接调用
window.request = request;
