import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/edu/trxorderdetail/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getListForUser = (current, size, params) => {
  return request({
    url: '/api/edu/trxorderdetail/listForUser',
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
    url: '/api/edu/trxorderdetail/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/edu/trxorderdetail/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/edu/trxorderdetail/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/edu/trxorderdetail/submit',
    method: 'post',
    data: row
  })
}

