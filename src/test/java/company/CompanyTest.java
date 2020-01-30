package company;

import org.junit.Assert;
import org.junit.Test;

public class CompanyTest {

    @Test
    public void getInstance() {
        Company com1 = Company.getInstance(null);
        Company com2 = Company.getInstance(null);
        Assert.assertEquals(com1, com2);
    }

    @Test
    public void CheckFieldCompanyName() {
        Company com1 = Company.getInstance(null);
        Assert.assertEquals(com1.getMyCompanyName(), "SuperSoft");
    }

    @Test
    public void CheckFieldCompanyOwner() {
        Company com1 = Company.getInstance(null);
        Assert.assertEquals(com1.getMyCompanyOwner(), "I am owner.");
    }
}