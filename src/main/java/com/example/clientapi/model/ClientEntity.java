package com.example.clientapi.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class ClientEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
	private Long id;
    @Column(name = "client_type")
	private String clientType;
    @Column(name = "phone")
	private String phone;
    @Column(name = "email")
	private String email;
    @Column(name = "name")
	private String name;
    @Column(name = "birth_date")
	private LocalDate birthDate;
    @Column(name = "company_identifier")
	private String companyIdentifier;
    @OneToMany(mappedBy = "clientEntity", fetch = FetchType.LAZY)
    private List<ContractEntity> contracts = new ArrayList<>();
    
    @PreRemove
    private void preRemove() {
      LocalDate today = LocalDate.now();
      for (ContractEntity c : contracts) {
        c.setClientEntity(null);      
        c.setUpdateDate(today);       
      }
    }
}
