package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Article {

    private Long id;
    private String description;
    private String nom;
    private String category;
    private String image;
    private LocalDateTime datecreation;
    private Float prix;
    private Integer quantitestock;
    private List<ListArticle> listArticles;
    private List<Favorie> favories;

    // Constructor
    public Article() {
        this.listArticles = new ArrayList<>();
        this.favories = new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(LocalDateTime datecreation) {
        this.datecreation = datecreation;
    }

    public Float getPrix() {
        return prix;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }

    public Integer getQuantitestock() {
        return quantitestock;
    }

    public void setQuantitestock(Integer quantitestock) {
        this.quantitestock = quantitestock;
    }

    public List<ListArticle> getListArticles() {
        return listArticles;
    }

    public void setListArticles(List<ListArticle> listArticles) {
        this.listArticles = listArticles;
    }

    public List<Favorie> getFavories() {
        return favories;
    }

    public void setFavories(List<Favorie> favories) {
        this.favories = favories;
    }
}