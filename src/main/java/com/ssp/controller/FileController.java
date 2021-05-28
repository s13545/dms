package com.ssp.controller;

import com.ssp.entity.File;
import com.ssp.entity.User;
import com.ssp.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    //    展示所有文件
    @GetMapping("showAll")
    public String findAll(Model model){
        List<File> files = fileService.findAll();
        //存入作用域
        model.addAttribute("files",files);
        return "showAllFile";
    }

    //    上传文件
    @PostMapping("upload")
    public String upload(MultipartFile uploadFile, HttpSession session) throws IOException {
        //获取文件的名称
        String fileName = uploadFile.getOriginalFilename();
        //获取文件后缀
        String ext = "." + FilenameUtils.getExtension(uploadFile.getOriginalFilename());
        //文件大小
        long size = uploadFile.getSize();
        //文件类型
        String type = uploadFile.getContentType();
        //处理文件上传
        //先获取文件存储路径，再由日期构建一个文件夹
        String realPath = ResourceUtils.getURL("classpath:").getPath()+"/static/files";
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dateDirPath = realPath + "/" + dateFormat;

        java.io.File dateDir = new java.io.File(dateDirPath);
        if (!dateDir.exists()){
            dateDir.mkdirs();  //如果该日期目录不存在，则创建
        }

        uploadFile.transferTo(new java.io.File(dateDir,fileName));

        //将文件信息放入数据库中
        File userFile = new File();
        userFile.setFileName(fileName);
        userFile.setExt(ext);
        userFile.setType(type);
        userFile.setPath("static/files/" + dateFormat);
        userFile.setSize(String.valueOf(size));

        User user = (User)session.getAttribute("user");
        userFile.setDepartment(user.getDepartment());
        userFile.setUploadPeople(user.getUid());
        fileService.save(userFile);

        return "redirect:/file/showAll";
    }

    //文件下载
    @GetMapping("download")
    public void download(String fid, HttpServletResponse response ) throws IOException {
        //通过文件id找到文件的信息
        File userFile = fileService.findByFId(fid);
        //根据文件信息中的文件名字和存储路径获取文件输入流
        String realPath = ResourceUtils.getURL("classpath:").getPath()+"/"+userFile.getPath();
        //获取文件输入流
        FileInputStream is = new FileInputStream(new java.io.File(realPath, userFile.getFileName()));
        //以附件的形式下载
        response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode(userFile.getFileName(),"UTF-8"));
        //获取响应输出流
        ServletOutputStream os = response.getOutputStream();
        //文件拷贝
        IOUtils.copy(is,os);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
    }

    //文件在线打开
    @GetMapping("open")
    public void open(String fid,HttpServletResponse response ) throws IOException {
        //通过文件id找到文件的信息
        File userFile = fileService.findByFId(fid);
        //根据文件信息中的文件名字和存储路径获取文件输入流
        String realPath = ResourceUtils.getURL("classpath:").getPath()+"/"+userFile.getPath();
        //获取文件输入流
        FileInputStream is = new FileInputStream(new java.io.File(realPath, userFile.getFileName()));
        //将文件在线打开
        response.setHeader("content-disposition","inline;filename="+ URLEncoder.encode(userFile.getFileName(),"UTF-8"));
        //获取响应输出流
        ServletOutputStream os = response.getOutputStream();
        //文件拷贝
        IOUtils.copy(is,os);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
    }

    //删除文件信息
    @GetMapping("delete")
    public String delete(String fid) throws FileNotFoundException {
        //根据id查找文件
        File userFile = fileService.findByFId(fid);
        //删除文件
        String realPath = ResourceUtils.getURL("classpath:").getPath()+"/"+userFile.getPath();
        System.out.println(realPath);
        java.io.File file = new java.io.File(realPath,userFile.getFileName());
        if (file.exists()){
            file.delete();
        }
        //删除数据库中信息
        fileService.delete(fid);
        return "redirect:/file/showAll";
    }

    //跳转至我的文档界面
    @GetMapping("/toMyFile")
    public String toMyFile(Model model,HttpSession session){
        User user = (User)session.getAttribute("user");
        List<File> files = fileService.findMyFile(user.getUid());
        model.addAttribute("MyFiles",files);
        return "myFile";
    }

    //跳转至检索文档界面
    @GetMapping("/toFindFile")
    public String toFindFile(){
        return "findFile";
    }

    //根据名称搜索文档
    @PostMapping("/findByName")
    public String findByName(String fileName,Model model){
        List<File> files = fileService.findByName(fileName);
        if (files.size()==0){
            model.addAttribute("fMsg","未搜索到相关文档");
            return "findFile";
        }else {
            model.addAttribute("fMsg","搜索完成！");
            model.addAttribute("findFiles",files);
            return "findFile";
        }
    }

    //跳转至部门文档页面
    @GetMapping("/toDepartmentFile")
    public String toDepartmentFile(HttpSession session,Model model){
        User user = (User)session.getAttribute("user");
        String department = user.getDepartment();
        List<File> files = fileService.departmentFile(department);
        model.addAttribute("departmentFiles",files);
        return "departmentFiles";
    }
}
