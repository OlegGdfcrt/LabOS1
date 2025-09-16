public class Employee {
    private int num;
    private String name;
    private double hours;

    public Employee(int num, String name, double hours) {
        this.num = num;
        this.name = name;
        this.hours = hours;
    }

    public int getNum() { return num; }
    public String getName() { return name; }
    public double getHours() { return hours; }

    @Override
    public String toString() {
        return "Num: " + num + ", Name: " + name + ", Hours: " + hours;
    }
}
