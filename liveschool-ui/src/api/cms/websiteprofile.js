import request from '@/router/axios';

export const getList = (params) => {
  return request({
    url: '/api/cms/websiteprofile/list',
    method: 'get',
    params: {
      ...params
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/cms/websiteprofile/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/cms/websiteprofile/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/cms/websiteprofile/save',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/cms/websiteprofile/submit',
    method: 'post',
    data: row
  })
}

