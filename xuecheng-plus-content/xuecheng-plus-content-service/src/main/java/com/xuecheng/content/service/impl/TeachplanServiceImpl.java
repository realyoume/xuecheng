package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2023/2/14 12:11
 */
@Service
public class TeachplanServiceImpl implements TeachplanService {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachplanTree(Long courseId) {
        List<TeachplanDto> teachplanDtos = teachplanMapper.selectTreeNodes(courseId);
        return teachplanDtos;
    }

    private int getTeachplanCount(Long courseId,Long parentId){
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(Teachplan::getCourseId, courseId).eq(Teachplan::getParentid, parentId).orderByDesc(Teachplan::getOrderby);
        List<Teachplan> teachplans = teachplanMapper.selectList(queryWrapper);

        if (teachplans.size() == 0){
            return  1;
        }

        Integer count = teachplans.get(0).getOrderby();

        return count+1;
    }


    @Override
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto) {
        //通过课程计划id判断是新增和修改
        Long teachplanId = saveTeachplanDto.getId();
        if(teachplanId ==null){
            //新增
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(saveTeachplanDto,teachplan);
            //确定排序字段，找到它的同级节点个数，排序字段就是个数加1  select count(1) from teachplan where course_id=117 and parentid=268
            Long parentid = saveTeachplanDto.getParentid();
            Long courseId = saveTeachplanDto.getCourseId();
            int teachplanCount = getTeachplanCount(courseId, parentid);
            teachplan.setOrderby(teachplanCount);
            teachplanMapper.insert(teachplan);

        }else{
            //修改
            Teachplan teachplan = teachplanMapper.selectById(teachplanId);
            //将参数复制到teachplan
            BeanUtils.copyProperties(saveTeachplanDto,teachplan);
            teachplanMapper.updateById(teachplan);
        }

    }

    @Override
    public void deleteTeachplan(Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);

        if (teachplan.getGrade() == 1){
            // 大章节
            int count = getTeachplanCount(teachplan.getCourseId(), teachplan.getId());

            if (count > 1){
                XueChengPlusException.cast("课程计划信息还有子级信息，无法操作");
            }

            teachplanMapper.deleteById(teachplan.getId());
        }else {
            // 小章节
            teachplanMapper.deleteById(teachplan.getId());

            LambdaQueryWrapper<TeachplanMedia> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TeachplanMedia::getTeachplanId, teachplan.getId());

            teachplanMediaMapper.delete(wrapper);
        }
    }



    @Override
    public void moveupTeachplan(Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);

        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(Teachplan::getCourseId, teachplan.getCourseId()).eq(Teachplan::getParentid, teachplan.getParentid()).orderByAsc(Teachplan::getOrderby);

        List<Teachplan> teachplans = teachplanMapper.selectList(queryWrapper);

        if (teachplans.get(0).getId().equals(id)){
            XueChengPlusException.cast("已经在最上层");
        }

        for (int i = 1; i < teachplans.size(); ++i) {
            if (teachplans.get(i).getId().equals(id)){
                Teachplan prevTeachplan = teachplans.get(i-1);
                teachplan.setOrderby(prevTeachplan.getOrderby());
                prevTeachplan.setOrderby(teachplans.get(i).getOrderby());

                teachplanMapper.updateById(prevTeachplan);
                teachplanMapper.updateById(teachplan);
            }
        }
    }

    @Override
    public void movedownTeachplan(Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);

        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(Teachplan::getCourseId, teachplan.getCourseId()).eq(Teachplan::getParentid, teachplan.getParentid()).orderByAsc(Teachplan::getOrderby);

        List<Teachplan> teachplans = teachplanMapper.selectList(queryWrapper);

        if (teachplans.get(teachplans.size()-1).getId().equals(id)){
            XueChengPlusException.cast("已经在最下层");
        }

        for (int i = 0; i < teachplans.size() - 1; ++i) {
            if (teachplans.get(i).getId().equals(id)){
                Teachplan nextTeachplan = teachplans.get(i+1);
                teachplan.setOrderby(nextTeachplan.getOrderby());
                nextTeachplan.setOrderby(teachplans.get(i).getOrderby());

                teachplanMapper.updateById(nextTeachplan);
                teachplanMapper.updateById(teachplan);
            }
        }
    }
}
