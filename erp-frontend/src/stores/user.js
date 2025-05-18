// 定义状态库,全局共享数据
import { getAction } from "@/api/manage";
import { defineStore } from "pinia";
import { welcome } from "@/utils/util";
import { login } from "@/api/login"
export const useUserStore = defineStore("user", {
  // 定义state
  state: () => ({
    token: "",
    username: "",
    realname: "",
    welcome: "",
    avatar: "",
    permissionList: [],
    info: "",
  }),

  actions: {
    // cas 验证登录
    async validateLogin(params) {
      try {
        const response = await getAction("/cas/client/validateLogin", params);
        console.log("---------cas login---------", response);
        if (response.success) {
          const result = response.result;
          const userInfo = result.userInfo;
          this.token = result.token;
          this.info = userInfo;
          this.username = userInfo.username;
          this.realname = userInfo.realname;
          this.welcome = welcome();
          this.avatar = userInfo.avatar;
        }
        return response;
      } catch (error) {
        // Promise.reject(error)
        // / 方案1：返回错误信息（推荐）
        return { error };
      }
    },
  },

  // 登录
  async login(params) {
    try {
      const response = await login(params);
      if (response.code === 200) {
        if (response.data.msgTip === "user can login") {
          const result = response.data;
          // 进行缓存操作
          this.token = result.token;
        }
        this.info = params;
      }
      return response;
    } catch (error) {
      return { error };
    }
  },
});
