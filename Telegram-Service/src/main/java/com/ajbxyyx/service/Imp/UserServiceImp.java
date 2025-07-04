package com.ajbxyyx.service.Imp;

import com.ajbxyyx.common.Exception.BusinessException;
import com.ajbxyyx.dao.DeviceRecordDao;
import com.ajbxyyx.dao.UserDao;
import com.ajbxyyx.entity.dto.SaveProfileDTO;
import com.ajbxyyx.entity.enums.PrivacyEnum;
import com.ajbxyyx.entity.enums.PrivacyLevelEnum;
import com.ajbxyyx.entity.po.Contact;
import com.ajbxyyx.entity.po.DeviceRecord;
import com.ajbxyyx.entity.po.User;
import com.ajbxyyx.entity.vo.LoginUserVO;
import com.ajbxyyx.entity.vo.UserPrivacyVO;
import com.ajbxyyx.entity.vo.UserVO;
import com.ajbxyyx.service.UserService;
import com.ajbxyyx.utils.Redis.CacheHandlers.UserCacheHandler;
import com.ajbxyyx.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ajbxyyx.entity.vo.LoginUserVO.buildLoginUserVoWithoutToken;

@Service
public class UserServiceImp implements UserService {


    @Resource
    private UserDao userDao;
    @Resource
    private DeviceRecordDao deviceRecordDao;
    @Resource
    private UserCacheHandler userCacheHandler;


    @Resource
    @Lazy
    private ContactServiceImp contactServiceImp;


    /**
     * update user last seen time
     * @param uid
     * @param lastSeenTime
     */
    @Override
    public void updateLastSeenTime(Long uid, Long lastSeenTime) {
        boolean update = userDao.lambdaUpdate()
                .eq(User::getId, uid)
                .set(User::getLastSeen, 0)
                .update();
    }

    /**
     * get userPO by mobile number
     * @param country
     * @param phone
     * @return
     */
    @Override
    public User queryByMobileNumber(String country, String phone) {
        User user = userDao.lambdaQuery()
                .eq(User::getPhoneCountry, country)
                .eq(User::getPhone, phone)
                .one();
        return user;
    }

    /**
     * query redis -> null ? query db : redis
     * @param uidList
     * @param uid
     * @return
     */
    @Override
    public Map<Long, UserVO> getUserBaseInfo(List<Long> uidList,Long uid){

        Map<Long, UserVO> fromCache = userCacheHandler.getFromCache(uidList);


        Map<Long, User> userFromDB = userDao.lambdaQuery()
                .in(User::getId, uidList)
                .list().stream().collect(Collectors.toMap(k -> k.getId(), v -> v));

        //封裝UserVO
        Map<Long, UserVO> result = fromCache.entrySet().stream()
                .collect(Collectors.toMap(o -> o.getKey(), o -> {
                    if (o.getValue().getUid() == null) {
                        return o.getValue();
                    }

                    UserVO userVO = o.getValue();
                    //获取关系
                    Contact contact = contactServiceImp.getContactByUid(uid, userVO.getUid());
                    PrivacyLevelEnum relationship = getRelationship(contact);

                    userVO.setRelationship(contact==null?1:2);//1陌生人 2联系人

                    userVO = privacyFilter(userFromDB.get(userVO.getUid()), userVO, relationship);//隱私信息設置
                    if (relationship == PrivacyLevelEnum.MY_CONTACTS) {//設置聯係人姓名
                        userVO.setFirstName(contact.getFirstName());
                        userVO.setLastName(contact.getLastName());
                    }

                    return userVO;
                }));
        return result;
    }



    @Override
    public void privacyModify(PrivacyEnum privacyEnum, PrivacyLevelEnum privacyLevelEnum) throws BusinessException {
        User user = userDao.getById(ThreadLocalUtil.getUid());

        Integer privacyLevel = privacyLevelEnum.getLevel();
        switch (privacyEnum){
            case PHONE_NUMBER ->user.setPhonePrivacy(privacyLevel);
            case LAST_SEEN_ONLINE -> user.setLastSeenPrivacy(privacyLevel);
            case PROFILE_PHOTOS -> user.setAvatarPrivacy(privacyLevel);
            case BIO -> user.setBioPrivacy(privacyLevel);
            case DATE_OF_BIRTH -> user.setDateOfBirthPrivacy(privacyLevel);

            case CALLS -> user.setCallsPrivacy(privacyLevel);
            case VOICE_VIDEO_MESSAGE -> user.setVoiceVideoMessagesPrivacy(privacyLevel);
            case MESSAGES -> user.setMessagesPrivacy(privacyLevel);
            case INVITES -> user.setInvitesPrivacy(privacyLevel);

            default -> throw new BusinessException(200,"參數不正確");
        }
        userDao.updateById(user);
    }

