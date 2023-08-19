package daniil.catalog;

import daniil.catalog.entity.Category;
import daniil.catalog.entity.Description;
import daniil.catalog.entity.Product;
import daniil.catalog.entity.Specification;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Выберите действие:");
        System.out.println(" - Добавить [1]");
        System.out.println(" - Редактировать [2]");
        System.out.println(" - Удалить [3]");

        try {
            int answer = Integer.parseInt(in.readLine());
            if (answer == 1) {
                createProduct();
            } else if(answer == 2){
                updateProduct();
            } else if (answer == 3){
                deleteProduct();
                System.out.println("Товар удалён успешно!");
            } else {
                System.out.println("Некорректный ответ!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createProduct() {

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

    public static void updateProduct() {
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

    public static void deleteProduct() {
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


