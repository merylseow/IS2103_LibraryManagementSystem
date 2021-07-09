package librarymanagementsystemclient;


import ejb.session.stateless.BookEntitySessionBeanRemote;
import ejb.session.stateless.LendAndReturnEntitySessionBeanRemote;
import ejb.session.stateless.MemberEntitySessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.StaffEntitySessionBeanRemote;


public class Main
{
    @EJB
    private static StaffEntitySessionBeanRemote staffEntitySessionBeanRemote;
    @EJB
    private static MemberEntitySessionBeanRemote memberEntitySessionBeanRemote;
    @EJB
    private static BookEntitySessionBeanRemote bookEntitySessionBeanRemote;
    @EJB
    private static LendAndReturnEntitySessionBeanRemote lendAndReturnSessionBeanRemote;
    
    
    public static void main(String[] args)
    {
        MainApp mainApp = new MainApp(staffEntitySessionBeanRemote, memberEntitySessionBeanRemote, bookEntitySessionBeanRemote, lendAndReturnSessionBeanRemote);
        mainApp.runApp();
    }
}