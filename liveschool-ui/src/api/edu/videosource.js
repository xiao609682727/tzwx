import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/edu/videosource/list',
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
    url: '/api/edu/videosource/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const getDetailByIdVarchar = (idVarchar) => {
  return request({
    url: '/api/edu/videosource/detail',
    method: 'get',
    params: {
      idVarchar
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/edu/videosource/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/edu/videosource/save',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/edu/videosource/submit',
    method: 'post',
    data: row
  })
}

export const getSyncById = (id) => {
  return request({
    url: '/api/edu/videosource/sync/'+id,
    method: 'get',
  })
}


export const sync = () => {
  return request({
    url: '/api/edu/videosource/sync',
    method: 'get',
  })
}



export const getWebSiteList = (params) => {
  return request({
    url: '/api/cms/websiteprofile/list',
    method: 'get',
    params: {
      ...params
    }
  })
}
