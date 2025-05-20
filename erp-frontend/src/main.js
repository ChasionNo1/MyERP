import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import App from './App.vue'
import router from './router'
const app = createApp(App)

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate) // 注册持久化插件

app.use(pinia)
app.use(router)
app.use(Antd)

app.mount('#app')
