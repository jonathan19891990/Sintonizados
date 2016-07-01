package ec.com.innovasystem.sintonizados.chatapp;

public class Chat {

    private String message;
    private String author;
    private String imagen;
    private int id;
    private String facebook;
    private Chat() {
    }

    Chat(String message, String author, String imagen, int id, String facebook) {
        this.message = message;
        this.author = author;
        this.imagen=imagen;
        this.id=id;
        this.facebook=facebook;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public String getImagen() {
        return imagen;
    }


    public int getId() {
        return id;
    }

    public String getFacebook() {
        return facebook;
    }

}
