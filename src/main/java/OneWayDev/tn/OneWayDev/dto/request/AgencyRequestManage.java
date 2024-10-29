package OneWayDev.tn.OneWayDev.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor

public class AgencyRequestManage {
    @Valid

    @NotBlank(message = "nom  is required and cannot be blank.")
    private String name;
    @NotBlank(message = "nom  is required and cannot be blank.")
    private String description;
    @Email(message = "inavalid mail format")
    @NotBlank(message = "email is required and cannot be blank.")
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "\\d{8}", message = "Invalid phone number format. It should be 8 numbers.")
    private String mobileNumber;
    @NotBlank(message = "adresse is required and cannot be blank.")
    private String address ;
    private Integer sinceYear;

    private MultipartFile logo;



}
