import request from '@/router/axios';
import {baseUrl} from '@/config/env';
import website from "@/config/website";

export const login = (account,password,grantType,key,captchaCode) => {
  return request({
    url: baseUrl + '/crazy-auth/token',
    method: 'post',
    headers: {
      'User-Type': 'student',
      'Captcha-Code': captchaCode,
      'Captcha-Key': key,
    },
    params: {
      password,
      account,
      grantType:grantType
    }
  })
}

export const checkToken = (token) => {
  return request({
    url: baseUrl + '/crazy-auth/checkToken',
    method: 'post',
    params: {
      token
    }
  })
}

/**
 * 通过手机验证码登录
 * @param account
 * @param smsCode
 */
export const loginBySMS = (account,smsCode) => {
  return request({
    url: baseUrl + '/crazy-auth/token',
    method: 'post',
    headers: {
      'User-Type': 'student',
      'Captcha-Code': smsCode,
    },
    params: {
      account,
      grantType:"sms"
    }
  })
}

//检查手机是否已经注册
export const checkMobile = async (mobile,type) => {
  return await request({
    url: baseUrl + '/front/user/checkMobile',
    method: 'get',
    params: {
      mobile,
      type
    }
  })
}

//注册用户
export const register = (password,mobile,smsCode,username) => {
  return request({
    url: baseUrl + '/front/user/register',
    method: 'post',
    params: {
      password,
      mobile,
      smsCode,
      username
    }
  })
}


export const getUserInfo = () => {
  return request({
    url: baseUrl + '/user/student',
  })
}

export const refeshToken = (refreshToken) => {
  return request({
    url: baseUrl + '/crazy-auth/token',
    method: 'post',
    headers: {
      'User-Type': 'student'
    },
    params: {
      grantType:"refresh_token",
      refreshToken:refreshToken
    }
  })
}



export const logout = () => request({
  url: baseUrl + '/front/user/logout',
  method: 'get'
})


export const getUserAccount = () => {
  return request({
    url: baseUrl + '/user/useraccount/list',
    params: {
    }
  })
}


export const getUserAccountHistory = (params) => {
  return request({
    url: baseUrl + '/user/useraccounthistory/list',
    params: params
  })
}


export const getTrxorderdetail = (params) => {
  return request({
    url: baseUrl + '/edu/trxorderdetail/page',
    params: params
  })
}

export const getTrxorderEnddetail = (params) => {
  return request({
    url: baseUrl + '/edu/trxorderdetail/pageLine',
    params: params
  })
}

//我的收藏
export const getCourseFavoriteList = (params) => {
  return request({
    url: baseUrl + '/edu/coursefavorites/page',
    method: 'get',
    params: params
  })
}


//获取区域
export const getAreaList = (params) => {
  return request({
    url: baseUrl + '/web/area/list',
    method: 'get',
    params: params
  })
}


//修改用户信息
export const updateUser = (row) => {
  return request({
    url: baseUrl + '/user/student/update',
    method: 'post',
    data: row
  })
}

//修改绑定用户信息
export const updateUserProfile = (row) => {
  return request({
    url: baseUrl + '/partyLogin/userprofile/submit',
    method: 'post',
    data: row
  })
}

//修改绑定用户信息
export const deleteUserProfile = (row) => {
  return request({
    url: baseUrl + '/partyLogin/userprofile/remove',
    method: 'post',
    data: row
  })
}


//上传文件
export const uploadFile = (row) => {
  return request({
    url: baseUrl + '/common/upload',
    method: 'post',
    data: row
  })
}


//获取订单
export const getOrderList = (params) => {
  return request({
    url: baseUrl + '/edu/orders',
    method: 'get',
    params: params
  })
}


//发送邮箱激活码
export const sendEmail = (params) => {
  return request({
    url: baseUrl + '/front/user/sendEmail',
    method: 'get',
    params: params
  })
}


//邮箱绑定
export const bindEmail = (params) => {
  return request({
    url: baseUrl + '/front/user/bindEmail',
    method: 'get',
    params: params
  })
}

