package models;

import java.util.ArrayList;
import java.util.List;

public class Commande {

    private Long id;
    private java.time.LocalDateTime dateCommande;
    private String modePaiement;
    private Float total;
    private User client;
    private List<Long> articleIds;
    private List<Integer> quantites;

    // Constructor
    public Commande() {
        // Initialisation des listes
        this.articleIds = new ArrayList<>();
        this.quantites = new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.time.LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(java.time.LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public List<Long> getArticleIds() {
        return articleIds;
    }

    public void setArticleIds(List<Long> articleIds) {
        this.articleIds = articleIds;
    }

    public List<Integer> getQuantites() {
        return quantites;
    }

    public void setQuantites(List<Integer> quantites) {
        this.quantites = quantites;
    }
}
