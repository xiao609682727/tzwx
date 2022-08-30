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
package org.springcrazy.modules.auth.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springcrazy.core.tool.api.R;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.modules.auth.service.OnlineUserService;
import org.springcrazy.modules.user.service.IStudentService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**

 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/online")
@Api(tags = "系统：在线用户管理")
public class OnlineController {

    private final OnlineUserService onlineUserService;
    private final IStudentService studentService;

    @ApiOperation("查询在线用户")
    @GetMapping("/list")
    public R<Object> getAll(String account, Pageable pageable){
        return R.data(onlineUserService.getAll(account, pageable));
    }


    @ApiOperation("踢出用户")
    @PostMapping("/remove")
    public R<Object> delete(@RequestParam String ids) throws Exception {
        List<String> keys = Func.toStrList(ids);
        for (String key : keys) {
            onlineUserService.kickOut(key);
        }
        return R.data(HttpStatus.OK);
    }

    @ApiOperation("冻结用户")
    @PostMapping("/update")
    public R<Object> update(@RequestParam String ids) throws Exception {
        List<String> keys = Func.toStrList(ids);
        for (String key : keys) {
            studentService.updateAvalible(key);
            onlineUserService.kickOut(key);
        }
        return R.data(HttpStatus.OK);
    }
}
