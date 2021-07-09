package ejb.session.stateless;

import entity.MemberEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.MemberNotFoundException;


@Stateless
@Local(MemberEntitySessionBeanLocal.class)
@Remote(MemberEntitySessionBeanRemote.class)

public class MemberEntitySessionBean implements MemberEntitySessionBeanLocal, MemberEntitySessionBeanRemote
{
    @PersistenceContext(unitName = "LibraryManagementSystem-ejbPU")
    private EntityManager entityManager;
    
    
    public MemberEntitySessionBean()
    {
    }
    
    @Override
    public MemberEntity createNewMember(MemberEntity newMemberEntity)
    {
        entityManager.persist(newMemberEntity);
        entityManager.flush();
        
        return newMemberEntity;
    }
    
    
    @Override
    public List<MemberEntity> retrieveAllMembers()
    {
        Query query = entityManager.createQuery("SELECT m FROM MemberEntity m");
        
        return query.getResultList();
    }
    
    
    @Override
    public MemberEntity retrieveMemberByMemberId(Long memberId) throws MemberNotFoundException
    {
        MemberEntity memberEntity = entityManager.find(MemberEntity.class, memberId);
        
        if (memberEntity != null)
        {
            return memberEntity;
        }
        else
        {
            throw new MemberNotFoundException("Member ID" + memberId + " does not exist!");
        }
    }


    @Override
    public MemberEntity retrieveMemberByIdentityNo(String identityNo) throws MemberNotFoundException
    {
        Query query = entityManager.createQuery("SELECT m FROM MemberEntity m WHERE m.identityNo = :inIdentityNo");
        query.setParameter("inIdentityNo", identityNo);
        
        try
        {
            return (MemberEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new MemberNotFoundException("Member Identity Number " + identityNo + " does not exist!");
        }
    }
    
    
    @Override
    public void updateMember(MemberEntity memberEntity) throws MemberNotFoundException
    {
        if (memberEntity != null && memberEntity.getMemberId() != null)
        {
            MemberEntity memberEntityToUpdate = retrieveMemberByMemberId(memberEntity.getMemberId());
            
            if (memberEntityToUpdate.getMemberId().equals(memberEntity.getMemberId()))
            {
                memberEntityToUpdate.setFirstName(memberEntity.getFirstName());
                memberEntityToUpdate.setLastName(memberEntity.getLastName());
                memberEntityToUpdate.setGender(memberEntity.getGender());
                memberEntityToUpdate.setAge(memberEntity.getAge());
                memberEntityToUpdate.setIdentityNo(memberEntity.getIdentityNo());
                memberEntityToUpdate.setPhoneNo(memberEntity.getPhoneNo());
                memberEntityToUpdate.setAddress(memberEntity.getAddress());
            }
        }
    }
    
    
    @Override
    public void deleteMember(Long memberId) throws MemberNotFoundException
    {
        MemberEntity memberEntityToRemove = retrieveMemberByMemberId(memberId);
        entityManager.remove(memberEntityToRemove);
    }
}
