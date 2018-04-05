package uniandrade.br.edu.com.popseries.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import uniandrade.br.edu.com.popseries.config.ConfigFirebase;

/**
 * Created by pnda on 06/03/18.
 */

public class Usuario {

    private String id;
    private String photo;
    private String nome;
    private String email;
    private String senha;

    public Usuario(){

    }

    public void salvar(){
        DatabaseReference referenciaFirebase = ConfigFirebase.getFirebase();
        referenciaFirebase.child("usuarios").child( getId() ).setValue( this );

    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
