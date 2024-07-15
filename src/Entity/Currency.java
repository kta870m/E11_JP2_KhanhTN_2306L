package Entity;

public enum Currency {
    VND("VND"),USD("USD");
    private String label;

    Currency(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
