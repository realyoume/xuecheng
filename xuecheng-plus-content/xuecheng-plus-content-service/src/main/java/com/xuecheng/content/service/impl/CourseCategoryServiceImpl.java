package com.xuecheng.content.service.impl;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {

        List<CourseCategoryTreeDto> courseCategorys = courseCategoryMapper.selectTreeNodes(id);

        Map<String, CourseCategoryTreeDto> categoryMap = courseCategorys.stream().filter(item -> !id.equals(item.getId())).collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));

        List<CourseCategoryTreeDto> result = new ArrayList<>();

        courseCategorys.forEach(item -> {
            if (item.getParentid().equals(id)){
                result.add(item);
            }else {
                CourseCategoryTreeDto parentCourseCategory = categoryMap.get(item.getParentid());

                if (parentCourseCategory != null){
                    if (parentCourseCategory.getChildrenTreeNodes() == null){
                        parentCourseCategory.setChildrenTreeNodes(new ArrayList<>());
                    }

                    parentCourseCategory.getChildrenTreeNodes().add(item);
                }
            }
        });


        return result;
    }
}
