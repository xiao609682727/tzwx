import request from '@/router/axios';

export const getAliyunAccount = () => {
  return request({
    url: '/api/cms/websiteprofile/getAliyunAccount',
    method: 'get'
  })
}
