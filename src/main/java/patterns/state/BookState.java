package patterns.state;

public interface BookState {
    void handle();
    String getStateName();
}
