import { defineStore } from "pinia";
import { ref } from "vue";


export const useAuthStore = defineStore("auth", ()=>{
    const accessToken = ref('')
    console.log('Store initialized. Current token:', accessToken.value)

    const setToken = (newToken) => {
        accessToken.value = newToken
    }

    const removeToken = () => {
        accessToken.value = ''
    }

    return {
        accessToken, setToken, removeToken
    }
}, {
    persist: true
})