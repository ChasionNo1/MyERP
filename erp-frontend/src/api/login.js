// 登录接口
import {axios} from '@/utils/request'

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
    return axios({
        url: '/user/login',
        method: 'post',
        data: parameter
    })
}

export const logout = () => {
    return axios({
        url: '/user/logout',
        method: 'get'
    })
}
