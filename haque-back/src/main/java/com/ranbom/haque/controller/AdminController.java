package com.ranbom.haque.controller;


import com.alibaba.fastjson.JSONObject;
import com.ranbom.haque.entity.*;
import com.ranbom.haque.service.ArticleService;
import com.ranbom.haque.service.ImageService;
import com.ranbom.haque.service.QuserService;
import com.ranbom.haque.service.TagService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author dingj
 * @create 2020/11/1 10:30
 */

@Controller
@RequestMapping(path = "/admin")
public class AdminController {

    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Value("${file.upload.img.path}")
    private String uploadImgPath;

    @Value("${file.upload.doc.path}")
    private String uploadDocPath;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private QuserService quserService;

    @Autowired
    private TagService tagService;

    @GetMapping("/admin_page")
    public String getAdminPage(Model model) {
        Quser quser = (Quser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("quser", quser);
        return "admin/admin_page";
    }


    @GetMapping("/pages_doc")
    public String getPagesDoc(Model model, Page page) {
        // 拿到当前的quser
        Quser quser = (Quser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("quser", quser);

        int quserId = quser.getId();

        // 设置分页
        page.setLimit(5);
        page.setRows(articleService.findArticleRows(quserId));
        page.setPath("/admin/pages_doc");

        List<Article> articles = articleService.findArticles(quserId, page.getLimit(), page.getOffset());
        model.addAttribute("articles", articles);

        return "admin/pages_doc";
    }


    @GetMapping("/pages_add_doc")
    public String getPagesAddDoc(Model model) {
        Quser quser = (Quser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("quser", quser);
        // 获取所有的tags
        List<Tag> tags = tagService.findTags();
        model.addAttribute("tags", tags);
        return "admin/pages_add_doc";
    }


    @PostMapping("/pages_add_doc")
    public String getPagesAddDoc(@RequestParam("quser_id") int quserId,
                                 @RequestParam("title") String title,
                                 @RequestParam("test-editormd-markdown-doc") String content,
                                 @RequestParam("checkbox") int[] checkbox) {

        for (Integer s : checkbox) {
            logger.info("选中标签：{}", s);
        }
        articleService.addArticle(quserId, title, content, checkbox);
        return "redirect:/admin/pages_doc";
    }


    @PostMapping("/modify_article/{articleId}")
    public String modifyArticle(@RequestParam("title") String title,
                                @RequestParam("content") String content,
                                @PathVariable("articleId") int articleId) {
        articleService.modifyArticle(articleId, title, content);
        return "redirect:/admin/pages_doc";
    }


    @GetMapping("/pages_gallery")
    public String getPagesGallery(Model model) {
        Quser quser = (Quser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("quser", quser);

        List<Image> images = imageService.findImages();
        model.addAttribute("images", images);
        return "admin/pages_gallery";
    }


    @PostMapping("/upload_doc")
    public String SingleFileUpLoad(@RequestParam("file[]") MultipartFile[] files, Model model) {
        Quser quser = (Quser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int quserId = quser.getId();

        if (files == null || files.length == 0) {
            model.addAttribute("uploadMsg", "请至少选择一个文件");
            return "admin/pages_doc";
        }

        //创建输入输出流
        InputStream inputStream = null;
        OutputStream outputStream = null;

        for (MultipartFile file : files) {
            try {
                //获取文件的输入流
                inputStream = file.getInputStream();
                //获取上传时的文件名
                String fileName = file.getOriginalFilename();
                //注意是路径+文件名
                File targetFile = new File(uploadDocPath + "/"  + fileName);
                //如果之前的 String path = "d:/upload/" 没有在最后加 / ，那就要在 path 后面 + "/"

                //判断文件父目录是否存在
                if (!targetFile.getParentFile().exists()) {
                    //不存在就创建一个
                    targetFile.getParentFile().mkdir();
                }

                //获取文件的输出流
                outputStream = new FileOutputStream(targetFile);
                //最后使用资源访问器FileCopyUtils的copy方法拷贝文件
                FileCopyUtils.copy(inputStream, outputStream);

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile), "UTF-8"));
                String line = null;
                String mdContent = "";
                while ((line = br.readLine()) != null) {
                    mdContent += line + "\r\n";
                }

                articleService.addArticle(quserId, fileName, mdContent, new int[1]);

            } catch (IOException e) {
                e.printStackTrace();
                //出现异常，则告诉页面失败
                model.addAttribute("uploadMsg", "上传失败");
            } finally {
                //无论成功与否，都有关闭输入输出流
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //告诉页面上传成功了
        model.addAttribute("uploadMsg", "上传成功");
        return "redirect:/admin/pages_doc";
    }


    @PostMapping("/upload_img")
    @ResponseBody
    public String uploadImg(@RequestParam("file")MultipartFile img, Model model, HttpServletRequest request) throws IOException {
        JSONObject json = new JSONObject();

        // 判断图片是否为空
        if (img == null) {
            json.put("code", 1);
            json.put("msg", "未选择图片");
        }
        //创建输入输出流
        InputStream inputStream = null;
        OutputStream outputStream = null;

        inputStream = img.getInputStream();
        // 获取文件名
        String fileName = img.getOriginalFilename();
        // 获取图片后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "图片文件的格式不正确");
            return "admin/pages_gallery";
        }

        // 通过UUID生成图片的文件名
        String imgName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;

        // 定义图片上传路径
        String path = uploadImgPath;
        File filePath = new File(path + "/" + imgName);
        // 判断路径是否存在
        if (!filePath.getParentFile().exists()) {
            filePath.getParentFile().mkdir();
        }

        try {

            outputStream = new FileOutputStream(filePath);
            FileCopyUtils.copy(inputStream, outputStream);
            logger.info(String.valueOf(filePath));
            imageService.addImage(imgName);
            json.put("code", 0);
            json.put("msg", "上传成功");

            return json.toJSONString();

        } catch (IOException e) {
            throw new RuntimeException("上传文件失败，服务器发生异常！", e);
        }
    }


    @GetMapping("/img/{filename}")
    public void getAvatar(@PathVariable("filename") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadImgPath + "/" + fileName;
        // 文件的后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);

        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {

        }

    }


    @GetMapping("/delete_img/{img_url}")
    public String deleteImg(@PathVariable("img_url") String imgUrl) {
        imageService.deleteImage(imgUrl);
        return "redirect:/admin/pages_gallery";
    }


    @GetMapping("/delete_article")
    public String deleteArticle(@RequestParam(name = "id") int id) {
        articleService.deleteArticleById(id);
        return "redirect:/admin/pages_doc";
    }

    @PostMapping("/upload_avatar")
    @ResponseBody
    public String uploadAvatar(@RequestParam("file")MultipartFile img, Model model, HttpServletRequest request) {
        // 拿到当前的quser
        Quser quser = (Quser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        JSONObject json = new JSONObject();

        // 判断图片是否为空
        if (img == null) {
            json.put("code", 1);
            json.put("avatar_url", "");
            json.put("msg", "未选择图片");
        }

        // 获取文件名
        String fileName = img.getOriginalFilename();
        // 获取图片后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "图片文件的格式不正确");
            return "admin/pages_profile";
        }

        // 通过UUID生成图片的文件名
        String imgName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;

        // 定义图片上传路径
        String path = uploadImgPath;
        File filePath = new File(path, imgName);
        // 判断路径是否存在
        if (!filePath.getParentFile().exists()) {
            filePath.getParentFile().mkdir();
        }

        try {
            img.transferTo(filePath);
//            imageService.addImage(img_name);
            quserService.updateUserAvatar(quser.getId(), imgName);
            quser.setAvatar(imgName);
            json.put("code", 0);
            json.put("quser_avatar", imgName);
            json.put("msg", "上传成功");

            return json.toJSONString();

        } catch (IOException e) {
            throw new RuntimeException("上传文件失败，服务器发生异常！", e);
        }



    }


    @GetMapping("/pages_profile")
    public String profile(Model model) {
        // 拿到当前的quser
        Quser quser = (Quser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("quser", quser);

        return "admin/pages_profile";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal);
        return null;
    }

    @GetMapping("/pages_tag")
    public String getTagPage(Model model) {
        Quser quser = (Quser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("quser", quser);

        List<Tag> tags = tagService.findTags();
        model.addAttribute("tags", tags);
        return "admin/pages_tag";
    }

    @PostMapping("/pages_add_tag")
    public String getPagesAddTag(@RequestParam("tag_name") String tagName) {
        tagService.saveNewTag(tagName);
        return "redirect:/admin/pages_tag";
    }
}
