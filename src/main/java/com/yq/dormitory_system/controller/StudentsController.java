package com.yq.dormitory_system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yq.dormitory_system.model.Students;
import com.yq.dormitory_system.model.User;
import com.yq.dormitory_system.service.StudentsService;
import com.yq.dormitory_system.service.UserService;
import com.yq.dormitory_system.tools.ResponseDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 贺哲
 * @2020-02-19 19:50
 */
@RestController
public class StudentsController {
    @Autowired
    private StudentsService studentsService;

    @Autowired
    private UserService userService;

    @PostMapping("/insertStudents")
    public ResponseDate<Integer> insertStudents(Students students) {
        User user = new User();
        user.setUsername(students.getStuName()).setPassword("123456").setUNum(students.getSid());
        ResponseDate<Integer> responseDate = new ResponseDate();
        students.setCheckInTime("");
        students.setIn(false);
        students.setLate(false);
        try {
            int i = studentsService.insertStudents(students);
            userService.insertUser(user); //新增登录表
            responseDate.setData(i);
            responseDate.setStatus(true);
            responseDate.setMessage("新增成功");
        } catch (Exception e) {
            responseDate.setStatus(false);
            responseDate.setMessage("新增失败");
        }
        return responseDate;
    }

    @DeleteMapping("/deleteStudentsById")
    public ResponseDate<Integer> deleteStudentsById(@RequestParam("ids") Integer[] ids) {
        ResponseDate<Integer> responseDate = new ResponseDate();
        try {
            int i = studentsService.deleteStudentsById(ids);
            responseDate.setData(i);
            responseDate.setStatus(true);
            responseDate.setMessage("删除成功");
        } catch (Exception e) {
            responseDate.setStatus(false);
            responseDate.setMessage("删除失败");
        }
        return responseDate;
    }

    @GetMapping("/getAllStudents")
    public ResponseDate<List<Students>> getAllStudents(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "key", required = false) String stuName) {
        ResponseDate<List<Students>> responseDate = new ResponseDate();
        try {
            PageHelper.startPage(page, limit);
            List<Students> students = studentsService.getAllStudents(stuName);
            PageInfo<Students> pageInfo = new PageInfo<>(students);
            responseDate.setData(students);
            responseDate.setCount(pageInfo.getTotal());
            responseDate.setMessage("查询成功");
            responseDate.setStatus(true);
        } catch (Exception e) {
            responseDate.setMessage("查询失败");
            responseDate.setStatus(false);
        }
        return responseDate;
    }

    @PutMapping("/updateStudents")
    public ResponseDate<Integer> updateStudents(Students students) {
        ResponseDate<Integer> responseDate = new ResponseDate();
        try {
            int i = studentsService.updateStudents(students);
            responseDate.setData(i);
            responseDate.setStatus(true);
            responseDate.setMessage("修改成功");
        } catch (Exception e) {
            responseDate.setStatus(false);
            responseDate.setMessage("修改失败");
        }
        return responseDate;
    }

    @GetMapping("/countNum")
    public ResponseDate<Map<Object, Object>> countNum() {
        ResponseDate<Map<Object, Object>> responseDate = new ResponseDate();
        Map<Object, Object> map = new HashMap<>();
        try {
            int allPeople = studentsService.allPeople();
            int signedIn = studentsService.signedIn();
            int noSignedIn = studentsService.noSignedIn();
            map.put("allPeople", allPeople);
            map.put("signedIn", signedIn);
            map.put("noSignedIn", noSignedIn);
            responseDate.setData(map);
            responseDate.setStatus(true);
            responseDate.setMessage("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            responseDate.setStatus(false);
            responseDate.setMessage("查询失败");
        }
        return responseDate;
    }

    /**
     * 点击签到
     *
     * @param sid 学号
     */
    @PutMapping("/signIn")
    public ResponseDate<Integer> signIn(@RequestParam("key") String sid) {
        ResponseDate<Integer> responseDate = new ResponseDate();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        Students students = new Students();
        students.setSid(sid).setCheckInTime(hour + ":" + minute);
        students.setIn(true);  //点击签到之后默认改变为归寝
        students.setLate((hour > 22) ? false : true);  //默认22：00为就寝时间
        try {
            int i = studentsService.signIn(students);
            responseDate.setData(i);
            responseDate.setStatus(true);
            responseDate.setMessage("签到成功");
        } catch (Exception e) {
            responseDate.setStatus(false);
            responseDate.setMessage("签到失败");
        }
        return responseDate;
    }
}
