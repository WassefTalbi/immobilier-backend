package OneWayDev.tn.OneWayDev.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String type;
    private int bedroom;
    private int bathroom;
    private int area;
    private double price;
    private String requirement;
    private String location;
    private String image;
    @ManyToOne
    private User angency;

    @ManyToMany
    private List<Feature> features;


}
