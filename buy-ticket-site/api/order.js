import request from '@/utils/request'
import qs from 'qs'
export function getOrdersByUserId(userId, currentPage, size) {
    return request({
        url: `/order/getOrdersByUserId/${userId}/${currentPage}/${size}`,
        method: 'get'
    })
}
export function getOrderTotalByUserId(userId) {
    return request({
        url: `/order/getOrderTotalByUserId/${userId}`,
        method: 'get'
    })
}
export function addOrder(order) {
    return request({
        url: '/order/addOrder',
        method: 'post',
        data: order
    })
}
export function getOrderById(id) {
    return request({
        url: `/order/getOrderById/${id}`,
        method: 'get'
    })
}
export function pay(data) {
    return request({
      url: '/order/pay',
      method: 'put',
      headers: { 'content-type': 'application/x-www-form-urlencoded' },
      data: qs.stringify(data)
    })
  }