import request from '@/utils/request'

//获取热门短评
export function getHotComment(filmId) {
    return request({
        url: `/comment/getHotComment/${filmId}`,
        method: 'get'
    })
}
//点赞
export function liked(commentId, userId) {
    return request({
        url: `/comment/likeComment/${commentId}/${userId}`,
        method: 'put'
    })
}
//评论
export function addComment(comment) {
    return request({
        url: `/comment/addComment`,
        method: 'post',
        data: comment
    })
}