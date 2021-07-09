package ejb.session.stateless;

import entity.MemberEntity;
import java.util.List;
import util.exception.MemberNotFoundException;


public interface MemberEntitySessionBeanRemote
{
    MemberEntity createNewMember(MemberEntity newMemberEntity);

    List<MemberEntity> retrieveAllMembers();

    MemberEntity retrieveMemberByMemberId(Long memberId) throws MemberNotFoundException;
    
    MemberEntity retrieveMemberByIdentityNo(String identityNo) throws MemberNotFoundException;
    
    void updateMember(MemberEntity memberEntity) throws MemberNotFoundException;

    void deleteMember(Long memberId) throws MemberNotFoundException;
}
