package com.ssp.service;

import com.ssp.entity.File;

import java.util.List;

public interface FileService {
    List<File> findAll();

    void save(File File);

    File findByFId(String fid);

    void delete(String fid);

    //查询个人上传的文档
    List<File> findMyFile(String uid);

    //根据名称模糊搜索文档
    List<File> findByName(String fileName);

    //查询自身部门的文档
    List<File> departmentFile(String department);
}
