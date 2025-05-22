import router from './router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { useAuthStore } from '@/stores/auth'
import { usePermissionStore } from '@/stores/permission'
import { useUserStore } from './stores/user'
import { USER_ID, INDEX_MAIN_PAGE_PATH } from '@/stores/erpConstant'
import { generateIndexRouter } from "@/utils/util"

NProgress.configure({ showSpinner: false })
const whiteList = ['/user/login', '/user/register', '/user/register-result']

router.beforeEach(async, (to, from) => {
    NProgress.start()
    const authStore = useAuthStore()
    const permissionStore = usePermissionStore()
    const userStore = useUserStore()
    // 从userStore获取用户ID
    const userId = userStore.userId
    if (userId) {
        // 已登录状态访问登录页重定向到首页
        if (to.path === '/' || to.path === '/user/login') {
            NProgress.done
            return {path: INDEX_MAIN_PAGE_PATH}
        }else {
            // 尚未获取权限列表
            if (permissionStore.permissionList.length === 0) {
                // 获取权限列表
                const  menuData = await permissionStore.getPermissionList()

            }
        }
    }
})
