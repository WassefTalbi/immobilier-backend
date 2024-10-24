package OneWayDev.tn.OneWayDev.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@Setter
public class PropertyDTO {
    private String title;
    private String type;
    private int bedroom;
    private int bathroom;
    private int area;
    private double price;
    private String requirement;
    private String location;
    private String streetAddress;
    private String state;
    private String country;
    private String zipcode;
    private List<String> additionalFeatures;
    private MultipartFile image;
}
