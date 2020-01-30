package company;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 */
public class Company {
    private static Logger logger = Logger.getLogger(Company.class.getName());
    private static String executePath;
    private static Company instance;

    static {
        FileHandler handler = null;
        try {
            executePath = new File(Company.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getPath();
            handler = new FileHandler(String.format("%s%scompany.log", executePath, File.separator), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(handler).setFormatter(new SimpleFormatter());
        handler.setLevel(Level.INFO);
        logger.addHandler(handler);
    }

    private String fileProperty = "test.properties";
    private Properties prop = new Properties();
    @Property(propertyName = "com.mycompany.name")
    private String myCompanyName;

    @Property(propertyName = "com.mycompany.owner", defaultValue = "I am owner.")
    private String myCompanyOwner;

    @Property(propertyName = "com.mycompany.address")
    private Address address;

    @Property(propertyName = "com.mycompany.years.old")
    private Integer yearsOld;

    @Property(propertyName = "com.mycompany.capitalization", defaultValue = "30.3")
    private Double capitalization;

    private Company(String fileProperties) {
        if (fileProperties != null) {
            fileProperty = fileProperties;
        }
        try {
            loadClassFromProperties();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Exception in readFileProperties method", e);
        }
    }

    /**
     * @param fileProperties name file properties
     * @return instance Company
     */
    public static Company getInstance(String fileProperties) {
        if (instance == null) {
            instance = new Company(fileProperties);
        } else if (fileProperties != null) {
            instance.fileProperty = fileProperties;
        }
        return instance;
    }


    private void loadClassFromProperties() throws Exception {
        refreshProperties();
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                setFieldFromProperties(field);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Exception in setFieldFromProperties method", e);
                field.set(this, null);
            }
        }

    }

    private void setFieldFromProperties(Field field) throws Exception {
        Property annotation = field.getAnnotation(Property.class);
        if (annotation == null) return;
        String currProp = prop.getProperty(annotation.propertyName());
        if (currProp == null) {
            currProp = annotation.defaultValue();
        }
        if (currProp.equals("")) {
            field.set(this, null);
            return;
        }
        if (field.getType() == Integer.class) {
            Integer i = Integer.valueOf(currProp);
            field.set(this, i);
        } else if (field.getType() == String.class) {
            field.set(this, currProp);
        } else if (field.getType() == Double.class) {
            Double d = Double.valueOf(currProp);
            field.set(this, d);

        } else if (field.getType() == Address.class) {
            Gson gson = new Gson();
            Address addr = gson.fromJson(currProp, Address.class);
            field.set(this, addr);
        } else {
            logger.log(Level.WARNING, String.format("The type %s is not allowed for annotation", field.getName()));
            field.set(this, null);
        }

    }

    private void refreshProperties() throws IOException {
        try (FileInputStream fin = new FileInputStream(String.format("%s%s%s", executePath, File.separator, fileProperty))) {
            prop.load(fin);
        }
    }

    public String getMyCompanyName() {
        return myCompanyName;
    }

    public void setMyCompanyName(String myCompanyName) {
        this.myCompanyName = myCompanyName;
    }

    public String getMyCompanyOwner() {
        return myCompanyOwner;
    }

    public void setMyCompanyOwner(String myCompanyOwner) {
        this.myCompanyOwner = myCompanyOwner;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     *
     */
    public synchronized void doRefresh() {
        try {
            loadClassFromProperties();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Exception in doRefresh method", e);
        }
    }

    public Integer getYearsOld() {
        return yearsOld;
    }

    public void setYearsOld(Integer yearsOld) {
        this.yearsOld = yearsOld;
    }

    public Double getCapitalization() {
        return capitalization;
    }

    public void setCapitalization(Double capitalization) {
        this.capitalization = capitalization;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Company{").append("myCompanyName='").append(myCompanyName).append('\'').append(", myCompanyOwner='").append(myCompanyOwner).append('\'').append(", address=").append(address).append(", yearsOld=").append(yearsOld).append(", capitalization=").append(capitalization).append('}').toString();
    }
}
