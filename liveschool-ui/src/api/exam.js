import request from '@/router/axios';
import {baseUrl} from '@/config/env';



//获取考试记录
export const getExampaperRecord = (params) => {
  return request({
    url: baseUrl + '/front/exam/exampaperRecord/list',
    params: params
  })
}


//获取考试类型
export const getExampaperType = () => {
  return request({
    url: baseUrl + '/front/exam/paperType/list',
  })
}


//获取错题记录
export const getErrorquestion = (params) => {
  return request({
    url: baseUrl + '/front/exam/errorquestion/list',
    params: params
  })
}


//获取所有错题记录
export const getErrorquestionAll = (params) => {
  return request({
    url: baseUrl + '/front/exam/errorquestion/listAll',
    params: params
  })
}

//删除错题记录
export const removeErrorquestion = (ids) => {
  return request({
    url: baseUrl + '/front/exam/errorquestion/remove',
    method: 'post',
    params: {
      ids:ids
    }
  })
}


//获取收藏试题记录
export const getFavoritesqst = (params) => {
  return request({
    url: baseUrl + '/front/exam/favoritesqst/list',
    params: params
  })
}


//删除收藏试题记录
export const removeFavoritesqst = (data) => {
  return request({
    url: baseUrl + '/front/exam/favoritesqst/remove',
    method: 'post',
    data: data
  })
}

//删除收藏试题记录
export const removeFavoritesqstById = (ids) => {
  return request({
    url: baseUrl + '/front/exam/favoritesqst/removeById',
    method: 'post',
    params: {
      ids:ids
    }
  })
}


//添加收藏试题记录
export const saveFavoritesqst = (data) => {
  return request({
    url: baseUrl + '/front/exam/favoritesqst/save',
    method: 'post',
    data: data
  })
}
