package company;

/**
 *
 */
public class Address {
    private String street;
    private String home;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Address{").append("street='").append(street).append('\'').append(", home='").append(home).append('\'').append('}').toString();
    }
}
