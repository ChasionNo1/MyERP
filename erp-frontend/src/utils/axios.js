// Vue 插件，将axios实例注入到vue应用中

const VueAxios = {
    install(app, instance) {
        if (!instance) {
            console.error('你尚未安装axios')
            return
        }

        // 全局注册 axios实例
        app.config.globalProperties.$axios = instance
        app.config.globalProperties.$http = instance

    }
}

export {VueAxios}