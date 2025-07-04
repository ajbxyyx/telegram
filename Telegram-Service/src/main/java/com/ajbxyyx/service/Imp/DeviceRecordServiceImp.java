package com.ajbxyyx.service.Imp;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.dao.DeviceRecordDao;
import com.ajbxyyx.entity.po.DeviceRecord;
import com.ajbxyyx.entity.vo.DeviceRecordVO;
import com.ajbxyyx.entity.vo.IpParseVO;
import com.ajbxyyx.service.DeviceRecordService;
import com.ajbxyyx.utils.IpParseUtil;
import com.ajbxyyx.utils.Test;
import com.ajbxyyx.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DeviceRecordServiceImp implements DeviceRecordService {

    @Resource
    private IpParseUtil ipParseUtil;
    @Resource
    private DeviceRecordDao deviceRecordDao;
    @Override
    public DeviceRecord recordDevice(HttpServletRequest request,Long uid,String token) throws BusinessException {
        //        //存儲登入設備和IP信息
//        String ip = request.getHeader("X-Real-IP");
        String ip = "38.165.7.48";
        String deviceInfo = parseSecChUa(request.getHeader("sec-ch-ua"));
        String system =parseUserAgent(request.getHeader("user-agent"));
        DeviceRecord deviceRecord = buildDeviceRecord(ip, deviceInfo,system,uid,token);

        deviceRecord.setId(Test.testGetId());

        deviceRecordDao.save(deviceRecord);
        return deviceRecord;
    }

    @Override
    public List<DeviceRecordVO> getAllDeviceRecord(Long uid) throws BusinessException {
        List<DeviceRecord> list = deviceRecordDao.lambdaQuery()
                .eq(DeviceRecord::getUid, uid)
                .list();
        if(list == null){
            throw new BusinessException(500,"unknow error");
        }
        List<DeviceRecordVO> result = list.stream().map(o -> {
            DeviceRecordVO vo = new DeviceRecordVO();
            vo.setId(o.getId());
            vo.setCity(o.getCity());
            vo.setCountry(o.getCountry());
            vo.setDate(o.getDate().getTime());
            vo.setDevice(o.getDevice());
            vo.setSystem(o.getSystemName());
            return vo;
        }).collect(Collectors.toList());

        return result;
    }

    @Override
    public void terminateSession(Long id, Long uid) throws BusinessException {
        DeviceRecord one = deviceRecordDao.lambdaQuery()
                .eq(DeviceRecord::getUid, uid)
                .eq(DeviceRecord::getId, id)
                .one();
        if(one == null){
            throw new BusinessException(500,"unknow error");
        }
        if(one.getToken() == ThreadLocalUtil.getToken()){
            throw new BusinessException(500,"unlegal request");
        }
        deviceRecordDao.removeById(id);
    }


    private DeviceRecord buildDeviceRecord(String ip, String deviceInfo,String ststem, Long uid,String token) {
        IpParseVO ipParseVO = ipParseUtil.parseIp(ip);

        DeviceRecord deviceRecord = new DeviceRecord();

        deviceRecord.setDevice(deviceInfo);
        deviceRecord.setSystemName(ststem);

        deviceRecord.setIp(ip);
        deviceRecord.setLatitude(ipParseVO.getLatitude());
        deviceRecord.setLongitude(ipParseVO.getLongitude());
        deviceRecord.setCity(ipParseVO.getCity());
        deviceRecord.setCountry(ipParseVO.getCountry_name());

        deviceRecord.setUid(uid);
        deviceRecord.setDate(new Date());
        deviceRecord.setToken(token);

        return deviceRecord;
    }
    private String parseUserAgent(String userAgent) throws BusinessException {
        if(userAgent == null){
            throw new BusinessException(403,"bad request");
        }
        if(userAgent.contains("Windows")){
            return "Windows";
        }
        return "Unknow";
    }
    private String parseSecChUa(String secChUa) {

        if (secChUa == null || secChUa.isEmpty()) return "Unknow";

        String[] split = secChUa.split(",");
        if(split.length != 3) return "Unknow";
        secChUa = split[1];
        Pattern pattern = Pattern.compile("\"([^\"]+)\";v=\"(\\d+)\"");
        Matcher matcher = pattern.matcher(secChUa);


        while (matcher.find()) {
            String brand = matcher.group(1);
            String version = matcher.group(2);
            return brand + " " + version;
        }
        return "Unknow";
    }
}
