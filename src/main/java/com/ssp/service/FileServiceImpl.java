package com.ssp.service;

import com.ssp.dao.FileDao;
import com.ssp.entity.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class FileServiceImpl implements FileService{

    @Autowired
    private FileDao fileDao;

    @Override
    public List<File> findAll() {
        return fileDao.findAll();
    }

    @Override
    public void save(File file) {
        //先创建一个fid
        UUID fid = UUID.randomUUID();
        file.setFid(String.valueOf(fid));
        //上传时间
        file.setUploadTime(new Date());
        fileDao.save(file);
    }

    @Override
    public File findByFId(String fid) {
        return fileDao.findByFId(fid);
    }

    @Override
    public void delete(String fid) {
        fileDao.delete(fid);
    }

    @Override
    public List<File> findMyFile(String uid) {
        List<File> files = fileDao.findByUId(uid);
        if (files.contains(null)){
            files.remove(null);
        }
        return files;
    }

    @Override
    public List<File> findByName(String fileName) {
        List<File> files = fileDao.findByName(fileName);
        if (files.contains(null)){
            files.remove(null);
        }
        return files;
    }

    @Override
    public List<File> departmentFile(String department) {
        List<File> files = fileDao.findByDepartment(department);
        if (files.contains(null)){
            files.remove(null);
        }
        return files;
    }
}
