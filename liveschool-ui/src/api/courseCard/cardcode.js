import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/coursecard/cardcode/page',
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
    url: '/api/coursecard/cardcode/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/coursecard/cardcode/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/coursecard/cardcode/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/coursecard/cardcode/submit',
    method: 'post',
    data: row
  })
}

export const updateInvalidCourseCardCode = (row) => {
  return request({
    url: '/api/edu/card/updateInvalidCourseCardCode',
    method: 'post',
    data: row
  })
}



