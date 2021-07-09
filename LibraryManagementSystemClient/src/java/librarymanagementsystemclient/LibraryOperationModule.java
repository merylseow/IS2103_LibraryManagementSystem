package librarymanagementsystemclient;

import ejb.session.stateless.BookEntitySessionBeanRemote;
import ejb.session.stateless.LendAndReturnEntitySessionBeanRemote;
import ejb.session.stateless.MemberEntitySessionBeanRemote;
import ejb.session.stateless.StaffEntitySessionBeanRemote;
import entity.BookEntity;
import entity.LendAndReturnEntity;
import entity.MemberEntity;
import java.util.Scanner;
import static util.enumeration.StatusEnum.AVAILABLE;
import static util.enumeration.StatusEnum.LOANED;
import util.exception.BookNotFoundException;
import util.exception.FineNotPaidException;
import util.exception.LendingNotFoundException;
import util.exception.MemberNotFoundException;



public class LibraryOperationModule
{
    private StaffEntitySessionBeanRemote staffEntitySessionBeanRemote;
    private MemberEntitySessionBeanRemote memberEntitySessionBeanRemote;
    private BookEntitySessionBeanRemote bookEntitySessionBeanRemote;
    private LendAndReturnEntitySessionBeanRemote lendAndReturnEntitySessionBeanRemote;
    
    
    public LibraryOperationModule()
    {
    }
    
    
    public LibraryOperationModule(StaffEntitySessionBeanRemote staffEntitySessionBeanRemote, MemberEntitySessionBeanRemote memberEntitySessionBeanRemote, BookEntitySessionBeanRemote bookEntitySessionBeanRemote, LendAndReturnEntitySessionBeanRemote lendAndReturnSessionBeanRemote) 
    {
        this();
        this.staffEntitySessionBeanRemote = staffEntitySessionBeanRemote;
        this.memberEntitySessionBeanRemote = memberEntitySessionBeanRemote;
        this.bookEntitySessionBeanRemote = bookEntitySessionBeanRemote;
        this.lendAndReturnEntitySessionBeanRemote = lendAndReturnSessionBeanRemote;
    }
    
    
    public void menuLibraryOperation()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** LMS :: Library Operation ***\n");
            System.out.println("1: Register Member");
            System.out.println("2: Lend Book");
            System.out.println("3: View fine amount");
            System.out.println("4: Return Book");
            System.out.println("5: Back\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doRegisterMember();
                }
                else if(response == 2)
                {
                    doLendBook();
                }
                else if(response == 3)
                {
                    doViewFineAmount();
                }
                else if(response == 4)
                {
                    doReturnBook();
                }
                else if(response == 5)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 5)
            {
                break;
            }
        }
    }
    
    private void doRegisterMember()
    {
        Scanner scanner = new Scanner(System.in);
        MemberEntity newMemberEntity = new MemberEntity();
        
        System.out.println("*** LMS :: Library Operation :: Register Member ***\n");
        System.out.print("Enter First Name> ");
        newMemberEntity.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newMemberEntity.setLastName(scanner.nextLine().trim());
        System.out.print("Enter Gender> ");
        newMemberEntity.setGender(scanner.next().charAt(0));
        System.out.print("Enter Age> ");
        newMemberEntity.setAge(scanner.nextInt());
        scanner.nextLine();
        System.out.print("Enter Identity Number> ");
        newMemberEntity.setIdentityNo(scanner.nextLine().trim());
        System.out.print("Enter Phone> ");
        newMemberEntity.setPhoneNo(scanner.nextLine().trim());
        System.out.print("Enter Address> ");
        newMemberEntity.setAddress(scanner.nextLine().trim());
        
        MemberEntity newMember = memberEntitySessionBeanRemote.createNewMember(newMemberEntity);
        System.out.println("Member has been registered successfully!\n");
    }
    
    
    private void doLendBook()
    {
        try
        {
            Scanner scanner = new Scanner(System.in);
            System.out.println("*** LMS :: Library Operation :: Lend Book ***\n");
            System.out.print("Enter Book ID> ");
            Long bookId = scanner.nextLong();
            scanner.nextLine();
            System.out.print("Enter Member Identity Number> ");
            String idNum = scanner.nextLine();
            
            BookEntity bookEntity = bookEntitySessionBeanRemote.retrieveBookByBookId(bookId);
            MemberEntity memberEntity = memberEntitySessionBeanRemote.retrieveMemberByIdentityNo(idNum);
            
            if (!bookEntitySessionBeanRemote.isAvailable(bookId))
            {
                System.out.println("The book is currently on loan.\n");
            }
            else
            {                
                lendAndReturnEntitySessionBeanRemote.lendBook(bookId, idNum);
            
                System.out.println("Book: " + bookEntity.getTitle() + " lent to " + memberEntity.getFirstName() + " " + memberEntity.getLastName() + "." + "\n");
            } 
        }
        catch (MemberNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving member: " + ex.getMessage() + "\n");
        }
        catch (BookNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving book: " + ex.getMessage() + "\n");
        }
    }
    
    
    private void doViewFineAmount()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** LMS :: Library Operation :: View fine amount ***\n");
        System.out.print("Enter Lend Id> ");
        Long lendId = scanner.nextLong();
        scanner.nextLine();
        
        try
        {
            LendAndReturnEntity lendAndReturnEntity = lendAndReturnEntitySessionBeanRemote.retrieveLendAndReturnByLendId(lendId);
            MemberEntity memberEntity = lendAndReturnEntity.getMemberEntity();
            double fineAmount = lendAndReturnEntitySessionBeanRemote.computeFineAmount(lendId);
            
            if (lendAndReturnEntity.getStatus() == LOANED)
            {
                System.out.println(memberEntity.getFirstName() + " " + memberEntity.getLastName() + " is fined with " + fineAmount + " $." + "\n");
            }
            else // book has already been returned
            {
                System.out.println(memberEntity.getFirstName() + " " + memberEntity.getLastName() + " was fined with " + fineAmount + " $. The fine has been paid." + "\n");
            }
        }
        catch (LendingNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving lending: " + ex.getMessage() + "\n");
        }
    }
    
    
    private void doReturnBook()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** LMS :: Library Operation :: Return Book ***\n");
        System.out.print("Enter Lend Id> ");
        Long lendId = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter fine amount paid> ");
        Double fineAmountPaid = scanner.nextDouble();
        scanner.nextLine();
        
        try
        {
            LendAndReturnEntity lendAndReturnEntity = lendAndReturnEntitySessionBeanRemote.retrieveLendAndReturnByLendId(lendId);
            
            if (lendAndReturnEntity.getStatus() == LOANED)
            {
                BookEntity bookEntity = lendAndReturnEntity.getBookEntity();
                MemberEntity memberEntity = lendAndReturnEntity.getMemberEntity();
                lendAndReturnEntitySessionBeanRemote.returnBook(lendId, fineAmountPaid);
            
                System.out.println("Book: " + bookEntity.getTitle() + " is successfully returned by " + memberEntity.getFirstName() + " " + memberEntity.getLastName() + "." + "\n");
            }
            else if (lendAndReturnEntity.getStatus() == AVAILABLE)
            {
                System.out.println("Book has already been returned.\n");
            }
        }
        catch (LendingNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving lending: " + ex.getMessage() + "\n");
        }
        catch (FineNotPaidException ex)
        {
            System.out.println("Book cannot be returned until fine is paid.\n");
        }
    }
}
