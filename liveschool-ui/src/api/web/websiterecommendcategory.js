import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/web/websiterecommendcategory/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/web/websiterecommendcategory/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/web/websiterecommendcategory/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/web/websiterecommendcategory/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/web/websiterecommendcategory/submit',
    method: 'post',
    data: row
  })
}

