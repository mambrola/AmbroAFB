/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambro.AView;
import ambroafb.general.Editable;
import ambroafb.general.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 *
 * @author mambroladze
 */
public class Client {

    // ვინაიდან ეს მხოლოდ ჩვენებაა და თვითო tableView-ს ველში არ ხდება ჩასწორება Property-ები არ გვჭირდება
    public int clientId;

    @AView.Column(width = "24", cellFactory = FirmPersonCellFactory.class)
    private SimpleBooleanProperty isJur;

    @AView.Column(width = "24", cellFactory = RezCellFactory.class)
    private SimpleBooleanProperty isRez;

    private SimpleStringProperty firstName, lastName;

    @AView.Column(title = "%descrip", width = "152")
    private StringExpression descrip;

    @AView.Column(title = "%email", width = "170")
    private SimpleStringProperty email;

    private SimpleStringProperty address, zipCode, city;

    @AView.Column(title = "%full_address", width = "270")
    private StringExpression fullAddress;

    private SimpleStringProperty country_code;

    @AView.Column(title = "%country", width = "80") 
    private SimpleStringProperty country;

    @AView.Column(title = "%id_number", width = "100")
    private SimpleStringProperty IDNumber;

    private SimpleStringProperty phoneIds;      // ეს ჩაემატა აქ და კიდევ ერთ ადგილას, ჩასამატებელია ყველგან

    @AView.Column(title = "%phones", width = "300")
    private SimpleStringProperty phones;
    
    private ArrayList<PhoneNumber> phoneList;

    @AView.Column(title = "%fax", width = "80")
    private SimpleStringProperty fax;

