import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/edu/coursestudyhistory/getkpointStudentlist',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}
export const getCourseStudentlist = (current, size, params) => {
  return request({
    url: '/api/edu/coursestudyhistory/getCourseStudentlist',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}
export const getCourseLinelist = (current, size, params) => {
  return request({
    url: '/api/lineclass/lineclassenroll/page',
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
    url: '/api/edu/coursestudyhistory/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/edu/coursestudyhistory/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/edu/coursestudyhistory/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/edu/coursestudyhistory/submit',
    method: 'post',
    data: row
  })
}

