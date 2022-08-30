package org.springcrazy.common.sensitive;


import lombok.AllArgsConstructor;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.RedisUtil;
import org.springcrazy.modules.web.entity.SensitiveWords;
import org.springcrazy.modules.web.service.ISensitiveWordsService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 初始化敏感词库，将敏感词加入到HashMap中，构建DFA算法模型
 */
@Component
@AllArgsConstructor
public class SensitiveWordInit {

	public HashMap sensitiveWordMap;
	private RedisUtil redisUtil;
	private ISensitiveWordsService sensitiveWordsService;

	/**
	 * 读取敏感词库
	 */
	public Map initKeyWord(){
		try {
			//读取敏感词库
			Set<String> keyWordSet=null;
			sensitiveWordMap = (HashMap)redisUtil.get("sensitiveCache");
			if(Func.isEmpty(sensitiveWordMap)){
				List<SensitiveWords> sensitiveWordsList = sensitiveWordsService.list();
				keyWordSet = new HashSet<String>();
				for(SensitiveWords sensitiveWords:sensitiveWordsList){
					keyWordSet.add(sensitiveWords.getSensitiveWord());
				}
				//将敏感词库加入到HashMap中
				addSensitiveWordToHashMap(keyWordSet);
				redisUtil.set("sensitiveCache",sensitiveWordMap,1, TimeUnit.DAYS);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sensitiveWordMap;
	}

	/**
	 * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
	 * 中 = {
	 *      isEnd = 0
	 *      国 = {<br>
	 *      	 isEnd = 1
	 *           人 = {isEnd = 0
	 *                民 = {isEnd = 1}
	 *                }
	 *           男  = {
	 *           	   isEnd = 0
	 *           		人 = {
	 *           			 isEnd = 1
	 *           			}
	 *           	}
	 *           }
	 *      }
	 *  五 = {
	 *      isEnd = 0
	 *      星 = {
	 *      	isEnd = 0
	 *      	红 = {
	 *              isEnd = 0
	 *              旗 = {
	 *                   isEnd = 1
	 *                  }
	 *              }
	 *      	}
	 *      }
	 * @param keyWordSet  敏感词库
	 */
	private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
		sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
		String key ;
		Map nowMap;
		Map<String, String> newWorMap;
		//迭代keyWordSet
		Iterator<String> iterator = keyWordSet.iterator();
		while(iterator.hasNext()){
			key = iterator.next();    //关键字
			nowMap = sensitiveWordMap;
			for(int i = 0 ; i < key.length() ; i++){
				char keyChar = key.charAt(i);       //转换成char型
				Object wordMap = nowMap.get(keyChar);       //获取

				if(wordMap != null){        //如果存在该key，直接赋值
					nowMap = (Map) wordMap;
				}
				else{     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
					newWorMap = new HashMap<String,String>();
					newWorMap.put("isEnd", "0");     //不是最后一个
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}

				if(i == key.length() - 1){
					nowMap.put("isEnd", "1");    //最后一个
				}
			}
		}
	}

}
