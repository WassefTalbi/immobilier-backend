package OneWayDev.tn.OneWayDev.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
public class RatePropertyRequest {
    private Long userId;
    private Long propertyId;
    private Integer score;
}
