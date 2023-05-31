package com.online.yugaofan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.online.yugaofan.constant.UserConstants;
import com.online.yugaofan.domain.entity.IpHistory;
import com.online.yugaofan.domain.entity.SysMenu;
import com.online.yugaofan.domain.result.RouterVo;
import com.online.yugaofan.mapper.IpHistoryMapper;
import com.online.yugaofan.mapper.MenuMapper;
import com.online.yugaofan.service.MenuService;
import com.online.yugaofan.utils.IpUtils;
import com.online.yugaofan.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

@RequiredArgsConstructor
@Service
public class MenuServiceImpl implements MenuService {

    final MenuMapper menuMapper;
    final IpHistoryMapper ipHistoryMapper;
    @Override
    public List<RouterVo> topMenus() {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        // 设置状态可用的 查询所有
        queryWrapper.eq(SysMenu::getStatus, UserConstants.NORMAL);
        // 排序
        queryWrapper.orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> menus = menuMapper.selectList(queryWrapper);
        return buildMenus(getChildPerms(menus, 0));
    }

    @Override
    public List<RouterVo> hotMenus() {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        // 设置状态可用的 查询所有
        queryWrapper.eq(SysMenu::getStatus, UserConstants.NORMAL);
        queryWrapper.eq(SysMenu::getMenuType, UserConstants.TYPE_MENU);
        // 1状态为hot
        queryWrapper.eq(SysMenu::getVisible, UserConstants.EXCEPTION);
        // 排序
        queryWrapper.orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> menus = menuMapper.selectList(queryWrapper);
        return buildMenusRouterVo(menus);
    }

    @Override
    public List<RouterVo> tabMenus(Long id) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        // 设置状态可用的 查询所有
        queryWrapper.eq(SysMenu::getStatus, UserConstants.NORMAL);
        queryWrapper.eq(SysMenu::getMenuType, UserConstants.TYPE_MENU);
        queryWrapper.eq(SysMenu::getParentId, id);
        // 排序
        queryWrapper.orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> menus = menuMapper.selectList(queryWrapper);
        return buildMenusRouterVo(menus);
    }

    @Override
    @Transactional
    public void clickNum(String path) {
        if (StringUtils.isBlank(path)) {
            return;
        }

        if (path.equals("/index") || path.equals("/")) {
            async();
        }

        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getPath, path);
        List<SysMenu> menus = menuMapper.selectList(queryWrapper);
        if (null != menus && menus.size() > 0) {
            SysMenu sysMenu = menus.get(0);
            LambdaUpdateWrapper<SysMenu> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(SysMenu::getMenuId, sysMenu.getMenuId());
            updateWrapper.set(SysMenu::getNum, sysMenu.getNum() + 1L);
            menuMapper.update(null, updateWrapper);
        }
    }

    @Async
    void async() {
        // 判断菜单是否是主页 主页记录ip地址
        IpHistory ipHistory = new IpHistory();
        ServletRequestAttributes requestAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        ipHistory.setIpAddress(IpUtils.getRequestIp(requestAttr.getRequest()));
        ipHistoryMapper.insert(ipHistory);
    }

    private List<RouterVo> buildMenusRouterVo(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setName(menu.getMenuName());
            router.setPath(menu.getPath());
            router.setComponent(menu.getComponent());
            router.setQuery(menu.getQuery());
            router.setDesc(menu.getPerms());
            router.setIcon(menu.getIcon());
            routers.add(router);
        }
        return routers;
    }

    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setName(menu.getMenuName());
            router.setPath(menu.getPath());
            router.setComponent(menu.getComponent());
            router.setQuery(menu.getQuery());
            router.setDesc(menu.getPerms());
            router.setIcon(menu.getIcon());
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu) {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
//    public String getComponent(SysMenu menu) {
//        String component = UserConstants.LAYOUT;
//        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
//            component = menu.getComponent();
//        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
//            component = UserConstants.INNER_LINK;
//        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
//            component = UserConstants.PARENT_VIEW;
//        }
//        return component;
//    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }

}
