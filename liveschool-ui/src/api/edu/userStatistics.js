import request from '@/router/axios';

export const getDetail = (code) => {
  return request({
    url: '/api/web/statdata/detail',
    method: 'get',
    params: {
      code
    }
  })
}

export const getDataByDay = (days) =>
{
  if (days == 1) {
    return request({
      url: '/api/web/statdata/detail?code=statadminuserdata',
      method: 'get'
    })
  }else if (days == 2){
    return request({
      url: '/api/web/statdata/user/statistics?code=statadminuserdata',
      method: 'get'
    })
  }
}


