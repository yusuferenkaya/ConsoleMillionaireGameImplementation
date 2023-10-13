public class Date {
    private int day;
    private int month;
    private int year;

    Date(int day, int month, int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return this.day;
    }

    public int getMonth() {
        return this.month;
    }

    public int getYear() {
        return this.year;
    }

    public int calculateAge(){
        return 2022 - getYear();
    }
}
