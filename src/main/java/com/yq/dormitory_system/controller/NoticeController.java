package com.yq.dormitory_system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yq.dormitory_system.model.Notice;
import com.yq.dormitory_system.service.NoticeService;
import com.yq.dormitory_system.tools.ResponseDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 贺哲
 * @2020-02-18 10:51
 */
@RestController
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    @PostMapping("/insertNotice")
    public ResponseDate<Integer> insertNotice(Notice notice) {
        ResponseDate<Integer> responseDate = new ResponseDate();
        try {
            int i = noticeService.insertNotice(notice);
            responseDate.setData(i);
            responseDate.setStatus(true);
            responseDate.setMessage("新增成功");
        } catch (Exception e) {
            responseDate.setStatus(false);
            responseDate.setMessage("新增失败");
        }
        return responseDate;
    }

    @DeleteMapping("/deleteNoticeById")
    public ResponseDate<Integer> deleteNoticeById(@RequestParam("ids") Integer[] ids) {
        ResponseDate<Integer> responseDate = new ResponseDate();
        try {
            int i = noticeService.deleteNoticeById(ids);
            responseDate.setData(i);
            responseDate.setStatus(true);
            responseDate.setMessage("删除成功");
        } catch (Exception e) {
            responseDate.setStatus(false);
            responseDate.setMessage("删除失败");
        }
        return responseDate;
    }

    @GetMapping("/getAllNotice")
    public ResponseDate<List<Notice>> getAllNotice(@RequestParam("page") Integer page,
                                                   @RequestParam("limit") Integer limit,
                                                   @RequestParam(value = "key", required = false) String noticeName) {
        ResponseDate<List<Notice>> responseDate = new ResponseDate();
        try {
            PageHelper.startPage(page, limit);
            List<Notice> notices = noticeService.getAllNotice(noticeName);
            PageInfo<Notice> pageInfo = new PageInfo<>(notices);
            responseDate.setData(notices);
            responseDate.setCount(pageInfo.getTotal());
            responseDate.setMessage("查询成功");
            responseDate.setStatus(true);
        } catch (Exception e) {
            responseDate.setMessage("查询失败");
            responseDate.setStatus(false);
        }
        return responseDate;
    }

    @PutMapping("/updateNotice")
    public ResponseDate<Integer> updateNotice(Notice notice) {
        ResponseDate<Integer> responseDate = new ResponseDate();
        try {
            int i = noticeService.updateNotice(notice);
            responseDate.setData(i);
            responseDate.setStatus(true);
            responseDate.setMessage("修改成功");
        } catch (Exception e) {
            responseDate.setStatus(false);
            responseDate.setMessage("修改失败");
        }
        return responseDate;
    }
}
