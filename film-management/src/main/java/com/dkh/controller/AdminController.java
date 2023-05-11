package com.dkh.controller;

import com.dkh.dto.Result;
import com.dkh.pojo.Admin;
import com.dkh.service.AdminService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dkh
 * @since 2023-01-08
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @PostMapping("/login")
    public Result login(String username, String password) {
        return adminService.login(username, password);
    }

    @PutMapping("/changePwd")
    public Result changePwd(String token, String oldPassword, String newPassword) {
        System.out.println("token = " + token);
        System.out.println("oldPassword = " + oldPassword);
        System.out.println("newPassword = " + newPassword);
        return adminService.updatePwd(token, oldPassword, newPassword);
    }

    @GetMapping("/getAdminList/{current}/{size}")
    public Result getAdminList(@PathVariable Integer current, @PathVariable Integer size) {
        return adminService.getAdminList(current, size);
    }

    @GetMapping("/getAdminTotal")
    public Result getAdminTotal() {

        return adminService.getAdminTotal();
    }

    @PutMapping("/updateAdmin")
    public Result updateAdmin(@RequestBody Admin admin) {
        return adminService.updateAdmin(admin);
    }

    @DeleteMapping("/deleteAdmin/{id}")
    public Result deleteAdmin(@PathVariable Long id){
        return adminService.deleteAdmin(id);
    }

}
