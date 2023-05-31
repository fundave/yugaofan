package com.online.yugaofan.controller.index;

import com.online.yugaofan.domain.base.BaseResponse;
import com.online.yugaofan.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 菜单信息
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {

    final MenuService menuService;
    /***
     * 首页菜单
     * @return 结果
     */
    @GetMapping(value = "/topMenus")
    public BaseResponse topMenus() {
       return BaseResponse.ok(menuService.topMenus());
    }

    /***
     * 推荐菜单
     * @return 结果
     */
    @GetMapping(value = "/hotMenus")
    public BaseResponse hotMenus() {
        return BaseResponse.ok(menuService.hotMenus());
    }

    /***
     * 子页面菜单
     * @return 结果
     */
    @GetMapping(value = "/tabMenus/{id}")
    public BaseResponse tabMenus(@PathVariable("id") Long id) {
        return BaseResponse.ok(menuService.tabMenus(id));
    }

    /***
     * 菜单点击次数
     * @return 结果
     */
    @GetMapping(value = "/click")
    public void clickNum(@RequestParam("path") String path) {
        menuService.clickNum(path);
    }

}
