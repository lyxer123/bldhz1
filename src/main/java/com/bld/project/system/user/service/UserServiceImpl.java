package com.bld.project.system.user.service;

import com.alibaba.fastjson.JSONObject;
import com.bld.common.constant.UserConstants;
import com.bld.common.exception.BusinessException;
import com.bld.common.utils.StringUtils;
import com.bld.common.utils.security.ShiroUtils;
import com.bld.common.utils.text.Convert;
import com.bld.framework.aspectj.lang.annotation.DataScope;
import com.bld.framework.aspectj.lang.annotation.TokenNotNull;
import com.bld.framework.shiro.service.PasswordService;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.system.config.service.IConfigService;
import com.bld.project.system.post.domain.Post;
import com.bld.project.system.post.mapper.PostMapper;
import com.bld.project.system.role.domain.Role;
import com.bld.project.system.role.mapper.RoleMapper;
import com.bld.project.system.user.domain.ThingsboardUser;
import com.bld.project.system.user.domain.User;
import com.bld.project.system.user.domain.UserPost;
import com.bld.project.system.user.domain.UserRole;
import com.bld.project.system.user.mapper.UserMapper;
import com.bld.project.system.user.mapper.UserPostMapper;
import com.bld.project.system.user.mapper.UserRoleMapper;
import com.bld.project.utils.TbApiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.bld.project.utils.TbApiUtils.*;

/**
 * 用户 业务层处理
 * 
 * @author bld
 */
@Service
public class UserServiceImpl implements IUserService
{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PostMapper postMapper;

    @Resource
    private UserPostMapper userPostMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Autowired
    private IConfigService configService;

