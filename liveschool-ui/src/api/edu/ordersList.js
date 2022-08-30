import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/edu/orders/pageList',
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
    url: '/api/edu/orders/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/edu/orders/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/edu/orders/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/edu/orders/submit',
    method: 'post',
    data: row
  })
}

export const updateOrder = (row) => {
  return request({
    url: '/api/edu/orders/updateOrder',
    method: 'post',
    data: row
  })
}


