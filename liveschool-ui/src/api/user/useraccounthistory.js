import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/user/useraccounthistory/page',
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
    url: '/api/user/useraccounthistory/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/user/useraccounthistory/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/user/useraccounthistory/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/user/useraccounthistory/submit',
    method: 'post',
    data: row
  })
}

