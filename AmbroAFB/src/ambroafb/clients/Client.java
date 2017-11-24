/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambro.AView;
import ambroafb.clients.helper.ClientStatus;
import ambroafb.countries.Country;
import ambroafb.general.DBUtils;
import ambroafb.general.DateConverter;
import ambroafb.general.Utils;
import ambroafb.general.image_gallery.ImageGalleryController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnFeatures;
import ambroafb.phones.Phone;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 *
 * @author mambroladze
 */

@SuppressWarnings("EqualsAndHashcode")
public class Client extends EditorPanelable{

    private final StringProperty createdDate;
    
    @AView.Column(width = "24", cellFactory = FirmPersonCellFactory.class)
    private final SimpleBooleanProperty isJurBool;

    @AView.Column(width = "24", cellFactory = RezCellFactory.class)
    private final SimpleBooleanProperty isRezidentBool;

    private final SimpleStringProperty firstName, lastName;

    @AView.Column(title = "%descrip", width = TableColumnFeatures.Width.CLIENT)
    @JsonIgnore
    private final StringExpression descrip;

    @AView.Column(title = "%id_number", width = "120")
    @JsonProperty("passNumber")
    private final SimpleStringProperty IDNumber;

    @AView.Column(title = "%email", width = TableColumnFeatures.Width.MAIL)
    private final SimpleStringProperty email;

    @AView.Column(title = "%phones", width = TableColumnFeatures.Width.PHONE)
    private final SimpleStringProperty phoneNumbers;

    private final ObservableList<Phone> phones;
    
    @AView.Column(title = "%client_status", width = "100", cellFactory = StatusCellFactory.class)
//    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final ObjectProperty<ClientStatus> clientStatus;
    
    private final SimpleStringProperty address, zipCode, city;

    @AView.Column(title = "%full_address", width = "250")
    private final StringExpression fullAddress;

    @AView.Column(title = "%country", width = "50", styleClass = TableColumnFeatures.Style.TEXT_CENTER, cellFactory = CountryCellFactory.class)
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final ObjectProperty<Country> country;

    @AView.Column(title = "%fax", width = TableColumnFeatures.Width.PHONE)
    private final SimpleStringProperty fax;
    
    @AView.Column(title = "%www_address", width = "140")
    private final SimpleStringProperty www;
    
    private final SimpleStringProperty remark;

    private final ObservableList<Document> documents;
    private ImageGalleryController clientImageGallery;
    
    private static final String IMAGE_OFFICE_URL = "/images/office.png", IMAGE_PERSON_URL = "/images/person.png";

    @JsonIgnore
    public static int SPECIFIC_STATUS = 1;
    
    
    // Every property object has default values because of avoide NullpointerException in compares or any other methods in any case.
    public Client() {
        createdDate = new SimpleStringProperty("");
        isJurBool = new SimpleBooleanProperty();
        isRezidentBool = new SimpleBooleanProperty();
        firstName =         new SimpleStringProperty("");
        lastName =          new SimpleStringProperty("");
        descrip = Utils.avoidNull(firstName).concat(" ").concat(Utils.avoidNull(lastName));
        email =             new SimpleStringProperty("");
        address =           new SimpleStringProperty("");
        zipCode =           new SimpleStringProperty("");
        city =              new SimpleStringProperty("");
        fullAddress = Utils.avoidNull(address).concat(Utils.getDelimiterAfter(address, ", ")).
                            concat(Utils.avoidNull(zipCode)).concat(Utils.getDelimiterAfter(zipCode, ", ")).
                            concat(Utils.avoidNull(city));
        country =           new SimpleObjectProperty<>(new Country());
        IDNumber =          new SimpleStringProperty("");
        phones = FXCollections.observableArrayList();
        phoneNumbers =      new SimpleStringProperty("");
        fax =               new SimpleStringProperty("");
        clientStatus = new SimpleObjectProperty(new ClientStatus());
        www = new SimpleStringProperty("");
        remark = new SimpleStringProperty("");
        documents = FXCollections.observableArrayList();

        phones.addListener((ListChangeListener.Change<? extends Phone> c) -> {
            rebindPhoneNumbers();
        });
//        rebindPhoneNumbers(); // not needed. setPhones(..) methods and above list listener provides phonesNumbers changing.


        clientStatus.addListener((ObservableValue<? extends ClientStatus> observable, ClientStatus oldValue, ClientStatus newValue) -> {
            System.out.println("client status: " + newValue.getRecId());
            System.out.println("client status: " + newValue.getClientStatusId());
        });
    }
    
