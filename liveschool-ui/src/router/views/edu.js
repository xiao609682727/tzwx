import Layout from '@/page/index/'
export default [{
  path: '/edu',
  component: Layout,
  children: [{
    path: '/coursekpoint',
    name: '课程目录',
    component: () =>
      import( /* webpackChunkName: "views" */ '@/views/edu/coursekpoint')
  },{
    path: '/coursefavorites',
    name: '课程收藏',
    component: () =>
      import( /* webpackChunkName: "views" */ '@/views/edu/coursefavorites')
  }]
}]
