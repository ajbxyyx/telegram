package com.ajbxyyx.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPrivacyVO {

    Integer avatarPrivacy;
    Integer bioPrivacy;
    Integer callsPrivacy;
    Integer dateOfBirthPrivacy;
    Integer invitesPrivacy;
    Integer lastSeenPrivacy;
    Integer messagesPrivacy;
    Integer phonePrivacy ;
    Integer voiceVideoMessagesPrivacy;
}
