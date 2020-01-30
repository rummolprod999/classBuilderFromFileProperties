package company;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
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
    private static String fileProperty = "test.properties";

    static {
        FileHandler handler = null;
        try {
            executePath = new File(Company.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getPath();
            handler = new FileHandler(String.format("%s%scompany.log", executePath, File.separator), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.INFO);
        logger.addHandler(handler);
    }

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

    private Company(String fp) {
        if (fp != null) {
            fileProperty = fp;
        }
        try {
            loadClassFromProperties();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Exception in readFileProperties method", e);
        }
    }

    /**
     * @param fp name file properties
     * @return instance Company
     */
    public static Company getInstance(String fp) {
        if (instance == null) {
            instance = new Company(fp);
        }
        if (fp != null) {
            fileProperty = fp;
        }
        return instance;
    }


    private void loadClassFromProperties() throws Exception {
        refreshProperties();
        for (Field f : this.getClass().getDeclaredFields()) {
            try {
                setFieldFromProperties(f);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Exception in setFieldFromProperties method", e);
                f.set(this, null);
            }
        }

    }

    private void setFieldFromProperties(Field f) throws Exception {
        Property annotation = f.getAnnotation(Property.class);
        if (annotation == null) return;
        String currProp = prop.getProperty(annotation.propertyName());
        if (currProp == null) {
            currProp = annotation.defaultValue();
        }
        if (currProp.equals("")) {
            f.set(this, null);
            return;
        }
        if (f.getType() == Integer.class) {
            Integer i = Integer.valueOf(currProp);
            f.set(this, i);
        } else if (f.getType() == String.class) {
            f.set(this, currProp);
        } else if (f.getType() == Double.class) {
            Double d = Double.valueOf(currProp);
            f.set(this, d);

        } else if (f.getType() == Address.class) {
            Gson gson = new Gson();
            Address addr = gson.fromJson(currProp, Address.class);
            f.set(this, addr);
        } else {
            logger.log(Level.WARNING, String.format("The type %s is not allowed for annotation", f.getName()));
            f.set(this, null);
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
