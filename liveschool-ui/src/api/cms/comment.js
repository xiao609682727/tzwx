import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/cms/comment/page',
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
    url: '/api/cms/comment/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/cms/comment/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/cms/comment/submit',
    method: 'post',
    data: row
  })
}

export const reply = (row) => {
  return request({
    url: '/api/cms/comment/reply',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/cms/comment/submit',
    method: 'post',
    data: row
  })
}

