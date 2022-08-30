/**
 * Copyright (c) 2018-2028, Clear HuaiY  (huaiyong198608@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springcrazy.modules.auth.event;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springcrazy.core.launch.constant.TokenConstant;
import org.springcrazy.core.secure.AuthInfo;
import org.springcrazy.core.secure.registry.SecureRegistry;
import org.springcrazy.core.secure.utils.SecureUtil;
import org.springcrazy.core.tool.utils.DateUtil;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.auth.service.OnlineUserService;
import org.springcrazy.modules.web.entity.StatUserArea;
import org.springcrazy.modules.web.service.IStatUserAreaService;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 异步监听在线用户记录事件

 */
@Slf4j
@AllArgsConstructor
@Component
public class OnlineUserListener {

	private OnlineUserService onlineUserService;
	private SecureRegistry secureRegistry;
	private IStatUserAreaService iStatUserAreaService;

	@Async
	@Order
	@EventListener(OnlineUserEvent.class)
	public void saveOnlineUser(OnlineUserEvent event) {
		Map<String, Object> source = (Map<String, Object>) event.getSource();
		Map<String, Object> cityInfo = (Map<String, Object>) source.get("cityInfo");
		AuthInfo authInfo = (AuthInfo) source.get("authInfo");

		//单点判断
		if(secureRegistry.isSingleLogin()){
			//踢掉之前已经登录的token
			onlineUserService.checkLoginOnUser(authInfo.getAccount(),authInfo.getAccessToken());
		}
		//根据登录用户获取用户区域信息进行保存
		Claims claims = SecureUtil.parseJWT(authInfo.getAccessToken());
		Integer userId = Func.toInt(claims.get(TokenConstant.USER_ID));
		StatUserArea statUserArea = new StatUserArea();
		statUserArea.setUserId(userId);
		StatUserArea area = iStatUserAreaService.getOne(new QueryWrapper<StatUserArea>(statUserArea));
		if(Func.isNull(area)){
			area = statUserArea;
		}
		area.setUpdateTime(DateUtil.now());
		area.setIp(Func.toStr(cityInfo.get("ip")));
		area.setArea(Func.toInt(cityInfo.get("regionCode")));
		area.setCity(Func.toInt(cityInfo.get("cityCode")));
		area.setProvince(Func.toInt(cityInfo.get("proCode")));
		iStatUserAreaService.saveOrUpdate(area);
	}

}
