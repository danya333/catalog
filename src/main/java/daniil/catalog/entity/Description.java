package daniil.catalog.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "descriptions")
public class Description {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "specification_id")
    private Specification specification;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Description() {
    }

    public Description(Specification specification, Product product, String name) {
        this.specification = specification;
        this.product = product;
        this.name = name;
    }
}
