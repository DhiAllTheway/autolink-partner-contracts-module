package models;

public class ListArticle {

    private Long id;
    private Integer quantite;
    private Float prixUnitaire;
    private Article article;
    private User user;

    // Constructor
    public ListArticle() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Float getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Float prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
