import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/web/msgconfig/list',
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
    url: '/api/web/msgconfig/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/web/msgconfig/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/web/msgconfig/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/web/msgconfig/submit',
    method: 'post',
    data: row
  })
}


export const getSmsList = (current, size, params) => {
  return request({
    url: '/api/web/smsconfig/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}


export const testMsgSignAndTemp = (moblie,tempId, signId,paramsString) => {
  return request({
    url: '/api/web/smsconfig/testMsgSignAndTemp',
    method: 'post',
    params: {
      moblie,
      tempId,
      signId,
      paramsString,
    }
  })
}

