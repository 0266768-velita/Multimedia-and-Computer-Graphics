
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Menu menu = new Menu();
        CalculaTOR calc = new CalculaTOR();

        int option = menu.showMenu();

        switch (option) {
            case 1:
                System.out.print("Enter side: ");
                double side = sc.nextDouble();
                calc.square(side);
                break;

            case 2:
                System.out.print("Enter base: ");
                double base = sc.nextDouble();
                System.out.print("Enter height: ");
                double height = sc.nextDouble();
                calc.rectangle(base, height);
                break;

            case 3:
                System.out.print("Enter base: ");
                base = sc.nextDouble();
                System.out.print("Enter height: ");
                height = sc.nextDouble();
                System.out.print("Enter side 1: ");
                double s1 = sc.nextDouble();
                System.out.print("Enter side 2: ");
                double s2 = sc.nextDouble();
                System.out.print("Enter side 3: ");
                double s3 = sc.nextDouble();
                calc.triangle(base, height, s1, s2, s3);
                break;

            case 4:
                System.out.print("Enter radius: ");
                double radius = sc.nextDouble();
                calc.circle(radius);
                break;

            case 5:
                System.out.print("Enter side: ");
                side = sc.nextDouble();
                System.out.print("Enter apothem: ");
                double apothem = sc.nextDouble();
                calc.pentagon(side, apothem);
                break;

            default:
                System.out.println("Invalid option");
        }

        sc.close();
    }
}
