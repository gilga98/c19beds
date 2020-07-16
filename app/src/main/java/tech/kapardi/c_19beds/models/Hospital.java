package tech.kapardi.c_19beds.models;

public class Hospital {

    String name;
    String longitude;
    String latitude;
    String address;
    String pin_code;
    String phone_area_code;
    String contact_no;
    String country_code;
    String img_path;
    Integer total_beds;
    Integer remaining_beds;
    Integer id;
    Double comfortness_factor;
    Integer bed_availability;
    Double geodesic_distance;

    public Hospital() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getPhone_area_code() {
        return phone_area_code;
    }

    public void setPhone_area_code(String phone_area_code) {
        this.phone_area_code = phone_area_code;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public Integer getTotal_beds() {
        return total_beds;
    }

    public void setTotal_beds(Integer total_beds) {
        this.total_beds = total_beds;
    }

    public Integer getRemaining_beds() {
        return remaining_beds;
    }

    public void setRemaining_beds(Integer remaining_beds) {
        this.remaining_beds = remaining_beds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getComfortness_factor() {
        return comfortness_factor;
    }

    public void setComfortness_factor(Double comfortness_factor) {
        this.comfortness_factor = comfortness_factor;
    }

    public Integer getBed_availability() {
        return bed_availability;
    }

    public void setBed_availability(Integer bed_availability) {
        this.bed_availability = bed_availability;
    }

    public Double getGeodesic_distance() {
        return geodesic_distance;
    }

    public void setGeodesic_distance(Double geodesic_distance) {
        this.geodesic_distance = geodesic_distance;
    }
}