    @Override
    public void checkUsernameTaken(String username) throws BusinessException {
        Long count = userDao.lambdaQuery()
                .eq(User::getUsername, username)
                .count();
        if(count != 0){
            throw new BusinessException(200,"username already taken");
        }
    }

    @Override
    public void saveProfile(SaveProfileDTO req) {
        userDao.lambdaUpdate()
                .eq(User::getId,ThreadLocalUtil.getUid())
                .set(User::getUsername,req.getUsername())
                .set(User::getBio,req.getBio())
                .set(User::getFirstName,req.getFirstName())
                .set(User::getLastName,req.getLastName())
                .update();
    }

    @Override
    public LoginUserVO getMyLoginInfo(Long uid) throws BusinessException {
        User one = userDao.lambdaQuery()
                .eq(User::getId, uid)
                .one();
        if(one == null){
            throw new BusinessException(500,"unknow error");
        }

        DeviceRecord device = deviceRecordDao.lambdaQuery()
                .eq(DeviceRecord::getToken, ThreadLocalUtil.getToken())
                .select(DeviceRecord::getId)
                .one();
        LoginUserVO loginUserVO = buildLoginUserVoWithoutToken(one,device.getId());
        return loginUserVO;
    }

    /**
     * 查詢用戶隱私設置
     * @param uid
     * @return
     */
    @Override
    public UserPrivacyVO getUserPrivacySettins(Long uid) throws BusinessException {
        User one = userDao.lambdaQuery()
                .eq(User::getId, uid)
                .one();
        if(one == null){
            throw new BusinessException(500,"unknow error");
        }

        UserPrivacyVO userPrivacyVO = new UserPrivacyVO(
                one.getAvatarPrivacy(),
                one.getBioPrivacy(),
                one.getCallsPrivacy(),
                one.getDateOfBirthPrivacy(),
                one.getInvitesPrivacy(),
                one.getLastSeenPrivacy(),
                one.getMessagesPrivacy(),
                one.getPhonePrivacy(),
                one.getVoiceVideoMessagesPrivacy()
        );

        return userPrivacyVO;
    }

    /**
     * 通過username查用戶
     * @param username
     * @return
     */
    @Override
    public List<Long> searchByUsername(String username) {
        List<User> list = userDao.lambdaQuery()
                .likeRight(User::getUsername, username)
                .last("limit 5")
                .select(User::getId)
                .list();
        List<Long> result = list.stream().map(o -> o.getId()).collect(Collectors.toList());
        return result;
    }


    /**
     * 查詢目標用戶關係
     * @param uid
     * @param aimUid
     * @return
     */
    private PrivacyLevelEnum getRelationship(Contact contact){

        if(contact != null){
            return PrivacyLevelEnum.MY_CONTACTS;//聯係人關係
        }else{
            return PrivacyLevelEnum.EVERYBODY;//無任何關係
        }
    }