    private void rebindPhoneNumbers() {
        phoneNumbers.unbind();
        phoneNumbers.bind(phones
                .stream()
                .map(Phone::numberProperty)
                .reduce(new SimpleStringProperty(""), (StringProperty t, StringProperty u) -> {
                    SimpleStringProperty p = new SimpleStringProperty();
                    p.bind(
                            t.concat(
                                    Bindings.createStringBinding(() -> t.get().isEmpty() ? "" : ", ", t.isEmpty())
                            ).concat(u));
                    return p;
                }));
    }

    
    //Properties getters:
    public SimpleBooleanProperty isJurProperty() {
        return isJurBool;
    }

    public SimpleBooleanProperty isRezProperty() {
        return isRezidentBool;
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public StringExpression descripProperty() {
        return descrip;
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public SimpleStringProperty zipCodeProperty() {
        return zipCode;
    }

    public SimpleStringProperty cityProperty() {
        return city;
    }
    
    public StringExpression fullAddressProperty() {
        return fullAddress;
    }

    public ObjectProperty<Country> countryProperty() {
        return country;
    }

    public SimpleStringProperty IDNumberProperty() {
        return IDNumber;
    }

    public StringProperty phoneNumbersProperty() {
        return phoneNumbers;
    }

    public SimpleStringProperty faxProperty() {
        return fax;
    }
    
    public StringProperty wwwProperty(){
        return www;
    }
    
    public StringProperty remarkProperty(){
        return remark;
    }
    
    public ObjectProperty<ClientStatus> statusProperty(){
        return clientStatus;
    }
    
    
    // Getters:
    @JsonIgnore
    public LocalDate getCreatedDateObj(){
        return DateConverter.getInstance().parseDate(createdDate.get());
    }
    
    public boolean getIsJur() {
        return isJurBool.get();
    }

    public boolean getIsRezident() {
        return isRezidentBool.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getCity() {
        return city.get();
    }

    @JsonIgnore
    public String getFullAddress() {
        return fullAddress.get();
    }
    
    public int getStatus() {
        System.out.println("clientStatus.get().getClientStatusId();: " + clientStatus.get().getClientStatusId());
        return clientStatus.get().getClientStatusId();
    }
    
    // for sending: DB json need key name 'descrip' statusDescrip
    // for receiving: json contains key name 'statusDescrip', so we need setStatusDescrip method.
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getDescrip(){
        return clientStatus.get().getDescrip();
    }
    
    public String getRemark(){
        return remark.get();
    }
    
    @JsonIgnore
    public String getCountryCode(){
        return country.get().getCode();
    }
    
    public int getCountryId(){
        return country.get().getRecId();
    }

    public ObservableList<Phone> getPhones() {
        return phones;
    }

    @JsonIgnore
    public String getPhoneNumbers() {
        return phoneNumbers.get();
    }

    public String getFax() {
        return fax.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getZipCode() {
        return zipCode.get();
    }

    @JsonProperty("passNumber")
    public String getIDNumber() {
        return IDNumber.get();
    }
    
    public String getWww(){
        return www.get();
    }
    
    public ObservableList<Document> getDocuments(){
        return documents;
    }
    
    @JsonIgnore
    public ImageGalleryController getClientImageGallery(){
        return clientImageGallery;
    }
    
    /**
     * The method create short description of client by firstName, lastName and email.
     * @param delimiter The sign between of client name, last name and email.
     * @return The expression of client short information.
     */
    @JsonIgnore
    public StringExpression getShortDescrip(String delimiter){
        return  Utils.avoidNull(firstName).concat(Utils.getDelimiterBetween(firstName, lastName, new SimpleBooleanProperty(false), delimiter))
                    .concat(Utils.avoidNull(lastName)).concat(Utils.getDelimiterAfter(lastName, delimiter))
                    .concat(Utils.avoidNull(email));
    }
    

     // Setters:
    private void setCreatedTime(String date){
        this.createdDate.set(date);
    }
    
    public final void setIsJur(int isJur) {
        this.isJurBool.set(isJur == 1);
    }

    public final void setIsRezident(int isRez) {
        this.isRezidentBool.set(isRez == 1);
    }

    public final void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public final void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public final void setEmail(String email) {
        this.email.set(email);
    }

    public final void setAddress(String address) {
        this.address.set(address);
    }

    public final void setZipCode(String zipCode) {
        this.zipCode.set(zipCode);
    }

    public final void setCity(String city) {
        this.city.set(city);
    }
    
    public void setStatus(int status) {
        this.clientStatus.get().setClientStatusId(status);
    }
    
    public void setStatusDescrip(String statusDescrip){
        this.clientStatus.get().setDescrip(statusDescrip);
    }

    @JsonProperty("passNumber")
    public final void setIDNumber(String IDNumber) {
        this.IDNumber.set(IDNumber);
    }

    public final void setPhones(Collection<Phone> phones) {
        this.phones.setAll(phones);
    }
    
    public void setRemark(String remark){
        this.remark.set(remark);
    }
    
    @JsonProperty
    public void setCountryCode(String countryCode){
        this.country.get().setCode(countryCode);
    }
    
    public void setCountryId(int countryId){
        this.country.get().setRecId(countryId);
    }

    public final void setFax(String fax) {
        this.fax.set(fax);
    }
    
    public void setWww(String www){
        this.www.set(www);
    }
    
    public void setDocuments(Collection<Document> documents){
        this.documents.setAll(documents);
    }
    
    public void setClientImageGallery(ImageGalleryController imageGallery){
        this.clientImageGallery = imageGallery;
    }
    
    
    @Override
    public boolean compares(EditorPanelable backup){
        Client otherClient = (Client) backup;
        boolean fieldsCompareResult =   getIsJur() == otherClient.getIsJur() &&
                                        getIsRezident() == otherClient.getIsRezident() && 
                                        getFirstName().equals(otherClient.getFirstName()) &&
                                        Utils.avoidNull(this.lastNameProperty()).get().equals(Utils.avoidNull(otherClient.lastNameProperty()).get()) &&
                                        getEmail().equals(otherClient.getEmail())    &&
                                        getAddress().equals(otherClient.getAddress()) &&
                                        getZipCode().equals(otherClient.getZipCode()) &&
                                        getCity().equals(otherClient.getCity()) &&
                                        getCountryCode().equals(otherClient.getCountryCode()) &&
                                        getIDNumber().equals(otherClient.getIDNumber()) &&
                                        getFax().equals(otherClient.getFax()) &&
                                        getWww().equals(otherClient.getWww()) &&
                                        statusProperty().get().compares(otherClient.statusProperty().get()) &&
                                        getRemark().equals(otherClient.getRemark());
//                                        getCreatedDate().equals(otherClient.getCreatedDate());
        boolean equalsPhones = Utils.compareListsByElemOrder(phones, otherClient.getPhones());
        return fieldsCompareResult && equalsPhones;
    }
    
    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object other){
        if (other == null) return false;
        Client otherClient = (Client) other;
        return  getRecId() == otherClient.getRecId() ||
                getShortDescrip("").equals(otherClient.getShortDescrip(""));
    }
    
    // Override methods:
    @Override
    public Client cloneWithoutID() {
        Client clone = new Client();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Client cloneWithID() {
        Client clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable object) { 
        Client other = (Client) object;
        setIsJur(Utils.getIntFromBoolean(other.getIsJur()));
        setIsRezident(Utils.getIntFromBoolean(other.getIsRezident()));
        setFirstName(other.getFirstName());
        setLastName(other.getLastName());
        setEmail(other.getEmail());
        setAddress(other.getAddress());
        setZipCode(other.getZipCode());
        setCity(other.getCity());
        setIDNumber(other.getIDNumber());
        getPhones().clear(); // Avoid to add twise phones in tableView
        getPhones().addAll(other.getPhones()
                .stream()
                .map((Phone t) -> new Phone(t.getRecId(), t.getNumber()))
                .collect(Collectors.toList())
        );
        setFax(other.getFax());
        setWww(other.getWww());
        setRemark(other.getRemark());
        getDocuments().clear();
        getDocuments().addAll(other.getDocuments());
        
        this.country.set(other.countryProperty().get().cloneWithID());
        ClientStatus statusClone = new ClientStatus();
        statusClone.copyFrom(other.statusProperty().get());
        clientStatus.set(statusClone);
    }

    @Override
    public String toStringForSearch(){
        String clientPhones = getPhones().stream()
                                        .map((phoneNumber) -> phoneNumber.getNumber() + " ")
                                        .reduce("", String::concat);

        String otherFieldsText = getFirstName() + " " + Utils.avoidNull(lastNameProperty()).get() + " " + 
                                 getEmail() + " " + getAddress() + " " + getCity();
                
        return (otherFieldsText + " " + clientPhones);
    }
    
    
    @Override
    public String toString(){
        return getShortDescrip("").get();
    }
    
    public static class Document {
        public String path = "";
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public int recId;
    }
    
    public static class GalleryDBUpdater implements Runnable {
        
        private final Client client;
        
        public GalleryDBUpdater(Client client){
            this.client = client;
        }
        
        @Override
        public void run() {
            DBUtils.saveObjectToDB(client, "client");
        }
        
    }
    

    public static class FirmPersonCellFactory implements Callback<TableColumn<Client, Boolean>, TableCell<Client, Boolean>> {

        @Override
        public TableCell<Client, Boolean> call(TableColumn<Client, Boolean> param) {
            TableCell<Client, Boolean> cell = new TableCell<Client, Boolean>() {
                private final ImageView view = new ImageView();

                @Override
                public void updateItem(Boolean isFirm, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                        view.setImage(new Image(isFirm ? IMAGE_OFFICE_URL : IMAGE_PERSON_URL));
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
            return new TableCell<Client, Boolean>() {
                @Override
                public void updateItem(Boolean isFirm, boolean empty) {
                    setText(empty ? null : (isFirm ? "Rz" : null));
                }
            };
        }
    }
    
    public static class StatusCellFactory implements Callback<TableColumn<Client, ClientStatus>, TableCell<Client, ClientStatus>> {

        @Override
        public TableCell<Client, ClientStatus> call(TableColumn<Client, ClientStatus> param) {
            return new TableCell<Client, ClientStatus>() {
                @Override
                protected void updateItem(ClientStatus item, boolean empty) {
                    super.updateItem(item, empty);
                    setText((item == null || empty) ? null : item.getDescrip());
                }
                
            };
        }
        
    }
    
    public static class CountryCellFactory implements Callback<TableColumn<Client, Country>, TableCell<Client, Country>> {

        @Override
        public TableCell<Client, Country> call(TableColumn<Client, Country> param) {
            return new TableCell<Client, Country>() {
                @Override
                protected void updateItem(Country item, boolean empty) {
                    super.updateItem(item, empty);
                    setText((item == null || empty) ? null : item.getCode());
                }
                
            };
        }
        
    }
}
