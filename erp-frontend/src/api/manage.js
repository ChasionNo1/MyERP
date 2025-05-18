// 统一封装各种请求
import {axios} from '@/utils/request'

// 定义各种接口的访问路径
const api = {
  user: '/api/user',
  role: '/api/role',
  service: '/api/service',
  permission: '/api/permission',
  permissionNoPager: '/api/permission/no-pager',
  exportExcelByParam: '/systemConfig/exportExcelByParam'
}

export default api


// post请求
export const postAction = (url, parameter) => {
    return axios({
        url: url,
        method: 'post',
        data: parameter
    })
}

// get请求
export const getAction = (url, parameter) => {
    return axios({
        url: url,
        method: 'get',
        params: parameter
    })
}

