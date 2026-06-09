package bookstore.pojos;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    // Helper method to simulate console input
    private Scanner createMockScanner(String input) {
        return new Scanner(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    void testDefaultConstructorAndSetters() {
        Ticket ticket = new Ticket();

        // Verify default field values
        assertEquals("", ticket.getDescription());
        assertEquals(0.0, ticket.getPrice(), 0.001);
        assertNotNull(ticket.getProductId()); // Inherited from Product

        // Verify setters
        ticket.setDescription("Rock Concert Ticket");
        ticket.setPrice(75.50);
        ticket.setProductId("T-1001");

        assertEquals("Rock Concert Ticket", ticket.getDescription());
        assertEquals(75.50, ticket.getPrice(), 0.001);
        assertEquals("T-1001", ticket.getProductId());
    }

    @Test
    void testInitialize_PopulatesFieldsCorrectly() {
        // Prompts sequence for Ticket.initialize():
        // 1. Enter Description
        // 2. Enter Price
        String inputData = "Festival Day Pass\n120.00\n";
        Scanner mockScanner = createMockScanner(inputData);

        Ticket ticket = new Ticket();
        ticket.initialize(mockScanner);

        assertEquals("Festival Day Pass", ticket.getDescription());
        assertEquals(120.00, ticket.getPrice(), 0.001);
    }

    @Test
    void testInitialize_HandlesInvalidAndEmptyDefaults() {
        // Simulate empty and invalid inputs to check fallback behavior
        String inputData = "\nnot-a-double\n";
        Scanner mockScanner = createMockScanner(inputData);

        Ticket ticket = new Ticket();
        ticket.initialize(mockScanner);

        // Falling back to defaults specified in inputs
        assertEquals("Ticket", ticket.getDescription()); // Default description
        assertEquals(0.0, ticket.getPrice(), 0.001);     // Default fallback for invalid price
    }

    @Test
    void testEdit_UpdatesFieldsAndKeepsOthers() {
        Ticket ticket = new Ticket();
        ticket.setDescription("Movie Ticket");
        ticket.setPrice(12.00);

        // Prompts sequence for Ticket.edit():
        // 1. Edit Description
        // 2. Edit Price

        // Scenario: Change description, keep price
        String inputData = "3D IMAX Movie Ticket\n\n";
        Scanner mockScanner = createMockScanner(inputData);

        ticket.edit(mockScanner);

        assertEquals("3D IMAX Movie Ticket", ticket.getDescription()); // Updated
        assertEquals(12.00, ticket.getPrice(), 0.001);                 // Kept
    }

    @Test
    void testEdit_KeepsAllFieldsOnEmptyInput() {
        Ticket ticket = new Ticket();
        ticket.setDescription("Theater Ticket");
        ticket.setPrice(45.00);

        // Scenario: Empty inputs to accept existing values
        String inputData = "\n\n";
        Scanner mockScanner = createMockScanner(inputData);

        ticket.edit(mockScanner);

        assertEquals("Theater Ticket", ticket.getDescription());
        assertEquals(45.00, ticket.getPrice(), 0.001);
    }

    @Test
    void testSellItem_RunsWithoutException() {
        Ticket ticket = new Ticket();
        ticket.setDescription("Raffle Entry");
        ticket.setPrice(5.00);

        // Verify sellItem does not throw exceptions
        assertDoesNotThrow(ticket::sellItem);
    }

    @Test
    void testEqualsAndHashCode() {
        Ticket ticket1 = new Ticket();
        ticket1.setDescription("Museum Ticket");
        ticket1.setPrice(15.00);
        ticket1.setProductId("M-101");

        Ticket ticket2 = new Ticket();
        ticket2.setDescription("Museum Ticket");
        ticket2.setPrice(15.00);
        ticket2.setProductId("M-101"); // Match product IDs to test equality

        Ticket ticket3 = new Ticket();
        ticket3.setDescription("Museum Ticket");
        ticket3.setPrice(15.00);
        // ticket3 has a auto-generated different UUID product ID

        // Test equality with matching fields and product ID
        assertEquals(ticket1, ticket2);
        assertEquals(ticket1.hashCode(), ticket2.hashCode());

        // Test inequality due to differing product IDs (inherited from Product)
        assertNotEquals(ticket1, ticket3);

        // Test basic inequality
        Ticket ticketDifferentVal = new Ticket();
        ticketDifferentVal.setDescription("Different Event");
        ticketDifferentVal.setPrice(15.00);
        ticketDifferentVal.setProductId("M-101");
        assertNotEquals(ticket1, ticketDifferentVal);
    }

    @Test
    void testToString() {
        Ticket ticket = new Ticket();
        ticket.setDescription("Comedy Show");
        ticket.setPrice(25.00);

        String result = ticket.toString();

        assertTrue(result.contains("Comedy Show"));
        assertTrue(result.contains("price=25.0"));
    }
}