//修改密码
export const updatePassword = (params) => {
  return request({
    url: baseUrl + '/front/user/updatePassword',
    method: 'get',
    params: params
  })
}

//修改手机号
export const updateMobile = (params) => {
  return request({
    url: baseUrl + '/front/user/updateMobile',
    method: 'get',
    params: params
  })
}


//创建订单
export const createOrder = (params) => {
  return request({
    url: baseUrl + '/front/user/createOrder',
    method: 'get',
    params: params
  })
}

//创建课程订单
export const createCourseOrder = (params) => {
  return request({
    url: baseUrl + '/front/user/createCourseOrder',
    method: 'get',
    params: params
  })
}

//创建免费课程订单
export const createFreeCourseOrder = (params) => {
  return request({
    url: baseUrl + '/front/user/createFreeCourseOrder',
    method: 'get',
    params: params
  })
}

//获取订单详情
export const courseOrderDetail = (params) => {
  return request({
    url: baseUrl + '/edu/orders/detail',
    method: 'get',
    params: params
  })
}


//检查订单是否支付成功
export const checkOrder = (params) => {
  return request({
    url: baseUrl + '/front/user/checkOrder',
    method: 'get',
    params: params
  })
}


export const getDetail = (id) => {
  return request({
    url: baseUrl + '/edu/coursekpoint/detail',
    method: 'get',
    params: {
      id,
      userType:"student"
    }
  })
}



//查看学生是否拥有此课程
export const checkCourse = (params) => {
  return request({
    url: baseUrl + '/edu/trxorderdetail/checkCourse',
    method: 'get',
    params: params
  })
}


//修改订单信息
export const updateOrder = (row) => {
  return request({
    url: baseUrl + '/edu/orders/updateOrder',
    method: 'post',
    data: row
  })
}


//储值支付
export const accountPay = (params) => {
  return request({
    url: baseUrl + '/front/user/accountPay',
    method: 'get',
    params: params
  })
}


//收藏课程
export const coursefavorites = (row) => {
  return request({
    url: baseUrl + '/edu/coursefavorites/submit',
    method: 'post',
    data: row
  })
}
//取消课程
export const removeCoursefavorites = (ids) => {
  return request({
    url: baseUrl + '/edu/coursefavorites/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}
//检查是否已收藏此课程
export const checkFavorites = (params) => {
  return request({
    url: baseUrl + '/edu/coursefavorites/checkFavorites',
    method: 'get',
    params: params
  })
}


//创建评论
export const commentSubmit = (row) => {
  return request({
    url: baseUrl + '/cms/comment/save',
    method: 'post',
    data: row
  })
}


//检查是否已收藏此课程
export const getAuthInfo = (params) => {
  return request({
    url: baseUrl + '/front/oauth/getAuthInfo',
    method: 'get',
    params: params
  })
}


//创建学习记录
export const addCoursestudyhistory = (row) => {
  return request({
    url: baseUrl + '/edu/coursestudyhistory/submit',
    method: 'post',
    params: row
  })
}

//课学习记录完成状态修改
export const updateCoursestudyhistory = (row) => {
  return request({
    url: baseUrl + '/edu/coursestudyhistory/update',
    method: 'post',
    params: row
  })
}
//获取微信支付二维码
export const getwechatPay = (params) => {
  return request({
    url: baseUrl + '/front/wxPay/scanCode2',
    method: 'get',
    params: params
  })
}

//学习卡激活
export const courseCardActivation = (params) => {
  return request({
    url: baseUrl + '/edu/card/courseCardActivation',
    method: 'get',
    params: params
  })
}
//面授报名
export const courseFaceTeacherBaoMing = (params) => {
  return request({
    url: baseUrl + '/lineclass/lineclassenroll/addSaveAndPay',
    method: 'get',
    params: params
  })
}

//面授报名
export const getCourseFaceTeacherStatis = (params) => {
  return request({
    url: baseUrl + '/lineclass/lineclassenroll/getStatic',
    method: 'get',
    params: params
  })
}
