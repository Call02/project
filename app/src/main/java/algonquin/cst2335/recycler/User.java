package algonquin.cst2335.recycler;


public class User {
    private String ID;
    private String Type;
    private String Attributes;




    public User(String ID, String Type, String Attributes) {
        this.ID = ID;
        this.Type = Type;
        this.Attributes = Attributes;



    }


    public String getID(){

        return ID;
    }

    public String getType(){

        return Type;
    }

    public String getAttributes(){

        return Attributes;
    }



}
