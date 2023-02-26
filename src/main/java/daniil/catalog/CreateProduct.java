package daniil.catalog;

import daniil.catalog.entity.Category;
import daniil.catalog.entity.Description;
import daniil.catalog.entity.Product;
import daniil.catalog.entity.Specification;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CreateProduct {
    public static void main(String[] args) {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        int categoryIndex;
        String productName;
        int productPrice = 0;

        TypedQuery<Category> categoryTypedQuery = manager.createQuery(
                "select c from Category c", Category.class
        );
        List<Category> categories = categoryTypedQuery.getResultList();
        System.out.println("Список существующих категорий: ");
        for (int i = 0; i < categories.size(); i++) {
            System.out.printf(" - %s (%d)%n", categories.get(i).getName(), i + 1);
        }

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Введите ID категории: ");
            categoryIndex = Integer.parseInt(in.readLine());
            System.out.println("Введите название товара: ");
            productName = in.readLine();
            System.out.println("Введите стоимость товара: ");
            String prPrice = in.readLine();

            boolean x = true;
            while (x) {
                if (prPrice.matches("\\d+")) {
                    productPrice = Integer.parseInt(prPrice);
                    x = false;
                } else {
                    System.out.println("Ошибка! \n Введите стоимость товара: ");
                    prPrice = in.readLine();
                }
            }

            manager.getTransaction().begin();
            Category category = categories.get(categoryIndex - 1);
//            TypedQuery<Category> categoryTypedQuery1 = manager.createQuery(
//                    "select c from Category c where c.id = ?1", Category.class
//            );
//            categoryTypedQuery1.setParameter(1, categories.get(categoryIndex-1).getId());
//            Category category = categoryTypedQuery1.getSingleResult();

            Product product = new Product();
            product.setCategory(category);
            product.setName(productName);
            product.setPrice(productPrice);
            manager.persist(product);

            List<Specification> specifications = category.getSpecifications();
            System.out.println("Заполните описание товара: ");
            for (Specification specification : specifications) {
                Description description = new Description();
                System.out.println(specification.getName() + ": ");
                String descName = in.readLine();
                description.setProduct(product);
                description.setSpecification(specification);
                description.setName(descName);
                manager.persist(description);
            }

            manager.getTransaction().commit();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }

    }

}
