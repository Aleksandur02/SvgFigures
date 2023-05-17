import java.util.Scanner;

public class Menu {
    public static int menu(){
        Scanner scan= new Scanner(System.in);
        int choice;
        System.out.println("1.Open");
        System.out.println("2.Create");
        System.out.println("3.Print");
        System.out.println("4.Save");
        System.out.println("5.Save as");
        System.out.println("6.Help");
        System.out.println("7.Close");
        System.out.println("8.Remove");
        System.out.println("9.Exit");
        do{
            System.out.print("Select your option:");
            choice= scan.nextInt();
        }while(choice<1 || choice>9);
        return choice;
    }
}
