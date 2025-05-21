// 登录接口
import {service} from '@/utils/request'; // 从request.js导入service实例

/**
 * login func
 * parameter: {
 *     username: '',
 *     password: '',
 *     remember_me: true,
 *     captcha: '12345'
 * }
 * @param parameter
 * @returns {*}
 */

export const login = (parameter) => {
    return service({
        url: '/user/login',
        method: 'post',
        data: parameter
    })
}

export const logout = () => {
    return service({
        url: '/user/logout',
        method: 'get'
    })
}
