import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/auth/online/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}



export const remove = (ids) => {
  return request({
    url: '/api/auth/online/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const update = (ids) => {
  return request({
    url: '/api/auth/online/update',
    method: 'post',
    params: {
      ids,
    }
  })
}
