import request from '@/utils/request'
import qs from 'qs'
export function login(data) {
  return request({
    url: '/admin/login',
    method: 'post',
    headers: { 'content-type': 'application/x-www-form-urlencoded' },
    data: qs.stringify(data)
  })
}

export function getInfo(token) {
  return request({
    url: '/vue-user-template/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-user-template/user/logout',
    method: 'post'
  })
}
export function getUserList(current, size) {
  return request({
    url: `/user/getUserList/${current}/${size}`,
    method: 'get'
  })
}
export function getUserTotal() {
  return request({
    url: '/user/getUserTotal',
    method: 'get'
    
  })
}
export function updateUser(data) {
  return request({
    url: '/user/updateUser',
    method: 'put',
    data
  })
}
export function deleteUser(id) {
  return request({
    url: `/user/deleteUser/${id}`,
    method: 'delete'
  })
}
export function searchUser(data) {
  return request({
    url: '/user/searchUser',
    method: 'post',
    headers: { 'content-type': 'application/x-www-form-urlencoded' },
    data: qs.stringify(data)
  })
}