package librarymanagementsystemclient;


import entity.StaffEntity;
import java.util.Scanner;
import ejb.session.stateless.BookEntitySessionBeanRemote;
import ejb.session.stateless.LendAndReturnEntitySessionBeanRemote;
import ejb.session.stateless.MemberEntitySessionBeanRemote;
import ejb.session.stateless.StaffEntitySessionBeanRemote;
import util.exception.EntityManagerException;
import util.exception.InvalidLoginException;



public class MainApp
{
    private StaffEntitySessionBeanRemote staffEntitySessionBeanRemote;
    private MemberEntitySessionBeanRemote memberEntitySessionBeanRemote;
    private BookEntitySessionBeanRemote bookEntitySessionBeanRemote;
    private LendAndReturnEntitySessionBeanRemote lendAndReturnSessionBeanRemote;
    
    private LibraryOperationModule libraryOperationModule;
    
    private StaffEntity currentStaffEntity;
    
    
    
    public MainApp() 
    {        
    }

    
    
    public MainApp(StaffEntitySessionBeanRemote staffEntitySessionBeanRemote, MemberEntitySessionBeanRemote memberEntitySessionBeanRemote, BookEntitySessionBeanRemote bookEntitySessionBeanRemote, LendAndReturnEntitySessionBeanRemote lendAndReturnSessionBeanRemote)
    {
        this.staffEntitySessionBeanRemote = staffEntitySessionBeanRemote;
        this.memberEntitySessionBeanRemote = memberEntitySessionBeanRemote;
        this.bookEntitySessionBeanRemote = bookEntitySessionBeanRemote;
        this.lendAndReturnSessionBeanRemote = lendAndReturnSessionBeanRemote;
    }
    
    
    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Library Management System (LMS) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Logout\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        libraryOperationModule = new LibraryOperationModule(staffEntitySessionBeanRemote, memberEntitySessionBeanRemote, bookEntitySessionBeanRemote, lendAndReturnSessionBeanRemote);
                        menuMain();
                    }
                    catch(InvalidLoginException | EntityManagerException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2)
            {
                break;
            }
        }
    }
    
    
    
    private void doLogin() throws InvalidLoginException, EntityManagerException
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** LMS :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentStaffEntity = staffEntitySessionBeanRemote.staffLogin(username, password);      
        }
        else
        {
            throw new InvalidLoginException("Missing login credential!");
        }
    }
    
    
    
    private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Library Management System (LMS) ***\n");
            System.out.println("You are login as " + currentStaffEntity.getFirstName() + " " + currentStaffEntity.getLastName() + "\n");
            System.out.println("1: Library Operation");
            System.out.println("2: Logout\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    libraryOperationModule.menuLibraryOperation();
                }
                else if (response == 2)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2)
            {
                break;
            }
        }
    }
}