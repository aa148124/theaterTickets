import request from '@/utils/request'

//获取正在热映的电影
export function getShowFilm() {
    return request({
        url: '/film/getShowFilm',
        method: 'get'
    })
}
//获取正在热映的电影的数量
export function getShowFilmCount() {
    return request({
        url: '/film/getShowFilmCount',
        method: 'get'
    })
}
//获取即将上映的电影
export function getFutureFilm() {
    return request({
        url: '/film/getFutureFilm',
        method: 'get'
    })
}
//获取即将上映的电影的数量
export function getFutureFilmCount() {
    return request({
        url: '/film/getFutureFilmCount',
        method: 'get'
    })
}
//获取历史票房排行
export function getRanking() {
    return request({
        url: '/film/getRanking',
        method: 'get'
    })
}
//查询电影
export function getFilmById(filmId) {
    return request({
        url: `/film/getFilmById/${filmId}`,
        method: 'get'
    })
}
//海报
export function getPoster() {
    return request({
        url: `/film/getPoster/`,
        method: 'get'
    })
}
//搜索电影
export function search(context) {
    return request({
        url: `/film/search/${context}`,
        method: 'get'
    })
}

