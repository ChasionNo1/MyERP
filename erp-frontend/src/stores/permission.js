// 过滤账户是否拥有某一个权限，并将菜单从加载列表中移除
import { defineStore } from "pinia";

export const usePermissionStore = defineStore('permission', {
    state: () => ({
        permissionList: [],
        addRouters: [],
    }),

    actions: {
        
    }
})