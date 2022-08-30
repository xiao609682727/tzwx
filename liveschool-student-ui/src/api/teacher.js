import request from '@/router/axios';
import {baseUrl} from '@/config/env';



//获取教师列表
export const getTeacherList = (params) => {
  return request({
    url: baseUrl + '/front/teacher/teacherList',
    params: params
  })
}

//查询教师详情
export const getTeacherInfo = (id) => {
  return request({
    url: baseUrl + '/front/teacher/teacherInfo/'+id,
    method: 'get',
  })
}
