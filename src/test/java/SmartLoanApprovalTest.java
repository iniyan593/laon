import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SmartLoanApprovalTest {

    @Test
    public void testLowRiskApprovedCustomer() {

        SmartLoanApproval.Customer customer =
                new SmartLoanApproval.Customer(
                        "C001",
                        "Arun",
                        30,
                        50000,
                        "Salaried",
                        800,
                        5000,
                        500000,
                        5);

        String result =
                SmartLoanApproval.evaluateLoan(customer);

        assertEquals(
                "APPROVED - LOW RISK",
                result);
    }

    @Test
    public void testMinimumAgeBoundary() {

        SmartLoanApproval.Customer customer =
                new SmartLoanApproval.Customer(
                        "C002",
                        "Kumar",
                        18,
                        40000,
                        "Salaried",
                        750,
                        5000,
                        300000,
                        5);

        String result =
                SmartLoanApproval.evaluateLoan(customer);

        assertTrue(result.contains("APPROVED"));
    }

    @Test
    public void testPoorCreditScore() {

        SmartLoanApproval.Customer customer =
                new SmartLoanApproval.Customer(
                        "C003",
                        "Ravi",
                        30,
                        50000,
                        "Salaried",
                        550,
                        5000,
                        300000,
                        5);

        String result =
                SmartLoanApproval.evaluateLoan(customer);

        assertTrue(
                result.contains("Poor credit score"));

        assertTrue(
                result.contains("HIGH RISK"));
    }

    @Test
    public void testHighDebtToIncomeRatio() {

        SmartLoanApproval.Customer customer =
                new SmartLoanApproval.Customer(
                        "C004",
                        "Vijay",
                        35,
                        30000,
                        "Salaried",
                        700,
                        20000,
                        300000,
                        5);

        String result =
                SmartLoanApproval.evaluateLoan(customer);

        assertTrue(
                result.contains(
                        "High debt-to-income ratio"));

        assertTrue(
                result.contains("HIGH RISK"));
    }

    @Test
    public void testInvalidIncome() {

        SmartLoanApproval.Customer customer =
                new SmartLoanApproval.Customer(
                        "C005",
                        "Suresh",
                        30,
                        0,
                        "Salaried",
                        750,
                        5000,
                        300000,
                        5);

        String result =
                SmartLoanApproval.evaluateLoan(customer);

        assertTrue(
                result.contains(
                        "Monthly income must be greater than zero"));
    }

    @Test
    public void testInvalidCreditScore() {

        SmartLoanApproval.Customer customer =
                new SmartLoanApproval.Customer(
                        "C006",
                        "Manoj",
                        30,
                        50000,
                        "Salaried",
                        950,
                        5000,
                        300000,
                        5);

        String result =
                SmartLoanApproval.evaluateLoan(customer);

        assertTrue(
                result.contains(
                        "Credit score must be between 300 and 900"));
    }

    @Test
    public void testLoanAmountExceedsLimit() {

        SmartLoanApproval.Customer customer =
                new SmartLoanApproval.Customer(
                        "C007",
                        "Raj",
                        30,
                        20000,
                        "Salaried",
                        750,
                        2000,
                        1000000,
                        5);

        String result =
                SmartLoanApproval.evaluateLoan(customer);

        assertTrue(
                result.contains(
                        "Requested loan amount exceeds eligible limit"));
    }

    @Test
    public void testInvalidEmploymentType() {

        SmartLoanApproval.Customer customer =
                new SmartLoanApproval.Customer(
                        "C008",
                        "Ajay",
                        30,
                        50000,
                        "Student",
                        750,
                        5000,
                        300000,
                        5);

        String result =
                SmartLoanApproval.evaluateLoan(customer);

        assertTrue(
                result.contains(
                        "Invalid employment type"));
    }

    @Test
    public void testMultipleFailures() {

        SmartLoanApproval.Customer customer =
                new SmartLoanApproval.Customer(
                        "C009",
                        "Rahul",
                        17,
                        0,
                        "Student",
                        250,
                        -5000,
                        0,
                        0);

        String result =
                SmartLoanApproval.evaluateLoan(customer);

        assertTrue(
                result.contains(
                        "Customer must be at least 18 years old"));

        assertTrue(
                result.contains(
                        "Monthly income must be greater than zero"));

        assertTrue(
                result.contains(
                        "Credit score must be between 300 and 900"));

        assertTrue(
                result.contains(
                        "Existing loan amount cannot be negative"));

        assertTrue(
                result.contains(
                        "Requested loan amount must be greater than zero"));

        assertTrue(
                result.contains(
                        "Loan tenure must be greater than zero"));

        assertTrue(
                result.contains(
                        "Invalid employment type"));
    }

    @Test
    public void testDTICalculation() {

        SmartLoanApproval.Customer customer =
                new SmartLoanApproval.Customer(
                        "C010",
                        "Vimal",
                        30,
                        50000,
                        "Salaried",
                        750,
                        10000,
                        300000,
                        5);

        double dti =
                SmartLoanApproval.calculateDTI(customer);

        assertEquals(
                20.0,
                dti,
                0.01);
    }

    @Test
    public void testEMICalculation() {

        SmartLoanApproval.Customer customer =
                new SmartLoanApproval.Customer(
                        "C011",
                        "Karthik",
                        30,
                        50000,
                        "Salaried",
                        800,
                        5000,
                        500000,
                        5);

        double emi =
                SmartLoanApproval.calculateEMI(customer);

        assertTrue(emi > 0);
    }
}