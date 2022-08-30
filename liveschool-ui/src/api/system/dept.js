import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/crazy-system/dept/list',
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
    url: '/api/crazy-system/dept/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/crazy-system/dept/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/crazy-system/dept/submit',
    method: 'post',
    data: row
  })
}

export const getDept = (id) => {
  return request({
    url: '/api/crazy-system/dept/detail',
    method: 'get',
    params: {
      id,
    }
  })
}
export const getDeptTree = (tenantId) => {
  return request({
    url: '/api/crazy-system/dept/tree',
    method: 'get',
    params: {
      tenantId,
    }
  })
}

