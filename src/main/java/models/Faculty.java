package models;
import patterns.strategy.FacultyFineStrategy;

public class Faculty extends User {

    public Faculty(String userId, String name, String email, 
                   String contactNumber) {
        super(userId, name, email, contactNumber);
        this.fineStrategy = new FacultyFineStrategy(); }

    @Override
    public int getBorrowLimit() {
        return 10; }

    @Override
    public int getBorrowDays() {
        return 30; }
}
