import UserLayout from '@/components/layouts/UserLayout.vue'
import TabLayout from '@/components/layouts/TabLayout.vue'

// 基础路由
export const constantRouterMap = [
    {
        path: '/user',
        component: UserLayout,
        redirect: '/user/login',
        hidden: true,
        children: [
            {
                path: 'login',
                name: 'login',
                component: () => import('@/views/user/Login.vue')
            },
            {
                path: 'register',
                name: 'register',
                component: () => import('@/views/user/Register.vue')
            }
        ]

    },
]

// 异步路由，走权限控制
export const asyncRouterMap = [
     {
        path: '/',
        name: 'dashboard',
        component: TabLayout,
        meta: {title: '首页'},
        redirect: '/dashboard/analysis',
        children: [

        ]
        
    }
]