    @Autowired
    private PasswordService passwordService;

    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<User> selectUserList(User user)
    {
        // 生成数据权限过滤条件
        return userMapper.selectUserList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<User> selectAllocatedList(User user)
    {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<User> selectUnallocatedList(User user)
    {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public User selectUserByLoginName(String userName)
    {
        return userMapper.selectUserByLoginName(userName);
    }

    /**
     * 通过手机号码查询用户
     * 
     * @param phoneNumber 手机号码
     * @return 用户对象信息
     */
    @Override
    public User selectUserByPhoneNumber(String phoneNumber)
    {
        return userMapper.selectUserByPhoneNumber(phoneNumber);
    }

    /**
     * 通过邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户对象信息
     */
    @Override
    public User selectUserByEmail(String email)
    {
        return userMapper.selectUserByEmail(email);
    }

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public User selectUserById(Long userId)
    {
        return userMapper.selectUserById(userId);
    }

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteUserById(Long userId)
    {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(String ids) throws BusinessException
    {
        Long[] userIds = Convert.toLongArray(ids);
        for (Long userId : userIds)
        {
            checkUserAllowed(new User(userId));
        }
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 新增保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(User user)
    {
        user.randomSalt();
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setCreateBy(ShiroUtils.getLoginName());
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 修改保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(User user)
    {
        Long userId = user.getUserId();
        user.setUpdateBy(ShiroUtils.getLoginName());
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户个人详细信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserInfo(User user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetUserPwd(User user)
    {
        user.randomSalt();
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        return updateUserInfo(user);
    }

    /**
     * 新增用户角色信息
     * 
     * @param user 用户对象
     */
    public void insertUserRole(User user)
    {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles))
        {
            // 新增用户与角色管理
            List<UserRole> list = new ArrayList<UserRole>();
            for (Long roleId : user.getRoleIds())
            {
                UserRole ur = new UserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     * 
     * @param user 用户对象
     */
    public void insertUserPost(User user)
    {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts))
        {
            // 新增用户与岗位管理
            List<UserPost> list = new ArrayList<UserPost>();
            for (Long postId : user.getPostIds())
            {
                UserPost up = new UserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0)
            {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 校验登录名称是否唯一
     * 
     * @param loginName 用户名
     * @return
     */
    @Override
    public String checkLoginNameUnique(String loginName)
    {
        int count = userMapper.checkLoginNameUnique(loginName);
        if (count > 0)
        {
            return UserConstants.USER_NAME_NOT_UNIQUE;
        }
        return UserConstants.USER_NAME_UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(User user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        User info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.USER_PHONE_NOT_UNIQUE;
        }
        return UserConstants.USER_PHONE_UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(User user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        User info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.USER_EMAIL_NOT_UNIQUE;
        }
        return UserConstants.USER_EMAIL_UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     * 
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(User user)
    {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin())
        {
            throw new BusinessException("不允许操作超级管理员用户");
        }
    }

    /**
     * 查询用户所属角色组
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(Long userId)
    {
        List<Role> list = roleMapper.selectRolesByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (Role role : list)
        {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(Long userId)
    {
        List<Post> list = postMapper.selectPostsByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (Post post : list)
        {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 导入用户数据
     * 
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    @Override
    public String importUser(List<User> userList, Boolean isUpdateSupport)
    {
        if (StringUtils.isNull(userList) || userList.size() == 0)
        {
            throw new BusinessException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String operName = ShiroUtils.getLoginName();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (User user : userList)
        {
            try
            {
                // 验证是否存在这个用户
                User u = userMapper.selectUserByLoginName(user.getLoginName());
                if (StringUtils.isNull(u))
                {
                    user.setPassword(password);
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getLoginName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getLoginName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new BusinessException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 用户状态修改
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int changeStatus(User user)
    {
        return userMapper.updateUser(user);
    }



    @TokenNotNull
    @Override
    public ResultInfo getCurrentUserInfo() {
        User sysUser = ShiroUtils.getSysUser();
        /*因为获取sysUser时已经处理过thingsboard的Token空判，所以不用再次判断*/
        String s = OkHttpUtil.get(getCurrentUserInfoApi(), null, sysUser.getAuthorization());
        System.out.println("qingiqu");
        ThingsboardUser thingsboardUser;
        try {
            thingsboardUser = JSONObject.parseObject(s, ThingsboardUser.class);
            if (thingsboardUser == null){
                return ResultInfo.error("获取用户信息失败，请稍后再试");
            }
        } catch (Exception e) {
            log.error("****************************获取当前登录用户信息失败*************************");
            e.printStackTrace();
            return ResultInfo.error("获取用户信息失败，请稍后再试");
        }
        sysUser.setThingsboardUser(thingsboardUser);
        ShiroUtils.setSysUser(sysUser);
        return ResultInfo.success(thingsboardUser);
    }

    @TokenNotNull
    @Override
    public ResultInfo updateUserInfo(JSONObject json) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", ShiroUtils.getTbToken());
        return OkHttpUtil.bldPostJsonParams(updateUserInfoApi(), json.toJSONString(), headerMap);
    }

    @Override
    public ResultInfo addUserInfo(ThingsboardUser tbUser) {
        String s = tbUser.addCheck();
        if (s != null){
            return ResultInfo.error(s);
        }
        String url = addUserInfoApi(tbUser.isSendActivationMail());
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", ShiroUtils.getTbToken());
        return OkHttpUtil.bldPostJsonParams(url, JSONObject.toJSONString(tbUser), headerMap);
    }

    @Override
    public ResultInfo deleteUserInfo(String tbUserId) {
        String s = deleteUserInfoApi(tbUserId);
        return OkHttpUtil.bldDeleteJsonParams(s, ShiroUtils.getTbToken());
    }


    @Override
    public ResultInfo modifyPassword(String newPassword, String oldPassword) {
        String url = TbApiUtils.modifyPassword();
        JSONObject paramJ = new JSONObject();
        paramJ.put("currentPassword", oldPassword);
        paramJ.put("newPassword", newPassword);
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", ShiroUtils.getTbToken());
        ResultInfo<JSONObject> resultInfo = OkHttpUtil.bldPostJsonParams(url, paramJ.toJSONString(), headerMap);
        if (!resultInfo.isSuccess()){
            String message = resultInfo.getMessage();
            if (message.contains("Current password doesn't match")){
                resultInfo.setMessage("请输入正确的密码");
            }
        }
        return resultInfo;
    }

    @Override
    public ResultInfo getUserActivationUrl(String tbUserId) {
        if (StringUtils.isBlank(tbUserId)){
            return ResultInfo.error("没有获取到用户id，请稍后再试");
        }
        String url = TbApiUtils.getUserActivationUrl(tbUserId);
        JSONObject j = get(url);
        return j == null ? ResultInfo.error("获取激活链接失败") : ResultInfo.success(j, "获取激活链接成功");
    }

    @Override
    public ResultInfo getUserListByCustomerId(Integer limit, String search, String customerId) {
        if (StringUtils.isBlank(customerId)){
            return ResultInfo.error("客户ID不能为空");
        }
        String url = getUserListByCustomerIdApi(limit, search, customerId);
        JSONObject j = TbApiUtils.get(url);
        return j.get("data") == null ? ResultInfo.error("没有查询到数据") : ResultInfo.success(j.get("data"));
    }

    @Override
    public ResultInfo modifyUserStatus(String userId, Boolean aBoolean) {
        String url = modifyUserStatusApi(userId, aBoolean);
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", ShiroUtils.getTbToken());
        ResultInfo resultInfo = OkHttpUtil.bldPostJsonParams(url, headerMap);
        String message = resultInfo.getMessage();
        if ("Enabled user credentials should have password!".equalsIgnoreCase(message)){
            resultInfo.setMessage("先将该账号设置密码后才能启用该账号");
        }
        return resultInfo;
    }

    @Override
    public ResultInfo getUrl(String userId) {
        return TbApiUtils.bldGet(getUrlApi(userId));
    }

    @Override
    public ResultInfo loginUser(String loginUserId) {
        String url = loginUserApi(loginUserId);
        JSONObject jsonObject = get(url);
        return null;
    }
}
