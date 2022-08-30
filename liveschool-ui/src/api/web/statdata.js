import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/web/statdata/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getDetail = (code) => {
  return request({
    url: '/api/web/statdata/detail',
    method: 'get',
    params: {
      code
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/web/statdata/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/web/statdata/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/web/statdata/submit',
    method: 'post',
    data: row
  })
}

