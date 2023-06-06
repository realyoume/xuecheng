package com.xuecheng.content.service;

import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

/**
 * @version 1.0
 * @author: xk
 * @description 课程教师管理
 * @date: 2023/6/5 14:03
 */
public interface CourseTeacherService {

    /**
     * 得到课程教师列表
     * @param courseId
     * @return
     */
    List<CourseTeacher> getTeacherListByCourseId(Long courseId);


    /**
     * 创建教师
     * @param courseTeacher
     * @return
     */
    CourseTeacher addTeacher(CourseTeacher courseTeacher);

    /**
     * 修改教师
     * @param courseTeacher
     * @return
     */
    CourseTeacher editTeacher(CourseTeacher courseTeacher);


    /**
     * 删除教师
     * @param courseId
     * @param id
     */
    void deleteTeacher(Long courseId, Long id);
}
