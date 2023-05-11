import request from '@/utils/request'
import qs from 'qs'
export function changePwd(data) {
  return request({
    url: '/admin/changePwd',
    method: 'put',
    headers: { 'content-type': 'application/x-www-form-urlencoded' },
    data: qs.stringify(data)
  })
}
export function getAdminList(current, size) {
  return request({
    url: `/admin/getAdminList/${current}/${size}`,
    method: 'get'
  })
}
export function getFilmList(current, size) {
  return request({
    url: `/film/getFilmList/${current}/${size}`,
    method: 'get'
  })
}
export function getArrangeList(current, size) {
  return request({
    url: `/arrange/getArrangeList/${current}/${size}`,
    method: 'get'
  })
}
export function getCommentList(current, size) {
  return request({
    url: `/comment/getCommentList/${current}/${size}`,
    method: 'get'
  })
}
export function getOrderList(current, size) {
  return request({
    url: `/order/getOrderList/${current}/${size}`,
    method: 'get'
  })
}
export function getAdminTotal() {
  return request({
    url: '/admin/getAdminTotal',
    method: 'get'   
  })
}
export function getFilmTotal() {
  return request({
    url: '/film/getFilmTotal',
    method: 'get'
  })
}
export function getArrangeTotal() {
  return request({
    url: '/arrange/getArrangeTotal',
    method: 'get'
  })
}
export function addArrange(arrange) {
  return request({
    url: '/arrange/addArrange',
    method: 'post',
    data: arrange
  })
}
export function getCommentTotal() {
  return request({
    url: '/comment/getCommentTotal',
    method: 'get'
  })
}
export function getOrderTotal() {
  return request({
    url: '/order/getOrderTotal',
    method: 'get'
  })
}
export function updateAdmin(data) {
  return request({
    url: '/admin/updateAdmin',
    method: 'put',
    data
  })
}
export function updateFilm(data) {
  return request({
    url: '/film/updateFilm',
    method: 'put',
    data
  })
}
export function deleteAdmin(id) {
  return request({
    url: `/admin/deleteAdmin/${id}`,
    method: 'delete'
  })
}
export function searchFilm(data, current, size) {
  return request({
    url: `/film/searchFilm/${current}/${size}`,
    method: 'post',
    headers: { 'content-type': 'application/x-www-form-urlencoded' },
    data: qs.stringify(data)
  })
}
export function searchOrder(data, current, size) {
  return request({
    url: `/order/searchOrder/${current}/${size}`,
    method: 'post',
    headers: { 'content-type': 'application/x-www-form-urlencoded' },
    data: qs.stringify(data)
  })
}
export function getCountByCondition(data) {
  return request({
    url: '/film/getCountByCondition',
    method: 'post',
    headers: { 'content-type': 'application/x-www-form-urlencoded' },
    data: qs.stringify(data)
  })
}
export function addFilm(data) {
  return request({
    url: '/film/addFilm',
    method: 'post',
    data
  })
}