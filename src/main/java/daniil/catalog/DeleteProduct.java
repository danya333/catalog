package daniil.catalog;

import daniil.catalog.entity.Description;
import daniil.catalog.entity.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class DeleteProduct {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Введите ID товара: ");
            Long id = Long.parseLong(in.readLine());
            manager.getTransaction().begin();
            Product product = manager.find(Product.class, id);
            List<Description> descriptions = product.getDescriptions();
            for (Description description : descriptions) {
                manager.remove(description);
            }
            manager.remove(product);
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
