package OneWayDev.tn.OneWayDev.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

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
    private LocalDate createDate;
    private LocalDate updateDate;
    @ManyToOne

    private User angency;

    @ManyToMany
    private List<Feature> features;
    @PrePersist
    public void prePersist() {
        this.createDate = LocalDate.now();
    }
    @PreUpdate
    public void preUpdate() {
        this.updateDate = LocalDate.now();
    }

}
