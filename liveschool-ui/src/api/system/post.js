import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/crazy-system/post/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getPostList = (tenantId) => {
  return request({
    url: '/api/crazy-system/post/select',
    method: 'get',
    params: {
      tenantId
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/crazy-system/post/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/crazy-system/post/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/crazy-system/post/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/crazy-system/post/submit',
    method: 'post',
    data: row
  })
}

