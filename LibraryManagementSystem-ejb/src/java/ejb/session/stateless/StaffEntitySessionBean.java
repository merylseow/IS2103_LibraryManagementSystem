package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginException;
import util.exception.StaffNotFoundException;


@Stateless
@Local(StaffEntitySessionBeanLocal.class)
@Remote(StaffEntitySessionBeanRemote.class)

public class StaffEntitySessionBean implements StaffEntitySessionBeanLocal, StaffEntitySessionBeanRemote
{
    @PersistenceContext(unitName = "LibraryManagementSystem-ejbPU")
    private EntityManager entityManager;
    
    
    public StaffEntitySessionBean()
    {
    }
    
    
    @Override
    public StaffEntity createNewStaff(StaffEntity newStaffEntity)
    {
        entityManager.persist(newStaffEntity);
        entityManager.flush();
        
        return newStaffEntity;
    }
    
    
    @Override
    public List<StaffEntity> retrieveAllStaffs()
    {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s");
        
        return query.getResultList();
    }
    
    
    @Override
    public StaffEntity retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException
    {
        StaffEntity staffEntity = entityManager.find(StaffEntity.class, staffId);
        
        if (staffEntity != null)
        {
            return staffEntity;
        }
        else
        {
            throw new StaffNotFoundException("Staff ID" + staffId + " does not exist!");
        }
    }
    
    
    @Override
    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException
    {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.userName = :inUserName");
        query.setParameter("inUserName", username);
        
        try
        {
            return (StaffEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new StaffNotFoundException("Staff Username " + username + " does not exist!");
        }
    }
    
    
    @Override
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginException
    {
        try
        {
            StaffEntity staffEntity = retrieveStaffByUsername(username);
            
            if(staffEntity.getPassword().equals(password))
            {               
                return staffEntity;
            }
            else
            {
                throw new InvalidLoginException("Username does not exist or invalid password!");
            }
        }
        catch(StaffNotFoundException ex)
        {
            throw new InvalidLoginException("Username does not exist or invalid password!");
        }
    }
    
    
    @Override
    public void updateStaff(StaffEntity staffEntity) throws StaffNotFoundException
    {
        if(staffEntity != null && staffEntity.getStaffId() != null)
        {
            StaffEntity staffEntityToUpdate = retrieveStaffByStaffId(staffEntity.getStaffId());
            
            if(staffEntityToUpdate.getUserName().equals(staffEntity.getUserName()))
            {
                staffEntityToUpdate.setFirstName(staffEntity.getFirstName());
                staffEntityToUpdate.setLastName(staffEntity.getLastName());
            }
        }
    }
    
    @Override
    public void deleteStaff(Long staffId) throws StaffNotFoundException
    {
        StaffEntity staffEntityToRemove = retrieveStaffByStaffId(staffId);
        entityManager.remove(staffEntityToRemove);
    }
}
