import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/edu/card/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}
export const getCardCourseList = (cardId) => {
  return request({
    url: '/api/edu/card/courseList?cardId='+cardId,
    method: 'get',
    params: {}
  })
}
export const getDetail = (id) => {
  return request({
    url: '/api/edu/card/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/edu/card/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row,courseIds) => {
  return request({
    url: '/api/edu/card/submit?courseIds='+courseIds,
    method: 'post',
    data: row
  })
}

export const update = (row,courseIds) => {
  return request({
    url: '/api/edu/card/update?courseIds='+courseIds,
    method: 'post',
    data: row
  })
}

export const updateCourseCardInvalid = (row) => {
  return request({
    url: '/api/edu/card/updateInvalid',
    method: 'post',
    data: row
  })
}

export const addCourseCardNums = (row) => {
  return request({
    url: '/api/edu/card/addCourseCardNums',
    method: 'post',
    data: row
  })
}


