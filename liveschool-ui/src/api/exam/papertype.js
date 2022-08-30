import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/exam/papertype/list',
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
    url: '/api/exam/papertype/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/exam/papertype/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/exam/papertype/save',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/exam/papertype/submit',
    method: 'post',
    data: row
  })
}

