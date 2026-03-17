

public class CalculaTOR {

    private  double PI = 3.1416;

    public void square(double side) {
        double area = side * side;
        double perimeter = 4 * side;
        System.out.println("Area: " + area + ", Perimeter: " + perimeter);
    }

    public void rectangle(double base, double height) {
        double area = base * height;
        double perimeter = 2 * (base + height);
        System.out.println("Area: " + area + ", Perimeter: " + perimeter);
    }

    public void triangle(double base, double height, double s1, double s2, double s3) {
        double area = (base * height) / 2;
        double perimeter = s1 + s2 + s3;
        System.out.println("Area: " + area + ", Perimeter: " + perimeter);
    }

    public void circle(double radius) {
        double area = PI * radius * radius;
        double perimeter = 2 * PI * radius;
        System.out.println("Area: " + area + ", Perimeter: " + perimeter);
    }

    public void pentagon(double side, double apothem) {
        double perimeter = 5 * side;
        double area = (perimeter * apothem) / 2;
        System.out.println("Area: " + area + ", Perimeter: " + perimeter);
    }
}
