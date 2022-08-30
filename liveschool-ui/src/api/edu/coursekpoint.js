import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/edu/coursekpoint/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}


export const getSupplier = (type) => {
  return request({
    url: '/api/crazy-system/dict/dictionary?code='+type,
    method: 'get',
  })
}

export const getLiveSupplier = () => {
  return request({
    url: '/api/crazy-system/dict/dictionary?code=live_supplier',
    method: 'get',
  })
}

export const getDetail = (id,userType) => {
  return request({
    url: '/api/edu/coursekpoint/detail',
    method: 'get',
    params: {
      id,userType
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/edu/coursekpoint/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/edu/coursekpoint/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/edu/coursekpoint/update',
    method: 'post',
    data: row
  })
}

export const getUserDetail = (id) => {
  return request({
    url: '/api/user/teacher/detail',
    method: 'get',
    params: {
      id
    }
  })
}


export const createUrl = (streamName) => {
  return request({
    url: '/api/edu/coursekpoint/createUrl',
    method: 'get',
    params: {
      streamName
    }
  })
}

export const getRoomDetail = (id) => {
  return request({
    url: '/api/edu/liveroom/detail',
    method: 'get',
    params: {
      classroomId:id
    }
  })
}
