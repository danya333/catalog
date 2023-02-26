package daniil.catalog;

import daniil.catalog.entity.Category;
import daniil.catalog.entity.Specification;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CreateCategory2 {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        String categoryIn = "";
        String specificationIn = "";

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            TypedQuery<Long> categoryTypedQuery = manager.createQuery(
                    "select count(c.id) from Category c where c.name = ?1", Long.class
            );

            boolean check = true;
            while (check) {
                System.out.print("Введите название категории: ");
                categoryIn = in.readLine();
                categoryTypedQuery.setParameter(1, categoryIn);
                long categoryCount = categoryTypedQuery.getSingleResult();

                if (categoryCount == 0) {
                    break;
                }

                System.out.println("Категория " + "\"" + categoryIn + "\"" + " уже существует! \n");
            }


            System.out.print("Введите параметры, описывающие категорию, через запятую: ");
            specificationIn = in.readLine();
            String[] specs = specificationIn.split(",");

            manager.getTransaction().begin();
            Category newCategory = new Category();
            newCategory.setName(categoryIn);
            manager.persist(newCategory);

            for (int i = 0; i < specs.length; i++) {
                Specification option = new Specification();
                option.setName(specs[i].trim());
                option.setCategory(newCategory);
                manager.persist(option);
            }
            manager.getTransaction().commit();

        } catch (IOException e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}