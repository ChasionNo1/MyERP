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
        // if (token && message === "loginOut") {
        //   Modal.error({
        //     title: "登录已过期",
        //     content: "很抱歉，登录已过期，请重新登录",
        //     okText: "重新登录",
        //     mask: false,
        //     onOk: () => {
        //       localStorage.removeItem(ACCESS_TOKEN); // 使用原生 API 替代 Vue.ls
        //       window.location.reload();
        //     },
        //   });
        // }
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
        // notification.error({ message: "系统提示", description: "未授权，请重新登录", duration: 4 });
        // if (token) {
        //   // 这里的store是状态管理库，通过store.dispatch调用名为Logout的方法，
        //   // dispatch一般用于触发状态管理库中的某个动作。then方法会在Logout动作成功执行后被调用，
        //   // 在then的回调函数中，使用setTimeout设置一个 1500 毫秒（1.5 秒）的延迟，
        //   // 延迟结束后，通过window.location.reload()重新加载当前页面。
        //   userStore.dispatch("Logout").then(() => {
        //     setTimeout(() => {
        //       window.location.reload();
        //     }, 1500);
        //   });
        // }

        // 401的处理，先进行access token刷新
        if (message === "访问令牌已过期，请刷新" && !originalRequest._retry) {
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
    service = axios.create({
      baseURL: '/api',
      timeout: 300000,
      withCredentials: true
    });

    // 请求拦截器
    service.interceptors.request.use(config => {
      const authStore = useAuthStore();
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

export { VueAxios };