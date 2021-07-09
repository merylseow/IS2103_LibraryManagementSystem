package ejb.session.stateless;

import entity.BookEntity;
import entity.LendAndReturnEntity;
import entity.MemberEntity;
import java.util.Date;
import java.util.List;
import util.exception.BookNotFoundException;
import util.exception.FineNotPaidException;
import util.exception.LendingNotFoundException;
import util.exception.MemberNotFoundException;


public interface LendAndReturnEntitySessionBeanRemote
{
    List<LendAndReturnEntity> retrieveAllLendAndReturns();
    
    LendAndReturnEntity retrieveLendAndReturnByLendId(Long lendId) throws LendingNotFoundException;
    
    LendAndReturnEntity retrieveLendAndReturnByMemberId(Long memberId) throws MemberNotFoundException;
    
    LendAndReturnEntity retrieveLendAndReturnByBookId(Long bookId) throws BookNotFoundException;
    
    void lendBook(Long bookId, String identityNo) throws BookNotFoundException, MemberNotFoundException;

    MemberEntity retrieveMemberByIdentityNo(String identityNo) throws MemberNotFoundException;

    BookEntity retrieveBookByBookId(Long bookId) throws BookNotFoundException;
    
    Long daysOverdue(Date date1, Date date2);
    
    Double computeFineAmount(Long lendId) throws LendingNotFoundException;
    
    void returnBook(Long lendId, Double finePaid) throws FineNotPaidException;
}
