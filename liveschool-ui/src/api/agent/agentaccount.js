import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/edu/agentaccount/page',
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
    url: '/api/edu/agentaccount/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/edu/agentaccount/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/edu/agentaccount/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/edu/agentaccount/submit',
    method: 'post',
    data: row
  })
}

export const money = (userId,money,type) => {
  return request({
    url: '/api/edu/agentaccount/money?userId='+userId+"&money="+money+"&type="+type,
    method: 'post',
  })
}
