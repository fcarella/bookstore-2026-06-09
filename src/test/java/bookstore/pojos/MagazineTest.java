package bookstore.pojos;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MagazineTest {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    // Helper method to simulate console input
    private Scanner createMockScanner(String input) {
        return new Scanner(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    void testConstructorAndGetters() {
        // Setup dates
        Date testDate = new Date();

        // Exercise constructor
        Magazine magazine = new Magazine(250, testDate, "Java Developer Monthly", 12.99, 30);

        // Verify fields are populated correctly
        assertEquals("Java Developer Monthly", magazine.getTitle());
        assertEquals(12.99, magazine.getPrice(), 0.001);
        assertEquals(30, magazine.getCopies());
        assertEquals(250, magazine.getOrderQty());
        assertEquals(testDate, magazine.getCurrentIssue());
    }

    @Test
    void testSettersAndGetters() {
        Magazine magazine = new Magazine();
        Date testDate = new Date();

        magazine.setTitle("Tech Insider");
        magazine.setPrice(9.50);
        magazine.setCopies(15);
        magazine.setOrderQty(100);
        magazine.setCurrentIssue(testDate);

        assertEquals("Tech Insider", magazine.getTitle());
        assertEquals(9.50, magazine.getPrice(), 0.001);
        assertEquals(15, magazine.getCopies());
        assertEquals(100, magazine.getOrderQty());
        assertEquals(testDate, magazine.getCurrentIssue());
    }

    @Test
    void testInitialize_PopulatesFieldsCorrectly() throws Exception {
        // Prompts sequence for Magazine.initialize():
        // 1. super.initialize() -> Title (String)
        // 2. super.initialize() -> Copies (int)
        // 3. super.initialize() -> Price (double)
        // 4. this.initialize()  -> Order Qty (int)
        // 5. this.initialize()  -> Date (dd-MMM-yyyy)
        String inputData = "National Geographic\n50\n8.99\n500\n15-Jun-2026\n";
        Scanner mockScanner = createMockScanner(inputData);

        Magazine magazine = new Magazine();
        magazine.initialize(mockScanner);

        Date expectedDate = dateFormat.parse("15-Jun-2026");

        assertEquals("National Geographic", magazine.getTitle());
        assertEquals(50, magazine.getCopies());
        assertEquals(8.99, magazine.getPrice(), 0.001);
        assertEquals(500, magazine.getOrderQty());
        assertEquals(expectedDate, magazine.getCurrentIssue());
    }

    @Test
    void testInitialize_HandlesInvalidAndEmptyDefaults() {
        // Simulate inputs where copies, price, and order quantity are invalid,
        // and date input is empty.
        String inputData = "Empty News\nnot-a-number\nnot-a-double\nnot-an-orderqty\n\n";
        Scanner mockScanner = createMockScanner(inputData);

        Magazine magazine = new Magazine();

        // We capture the date right before calling initialize to assert default assignment
        Date beforeInit = new Date();
        magazine.initialize(mockScanner);
        Date afterInit = new Date();

        assertEquals("Empty News", magazine.getTitle());
        assertEquals(0, magazine.getCopies()); // Fallback default for copies
        assertEquals(0.0, magazine.getPrice(), 0.001); // Fallback default for price
        assertEquals(0, magazine.getOrderQty()); // Fallback default for order qty

        // Fallback default for date is new Date()
        assertTrue(magazine.getCurrentIssue().getTime() >= beforeInit.getTime() &&
                magazine.getCurrentIssue().getTime() <= afterInit.getTime());
    }

    @Test
    void testEdit_UpdatesFieldsAndKeepsOthers() throws Exception {
        Date originalDate = dateFormat.parse("01-Jan-2026");
        Magazine magazine = new Magazine(100, originalDate, "Retro Gamer", 6.99, 10);

        // Prompts sequence for Magazine.edit():
        // 1. super.edit() -> Edit Title
        // 2. super.edit() -> Edit Price
        // 3. super.edit() -> Edit Copies
        // 4. this.edit()  -> Edit Order Qty
        // 5. this.edit()  -> Edit Date

        // Scenario: Change title and price, keep copies, change order quantity, change date
        String inputData = "Retro Gamer Classic\n7.99\n\n150\n20-Jul-2026\n";
        Scanner mockScanner = createMockScanner(inputData);

        magazine.edit(mockScanner);

        Date expectedDate = dateFormat.parse("20-Jul-2026");

        assertEquals("Retro Gamer Classic", magazine.getTitle()); // Changed
        assertEquals(7.99, magazine.getPrice(), 0.001);            // Changed
        assertEquals(10, magazine.getCopies());                    // Kept original (from empty line)
        assertEquals(150, magazine.getOrderQty());                 // Changed
        assertEquals(expectedDate, magazine.getCurrentIssue());    // Changed
    }

    @Test
    void testEdit_KeepsAllFieldsOnEmptyInput() throws Exception {
        Date originalDate = dateFormat.parse("01-Jan-2026");
        Magazine magazine = new Magazine(100, originalDate, "Retro Gamer", 6.99, 10);

        // Scenario: All empty lines (representing user pressing [Enter] to accept existing values)
        String inputData = "\n\n\n\n\n";
        Scanner mockScanner = createMockScanner(inputData);

        magazine.edit(mockScanner);

        assertEquals("Retro Gamer", magazine.getTitle());
        assertEquals(6.99, magazine.getPrice(), 0.001);
        assertEquals(10, magazine.getCopies());
        assertEquals(100, magazine.getOrderQty());
        assertEquals(originalDate, magazine.getCurrentIssue());
    }

    @Test
    void testSellItem_DecrementsCopies() {
        Magazine magazine = new Magazine(100, new Date(), "Weekly Tech", 5.0, 5);

        magazine.sellItem();

        assertEquals(4, magazine.getCopies());
    }

    @Test
    void testToString() {
        Date originalDate = new Date();
        Magazine magazine = new Magazine(120, originalDate, "Fashion Now", 15.0, 8);

        String result = magazine.toString();

        assertTrue(result.contains("Fashion Now"));
        assertTrue(result.contains("orderQty=120"));
        assertTrue(result.contains("price=15.0"));
        assertTrue(result.contains("copies=8"));
    }
}