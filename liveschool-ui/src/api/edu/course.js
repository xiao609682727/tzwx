import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/edu/course/list',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}


export const gePage = (current, size, params) => {
  return request({
    url: '/api/edu/course/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const geClassPage = (current, size, params) => {
  return request({
    url: '/api/edu/course/ClassCoursePage',
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
    url: '/api/edu/course/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const getEndTimeType = () => {
  return request({
    url: '/api/crazy-system/dict/dictionary?code=end_time_type',
    method: 'get',
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/edu/course/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/edu/course/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/edu/course/update',
    method: 'post',
    data: row
  })
}
//赠送课程
export const give = (userIds,courseIds) => {
  return request({
    url: '/api/edu/course/give',
    method: 'post',
    params: {
      "userIds":userIds,
      "courseIds":courseIds
    }
  })
}
//开通课程
export const openCourse = (userIds,courseIds) => {
  return request({
    url: '/api/edu/course/openCourse',
    method: 'post',
    params: {
      "userIds":userIds,
      "courseIds":courseIds
    }
  })
}
export const getTeacherDetail = (id) => {
  return request({
    url: '/api/user/teacher/detail',
    method: 'get',
    params: {
      id
    }
  })
}


export const subjectTree = () => {
  return request({
    url: '/api/edu/subject/tree',
    method: 'get',
    params: {
    }
  })
}

export const linClasssubjectTree = () => {
  return request({
    url: '/api/edu/subject/lineClasstree',
    method: 'get',
    params: {
    }
  })
}


export const selectCourseKpoint = (courseId) => {
  return request({
    url: '/api/edu/course/kpoints',
    method: 'post',
    params: {
      "courseId":courseId
    }
  })
}
