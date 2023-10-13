public class Adress{

    private String city;
    private String district;
    private String country;
    private String avenue;
    private String apartmentNo;

    public Adress(String avenue,String apartmentNo, String district, String city, String country) {
        this.avenue = avenue;
        this.apartmentNo = apartmentNo;
        this.city = city;
        this.district = district;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
