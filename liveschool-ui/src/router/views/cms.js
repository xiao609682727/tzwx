import Layout from '@/page/index/'
export default [{
  path: '/cms',
  component: Layout,
  redirect: '/cms/comment',
  children: [{
    path: 'comment',
    name: '评论列表',
    meta: {
      i18n: 'comment'
    },
    component: () =>
      import( /* webpackChunkName: "views" */ '@/views/cms/comment')
  },{
    path: 'replyComment',
    name: '回复评论列表',
    meta: {
      i18n: 'replyComment'
    },
    component: () =>
      import( /* webpackChunkName: "views" */ '@/views/cms/replyComment')
  }]
}]
