package Entity;

public enum Status {
    C("Complete"),P("Pending"),R("Reject");
    private String label;

    Status(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
