import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/edu/liveplayback/list',
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
    url: '/api/edu/liveplayback/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/edu/liveplayback/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/edu/liveplayback/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/edu/liveplayback/submit',
    method: 'post',
    data: row
  })
}

export const sync = (roomId) => {
  return request({
    url: '/api/edu/liveplayback/sync',
    method: 'get',
    params: {
      roomId
    }
  })
}


export const getUrl = (id) => {
  return request({
    url: '/api/edu/liveplayback/getUrl',
    method: 'get',
    params: {
      id
    }
  })
}
