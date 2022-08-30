import request from '@/router/axios';

export const getAccountInfo = (params) => {
  return request({
    url: '/api/web/baijia/getAccountInfo',
    method: 'get',
    params: params
  })
}


export const getResumeUploadUrl = (params) => {
  return request({
    url: '/api/web/baijia/getResumeUploadUrl',
    method: 'get',
    params: params
  })
}


export const getUploadUrl = (params) => {
  return request({
    url: '/api/web/baijia/getUploadUrl',
    method: 'get',
    params: params
  })
}


export const uploadFile = (params) => {
  return request(params)
}