    public Client() {
        isJur =         new SimpleBooleanProperty();
        isRez =         new SimpleBooleanProperty();
        firstName =     new SimpleStringProperty();
        lastName =      new SimpleStringProperty();
        descrip = firstName.concat(" ").concat(lastName);
        email =         new SimpleStringProperty();
        address =       new SimpleStringProperty();
        zipCode =       new SimpleStringProperty();
        city =          new SimpleStringProperty();
        fullAddress = address.concat(", ").concat(zipCode).concat(", ").concat(city);
        country_code =  new SimpleStringProperty();
        country =       new SimpleStringProperty();
        IDNumber =      new SimpleStringProperty();
        phoneIds =      new SimpleStringProperty();
        phones =        new SimpleStringProperty();
        phoneList =     new ArrayList<>();
        
        fax =           new SimpleStringProperty();
        phones.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            phoneList.clear();
            if (newValue != null) {
                for(String phone : Arrays.asList(newValue.split(", ")))
                {
                    phoneList.add(new PhoneNumber(5, phone));  //აქ გასარკვევია და გასაანალიზებელი, შეიძლება თოკა დაგვჭირდეს!
                }
                
                
                
                
            }
        });
    }

    public Client(Object[] values) {
        this();
        System.out.println("values: " + Utils.avoidNullAndReturnBoolean(values[1]) + ":" + values[1]);
        clientId =      Utils.avoidNullAndReturnInt(    values[0]);
        setIsJur(       Utils.avoidNullAndReturnBoolean(values[1]));
        setIsRez(       Utils.avoidNullAndReturnBoolean(values[2]));
        setFirstName(   Utils.avoidNullAndReturnString( values[3]));
        setLastName(    Utils.avoidNullAndReturnString( values[4]));
        setEmail(       Utils.avoidNullAndReturnString( values[5]));
        setAddress(     Utils.avoidNullAndReturnString( values[6]));
        setZipCode(     Utils.avoidNullAndReturnString( values[7]));
        setCity(        Utils.avoidNullAndReturnString( values[8]));
        setCountry_code(Utils.avoidNullAndReturnString( values[9]));
        setCountry(     Utils.avoidNullAndReturnString( values[10]));
        setIDNumber(    Utils.avoidNullAndReturnString( values[11]));
        setPhones(      Utils.avoidNullAndReturnString( values[12]));
        setFax(         Utils.avoidNullAndReturnString( values[13]));
    }

    @Override
    public String toString() {
        return descrip.get() + " : " + email.get() + " : " + fullAddress.get();
    }

    public Client dbGetClient(int clientId) {
        return dbGetClients(clientId).get(clientId);
    }

    static HashMap<Integer, Client> dbGetClients(int recId) {
        HashMap<Integer, Client> clients = new HashMap();
        String query = "SELECT * FROM clients_to_java" + (recId == 0 ? "" : " where rec_id = " + Integer.toString(recId)) + " ORDER BY rec_id";
        String[] orderedRequestedFields = new String[]{"rec_id", "is_jur", "is_rezident", "first_name", "last_name", "email", "address", "zip_code", "city", "country_code", "country_descrip", "pass_number", "phones", "fax"};
        Utils.getArrayListsByQueryFromDB(query, orderedRequestedFields).stream().forEach((row) -> {

            System.out.println("row: " + row[12]);

            clients.put((int) row[0], new Client(row));
        });
        return clients;
    }

    public SimpleBooleanProperty isJurProperty() {
        return isJur;
    }

    public boolean              getIsJur()          { return isJur.get();}
    public boolean              getIsRez()          { return isRez.get();}
    public String               getFirstName()      { return firstName.get();}
    public String               getLastName()       { return lastName.get();}
    public String               getDescrip()        { return descrip.get();}
    public String               getEmail()          { return email.get();}
    public String               getCity()           { return city.get();}
    public String               getFullAddress()    { return fullAddress.get();}
    public String               getCountry_code()   { return country_code.get();}
    public ArrayList<String>    getPhoneList()      { return phoneList;}
    public String               getPhones()         { return phones.get();}
    public String               getFax()            { return fax.get();}
    public String               getAddress()        { return address.get();}
    public String               getZipCode()        { return zipCode.get();}
    public String               getCountry()        { return country.get();}
    public String               getIDNumber()       { return IDNumber.get();}

    public final void setIsJur(         boolean isJur)              { this.isJur.set(isJur);}
    public final void setIsRez(         boolean isRez)              { this.isRez.set(isRez);}
    public final void setFirstName(     String firstName)           { this.firstName.set(firstName);}
    public final void setLastName(      String lastName)            { this.lastName.set(lastName);}
    public final void setEmail(         String email)               { this.email.set(email);}
    public final void setAddress(       String address)             { this.address.set(address);}
    public final void setZipCode(       String zipCode)             { this.zipCode.set(zipCode);}
    public final void setCity(          String city)                { this.city.set(city);}
    public final void setCountry_code(  String country_code)        { this.country_code.set(country_code);}
    public final void setCountry(       String country)             { this.country.set(country);}
    public final void setIDNumber(      String IDNumber)            { this.IDNumber.set(IDNumber);}
    public final void setPhoneList(     ArrayList<String> phoneList){ this.phoneList = phoneList;}
    public final void setPhones(        String phones)              { this.phones.set(phones.replaceAll(":;:", ",  "));}
    public final void setFax(           String fax)                 { this.fax.set(fax);}
    
    public static class FirmPersonCellFactory implements Callback<TableColumn<Client, Boolean>, TableCell<Client, Boolean>> {

        @Override
        public TableCell<Client, Boolean> call(TableColumn<Client, Boolean> param) {
            TableCell<Client, Boolean> cell = new TableCell<Client, Boolean>() {
                private ImageView view = new ImageView();

                @Override
                public void updateItem(Boolean isFirm, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                        view.setImage(new Image(isFirm ? "/images/office.png" : "/images/person.png"));
                        setGraphic(view);
                    }
                }
            };
            return cell;
        }
    }
    
    public static class RezCellFactory implements Callback<TableColumn<Client, Boolean>, TableCell<Client, Boolean>> {

        @Override
        public TableCell<Client, Boolean> call(TableColumn<Client, Boolean> param) {
            return new TableCell<Client, Boolean>(){
                @Override
                public void updateItem(Boolean isFirm, boolean empty) {
                    setText(empty?null:(isFirm?"Rz":null));
                }
            };
        }
        
    }
    
    public static class PhoneNumber implements Editable<String>{
        
        private int id;
        private final StringProperty number = new SimpleStringProperty();

        public PhoneNumber(){}
        public PhoneNumber(int id, String number){
            this.id = id;
            this.number.set(number);
        }
        
        public PhoneNumber(String number){
            this.number.set(number);
        }
        
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
        
        public String getNumber() {
            return number.get();
        }

        public void setNumber(String value) {
            number.set(value);
        }

        public StringProperty numberProperty() {
            return number;
        }

        @Override
        public void edit(String param) {
            setNumber(param);
        }

    }
}
