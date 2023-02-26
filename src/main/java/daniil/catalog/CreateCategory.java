package daniil.catalog;

import daniil.catalog.entity.Category;
import daniil.catalog.entity.Specification;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CreateCategory {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();
//
//        Category category = manager.find(Category.class, 1L);
//        System.out.println(category.getName());

        String categoryIn = "";
        String specificationIn = "";

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Введите название категории: ");
            categoryIn = in.readLine();
            System.out.println("Введите параметры, описывающие категорию, через запятую: ");
            specificationIn = in.readLine();
        } catch (IOException e) {
            System.out.println("error");
        }


        String[] specs = specificationIn.split(",");

        try{
            manager.getTransaction().begin();
            Category newCategory = new Category();
            newCategory.setName(categoryIn);
            manager.persist(newCategory);


            for (int i = 0; i< specs.length; i++){
                Specification option = new Specification();
                option.setName(specs[i].trim());
                option.setCategory(newCategory);
                manager.persist(option);
            }
            manager.getTransaction().commit();
        } catch (Exception e){
            System.out.println(e.toString());
            manager.getTransaction().rollback();
        }
    }
}
