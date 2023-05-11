package com.dkh.service;

import com.dkh.dto.Result;
import com.dkh.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dkh
 * @since 2023-01-08
 */
public interface AdminService extends IService<Admin> {

    Result login(String username, String password);

    Result updatePwd(String token, String oldPassword, String newPassword);

    Result updateAdmin(Admin admin);

    Result deleteAdmin(Long id);

    Result getAdminList(Integer current, Integer size);

    Result getAdminTotal();
}
