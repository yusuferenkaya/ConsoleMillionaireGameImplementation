public class Participant{

    private String name;
    private String birthDate;
    private int age;
    private String phoneNumber;
    private Adress adress;
    private boolean contested;
    private int gainedMoney;

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public int getAge(){return age;}

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Adress getAdress() {
        return adress;
    }

    public String getCity(){
        return adress.getCity();
    }

    public void setContested(){
        contested = true;
    }

    public boolean isContested(){
        return contested;
    }

    public void setGainedMoney(int money){
        gainedMoney = money;
    }

    public int getGainedMoney(){
        return gainedMoney;
    }



    Participant(String name, int age, String phoneNumber, Adress adress){
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.adress = adress;
        contested = false;
        gainedMoney = 0;


    }




}