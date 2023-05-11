import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: '首页',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  },
  {
    path: '/电影信息管理',
    component: Layout,
    redirect: '/film/list',
    name: '电影信息管理',
    meta: { title: '电影信息管理', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'list',
        name: '影片列表',
        component: () => import('@/views/film/list'),
        meta: { title: '影片列表', icon: 'table' }
      },
      {
        path: 'add',
        name: '影片添加',
        component: () => import('@/views/film/add'),
        meta: { title: '影片添加', icon: 'tree' }
      },
      {
        path: 'arrange',
        name: '电影排片',
        component: () => import('@/views/film/arrange'),
        meta: { title: '电影排片', icon: 'tree' }
      }
    ]
  },
  {
    path: '/用户信息管理',
    component: Layout,
    redirect: '/user/list',
    name: '用户信息管理',
    meta: { title: '用户信息管理', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'list',
        name: '普通用户列表',
        component: () => import('@/views/user/list'),
        meta: { title: '用户列表', icon: 'table' }
      },
      {
        path: 'add',
        name: '管理员列表',
        component: () => import('@/views/user/adminList'),
        meta: { title: '管理员列表', icon: 'table' }
      }
    ]
  },
  {
    path: '/评论管理',
    component: Layout,
    children: [
      {
        path: 'comment',
        name: '评论管理',
        component: () => import('@/views/comment/comment'),
        meta: { title: '评论管理', icon: 'form' }
      }
    ]
  },
  {
    path: '/订单管理',
    component: Layout,
    children: [
      {
        path: 'order',
        name: '订单管理',
        component: () => import('@/views/order/order'),
        meta: { title: '订单管理', icon: 'form' }
      }
    ]
  },
  {
    path: '/修改密码',
    component: Layout,
    children: [
      {
        path: 'change',
        name: '修改密码',
        component: () => import('@/views/change/change'),
        meta: { title: '修改密码', icon: 'form' }
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
