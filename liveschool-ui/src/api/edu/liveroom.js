import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/edu/liveroom/list',
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
    url: '/api/edu/liveroom/detail',
    method: 'get',
    params: {
      id
    }
  })
}


export const getUrl = (id) => {
  return request({
    url: '/api/edu/liveroom/getUrl',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/edu/liveroom/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/edu/liveroom/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/edu/liveroom/update',
    method: 'post',
    data: row
  })
}

