package models;
import patterns.strategy.StudentFineStrategy;

public class Student extends User {

    public Student(String userId, String name, String email, 
                   String contactNumber) {
        super(userId, name, email, contactNumber);
        this.fineStrategy = new StudentFineStrategy(); }

    @Override
    public int getBorrowLimit() {
        return 5; }

    @Override
    public int getBorrowDays() {
        return 14; }
}