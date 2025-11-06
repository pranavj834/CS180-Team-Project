public interface ResInterface {
    boolean getDate();
    boolean getTime();
    int getAmtOfPeople();
    
    void setDate();
    void setTime();
    void setAmtOfPeople();
    
    boolean booked();
    void assignTable(int tableNum);
    void cancel();
    
}
