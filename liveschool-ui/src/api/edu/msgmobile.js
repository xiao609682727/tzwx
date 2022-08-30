import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/msg/msgmobile/list',
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
    url: '/api/msg/msgmobile/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/msg/msgmobile/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/msg/msgmobile/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/msg/msgmobile/submit',
    method: 'post',
    data: row
  })
}

export const sendMobile = (id) => {
  return request({
    url: '/api/msg/msgmobile/sendMobile',
    method: 'get',
    params: {
      id
    }
  })
}
