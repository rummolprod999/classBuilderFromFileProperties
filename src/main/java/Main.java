import company.Company;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        String fileProperty = null;
        if (args.length > 0) {
            fileProperty = args[0];
        }
        Company company = Company.getInstance(fileProperty);
        out.println(company);
    }

}
