package ejb.session.singleton;

import ejb.session.stateless.BookEntitySessionBeanLocal;
import ejb.session.stateless.LendAndReturnEntitySessionBeanLocal;
import ejb.session.stateless.MemberEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.BookEntity;
import entity.MemberEntity;
import entity.StaffEntity;
import java.text.ParseException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Singleton
@LocalBean
@Startup

public class DataInitializationSessionBean
{    
    
    @PersistenceContext(unitName = "LibraryManagementSystem-ejbPU")
    private EntityManager entityManager;
    
    
    @EJB
    private StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
    @EJB
    private MemberEntitySessionBeanLocal memberEntitySessionBeanLocal;
    @EJB
    private BookEntitySessionBeanLocal bookEntitySessionBeanLocal;
    @EJB
    private LendAndReturnEntitySessionBeanLocal lendAndReturnEntitySessionBeanLocal;
    
    public DataInitializationSessionBean()
    {
    }
    
    
    @PostConstruct
    public void postConstruct()
    {
        try
        {
            if (entityManager.find(StaffEntity.class, 1L) == null)
            {
                initializeData();
            }
        }
        catch (Exception ex)
        {
        }
    }
    
    
    
    private void initializeData() throws ParseException
    {
        staffEntitySessionBeanLocal.createNewStaff(new StaffEntity("Eric", "Some", "eric", "password"));
        staffEntitySessionBeanLocal.createNewStaff(new StaffEntity("Sarah", "Brightman", "sarah", "password"));
        
        memberEntitySessionBeanLocal.createNewMember(new MemberEntity("Tony", "Shade", 'M', 31, "S8900678A", "83722773", "13 Jurong East, Ave 3"));
        memberEntitySessionBeanLocal.createNewMember(new MemberEntity("Dewi", "Tan", 'F', 35, "S8581028X", "94602711", "15 Computing Dr"));
        
        bookEntitySessionBeanLocal.createNewBook(new BookEntity("Anna Karenina", "0451528611", "Leo Tolstoy"));
        bookEntitySessionBeanLocal.createNewBook(new BookEntity("Madame Bovary", "979-8649042031", "Gustave Flaubert"));
        bookEntitySessionBeanLocal.createNewBook(new BookEntity("Hamlet", "1980625026", "William Shakespeare"));
        bookEntitySessionBeanLocal.createNewBook(new BookEntity("The Hobbit", "9780007458424", "J R R Tolkien"));
        bookEntitySessionBeanLocal.createNewBook(new BookEntity("Great Expectations", "1521853592", "Charles Dickens"));
        bookEntitySessionBeanLocal.createNewBook(new BookEntity("Pride and Prejudice", "979-8653642272", "Jane Austen"));
        bookEntitySessionBeanLocal.createNewBook(new BookEntity("Wuthering Heights", "3961300224", "Emily Brontë"));
    }
}