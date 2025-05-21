import { createPinia } from 'pinia'
import { createApp } from 'vue'

import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import App from './App.vue'
import router from './router'
import { VueAxios } from '@/utils/request'
import Storage from 'vue-lsp';

const pinia = createPinia()
const app = createApp(App)
pinia.use(piniaPluginPersistedstate) // 注册持久化插件

app.use(pinia)
app.use(Storage, {
    namespace: 'vuejs__', // key prefix
    name: 'ls', // name variable [ls] or [$ls],
    storage: 'local', // storage name session, local, memory
  }
)
app.use(router)
app.use(VueAxios) // 安装插件
app.use(Antd)
app.mount('#app')
