package models;
import patterns.strategy.GuestFineStrategy;

public class Guest extends User {

    public Guest(String userId, String name, String email, 
                 String contactNumber) {
        super(userId, name, email, contactNumber);
        this.fineStrategy = new GuestFineStrategy();
    }

    @Override
    public int getBorrowLimit() {
        return 2; }

    @Override
    public int getBorrowDays() {
        return 7; }
}
