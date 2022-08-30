import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/crazy-system/dict/page',
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
    url: '/api/crazy-system/dict/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/crazy-system/dict/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/crazy-system/dict/submit',
    method: 'post',
    data: row
  })
}


export const getDict = (id) => {
  return request({
    url: '/api/crazy-system/dict/detail',
    method: 'get',
    params: {
      id,
    }
  })
}
export const getDictTree = () => {
  return request({
    url: '/api/crazy-system/dict/tree?code=DICT',
    method: 'get'
  })
}
export const dictionary = (code) => {
  return request({
    url: '/api/crazy-system/dict/dictionary?code=' + code,
    method: 'get',
  })
}
