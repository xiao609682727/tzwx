/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.springcrazy.modules.auth.service;

import cn.hutool.core.convert.Convert;
import com.google.api.client.util.Sets;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springcrazy.common.tool.PageUtil;
import org.springcrazy.core.secure.AuthInfo;
import org.springcrazy.core.secure.provider.IClientDetails;
import org.springcrazy.core.secure.provider.IClientDetailsService;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.constant.CrazyConstant;
import org.springcrazy.core.tool.constant.SystemConstant;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.RedisUtil;
import org.springcrazy.core.tool.utils.WebUtil;
import org.springcrazy.modules.auth.entity.OnlineUser;
import org.springcrazy.modules.auth.publisher.OnlineUserPublisher;
import org.springcrazy.modules.user.entity.Student;
import org.springcrazy.modules.user.service.IStudentService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.springcrazy.core.secure.utils.SecureUtil.extractAndDecodeHeader;

/**

 * @date 2019年10月26日21:56:27
 */
@Service
@Slf4j
@AllArgsConstructor
public class OnlineUserService {

    private final RedisUtil redisUtil;

    private final IClientDetailsService clientDetailsService;

    private IStudentService studentService;
    /**
     * 保存在线用户信息
     * @param  /
     */
    public void save(AuthInfo authInfo){
        HttpServletRequest request = WebUtil.getRequest();

        String[] tokens = extractAndDecodeHeader();
        assert tokens.length == 2;
        String clientId = tokens[0];
//        String clientSecret = tokens[1];

        IClientDetails iClientDetails = clientDetailsService.loadClientByClientId(clientId);
        String ip = WebUtil.getIP(request);
        String browser = WebUtil.getBrowser(request);
        Map<String, Object> cityInfo = WebUtil.getCityInfo(ip);
        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setAddress(Func.toStr(cityInfo.get("addr")));
        onlineUser.setBrowser(browser);
        onlineUser.setIp(ip);
        onlineUser.setLoginTime(new Date());

        Claims claims = SecureUtil.parseJWT(authInfo.getAccessToken());
        Integer userId = Func.toInt(claims.get(SecureUtil.USER_ID));
        Student student = studentService.getById(userId);
        onlineUser.setMobile(student.getMobile());
        onlineUser.setEmail(student.getEmail());
        onlineUser.setAccount(student.getUserName());
        onlineUser.setUserName(authInfo.getUserName());
        onlineUser.setKey(authInfo.getAccessToken());
        redisUtil.set(CrazyConstant.ONLINE_KEY + authInfo.getAccount(), onlineUser, iClientDetails.getAccessTokenValidity());
        //存储用户token
        Set<AuthInfo> authInfoSet = Convert.convert(Set.class,redisUtil.get("token" + authInfo.getAccount())) ;
        if(Objects.isNull(authInfoSet)){
            authInfoSet = Sets.newHashSet();
        }
        authInfoSet.add(authInfo);
        redisUtil.set("token" + authInfo.getAccount(), authInfoSet, 7*24*60*60);

        //更新用户地域信息
        OnlineUserPublisher.publishEvent(authInfo,cityInfo);
    }

    /**
     * 查询全部数据
     * @param filter /
     * @param pageable /
     * @return /
     */
    public Map<String,Object> getAll(String filter, Pageable pageable){
        List<OnlineUser> onlineUserDtos = getAll(filter);
        return PageUtil.toPage(
                PageUtil.toPage(pageable.getPageNumber(),pageable.getPageSize(), onlineUserDtos),
                onlineUserDtos.size()
        );
    }

    /**
     * 查询全部数据，不分页
     * @param filter /
     * @return /
     */
    public List<OnlineUser> getAll(String filter){
        List<String> keys = redisUtil.scan(CrazyConstant.ONLINE_KEY + "*");
        Collections.reverse(keys);
        List<OnlineUser> onlineUserDtos = new ArrayList<>();
        for (String key : keys) {
            //清除redis key 前缀
            key = key.replace(SystemConstant.me().getRedisPrefix(),"");
            OnlineUser onlineUserDto = (OnlineUser) redisUtil.get(key);
            //如果为空则不添加
            if(Func.isEmpty(onlineUserDto)){
                continue;
            }
            if(Func.isNotBlank(filter)){
                if(onlineUserDto.toString().contains(filter)){
                    onlineUserDto.setRedisKey(key);
                    onlineUserDtos.add(onlineUserDto);
                }
            } else {
                onlineUserDto.setRedisKey(key);
                onlineUserDtos.add(onlineUserDto);
            }
        }
        onlineUserDtos.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUserDtos;
    }

    /**
     * 踢出用户
     * @param key /
     */
    public void kickOut(String key){
        redisUtil.del(key);
    }

    /**
     * 退出登录
     * @param token /
     */
    public void logout(String token) {
        String key = CrazyConstant.ONLINE_KEY + token;
        redisUtil.del(key);
    }



    /**
     * 查询用户
     * @param key /
     * @return /
     */
    public OnlineUser getOne(String key) {
        return (OnlineUser)redisUtil.get(key);
    }

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     * @param userName 用户名
     */
    public void checkLoginOnUser(String userName,String igoreToken){
        List<OnlineUser> onlineUserDtos = getAll(userName);
        if(onlineUserDtos ==null || onlineUserDtos.isEmpty()){
            return;
        }
        for(OnlineUser onlineUserDto : onlineUserDtos){
            if(onlineUserDto.getAccount().equals(userName)){
                if(!Func.equals(igoreToken,onlineUserDto.getKey())){
                    this.kickOut(onlineUserDto.getKey());
                }
            }
        }
    }

}
