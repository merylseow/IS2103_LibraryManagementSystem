package ejb.session.stateless;

import entity.BookEntity;
import entity.LendAndReturnEntity;
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
import util.exception.BookNotFoundException;

@Stateless
@Local(BookEntitySessionBeanLocal.class)
@Remote(BookEntitySessionBeanRemote.class)

public class BookEntitySessionBean implements BookEntitySessionBeanLocal, BookEntitySessionBeanRemote
{
    
    @PersistenceContext(unitName = "LibraryManagementSystem-ejbPU")
    private EntityManager entityManager;
    
    
    public BookEntitySessionBean()
    {
    }
    
    
    @Override
    public BookEntity createNewBook(BookEntity newBookEntity)
    {
        entityManager.persist(newBookEntity);
        entityManager.flush();
        
        return newBookEntity;   
    }
    
    
    @Override
    public List<BookEntity> retrieveAllBooks()
    {
        Query query = entityManager.createQuery("SELECT b FROM BookEntity b");
        
        return query.getResultList();
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
    public BookEntity retriveBookByTitle(String title) throws BookNotFoundException
    {
        Query query = entityManager.createQuery("SELECT b FROM BookEntity b WHERE b.title = :inTitle");
        query.setParameter("inTitle", title);
        
        try
        {
            return (BookEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new BookNotFoundException("Book Title " + title + " does not exist!");
        }
    }
    
    
    @Override
    public BookEntity retriveBookByIsbn(String isbn) throws BookNotFoundException
    {
        Query query = entityManager.createQuery("SELECT b FROM BookEntity b WHERE b.isbn = :inIsbn");
        query.setParameter("inIsbn", isbn);
        
        try
        {
            return (BookEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new BookNotFoundException("ISBN " + isbn + " does not exist!");
        }
    }
    
    
    @Override
    public void updateBook(BookEntity bookEntity) throws BookNotFoundException
    {
        if (bookEntity != null && bookEntity.getBookId() != null)
        {
            BookEntity bookEntityToUpdate = retrieveBookByBookId(bookEntity.getBookId());
            
            if (bookEntityToUpdate.getBookId().equals(bookEntity.getBookId()))
            {
                bookEntityToUpdate.setTitle(bookEntity.getTitle());
                bookEntityToUpdate.setIsbn(bookEntity.getIsbn());
                bookEntityToUpdate.setAuthor(bookEntity.getAuthor());
            }
        }
    }
       
    
    @Override
    public void deleteBook(Long bookId) throws BookNotFoundException
    {
        BookEntity bookEntityToRemove = retrieveBookByBookId(bookId);
        entityManager.remove(bookEntityToRemove);
    }
    
    
    @Override
    public boolean isAvailable(Long bookId) throws BookNotFoundException
    {
        BookEntity book = retrieveBookByBookId(bookId);
        List<LendAndReturnEntity> bookLendRecords = book.getLendings();
        for (LendAndReturnEntity bookLendRecord : bookLendRecords)
        {
            if (bookLendRecord.getStatus() == StatusEnum.LOANED)
            {
                return false;
            }
        }
        return true;
    }
}