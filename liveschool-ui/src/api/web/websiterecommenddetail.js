import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/web/websiterecommenddetail/page',
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
    url: '/api/web/websiterecommenddetail/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/web/websiterecommenddetail/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/web/websiterecommenddetail/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/web/websiterecommenddetail/submit',
    method: 'post',
    data: row
  })
}


export const saveBatch = (row,recommendId) => {
  return request({
    url: '/api/web/websiterecommenddetail/saveBatch?recommendId='+recommendId,
    method: 'post',
    data: row
  })
}

