import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/exam/exampaper/list',
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
    url: '/api/exam/exampaper/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/exam/exampaper/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/exam/exampaper/save',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/exam/exampaper/update',
    method: 'post',
    data: row
  })
}

export const subjectTree = () => {
  return request({
    url: '/api/edu/subject/exam/tree',
    method: 'get',
    params: {
    }
  })
}


export const saveQuestion = (row,id) => {
  return request({
    url: '/api/exam/exampaper/saveQuestion?id='+id,
    method: 'post',
    data: row
  })
}


export const queryQuestion = (id) => {
  return request({
    url: '/api/exam/exampaper/queryQuestion',
    method: 'get',
    params: {
      id
    }
  })
}


export const getPapertypeList = () => {
  return request({
    url: '/api/exam/papertype/dictionary',
    method: 'get',
  })
}

