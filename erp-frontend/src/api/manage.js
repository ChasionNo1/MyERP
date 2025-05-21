// 统一封装各种请求
import {service} from '@/utils/request'; // 从request.js导入service实例

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
    return service ({
        url: url,
        method: 'post',
        data: parameter
    })
}

// get请求
export const getAction = (url, parameter) => {
    return service({
        url: url,
        method: 'get',
        params: parameter
    })
}

