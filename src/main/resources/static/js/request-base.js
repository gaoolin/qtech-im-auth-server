// static/js/request-base.js

// 创建 axios 实例
const service = axios.create({
    timeout: 10000 // 默认超时时间
});

// 请求拦截器
service.interceptors.request.use(
    config => {
        // 从 localStorage 获取 token
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = 'Bearer ' + token;
        }
        config.headers['Content-Type'] = 'application/json;charset=UTF-8';
        return config;
    },
    error => {
        console.error('请求发送失败: ', error);
        return Promise.reject(error);
    }
);

// 响应拦截器
service.interceptors.response.use(
    response => {
        return response.data;
    },
    error => {
        console.error('接口响应错误: ', error);
        let msg = '请求失败，请稍后重试';
        if (error.response && error.response.data && error.response.data.message) {
            msg = error.response.data.message;
        }
        alert(msg);
        return Promise.reject(error);
    }
);

// 封装通用请求方法
const http = {
    get(url, params = {}) {
        return service.get(url, {params});
    },
    post(url, data = {}) {
        return service.post(url, data);
    },
    put(url, data = {}) {
        return service.put(url, data);
    },
    delete(url, data = {}) {
        return service.delete(url, {data});
    }
};

export default http;
