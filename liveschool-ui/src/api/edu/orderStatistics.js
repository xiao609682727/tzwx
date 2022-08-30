import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/edu/orders/statistics/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}



