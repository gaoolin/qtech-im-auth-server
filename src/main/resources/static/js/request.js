// 双 Token 版 request.js
// 功能：
//
// 自动在请求头中携带 Access Token
// 自动处理 401 错误，使用 Refresh Token 续签 Access Token
// 多请求并发情况下自动队列，防止重复刷新
// 刷新失败时自动跳转登录
import axios from 'axios';

const baseURL = '/api';
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
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    window.location.href = '/login';
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
        if (response && response.status === 401 && !config._retry) {
            if (!isRefreshing) {
                isRefreshing = true;
                const refreshToken = getRefreshToken();

                if (!refreshToken) {
                    redirectToLogin();
                    return Promise.reject(error);
                }

                return axios
                    .post(`${baseURL}/auth/refresh`, {refreshToken})
                    .then((res) => {
                        const {accessToken} = res.data;
                        setAccessToken(accessToken);
                        requestQueue.forEach((cb) => cb(accessToken));
                        requestQueue = [];
                        isRefreshing = false;
                        config._retry = true;
                        config.headers['Authorization'] = `Bearer ${accessToken}`;
                        return instance(config);
                    })
                    .catch((err) => {
                        isRefreshing = false;
                        redirectToLogin();
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
        }

        return Promise.reject(error);
    }
);

export default instance;
