package com.online.yugaofan.service;

import com.online.yugaofan.domain.result.RouterVo;
import java.util.List;

public interface MenuService {

    /**
     * 获取首页菜单信息
     * @return 结果
     */
    List<RouterVo> topMenus();

    /**
     * 推荐菜单
     * @return 结果
     */
    List<RouterVo> hotMenus();

    /**
     * 根据id查询所有菜单
     * @param id 编号
     * @return 结果
     */
    List<RouterVo> tabMenus(Long id);

    /**
     * 添加菜单点击次数
     * @param path 菜单路径
     */
    void clickNum(String path);
}
