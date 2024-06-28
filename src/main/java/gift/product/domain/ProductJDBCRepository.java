package gift.product.domain;

import gift.product.application.command.ProductCreateCommand;
import gift.product.application.command.ProductUpdateCommand;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductJDBCRepository implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, productRowMapper());
    }

    @Override
    public Optional<Product> findById(Long productId) {
        String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.query(sql, productRowMapper(), productId)
                .stream()
                .findFirst();
    }

    @Override
    public void addProduct(ProductCreateCommand productCreateCommand) {
        String sql = "INSERT INTO product (name, price, imageUrl) VALUES (?, ?, ?)";
        jdbcTemplate.update(
                sql,
                productCreateCommand.name(),
                productCreateCommand.price(),
                productCreateCommand.imageUrl()
        );
    }

    @Override
    public void deleteProduct(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }

    public void updateProduct(ProductUpdateCommand command) {
        String sql = "UPDATE product SET name = ?, price = ?, imageUrl = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                command.name(),
                command.price(),
                command.imageUrl(),
                command.productId()
        );
    }

    private RowMapper<Product> productRowMapper() {
        return (rs, rowNum) -> new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("imageUrl"));
    }
}