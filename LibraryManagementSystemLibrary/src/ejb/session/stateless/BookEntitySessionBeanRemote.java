package ejb.session.stateless;

import entity.BookEntity;
import java.util.List;
import util.exception.BookNotFoundException;


public interface BookEntitySessionBeanRemote
{
    BookEntity createNewBook(BookEntity newBookEntity);
    
    List<BookEntity> retrieveAllBooks();
    
    BookEntity retrieveBookByBookId(Long bookId) throws BookNotFoundException;
    
    BookEntity retriveBookByTitle(String title) throws BookNotFoundException;
    
    BookEntity retriveBookByIsbn(String title) throws BookNotFoundException;
    
    void updateBook(BookEntity bookEntity) throws BookNotFoundException;
    
    void deleteBook(Long bookId) throws BookNotFoundException;
    
    boolean isAvailable(Long bookId) throws BookNotFoundException;
}