    /**
     * 根據用戶隱私設置 和 目標用戶關係 過濾隱私信息
     * @param user
     * @param userVO
     * @param relationship
     * @return
     */
    private UserVO privacyFilter(User user,UserVO userVO,PrivacyLevelEnum relationship){
        //頭像是否可見  不可见则
        if(!checkNeedSetAttribute(PrivacyLevelEnum.of(user.getAvatarPrivacy()),relationship)){
            userVO.setAvatar(null);
        }
        //BIO是否可見  不可见则
        if(!checkNeedSetAttribute(PrivacyLevelEnum.of(user.getBioPrivacy()),relationship)){
            userVO.setBio(null);
        }
        //birthday是否可見  不可见则
        if(!checkNeedSetAttribute(PrivacyLevelEnum.of(user.getDateOfBirthPrivacy()),relationship)){
            userVO.setDateOfBirth(null);
        }
        //mobile是否可見  不可见则
        if(!checkNeedSetAttribute(PrivacyLevelEnum.of(user.getPhonePrivacy()),relationship)){
            userVO.setMobile(null);
            userVO.setMobileCountry(null);

        }
        return userVO;
    }
    /**
     * 判斷是否可以設置該隱私屬性
     * @param privacyLevel
     * @param relationship
     * @return
     */
    private Boolean checkNeedSetAttribute(PrivacyLevelEnum privacyLevel,PrivacyLevelEnum relationship){
        if(privacyLevel == PrivacyLevelEnum.NOBODY){
            return false;
        }
        if(privacyLevel == PrivacyLevelEnum.EVERYBODY){
            return true;
        }
        if(privacyLevel == PrivacyLevelEnum.MY_CONTACTS && relationship == PrivacyLevelEnum.MY_CONTACTS){
            return true;
        }
        return false;
    }





























//
//
//
//
//
//
//    /**
//     * get user channel list
//     * @param user
//     * @return channelVO list
//     */
//    public List<ChannelVO> getUserChannels(User user){
//        List<ChannelVO> list = new ArrayList<>();
//        Map<Long, Boolean> muteChannel = user.getMuteChannels().stream().collect(Collectors.toMap(k -> k, v -> true));
//        Map<Long, Boolean> pinChannel = user.getPinChannels().stream().collect(Collectors.toMap(k -> k, v -> true));
//
//        //get user all joined channels' id and last read date
//        List<User.channel> channels = JSONUtil.toList(user.getChannels(),User.channel.class);
//        //get channel by channel id and turn into channelMap
//        Map<Long, Group> channelMap = channelDao.lambdaQuery()
//                .in(Group::getId, dealEmptyList(channels.stream().map(o->o.getChannelId()).collect(Collectors.toList()))).list().stream()
//                .collect(Collectors.toMap(k -> k.getId(), v -> v));
//        //filter mute and pin channel and turn into channelVO
//        for (User.channel userChannel : channels) {
//            //base attributes
//            Long channelId = userChannel.getChannelId();
//            Date channelLastReadDate = new Date(userChannel.getLastReadDate());
//            //build channelVO
//            ChannelVO channelVO = new ChannelVO();
//            //fill id muteStatus pinStatus
//            channelVO.setId(channelId);
//            if(muteChannel.containsKey(channelId)){
//                channelVO.setMuteStatus(true);
//            }
//            if(pinChannel.containsKey(channelId)){
//                channelVO.setPinStatus(true);
//            }
//            //fill channel's base info
//            Group group = channelMap.get(channelId);
//            channelVO.setAvatar(group.getAvatar());
//            channelVO.setName(group.getName());
//            //get channel's last msg by channel's lastMsgId and turn into messageMap
//            Message message = messageDao.lambdaQuery().eq(Message::getId, group.getLastMsgId()).one();
//            //get last message sender's displayName by last message's sender's uid
//            String displayName = userDao.lambdaQuery().eq(User::getId, message.getUid()).one().getName();
//            //fill channel's last message info
//            channelVO.setDate(message.getDate());
//            channelVO.setIsSystem(message.getIsSystem());
//            channelVO.setDisplayMessage(message.getText());
//            channelVO.setDisplayPhoto(message.getPhoto());
//            channelVO.setDisplayVideo(message.getVideo());
//            channelVO.setDisplayMessageUser(displayName);
//            //get each channel's unread count by user's each channel last read date
//            Long count = messageDao.lambdaQuery().eq(Message::getChannelId, channelId)
//                    .gt(Message::getDate, channelLastReadDate).count();
//            //fill channel's unread count
//            channelVO.setUnread(count.intValue());//todo Long -> Integer : may be a bug in the furture
//            //add to list
//            list.add(channelVO);
//        }
//        return list;
//    }
//
//
//    public List dealEmptyList(List list){
//        if(list.size() == 0){
//            ArrayList<Long> newList = new ArrayList<>();
//            newList.add(0L);
//            return newList;
//        }
//        return list;
//    }




}
