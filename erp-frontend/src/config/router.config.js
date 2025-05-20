import UserLayout from '@/components/layouts/UserLayout.vue'
import IndexPage from '@/views/IndexPage.vue'

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
    {
        path: '/',
        component: IndexPage
    }
]