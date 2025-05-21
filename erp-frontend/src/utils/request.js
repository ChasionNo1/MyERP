// 封装请求
import axios from "axios";
import { notification } from "ant-design-vue";
import { useAuthStore } from "@/stores/auth";

/**
 * 指定 axios 的 baseURL
 */
// let apiBaseUrl = window._CONFIG["domainURL"] || "/";
// let apiBaseUrl = "/api";
// 确保 CORS 配置允许 credentials：
// axios.defaults.withCredentials = true
// ✅ 直接创建实例，无需等待插件安装

const err = async (error) => {
  if (error.response) {
    let message = error.response.message;
    const originalRequest = error.config;
    // 判断错误类型，根据响应码
    switch (error.response.status) {
      case 403:
        notification.error({ message: "系统提示", description: "拒绝访问", duration: 4 });
        break;
      case 500:
        notification.error({ message: "系统提示", description: "服务器异常", duration: 4 });
        break;
      case 404:
        notification.error({
          message: "系统提示",
          description: "很抱歉，资源未找到!",
          duration: 4,
        });
        break;
      case 504:
        notification.error({ message: "系统提示", description: "网络超时" });
        break;
      case 401:
        // 401的处理，先进行access token刷新
        if (message === "缺少token" && !originalRequest._retry) {
          try {
            originalRequest._retry = true;
            // 刷新access token
            await axios.post("/refresh/token", {}, { withCredentials: true });
            // 重试原请求
            return service(originalRequest);
          } catch (refreshError) {
            window.location.href = "/login";
            return Promise.reject(refreshError);
          }
        } else if (
          (message === "刷新令牌无效" || message == "未提供刷新令牌") &&
          !originalRequest._retry
        ) {
          // 这里就直接重新登录了
          window.location.href = "/login";
        }
        break;
      default:
        notification.error({
          message: "系统提示",
          description: message,
          duration: 4,
        });
        break;
    }
  }
  // 这段代码的作用是将错误继续传递给下一个错误处理函数，确保整个 Promise 链能够正确处理异常。
  return Promise.reject(error);
};

let service = null;

const VueAxios = {
  install(app) {
    // 在插件安装时获取 authStore，确保在 Vue 上下文内
    const authStore = useAuthStore();
    service = axios.create({
      baseURL: '/api',
      timeout: 300000,
      withCredentials: true
    });

    // 请求拦截器
    service.interceptors.request.use(config => {
      const token = authStore.accessToken;
      console.log("Interceptor Token:", token);
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    }, error => Promise.reject(error));

    // 响应拦截器
    service.interceptors.response.use(
      response => response.data,
      err
    );

    // 挂载到全局属性
    app.config.globalProperties.$axios = service;
    app.config.globalProperties.$http = service;
  }
};

export { VueAxios, service };