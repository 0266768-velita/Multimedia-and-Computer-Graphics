
import java.util.Scanner;

public class Menu {

    public int showMenu() {
        Scanner sc = new Scanner(System.in);

        System.out.println("----- MENU -----");
        System.out.println("1. SQUARE");
        System.out.println("2. RECTANGLE");
        System.out.println("3. TRIANGLE");
        System.out.println("4. CIRCLE");
        System.out.println("5. PENTAGON");
        System.out.print("Choose an option: ");

        return sc.nextInt();
    }
}
