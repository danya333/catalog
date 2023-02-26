package daniil.catalog;

import daniil.catalog.entity.Description;
import daniil.catalog.entity.Product;
import daniil.catalog.entity.Specification;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class UpdateProduct {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        TypedQuery<Product> productTypedQuery = manager.createQuery(
                "select p from Product p", Product.class
        );
        TypedQuery<Description> descriptionTypedQuery = manager.createQuery(
                "select d from Description d where d.specification = ?1 and d.product = ?2",
                Description.class
        );

        try {
            String answer = "";

            System.out.println("Перечень продуктов:");
            List<Product> products = productTypedQuery.getResultList();
            for (int i = 0; i < products.size(); i++) {
                System.out.printf(" %d. %s%n", i + 1, products.get(i).getName());
            }
            System.out.print("Введите номер редактируемого продукта: ");
            int productIndex = Integer.parseInt(in.readLine());
            Product product = products.get(productIndex - 1);
            List<Specification> specifications = product.getCategory().getSpecifications();

            manager.getTransaction().begin();
            System.out.printf("Введите новое название продукта (%s): ", product.getName());
            answer = in.readLine();
            if (!answer.isEmpty()) {
                product.setName(answer);
            }
            System.out.printf("Введите новую цену продукта (%d): ", product.getPrice());
            answer = in.readLine();
            if (!answer.isEmpty()) {
                product.setPrice(Integer.parseInt(answer));
            }
            manager.persist(product);


            System.out.println("Введите новые значения описания продукта: ");
            for (Specification specification : specifications) {
                descriptionTypedQuery.setParameter(1, specification);
                descriptionTypedQuery.setParameter(2, product);
                try {
                    Description description = descriptionTypedQuery.getSingleResult();
                    System.out.printf(" - %s (%s): ", specification.getName(), description.getName());
                    answer = in.readLine();
                    if (!answer.isEmpty()) {
                        description.setName(answer);
                    }
                    manager.persist(description);
                } catch (NoResultException e) {
                    System.out.printf(" - %s (null): ", specification.getName());
                    answer = in.readLine();
                    Description newDescription = new Description(specification, product, answer);
                    manager.persist(newDescription);
                }
            }
            manager.getTransaction().commit();
            System.out.println("Продукт обновлён успешно!");
        } catch (IOException e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}