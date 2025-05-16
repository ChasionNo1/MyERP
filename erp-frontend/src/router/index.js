import { createRouter, createWebHistory } from 'vue-router'
import { constantRouterMap } from '@/config/router.config'

// 创建路由实例
const router = createRouter({
  // 应用的基础路径，通常从环境变量中获取（如 /app/）
  history: createWebHistory(import.meta.env.BASE_URL),
  // 每次路由切换后，自动滚动到页面顶部。
  scrollBehavior: () => ({y:0}),
  routes: constantRouterMap
})

// 处理路由跳转错误（避免重复导航时的控制台报错）
const originalPush = router.push
router.push = function push(location) {
  return originalPush.call(this, location).catch(err => {
    // 处理重复跳转同一路由的错误
    if (err.name !== 'NavigationDuplicated') {
      throw err
    }
    return err
  })
}

export default router
