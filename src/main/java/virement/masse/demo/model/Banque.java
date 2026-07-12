package virement.masse.demo.model;



import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "banque")
public class Banque {

    @Id
    private String banque;

    private String libille;

    private String bic;


}