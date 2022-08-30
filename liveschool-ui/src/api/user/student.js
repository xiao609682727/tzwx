import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/user/student/list',
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
    url: '/api/user/student/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/user/student/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/user/student/save',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/user/student/submit',
    method: 'post',
    data: row
  })
}

export const setPassword = (row) => {
  return request({
    url: '/api/user/student/setPassword',
    method: 'post',
    data: row
  })
}


export const getAdminUserList = () => {
  return request({
    url: '/api/crazy-user/getAdminUserList',
    method: 'get',
  })
}

