// 双 Token 版 request.js
// 功能：
//
// 自动在请求头中携带 Access Token
// 自动处理 401 错误，使用 Refresh Token 续签 Access Token
// 多请求并发情况下自动队列，防止重复刷新
// 刷新失败时自动跳转登录
// 这里直接使用本地的 axios（不需要 import/export）

const baseURL = '/admin';
const instance = axios.create({
    baseURL,
    timeout: 10000,
    withCredentials: true // 保证发送请求时自动带上 Cookie
});

let isRefreshing = false;
let requestQueue = [];

const redirectToLogin = () => {
    console.warn('登录状态过期，正在跳转登录页...');
    window.location.href = '/admin/login';
};

// Token 刷新逻辑（调用后端 /refresh 获取新 Cookie）
const refreshToken = () => {
    return instance.post('/refresh')
        .then((res) => {
            // 后端设置新的 Cookie，这里不需要做任何处理
            return Promise.resolve();
        })
        .catch((err) => {
            requestQueue = [];
            redirectToLogin();
            return Promise.reject(err);
        });
};

// 请求拦截器（可以添加 loading、请求日志等）
instance.interceptors.request.use(
    (config) => {
        // 你可以根据需要添加统一 headers 或日志
        return config;
    },
    (error) => Promise.reject(error)
);

// 响应拦截器
instance.interceptors.response.use(
    (response) => response.data, // 默认返回数据字段
    (error) => {
        const {config, response} = error;

        if (!response) return Promise.reject(error);

        if (response.status === 401) {
            if (!isRefreshing) {
                isRefreshing = true;

                return refreshToken()
                    .then(() => {
                        requestQueue.forEach((cb) => cb());
                        requestQueue = [];
                        isRefreshing = false;

                        // retry 原请求
                        config._retry = true;
                        return instance(config);
                    })
                    .catch((err) => {
                        isRefreshing = false;
                        return Promise.reject(err);
                    });
            }

            // 多个请求等待刷新完成后再发送
            return new Promise((resolve) => {
                requestQueue.push(() => {
                    config._retry = true;
                    resolve(instance(config));
                });
            });
        }

        if (response.status === 403) {
            redirectToLogin();
        }

        return Promise.reject(error);
    }
);

// 封装统一请求方法
const request = {
    get: (url, params) => instance.get(url, {params}),
    post: (url, data) => instance.post(url, data),
    put: (url, data) => instance.put(url, data),
    delete: (url, params) => instance.delete(url, {params})
};

// 全局暴露，页面使用 window.request
window.request = request;
