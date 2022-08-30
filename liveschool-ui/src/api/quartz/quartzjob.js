import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/quartz/quartzjob/list',
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
    url: '/api/quartz/quartzjob/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/quartz/quartzjob/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/quartz/quartzjob/save',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/quartz/quartzjob/update',
    method: 'post',
    data: row
  })
}

export const exec = (id) => {
  return request({
    url: '/api/quartz/quartzjob/exec/'+id,
    method: 'get',
  })
}


export const updateIsPause = (id) => {
  return request({
    url: '/api/quartz/quartzjob/updateIsPause/'+id,
    method: 'get',
  })
}
