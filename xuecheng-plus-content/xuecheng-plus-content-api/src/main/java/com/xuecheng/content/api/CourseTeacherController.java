package com.xuecheng.content.api;

import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 1.0
 * @author: xk
 * @description TODO
 * @date: 2023/6/5 13:59
 */

@Api(value = "课程教师管理接口",tags = "课程教师管理接口")
@RestController
public class CourseTeacherController {

    @Autowired
    private CourseTeacherService courseTeacherService;


    @ApiOperation("得到教师列表")
    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacher> getTeacherListByCourseId(@PathVariable("courseId") Long courseId){
        List<CourseTeacher> courseTeacherList = courseTeacherService.getTeacherListByCourseId(courseId);
        return courseTeacherList;
    }

    @ApiOperation("新建教师")
    @PostMapping("/courseTeacher")
    public CourseTeacher addTeacher(@RequestBody CourseTeacher courseTeacher){
        CourseTeacher courseTeacherNew = courseTeacherService.addTeacher(courseTeacher);
        return courseTeacherNew;
    }

    @ApiOperation("修改教师")
    @PutMapping("/courseTeacher")
    public CourseTeacher editTeacher(@RequestBody CourseTeacher courseTeacher){
        CourseTeacher courseTeacherNew = courseTeacherService.editTeacher(courseTeacher);
        return courseTeacherNew;
    }

    @ApiOperation("删除教师")
    @DeleteMapping("/courseTeacher/course/{courseId}/{id}")
    public void deleteTeacher(@PathVariable("courseId") Long courseId, @PathVariable("id") Long id){
        courseTeacherService.deleteTeacher(courseId, id);
    }
}


    