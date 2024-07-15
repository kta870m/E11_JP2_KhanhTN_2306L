package Entity;

public enum Type {
    WITHDRAWAL("WITHDRAWAL"),DEPOSIT("DEPOSIT");

    private String label;

    Type(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
