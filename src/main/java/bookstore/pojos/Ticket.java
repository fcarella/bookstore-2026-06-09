package bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

public class Ticket extends Product {
    private String description = "";
    private Double price = 0.0;

    @Override
    public void sellItem() {
        System.out.println("Selling Ticket: " + description + " for " + price);
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public void initialize(Scanner input) {
        System.out.println("Enter Description:");
        this.description = getInput(input, "Ticket");

        System.out.println("Enter Price:");
        this.price = getInput(input, 0.0);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public void edit(Scanner input) {
        System.out.println("Edit Description [" + this.description + "]:");
        this.description = getInput(input, this.description);

        System.out.println("Edit Price [" + this.price + "]:");
        this.price = getInput(input, this.price);
    }

    @Override
    public String toString() {
        return "Ticket{desc='" + description + "', price=" + price + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(description, ticket.description) && Objects.equals(price, ticket.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description, price);
    }
}
