//package com.ajbxyyx.utils.Redis.CacheHandlers;
//
//import com.ajbxyyx.constant.RedisKey;
//import com.ajbxyyx.entity.vo.UrlParseVO;
//import com.ajbxyyx.entity.vo.UserVO;
//import com.ajbxyyx.utils.Redis.CacheUtil;
//
//import java.util.List;
//import java.util.Map;
//
//public class UrlParseHandler  extends CacheUtil<UrlParseVO> {
//    @Override
//    public Map<Long, UrlParseVO> queryFromDB(List<String> urls) {
//
//        return Map.of();
//    }
//
//    @Override
//    public Long getExpireTime() {
//        return 100L;
//    }
//
//    @Override
//    public String getRedisKey(String url) {
//        return RedisKey.UrlParseKey(url);
//    }
//}
