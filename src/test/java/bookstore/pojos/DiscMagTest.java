package bookstore.pojos;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class DiscMagTest {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    // Helper method to simulate console input
    private Scanner createMockScanner(String input) {
        return new Scanner(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    void testConstructorAndGetters() {
        Date testDate = new Date();
        DiscMag discMag = new DiscMag(true, 150, testDate, "PC Gamer with DVD", 14.99, 20);

        // Verify child-specific field
        assertTrue(discMag.isHasDisc());

        // Verify inherited fields from Magazine and Publication
        assertEquals("PC Gamer with DVD", discMag.getTitle());
        assertEquals(14.99, discMag.getPrice(), 0.001);
        assertEquals(20, discMag.getCopies());
        assertEquals(150, discMag.getOrderQty());
        assertEquals(testDate, discMag.getCurrentIssue());
    }

    @Test
    void testSettersAndGetters() {
        DiscMag discMag = new DiscMag();
        Date testDate = new Date();

        discMag.setHasDisc(true);
        discMag.setTitle("Linux Format");
        discMag.setPrice(12.50);
        discMag.setCopies(12);
        discMag.setOrderQty(80);
        discMag.setCurrentIssue(testDate);

        assertTrue(discMag.isHasDisc());
        assertEquals("Linux Format", discMag.getTitle());
        assertEquals(12.50, discMag.getPrice(), 0.001);
        assertEquals(12, discMag.getCopies());
        assertEquals(80, discMag.getOrderQty());
        assertEquals(testDate, discMag.getCurrentIssue());
    }

    @Test
    void testInitialize_PopulatesFieldsCorrectly() throws Exception {
        // Prompts sequence for DiscMag.initialize():
        // 1. Publication -> Title (String)
        // 2. Publication -> Copies (int)
        // 3. Publication -> Price (double)
        // 4. Magazine    -> Order Qty (int)
        // 5. Magazine    -> Current Issue Date (dd-MMM-yyyy)
        // 6. DiscMag     -> Has Disc (boolean)
        String inputData = "Retro Computing\n25\n19.99\n120\n01-May-2026\ntrue\n";
        Scanner mockScanner = createMockScanner(inputData);

        DiscMag discMag = new DiscMag();
        discMag.initialize(mockScanner);

        Date expectedDate = dateFormat.parse("01-May-2026");

        assertEquals("Retro Computing", discMag.getTitle());
        assertEquals(25, discMag.getCopies());
        assertEquals(19.99, discMag.getPrice(), 0.001);
        assertEquals(120, discMag.getOrderQty());
        assertEquals(expectedDate, discMag.getCurrentIssue());
        assertTrue(discMag.isHasDisc());
    }

    @Test
    void testInitialize_HandlesInvalidAndEmptyDefaults() {
        // Simulate empty/invalid inputs. The boolean input for "Has Disc" is empty.
        String inputData = "Fallback Tech\nnot-an-int\nnot-a-double\nnot-an-int\n\n\n";
        Scanner mockScanner = createMockScanner(inputData);

        DiscMag discMag = new DiscMag();

        Date beforeInit = new Date();
        discMag.initialize(mockScanner);
        Date afterInit = new Date();

        assertEquals("Fallback Tech", discMag.getTitle());
        assertEquals(0, discMag.getCopies());
        assertEquals(0.0, discMag.getPrice(), 0.001);
        assertEquals(0, discMag.getOrderQty());
        assertFalse(discMag.isHasDisc()); // Empty input defaults to false

        // Date defaults to current system date
        assertTrue(discMag.getCurrentIssue().getTime() >= beforeInit.getTime() &&
                discMag.getCurrentIssue().getTime() <= afterInit.getTime());
    }

    @Test
    void testEdit_UpdatesFieldsAndKeepsOthers() throws Exception {
        Date originalDate = dateFormat.parse("01-Mar-2026");
        DiscMag discMag = new DiscMag(true, 200, originalDate, "Amiga Active", 8.50, 15);

        // Prompts sequence for DiscMag.edit():
        // 1. Publication -> Edit Title
        // 2. Publication -> Edit Price
        // 3. Publication -> Edit Copies
        // 4. Magazine    -> Edit Order Qty
        // 5. Magazine    -> Edit Issue Date
        // 6. DiscMag     -> Edit Has Disc

        // Scenario: Change title, price, and "has disc", keeping copies, order quantity, and date
        String inputData = "Amiga Active Classic\n9.99\n\n\n\nfalse\n";
        Scanner mockScanner = createMockScanner(inputData);

        discMag.edit(mockScanner);

        assertEquals("Amiga Active Classic", discMag.getTitle()); // Changed
        assertEquals(9.99, discMag.getPrice(), 0.001);             // Changed
        assertEquals(15, discMag.getCopies());                     // Kept original
        assertEquals(200, discMag.getOrderQty());                  // Kept original
        assertEquals(originalDate, discMag.getCurrentIssue());     // Kept original
        assertFalse(discMag.isHasDisc());                          // Changed to false
    }

    @Test
    void testEdit_KeepsAllFieldsOnEmptyInput() throws Exception {
        Date originalDate = dateFormat.parse("10-Apr-2026");
        DiscMag discMag = new DiscMag(true, 75, originalDate, "Code & Disc", 11.00, 5);

        // Scenario: All blank inputs to simulate accepting all defaults during edit
        String inputData = "\n\n\n\n\n\n";
        Scanner mockScanner = createMockScanner(inputData);

        discMag.edit(mockScanner);

        assertEquals("Code & Disc", discMag.getTitle());
        assertEquals(11.00, discMag.getPrice(), 0.001);
        assertEquals(5, discMag.getCopies());
        assertEquals(75, discMag.getOrderQty());
        assertEquals(originalDate, discMag.getCurrentIssue());
        assertTrue(discMag.isHasDisc());
    }

    @Test
    void testSellItem_DecrementsCopies() {
        DiscMag discMag = new DiscMag(true, 50, new Date(), "Demo Disc", 5.99, 10);

        discMag.sellItem();

        assertEquals(9, discMag.getCopies());
    }

    @Test
    void testToString() {
        Date originalDate = new Date();
        DiscMag discMag = new DiscMag(true, 30, originalDate, "Retro Gamer Special", 14.95, 3);

        String result = discMag.toString();

        assertTrue(result.contains("DiscMag"));
        assertTrue(result.contains("hasDisc=true"));
        assertTrue(result.contains("Retro Gamer Special"));
        assertTrue(result.contains("orderQty=30"));
    }
}