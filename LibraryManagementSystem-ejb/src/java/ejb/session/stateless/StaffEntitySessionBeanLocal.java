package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import util.exception.InvalidLoginException;
import util.exception.StaffNotFoundException;


public interface StaffEntitySessionBeanLocal
{
    StaffEntity createNewStaff(StaffEntity newStaffEntity);
            
    List<StaffEntity> retrieveAllStaffs();
    
    StaffEntity retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException;
    
    StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException;
    
    StaffEntity staffLogin(String username, String password) throws InvalidLoginException;
    
    void updateStaff(StaffEntity staffEntity) throws StaffNotFoundException;
    
    void deleteStaff(Long staffId) throws StaffNotFoundException;
}
