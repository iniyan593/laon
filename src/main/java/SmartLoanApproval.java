import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SmartLoanApproval {

    static class Customer {
        String customerId;
        String name;
        int age;
        double monthlyIncome;
        String employmentType;
        int creditScore;
        double existingLoan;
        double requestedLoan;
        int loanTenure;

        Customer(String customerId, String name, int age,
                 double monthlyIncome, String employmentType,
                 int creditScore, double existingLoan,
                 double requestedLoan, int loanTenure) {

            this.customerId = customerId;
            this.name = name;
            this.age = age;
            this.monthlyIncome = monthlyIncome;
            this.employmentType = employmentType;
            this.creditScore = creditScore;
            this.existingLoan = existingLoan;
            this.requestedLoan = requestedLoan;
            this.loanTenure = loanTenure;
        }
    }

    public static boolean isValidEmploymentType(String employmentType) {

        return employmentType.equalsIgnoreCase("Salaried")
                || employmentType.equalsIgnoreCase("Self-Employed")
                || employmentType.equalsIgnoreCase("Business")
                || employmentType.equalsIgnoreCase("Unemployed");
    }

    public static double calculateDTI(Customer customer) {

        if (customer.monthlyIncome <= 0) {
            throw new IllegalArgumentException(
                    "Monthly income must be greater than zero.");
        }

        return (customer.existingLoan / customer.monthlyIncome) * 100;
    }

    public static double calculateEMI(Customer customer) {

        if (customer.loanTenure <= 0) {
            throw new IllegalArgumentException(
                    "Loan tenure must be greater than zero.");
        }

        double annualInterestRate;

        if (customer.creditScore >= 750) {
            annualInterestRate = 8.0;
        } else if (customer.creditScore >= 650) {
            annualInterestRate = 10.0;
        } else {
            annualInterestRate = 12.0;
        }

        double monthlyRate = annualInterestRate / (12 * 100);

        int months = customer.loanTenure * 12;

        return (customer.requestedLoan * monthlyRate
                * Math.pow(1 + monthlyRate, months))
                / (Math.pow(1 + monthlyRate, months) - 1);
    }

    public static String evaluateLoan(Customer customer) {

        List<String> reasons = new ArrayList<>();

        // Input validation
        if (customer.age < 18) {
            reasons.add(
                    "Customer must be at least 18 years old.");
        }

        if (customer.monthlyIncome <= 0) {
            reasons.add(
                    "Monthly income must be greater than zero.");
        }

        if (customer.creditScore < 300 ||
                customer.creditScore > 900) {

            reasons.add(
                    "Credit score must be between 300 and 900.");
        }

        if (customer.existingLoan < 0) {
            reasons.add(
                    "Existing loan amount cannot be negative.");
        }

        if (customer.requestedLoan <= 0) {
            reasons.add(
                    "Requested loan amount must be greater than zero.");
        }

        if (customer.loanTenure <= 0) {
            reasons.add(
                    "Loan tenure must be greater than zero.");
        }

        if (!isValidEmploymentType(customer.employmentType)) {
            reasons.add("Invalid employment type.");
        }

        // Return all validation failures
        if (!reasons.isEmpty()) {
            return "REJECTED - " + String.join(" ", reasons);
        }

        // Calculate DTI
        double dti = calculateDTI(customer);

        // Risk conditions
        if (customer.creditScore < 600) {
            reasons.add("Poor credit score.");
        }

        if (dti > 50) {
            reasons.add("High debt-to-income ratio.");
        }

        // Maximum eligible loan = 20 times monthly income
        double maximumEligibleLoan =
                customer.monthlyIncome * 20;

        if (customer.requestedLoan > maximumEligibleLoan) {
            reasons.add(
                    "Requested loan amount exceeds eligible limit.");
        }

        if (customer.employmentType.equalsIgnoreCase("Unemployed")) {
            reasons.add("Customer is unemployed.");
        }

        // Approval classification
        if (reasons.isEmpty()) {

            if (customer.creditScore >= 750 && dti <= 30) {
                return "APPROVED - LOW RISK";
            } else {
                return "APPROVED - MEDIUM RISK";
            }
        }

        // High-risk rejection
        if (customer.creditScore < 600 || dti > 50) {
            return "REJECTED - HIGH RISK: "
                    + String.join(" ", reasons);
        }

        // Other issues
        return "CONDITIONALLY APPROVED - "
                + String.join(" ", reasons);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {

            System.out.print("Enter number of customers: ");
            int n = sc.nextInt();
            sc.nextLine();

            if (n <= 0) {
                throw new IllegalArgumentException(
                        "Number of customers must be greater than zero.");
            }

            for (int i = 0; i < n; i++) {

                System.out.println();
                System.out.println("=================================");
                System.out.println("          CUSTOMER " + (i + 1));
                System.out.println("=================================");

                System.out.print("Enter Customer ID: ");
                String customerId = sc.nextLine();

                System.out.print("Enter Customer Name: ");
                String name = sc.nextLine();

                System.out.print("Enter Age: ");
                int age = sc.nextInt();

                System.out.print("Enter Monthly Income: ");
                double monthlyIncome = sc.nextDouble();
                sc.nextLine();

                System.out.print(
                        "Enter Employment Type " +
                        "(Salaried/Self-Employed/Business/Unemployed): ");

                String employmentType = sc.nextLine();

                System.out.print("Enter Credit Score: ");
                int creditScore = sc.nextInt();

                System.out.print("Enter Existing Loan Amount: ");
                double existingLoan = sc.nextDouble();

                System.out.print("Enter Requested Loan Amount: ");
                double requestedLoan = sc.nextDouble();

                System.out.print("Enter Loan Tenure (years): ");
                int loanTenure = sc.nextInt();
                sc.nextLine();

                Customer customer = new Customer(
                        customerId,
                        name,
                        age,
                        monthlyIncome,
                        employmentType,
                        creditScore,
                        existingLoan,
                        requestedLoan,
                        loanTenure);

                String result = evaluateLoan(customer);

                System.out.println();
                System.out.println("---------------------------------");
                System.out.println("        LOAN ASSESSMENT");
                System.out.println("---------------------------------");

                System.out.println(
                        "Customer ID       : "
                                + customer.customerId);

                System.out.println(
                        "Customer Name     : "
                                + customer.name);

                System.out.println(
                        "Age               : "
                                + customer.age);

                System.out.println(
                        "Monthly Income    : ₹"
                                + customer.monthlyIncome);

                System.out.println(
                        "Employment Type   : "
                                + customer.employmentType);

                System.out.println(
                        "Credit Score      : "
                                + customer.creditScore);

                System.out.println(
                        "Existing Loan     : ₹"
                                + customer.existingLoan);

                System.out.println(
                        "Requested Loan    : ₹"
                                + customer.requestedLoan);

                System.out.println(
                        "Loan Tenure       : "
                                + customer.loanTenure
                                + " years");

                if (customer.monthlyIncome > 0) {

                    double dti = calculateDTI(customer);

                    System.out.printf(
                            "Debt-to-Income    : %.2f%%%n",
                            dti);
                }

                if (customer.requestedLoan > 0
                        && customer.loanTenure > 0
                        && customer.creditScore >= 300
                        && customer.creditScore <= 900) {

                    double emi = calculateEMI(customer);

                    System.out.printf(
                            "Estimated EMI     : ₹%.2f%n",
                            emi);
                }

                System.out.println(
                        "Decision          : " + result);

                System.out.println("---------------------------------");
            }

        } catch (Exception e) {

            System.out.println(
                    "Invalid input: " + e.getMessage());

        } finally {

            sc.close();
        }
    }
}