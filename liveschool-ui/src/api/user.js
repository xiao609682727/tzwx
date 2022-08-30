import request from '@/router/axios';
import {baseUrl} from '@/config/env';
import website from "@/config/website";

export const loginByUsername = (tenantId, account, password, type, key, code) => request({
  url: '/api/crazy-auth/token',
  method: 'post',
  headers: {
    'Captcha-Key': key,
    'Captcha-Code': code,
  },
  params: {
    grantType: (website.captchaMode ? "captcha" : "password"),
    tenantId,
    account,
    password,
    type
  }
});

export const getButtons = () => request({
  url: '/api/crazy-system/menu/buttons',
  method: 'get'
});

export const getUserInfo = () => request({
  url: baseUrl + '/user/getUserInfo',
  method: 'get'
});

export const refeshToken = (refreshToken) => request({
  url: baseUrl + '/api/crazy-auth/token',
  method: 'post',
  params: {
    grantType:"refresh_token",
    refreshToken:refreshToken
  }
})

export const getMenu = (parentId) => request({
  url: '/api/crazy-system/menu/routes?parentId='+parentId,
  method: 'get'
});

export const getCaptcha = () => request({
  url: '/api/crazy-auth/captcha',
  method: 'get'
});

export const getTopMenu = () => request({
  url: '/api/crazy-system/menu/getTopMenu',
  method: 'get'
});

export const sendLogs = (list) => request({
  url: baseUrl + '/user/logout',
  method: 'post',
  data: list
})

export const logout = () => request({
  url: baseUrl + '/user/logout',
  method: 'get'
})
