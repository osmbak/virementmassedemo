package virement.masse.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity

@Table(name = "pain001_files")
public class Pain001File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "msg_id", length = 35, unique = true)
    private String msgId;

    @Column(name = "file_name", length = 255)
    private String fileName;

    private LocalDateTime creationDate;

    private LocalDateTime receptionDate = LocalDateTime.now();

    private Integer nbOfTxs;

    @Column(precision = 18, scale = 2)
   private BigDecimal ctrlSum;

    @Column(length = 140)
    private String debtorName;

    @Column(length = 34)
    private String debtorIban;

    @Column(length = 30)
    private String statut = "RECU";

    @Column(length = 1000)
    private String errorMessage;

    @OneToMany(
            mappedBy = "pain001File",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Pain001Transaction> transactions = new ArrayList<>();

    public void addTransaction(Pain001Transaction transaction) {
        if (transaction != null) {
            transaction.setPain001File(this);
            transactions.add(transaction);
        }
    }
}