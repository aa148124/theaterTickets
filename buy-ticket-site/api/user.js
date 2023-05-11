import request from '@/utils/request'
//发送验证码
export function send(phone) {
    return request({
        url: `/sms/send/${phone}`,
        method: 'get'
    })
}
//登录
export function login(loginVo) {
    return request({
        url: '/user/login',
        method: 'post',
        data: loginVo
    })
}
//修改用户
export function updateUser(user) {
    return request({
        url: '/user/updateUser',
        method: 'put',
        data: user
    })
}