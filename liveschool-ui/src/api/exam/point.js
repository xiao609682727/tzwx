import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/exam/point/list',
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
    url: '/api/exam/point/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/exam/point/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/exam/point/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/exam/point/submit',
    method: 'post',
    data: row
  })
}

export const subjectExamTree = () => {
  return request({
    url: '/api/edu/subject/exam/tree',
    method: 'get',
    params: {
    }
  })
}

