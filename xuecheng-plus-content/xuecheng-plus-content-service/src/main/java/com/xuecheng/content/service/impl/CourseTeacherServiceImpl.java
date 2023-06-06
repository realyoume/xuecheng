package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0
 * @author: xk
 * @description 课程教师管理
 * @date: 2023/6/5 14:03
 */

@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Override
    public List<CourseTeacher> getTeacherListByCourseId(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseTeacher::getCourseId, courseId);

        List<CourseTeacher> courseTeacherList = courseTeacherMapper.selectList(wrapper);

        return courseTeacherList;
    }

    @Override
    public CourseTeacher addTeacher(CourseTeacher courseTeacher) {
        courseTeacher.setCreateDate(LocalDateTime.now());
        int insert = courseTeacherMapper.insert(courseTeacher);

        if (insert < 1){
            XueChengPlusException.cast("创建教师失败");
        }

        CourseTeacher courseTeacherNew = courseTeacherMapper.selectById(courseTeacher.getId());

        return courseTeacherNew;
    }

    @Override
    public CourseTeacher editTeacher(CourseTeacher courseTeacher) {
        CourseTeacher courseTeacherNew = courseTeacherMapper.selectById(courseTeacher.getId());

        if (courseTeacherNew == null){
            XueChengPlusException.cast("修改教师失败");
        }

        BeanUtils.copyProperties(courseTeacher, courseTeacherNew);

        courseTeacherMapper.updateById(courseTeacherNew);

        return courseTeacherNew;
    }

    @Override
    public void deleteTeacher(Long courseId, Long id) {
        courseTeacherMapper.deleteById(id);
    }
}


    