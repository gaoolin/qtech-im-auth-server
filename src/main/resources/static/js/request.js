// 双 Token 版 request.js
// 功能：
//
// 自动在请求头中携带 Access Token
// 自动处理 401 错误，使用 Refresh Token 续签 Access Token
// 多请求并发情况下自动队列，防止重复刷新
// 刷新失败时自动跳转登录
import axios from './axios.min.js'

const baseURL = '/';
const instance = axios.create({
    baseURL,
    timeout: 10000,
});

let isRefreshing = false;
let requestQueue = [];

// 获取 Token
const getAccessToken = () => localStorage.getItem('access_token');
const getRefreshToken = () => localStorage.getItem('refresh_token');

// 更新 Token
const setAccessToken = (token) => localStorage.setItem('access_token', token);

// 重定向到登录
const redirectToLogin = () => {
    console.error('Token expired or invalid, redirecting to login...');
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    window.location.href = '/login';
};

// 日志记录
const logError = (message, error) => {
    console.error(message, error);
};

// 刷新 Token 的核心逻辑
const refreshAccessToken = () => {
    const refreshToken = getRefreshToken();
    if (!refreshToken) {
        logError('Refresh Token not found, redirecting to login...');
        redirectToLogin();
        return Promise.reject('No refresh token available');
    }

    return axios
        .post(`${baseURL}/auth/refresh`, {refreshToken})
        .then((res) => {
            const {accessToken} = res.data;
            setAccessToken(accessToken);
            return accessToken;
        })
        .catch((err) => {
            logError('Failed to refresh access token', err);
            redirectToLogin();
            return Promise.reject(err);
        });
};

// 请求拦截器
instance.interceptors.request.use(
    (config) => {
        const token = getAccessToken();
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// 响应拦截器
instance.interceptors.response.use(
    (response) => response,
    (error) => {
        const {config, response} = error;

        // 如果响应状态码为 401 且请求未重试过
        if (response && response.status === 401 && !config._retry) {
            if (!isRefreshing) {
                isRefreshing = true;

                return refreshAccessToken()
                    .then((newAccessToken) => {
                        requestQueue.forEach((cb) => cb(newAccessToken));
                        requestQueue = [];
                        isRefreshing = false;

                        config._retry = true;
                        config.headers['Authorization'] = `Bearer ${newAccessToken}`;
                        return instance(config);
                    })
                    .catch((err) => {
                        isRefreshing = false;
                        return Promise.reject(err);
                    });
            }

            // 如果正在刷新 Token，将请求加入队列
            return new Promise((resolve) => {
                requestQueue.push((newAccessToken) => {
                    config._retry = true;
                    config.headers['Authorization'] = `Bearer ${newAccessToken}`;
                    resolve(instance(config));
                });
            });
        }

        // 其他错误直接返回
        return Promise.reject(error);
    }
);

export default instance;