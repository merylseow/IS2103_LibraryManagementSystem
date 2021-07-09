package ejb.session.stateless;

import entity.BookEntity;
import entity.LendAndReturnEntity;
import entity.MemberEntity;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.StatusEnum;
import static util.enumeration.StatusEnum.AVAILABLE;
import static util.enumeration.StatusEnum.LOANED;
import util.exception.BookNotFoundException;
import util.exception.FineNotPaidException;
import util.exception.LendingNotFoundException;
import util.exception.MemberNotFoundException;


@Stateless
@Local(LendAndReturnEntitySessionBeanLocal.class)
@Remote(LendAndReturnEntitySessionBeanRemote.class)

public class LendAndReturnEntitySessionBean implements LendAndReturnEntitySessionBeanLocal, LendAndReturnEntitySessionBeanRemote
{
    @PersistenceContext(unitName = "LibraryManagementSystem-ejbPU")
    private EntityManager entityManager;
    
    
    public LendAndReturnEntitySessionBean()
    {
    }

    
    @Override
    public List<LendAndReturnEntity> retrieveAllLendAndReturns()
    {
        Query query = entityManager.createQuery("SELECT lr FROM LendAndReturnEntity lr");
        
        return query.getResultList();
    }
    
    
    @Override
    public LendAndReturnEntity retrieveLendAndReturnByLendId(Long lendId) throws LendingNotFoundException
    {
        LendAndReturnEntity lendAndReturnEntity = entityManager.find(LendAndReturnEntity.class, lendId);
        
        if(lendAndReturnEntity != null)
        {
            return lendAndReturnEntity;
        }
        else
        {
            throw new LendingNotFoundException("Lend ID " + lendId + " does not exist!");
        }                
    }
    
    
    @Override
    public LendAndReturnEntity retrieveLendAndReturnByMemberId(Long memberId) throws MemberNotFoundException
    {
        Query query = entityManager.createQuery("SELECT lr FROM LendAndReturnEntity lr WHERE lr.memberId = :inMemberId");
        query.setParameter("inMemberId", memberId);
        
        try
        {
            return (LendAndReturnEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new MemberNotFoundException("Member ID" + memberId + " does not exist!");
        }
    }
    
    
    @Override
    public LendAndReturnEntity retrieveLendAndReturnByBookId(Long bookId) throws BookNotFoundException
    {
        Query query = entityManager.createQuery("SELECT lr FROM LendAndReturnEntity lr WHERE lr.bookId = :inBookId");
        query.setParameter("inBookId", bookId);
        
        try
        {
            return (LendAndReturnEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new BookNotFoundException("Book ID" + bookId + " does not exist!");
        }
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
    public BookEntity retrieveBookByBookId(Long bookId) throws BookNotFoundException
    {
        BookEntity bookEntity = entityManager.find(BookEntity.class, bookId);
        
        if (bookEntity != null)
        {
            return bookEntity;
        }
        else
        {
            throw new BookNotFoundException("Book ID " + bookId + " does not exist!");
        }
    }
    
    
    @Override
    public void lendBook(Long bookId, String identityNo) throws BookNotFoundException, MemberNotFoundException
    {
        try
        {
            BookEntity borrowedBook = retrieveBookByBookId(bookId);
            MemberEntity member = retrieveMemberByIdentityNo(identityNo);
            
            // test case
            // Date lendDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse("2021-02-20 14:44");
            
            Date lendDate = new Date();
            
            LendAndReturnEntity lending = new LendAndReturnEntity(lendDate, null, 0.0, StatusEnum.LOANED);
            lending.setMemberEntity(member); // important to set!
            lending.setBookEntity(borrowedBook); // important to set!
            
            borrowedBook.addToBookLendRecord(lending);
            
            entityManager.persist(lending); // to persist the lending.setMemberEntity(member) and lending.setBookEntity(borrowedBook);
            entityManager.flush();
        }
        catch (BookNotFoundException ex)
        {
            throw new BookNotFoundException("An error has occurred while retrieving book: " + ex.getMessage() + "\n");
        }
        catch (MemberNotFoundException ex)
        {
            throw new MemberNotFoundException("An error has occurred while retrieving member: " + ex.getMessage() + "\n");
        }
    }
    
    
    @Override
    public Long daysOverdue(Date date1, Date date2)
    {
        LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period p = Period.between(localDate1, localDate2);
        Long daysOverdue = ChronoUnit.DAYS.between(localDate1, localDate2);
        return daysOverdue;
    }
    
    
    @Override
    public Double computeFineAmount(Long lendId) throws LendingNotFoundException
    {   
        Double fineAmount = 0.0;
        Long daysOverdue = new Long(0);
        LendAndReturnEntity lendAndReturnEntity = retrieveLendAndReturnByLendId(lendId);
        
        Date lendDate = lendAndReturnEntity.getLendDate();
        Calendar c = Calendar.getInstance();
        c.setTime(lendDate);
        c.add(Calendar.DAY_OF_MONTH, 14);
        Date dueDate = c.getTime();
        
        Date returnDate = lendAndReturnEntity.getReturnDate();
        Date currDate = new Date();
        
        if (returnDate == null)
        {
            if (currDate.compareTo(dueDate) > 0)
            {
                daysOverdue = daysOverdue(dueDate, currDate);
            }
        }
        else
        {
            if (returnDate.compareTo(dueDate) > 0)
            {
                daysOverdue = daysOverdue(dueDate, returnDate);
            }
        }
        
        Double finePerDay = 0.5;
        fineAmount = daysOverdue * finePerDay;
        
        lendAndReturnEntity.setFineAmount(fineAmount);
        // updateLendAndReturn(lendAndReturnEntity); // don't need since entity is within persistence context
        
        return fineAmount;
    }
    
    
    @Override
    public void returnBook(Long lendId, Double finePaid) throws FineNotPaidException
    {
        try
        {
            LendAndReturnEntity lendAndReturnEntity = retrieveLendAndReturnByLendId(lendId);
        
            if (lendAndReturnEntity.getStatus() == LOANED)
            {
                Double pendingFine = computeFineAmount(lendId);
                
                if (finePaid >= pendingFine)
                {
                    lendAndReturnEntity.setStatus(StatusEnum.AVAILABLE);
                    lendAndReturnEntity.setReturnDate(new Date());  
                }
                else
                {
                    throw new FineNotPaidException("Book cannot be returned as fine has not been paid.");
                }
            }
            else if (lendAndReturnEntity.getStatus() == AVAILABLE)
            {
                System.out.println("Book has already been returned.(LRSB)\n");
            }
        }
        catch (LendingNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving lending: " + ex.getMessage() + "\n");
        }
    }
}
