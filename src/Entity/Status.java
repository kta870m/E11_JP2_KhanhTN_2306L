package Entity;

public enum Status {
    C("Complete"),P("Pending"),R("Reject");
    private String lable;

    Status(String lable){
        this.lable = lable;
    }

    public String getLable(){
        return lable;
    }
}
