package com.ssp.dao;

import com.ssp.entity.File;

import java.util.List;

public interface FileDao {
    //查询所有文件
    List<File> findAll();

    //保存用户的文件信息
    void save(File userFile);

    //通过文件id查找文件
    File findByFId(String fid);

    //通过文件id删除文件
    void delete(String fid);

    //通过上传人员的id查找文件
    List<File> findByUId(String uid);

    //通过部门查找文件
    List<File> findByDepartment(String department);

    //根据文件名称模糊搜索文件
    List<File> findByName(String fileName);
}
