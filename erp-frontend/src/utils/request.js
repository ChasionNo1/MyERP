// 封装请求
import axios from "axios";
import { Modal, notification } from "ant-design-vue";
import { ACCESS_TOKEN } from "@/stores/erpConstant";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore()

/**
 * 指定 axios 的 baseURL
 */
// let apiBaseUrl = window._CONFIG["domainURL"] || "/";
let apiBaseUrl = '/api'

// 创建 axios 实例
const service = axios.create({
  baseURL: apiBaseUrl,
  timeout: 300000,
});

const err = (error) => {
  if (error.response) {
    let data = error.response.data;
    // 这里使用原生态
    const token = localStorage.getItem(ACCESS_TOKEN);
    // 判断错误类型
    switch (error.response.status) {
      case 403:
        notification.error({ message: "系统提示", description: "拒绝访问", duration: 4 });
        break;
      case 500:
        if (token && data === "loginOut") {
          Modal.error({
            title: "登录已过期",
            content: "很抱歉，登录已过期，请重新登录",
            okText: "重新登录",
            mask: false,
            onOk: () => {
              localStorage.removeItem(ACCESS_TOKEN); // 使用原生 API 替代 Vue.ls
              window.location.reload();
            },
          });
        }
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
        notification.error({ message: "系统提示", description: "未授权，请重新登录", duration: 4 });
        if (token) {
          // 这里的store是状态管理库，通过store.dispatch调用名为Logout的方法，
          // dispatch一般用于触发状态管理库中的某个动作。then方法会在Logout动作成功执行后被调用，
          // 在then的回调函数中，使用setTimeout设置一个 1500 毫秒（1.5 秒）的延迟，
          // 延迟结束后，通过window.location.reload()重新加载当前页面。
          userStore.dispatch("Logout").then(() => {
            setTimeout(() => {
              window.location.reload();
            }, 1500);
          });
        }
        break;
      default:
        notification.error({
          message: "系统提示",
          description: data.message,
          duration: 4,
        });
        break;
    }
  }
  // 这段代码的作用是将错误继续传递给下一个错误处理函数，确保整个 Promise 链能够正确处理异常。
  return Promise.reject(error);
};

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 如果token存在，则在请求头上加上
    const token = localStorage.getItem(ACCESS_TOKEN);
    if (token) {
      config.headers["X-Access-Token"] = token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use((response) => {
  return response.data;
}, err);

// 安装插件
const VueAxios = {
  install(app) {
    app.config.globalProperties.$axios = service;
    app.config.globalProperties.$http = service;
  },
};

export {
    VueAxios,
    service as axios
}
