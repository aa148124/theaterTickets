import request from '@/utils/request'
//根据电影id获取排片
export function getArrangeByFilmId(filmId) {
    return request({
        url: `/arrange/getArrangeByFilmId/${filmId}`,
        method: 'get'
    })
}
export function getArrangeById(id) {
    return request({
        url: `/arrange/getArrangeById/${id}`,
        method: 'get'
    })
}