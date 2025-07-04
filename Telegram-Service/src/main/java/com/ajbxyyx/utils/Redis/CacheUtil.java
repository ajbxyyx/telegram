package com.ajbxyyx.utils.Redis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;


public abstract class CacheUtil<Result> {


    private static final Logger log = LoggerFactory.getLogger(CacheUtil.class);
    private Class<Result> reqClass;


    protected CacheUtil(){//获取handler处理的result类
        ParameterizedType parameterizedType = (ParameterizedType)this.getClass().getGenericSuperclass();
        this.reqClass = (Class<Result>) parameterizedType.getActualTypeArguments()[0];
    }




    public Result getFromCache(Long keySuffixId) {
        return getFromCache(Arrays.asList(keySuffixId)).get(keySuffixId);
    }
    //先查Redis                                  //key后缀

    public Map<Long,Result> getFromCache(List<Long> keySuffixList) {

        List<String> redisKeyList = keySuffixList.stream()
                .map(o -> getRedisKey(o))
                .collect(Collectors.toList());//构建redisKey集合

        //第一轮查询
        List<Result> cache = RedisUtil.mget(redisKeyList, reqClass);
        //key : index
        Map<Long,Integer> needRequeryFromDB = new HashMap<>();
        for (int i = 0;i<keySuffixList.size();i++){
            if(cache.get(i)==null){//缓存中没有数据
                needRequeryFromDB.put(keySuffixList.get(i),i);//需要从DB查
            }
        }
        if(needRequeryFromDB.size() != 0){//需要从数据库里查数据
            synchronized (this){//  加锁
                //再次从redis中查
                cache = RedisUtil.mget(redisKeyList, reqClass);
                needRequeryFromDB = new HashMap<>();
                for (int i = 0;i<keySuffixList.size();i++){
                    if(cache.get(i)==null){
                        needRequeryFromDB.put(Long.valueOf(keySuffixList.get(i)),i);
                    }
                }
                if(needRequeryFromDB.size() != 0){//依旧数据不全
                    // 开始查DB补齐
                    Map<Long,Result> DBresults = queryFromDB(needRequeryFromDB.keySet().stream().toList());


                    //DB查到的结果塞到cache里
                    List<Result> finalCache = cache;
                    needRequeryFromDB.forEach((keyStr, Index)->{
                        if(!DBresults.containsKey(keyStr)){
                            try {
                                DBresults.put(keyStr,reqClass.newInstance());//key不存在 加个空值的key  存入redis
                            }catch (Exception e){

                            }
                        }
                        finalCache.set(Index,DBresults.get(keyStr));//填充数据
                    });
                    cache = finalCache;


                    //构建需要重新存入redis的Map  key:value
                    Map<String, Result> collect = DBresults.entrySet().stream().collect(Collectors.toMap(
                            k -> getRedisKey(k.getKey()),
                            v -> v.getValue()
                    ));
                    RedisUtil.mset(collect,getExpireTime()+getRandomExpireTime());//存入Redis
                }
            }

        }
        HashMap<Long, Result> resultMap = new HashMap<>();
        //组装最终结果
        for(int i = 0;i<cache.size();i++){
            resultMap.put(keySuffixList.get(i),cache.get(i));
        }
        return resultMap;
    }














    /**
     * 防缓存雪崩
     * @return
     */
    private Long getRandomExpireTime(){
        return Long.valueOf((int)(Math.random()*10));
    }

    /**
     * 查id集合对应数据
     * @param ids
     * @return
     */
    public abstract Map<Long,Result> queryFromDB(List<Long> ids);

    /**
     * 获取redis过期时间
     * @return
     */
    public abstract Long getExpireTime();

    public abstract String getRedisKey(Long id);

}
